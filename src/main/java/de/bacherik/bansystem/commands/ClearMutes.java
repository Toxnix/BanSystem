package de.bacherik.bansystem.commands;

import de.bacherik.bansystem.Main;
import de.bacherik.bansystem.config.MessagesConfig;
import de.bacherik.bansystem.utils.UUIDFetcher;
import de.bacherik.bansystem.utils.Util;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class ClearMutes extends Command {
    public ClearMutes(String name, String permission, String... aliases) {
        super(name, permission, aliases);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        MessagesConfig config = Main.getInstance().getMessagesConfig();

        if (args.length != 1) {
            sender.sendMessage(new TextComponent(config.get("bansystem.clearmutes.syntax", true)));
            return;
        }

        String playerName = args[0];

        UUIDFetcher.getUUID(playerName, uuid -> {
            if (uuid == null) {
                sender.sendMessage(new TextComponent(config.get("bansystem.playernotfound", true)
                        .replaceAll("%PLAYER%", args[0])));
                return;
            }


            String unmutedBy = (sender instanceof ProxiedPlayer) ? sender.getName() : config.get("bansystem.consolename");

            Main.getInstance().getSql().clearMutes(uuid.toString());
            Util.broadcastMessage(config.get("bansystem.clearmutes.successful")
                            .replaceAll("%PLAYER%", playerName)
                            .replaceAll("%UNBANNED_BY%", unmutedBy),
                    "bansystem.clearmutes");
        });
    }
}