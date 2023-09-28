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

public class UnMute implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        MessagesConfig config = Main.getInstance().getMessagesConfig();

        if (args.length == 0) {
            sender.sendMessage(config.get("bansystem.unmute.syntax", true));
            return false;
        }

        String playerName = args[0];

        UUIDFetcher.getUUID(playerName, uuid -> {
            if (uuid == null) {
                sender.sendMessage(config.get("bansystem.playernotfound", true)
                        .replaceAll("%PLAYER%", playerName));
                return;
            }
            Main.getInstance().getSql().getMute(uuid.toString(), record -> {
                if (record == null) {
                    sender.sendMessage(config.get("bansystem.unban.notmuted", true)
                            .replaceAll("%PLAYER%", playerName));
                    return;
                }

                StringBuilder unmuteReason = new StringBuilder();
                for (int i = 1; i < args.length; i++) {
                    unmuteReason.append(args[i]).append(" ");
                }


                if (sender instanceof Player) {
                    UUIDFetcher.getUUID(sender.getName(), unmutedByUuid ->
                            unmute(playerName, uuid.toString(), record.getMutedBy(),
                                    record.getReason(), record.getStartDate(), record.getEndDate(), sender.getName(),
                                    unmutedByUuid.toString(), unmuteReason.toString(), LocalDateTime.now()));
                } else {
                    unmute(playerName, uuid.toString(), record.getMutedBy(), record.getReason(),
                            record.getStartDate(), record.getEndDate(), config.get("bansystem.consolename"),
                            config.get("bansystem.consolename"), unmuteReason.toString(), LocalDateTime.now());
                }
            });
        });
        return false;
    }

    private void unmute(String playerName, String playerUuid, String mutedByUuid, String muteReason,
                        LocalDateTime start, LocalDateTime end, String unmutedByName, String unmutedByUuid,
                        String unmuteReason, LocalDateTime unmuteTime) {
        Main.getInstance().getSql().unmuteAsync(playerUuid, mutedByUuid, muteReason, start, end, unmutedByUuid,
                unmuteReason, unmuteTime);

        Util.broadcastMessage(Main.getInstance().getMessagesConfig().get("bansystem.unmute.successful")
                        .replaceAll("%PLAYER%", playerName)
                        .replaceAll("%UNMUTED_BY%", unmutedByName)
                        .replaceAll("%REASON%", unmuteReason),
                "bansystem.unmute", ServerSoftware.SPIGOT);

    }
}
