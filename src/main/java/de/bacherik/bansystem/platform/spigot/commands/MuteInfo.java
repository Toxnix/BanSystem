package de.bacherik.bansystem.platform.spigot.commands;

import de.bacherik.bansystem.platform.bungee.Main;
import de.bacherik.bansystem.utils.MuteRecord;
import de.bacherik.bansystem.utils.UUIDFetcher;
import de.bacherik.bansystem.utils.Util;
import de.bacherik.bansystem.utils.config.MessagesConfig;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.atomic.AtomicInteger;

public class MuteInfo implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        MessagesConfig config = Main.getInstance().getMessagesConfig();

        if (args.length != 1) {
            sender.sendMessage(config.get("bansystem.muteinfo.syntax", true));
            return false;
        }

        String playerName = args[0];

        UUIDFetcher.getUUID(playerName, uuid -> {
            if (uuid == null) {
                sender.sendMessage(config.get("bansystem.playernotfound", true)
                        .replaceAll("%PLAYER%", playerName));
                return;
            }

            Main.getInstance().getSql().getMute(uuid.toString(), currentMuteRecord -> {
                if (currentMuteRecord == null) {
                    sender.sendMessage(config.get("bansystem.muteinfo.nocurrentmute",
                            true).replaceAll("%PLAYER%", playerName));
                } else {
                    Util.UUIDtoName(currentMuteRecord.getMutedBy(), mutedByName
                            -> sender.sendMessage(config.get("bansystem.muteinfo.currentmute")
                            .replaceAll("%PLAYER%", playerName)
                            .replaceAll("%REASON%", currentMuteRecord.getReason())
                            .replaceAll("%MUTED_BY%", mutedByName)
                            .replaceAll("%MUTE_START%", currentMuteRecord.getStart())
                            .replaceAll("%MUTE_END%", currentMuteRecord.getEnd())
                            .replaceAll("%DURATION%", currentMuteRecord.getDuration())
                            .replaceAll("%REMAINING_TIME%", currentMuteRecord.getRemaining())));
                }
            });


            Main.getInstance().getSql().getPastMutes(uuid.toString(), pastMuteRecords -> {
                if (pastMuteRecords == null || pastMuteRecords.isEmpty()) {
                    sender.sendMessage(config.get("bansystem.muteinfo.nopastmute", true)
                            .replaceAll("%PLAYER%", playerName));
                    return;
                }

                AtomicInteger index = new AtomicInteger(0);
                for (MuteRecord muteRecord : pastMuteRecords) {

                    if (!muteRecord.isUnmuted()) {
                        Util.UUIDtoName(muteRecord.getMutedBy(), mutedByName ->
                                sender.sendMessage(config.get("bansystem.muteinfo.pastmuteNotunmuted")
                                        .replaceAll("%INDEX%", String.valueOf(index.incrementAndGet()))
                                        .replaceAll("%PLAYER%", playerName)
                                        .replaceAll("%REASON%", muteRecord.getReason())
                                        .replaceAll("%MUTED_BY%", mutedByName)
                                        .replaceAll("%MUTE_START%", muteRecord.getStart())
                                        .replaceAll("%MUTE_END%", muteRecord.getEnd())
                                        .replaceAll("%DURATION%", muteRecord.getDuration())));
                    } else {
                        Util.UUIDtoName(muteRecord.getMutedBy(), mutedByName ->
                                Util.UUIDtoName(muteRecord.getUnmutedBy(), unmutedByName ->
                                        sender.sendMessage(config.get("bansystem.muteinfo.pastmuteUnmuted")
                                                .replaceAll("%INDEX%", String.valueOf(index.incrementAndGet()))
                                                .replaceAll("%PLAYER%", playerName)
                                                .replaceAll("%REASON%", muteRecord.getReason())
                                                .replaceAll("%MUTED_BY%", mutedByName)
                                                .replaceAll("%MUTE_START%", muteRecord.getStart())
                                                .replaceAll("%MUTE_END%", muteRecord.getEnd())
                                                .replaceAll("%DURATION%", muteRecord.getDuration())
                                                .replaceAll("%UNMUTED_BY%", unmutedByName)
                                                .replaceAll("%UNMUTE_REASON%", muteRecord.getUnmuteReason())
                                                .replaceAll("%UNMUTE_TIME%", muteRecord.getUnmuteTime()))
                                ));
                    }
                }
            });
        });
        return false;
    }
}