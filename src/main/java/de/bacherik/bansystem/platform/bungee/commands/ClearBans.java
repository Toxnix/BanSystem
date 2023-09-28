package de.bacherik.bansystem.platform.bungee.commands;


import de.bacherik.bansystem.platform.bungee.Main;
import de.bacherik.bansystem.utils.ServerSoftware;
import de.bacherik.bansystem.utils.UUIDFetcher;
import de.bacherik.bansystem.utils.Util;
import de.bacherik.bansystem.utils.config.MessagesConfig;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class ClearBans extends Command {


    public ClearBans(String name, String permission, String... aliases) {
        super(name, permission, aliases);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        MessagesConfig config = Main.getInstance().getMessagesConfig();

        if (args.length != 1) {
            sender.sendMessage(new TextComponent(config.get("bansystem.clearbans.syntax", true)));
            return;
        }

        String playerName = args[0];

        UUIDFetcher.getUUID(playerName, uuid -> {
            if (uuid == null) {
                sender.sendMessage(new TextComponent(config.get("bansystem.playernotfound", true)
                        .replaceAll("%PLAYER%", args[0])));
                return;
            }


            String unbannedBy = (sender instanceof ProxiedPlayer) ? sender.getName() : config.get("bansystem.consolename");

            Main.getInstance().getSql().clearBans(uuid.toString());
            Util.broadcastMessage(config.get("bansystem.clearbans.successful")
                            .replaceAll("%PLAYER%", playerName)
                            .replaceAll("%UNBANNED_BY%", unbannedBy),
                    "bansystem.clearbans", ServerSoftware.BUNGEECORD);
        });
    }
}