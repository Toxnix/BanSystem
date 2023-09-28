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

import java.time.LocalDateTime;

public class UnBan implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        MessagesConfig config = Main.getInstance().getMessagesConfig();

        if (args.length == 0) {
            sender.sendMessage(config.get("bansystem.unban.syntax", true));
            return false;
        }

        String playerName = args[0];

        UUIDFetcher.getUUID(playerName, uuid -> {
            if (uuid == null) {
                sender.sendMessage(config.get("bansystem.playernotfound", true)
                        .replaceAll("%PLAYER%", playerName));
                return;
            }
            Main.getInstance().getSql().getBan(uuid.toString(), record -> {
                if (record == null) {
                    sender.sendMessage(config.get("bansystem.unban.notbanned", true)
                            .replaceAll("%PLAYER%", playerName));
                    return;
                }

                StringBuilder unbanReason = new StringBuilder();
                for (int i = 1; i < args.length; i++) {
                    unbanReason.append(args[i]).append(" ");
                }


                if (sender instanceof Player) {
                    UUIDFetcher.getUUID(sender.getName(), unbannedByUuid ->
                            unban(playerName, uuid.toString(), record.getBannedBy(),
                                    record.getReason(), record.getStartDate(), record.getEndDate(), sender.getName(),
                                    unbannedByUuid.toString(), unbanReason.toString(), LocalDateTime.now()));
                } else {
                    unban(playerName, uuid.toString(), record.getBannedBy(), record.getReason(),
                            record.getStartDate(), record.getEndDate(), config.get("bansystem.consolename"),
                            config.get("bansystem.consolename"), unbanReason.toString(), LocalDateTime.now());
                }
            });
        });
        return false;
    }

    private void unban(String playerName, String playerUuid, String bannedByUuid, String banReason,
                       LocalDateTime start, LocalDateTime end, String unbannedByName, String unbannedByUuid,
                       String unbanReason, LocalDateTime unbanTime) {
        Main.getInstance().getSql().unbanAsync(playerUuid, bannedByUuid, banReason, start, end, unbannedByUuid,
                unbanReason, unbanTime);

        Util.broadcastMessage(Main.getInstance().getMessagesConfig().get("bansystem.unban.successful")
                        .replaceAll("%PLAYER%", playerName)
                        .replaceAll("%UNBANNED_BY%", unbannedByName)
                        .replaceAll("%REASON%", unbanReason),
                "bansystem.unban", ServerSoftware.SPIGOT);

    }
}
