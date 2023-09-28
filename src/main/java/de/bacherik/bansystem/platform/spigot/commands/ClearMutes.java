package de.bacherik.bansystem.platform.spigot.commands;

import de.bacherik.bansystem.platform.bungee.Main;
import de.bacherik.bansystem.utils.ServerSoftware;
import de.bacherik.bansystem.utils.UUIDFetcher;
import de.bacherik.bansystem.utils.Util;
import de.bacherik.bansystem.utils.config.MessagesConfig;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ClearMutes implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        MessagesConfig config = Main.getInstance().getMessagesConfig();

        if (args.length != 1) {
            sender.sendMessage(config.get("bansystem.clearmutes.syntax", true));
            return false;
        }

        String playerName = args[0];

        UUIDFetcher.getUUID(playerName, uuid -> {
            if (uuid == null) {
                sender.sendMessage(config.get("bansystem.playernotfound", true)
                        .replaceAll("%PLAYER%", args[0]));
                return;
            }


            String unmutedBy = (sender instanceof Player) ? sender.getName() : config.get("bansystem.consolename");

            Main.getInstance().getSql().clearMutes(uuid.toString());
            Util.broadcastMessage(config.get("bansystem.clearmutes.successful")
                            .replaceAll("%PLAYER%", playerName)
                            .replaceAll("%UNBANNED_BY%", unmutedBy),
                    "bansystem.clearmutes", ServerSoftware.SPIGOT);
        });
        return false;
    }
}