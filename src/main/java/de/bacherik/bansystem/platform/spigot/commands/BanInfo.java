package de.bacherik.bansystem.platform.spigot.commands;

import de.bacherik.bansystem.platform.bungee.Main;
import de.bacherik.bansystem.utils.BanRecord;
import de.bacherik.bansystem.utils.UUIDFetcher;
import de.bacherik.bansystem.utils.Util;
import de.bacherik.bansystem.utils.config.MessagesConfig;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.atomic.AtomicInteger;

public class BanInfo implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        MessagesConfig config = Main.getInstance().getMessagesConfig();

        if (args.length != 1) {
            sender.sendMessage(config.get("bansystem.baninfo.syntax", true));
            return false;
        }

        String playerName = args[0];

        UUIDFetcher.getUUID(playerName, uuid -> {
            if (uuid == null) {
                sender.sendMessage(config.get("bansystem.playernotfound", true)
                        .replaceAll("%PLAYER%", playerName));
                return;
            }

            Main.getInstance().getSql().getBan(uuid.toString(), currentBanRecord -> {
                if (currentBanRecord == null) {
                    sender.sendMessage(config.get("bansystem.baninfo.nocurrentban",
                            true).replaceAll("%PLAYER%", playerName));
                } else {
                    Util.UUIDtoName(currentBanRecord.getBannedBy(), bannedByName
                            -> sender.sendMessage(config.get("bansystem.baninfo.currentban")
                            .replaceAll("%PLAYER%", playerName)
                            .replaceAll("%REASON%", currentBanRecord.getReason())
                            .replaceAll("%BANNED_BY%", bannedByName)
                            .replaceAll("%BAN_START%", currentBanRecord.getStart())
                            .replaceAll("%BAN_END%", currentBanRecord.getEnd())
                            .replaceAll("%DURATION%", currentBanRecord.getDuration())
                            .replaceAll("%REMAINING_TIME%", currentBanRecord.getRemaining())));
                }
            });


            Main.getInstance().getSql().getPastBans(uuid.toString(), pastBanRecords -> {
                if (pastBanRecords == null || pastBanRecords.isEmpty()) {
                    sender.sendMessage(config.get("bansystem.baninfo.nopastban", true)
                            .replaceAll("%PLAYER%", playerName));
                    return;
                }

                AtomicInteger index = new AtomicInteger(0);
                for (BanRecord banRecord : pastBanRecords) {

                    if (!banRecord.isUnbanned()) {
                        Util.UUIDtoName(banRecord.getBannedBy(), bannedByName ->
                                sender.sendMessage(config.get("bansystem.baninfo.pastbanNotunbanned")
                                        .replaceAll("%INDEX%", String.valueOf(index.incrementAndGet()))
                                        .replaceAll("%PLAYER%", playerName)
                                        .replaceAll("%REASON%", banRecord.getReason())
                                        .replaceAll("%BANNED_BY%", bannedByName)
                                        .replaceAll("%BAN_START%", banRecord.getStart())
                                        .replaceAll("%BAN_END%", banRecord.getEnd())
                                        .replaceAll("%DURATION%", banRecord.getDuration())));
                    } else {
                        Util.UUIDtoName(banRecord.getBannedBy(), bannedByName ->
                                Util.UUIDtoName(banRecord.getUnbannedBy(), unbannedByName ->
                                        sender.sendMessage(config.get("bansystem.baninfo.pastbanUnbanned")
                                                .replaceAll("%INDEX%", String.valueOf(index.incrementAndGet()))
                                                .replaceAll("%PLAYER%", playerName)
                                                .replaceAll("%REASON%", banRecord.getReason())
                                                .replaceAll("%BANNED_BY%", bannedByName)
                                                .replaceAll("%BAN_START%", banRecord.getStart())
                                                .replaceAll("%BAN_END%", banRecord.getEnd())
                                                .replaceAll("%DURATION%", banRecord.getDuration())
                                                .replaceAll("%UNBANNED_BY%", unbannedByName)
                                                .replaceAll("%UNBAN_REASON%", banRecord.getUnbanReason())
                                                .replaceAll("%UNBAN_TIME%", banRecord.getUnbanTime()))
                                ));
                    }
                }
            });
        });
        return false;
    }
}
