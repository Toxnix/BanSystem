package de.bacherik.bansystem.platform.spigot.commands;

import de.bacherik.bansystem.platform.spigot.Main;
import de.bacherik.bansystem.utils.ServerSoftware;
import de.bacherik.bansystem.utils.TimeHelper;
import de.bacherik.bansystem.utils.UUIDFetcher;
import de.bacherik.bansystem.utils.Util;
import de.bacherik.bansystem.utils.config.MessagesConfig;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Ban implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        MessagesConfig config = Main.getInstance().getMessagesConfig();

        if (args.length == 0 || (args.length == 1 && args[0].equalsIgnoreCase("help"))) {
            sender.sendMessage(config.get("bansystem.help"));
            return false;
        }

        if (args.length < 2) {
            sender.sendMessage(config.get("bansystem.ban.syntax", true));
            return false;
        }

        String playerName = args[0];

        //get uuid async
        UUIDFetcher.getUUID(playerName, uuid -> {

            if (uuid == null) {
                sender.sendMessage(config.get("bansystem.playernotfound", true)
                        .replaceAll("%PLAYER%", playerName));
                return;
            }

            Main.getInstance().getSql().getBan(uuid.toString(), banRecord -> {
                if (banRecord != null) {
                    sender.sendMessage(config.get("bansystem.alreadybanned", true)
                            .replaceAll("%PLAYER%", playerName));
                    return;
                }

                LocalDateTime start = LocalDateTime.now();

                /* Ban Template */
                Pattern pattern = Pattern.compile("^#([1-9]\\d*)$"); //Regex for #<number>
                Matcher matcher = pattern.matcher(args[1]);
                if (args.length == 2 && matcher.matches()) {
                    int templateId = Integer.parseInt(matcher.group(1));
                    Main.getInstance().getSql().getBanTemplateAsync(templateId, banTemplate -> {
                        if (banTemplate == null) {
                            sender.sendMessage(config.get("bansystem.template.add.error"));
                            return;
                        }


                        LocalDateTime end = TimeHelper.addTime(start, banTemplate.getTime());
                        if (!sender.hasPermission("bansystem.ban.permanent")) {
                            long duration = Duration.between(LocalDateTime.now(), end).getSeconds();
                            if (!Main.getInstance().getSettingsConfig().canBan(sender, duration)) {
                                sender.sendMessage(config.get("mutesystem.mute.nopermission", true));
                                return;
                            }
                        }

                        if (sender instanceof Player) {
                            UUIDFetcher.getUUID(sender.getName(), bannedByUuid -> ban(playerName, uuid.toString(),
                                    sender.getName(), bannedByUuid.toString(), banTemplate.getReason(), start, end));
                        } else {
                            ban(playerName, uuid.toString(), config.get("bansystem.consolename"),
                                    config.get("bansystem.consolename"), banTemplate.getReason(), start, end);
                        }
                    });
                    return;
                }

                LocalDateTime end = TimeHelper.addTime(start, args[1]);

                //No reason speicified
                if (args.length == 2 && end != null) {
                    sender.sendMessage(config.get("bansystem.ban.syntax", true));
                    return;
                }

                //reason
                StringBuilder sb = new StringBuilder();
                for (int i = (end == null) ? 1 : 2; i < args.length; i++) {
                    sb.append(args[i]).append(" ");
                }
                String reason = sb.toString().trim();

                //permanent or not
                if (end == null) {
                    end = TimeHelper.addTime(start, "500y"); //permanent ban
                }

                if (!sender.hasPermission("bansystem.ban.permanent")) {
                    long duration = Duration.between(LocalDateTime.now(), end).getSeconds();
                    if (!Main.getInstance().getSettingsConfig().canBan(sender, duration)) {
                        sender.sendMessage(config.get("bansystem.ban.nopermission", true));
                        return;
                    }
                }

                //ban
                if (sender instanceof Player) {
                    LocalDateTime finalEnd = end;
                    UUIDFetcher.getUUID(sender.getName(), bannedByUuid -> ban(playerName, uuid.toString(),
                            sender.getName(), bannedByUuid.toString(), reason, start, finalEnd));
                } else {
                    ban(playerName, uuid.toString(), config.get("bansystem.consolename"),
                            config.get("bansystem.consolename"), reason, start, end);
                }
            });
        });
        return false;
    }

    private void ban(String playerName, String playerUuid, String bannedByName,
                     String bannedByUuid, String reason, LocalDateTime start, LocalDateTime end) {
        MessagesConfig config = Main.getInstance().getMessagesConfig();

        Main.getInstance().getSql().banAsync(playerUuid, bannedByUuid, reason, start, end);

        Util.broadcastMessage(config.get("bansystem.ban.successful")
                        .replaceAll("%PLAYER%", playerName)
                        .replaceAll("%BANNED_BY%", bannedByName)
                        .replaceAll("%REASON%", reason)
                        .replaceAll("%TIME%", TimeHelper.getDifference(start, end)),
                "bansystem.ban", ServerSoftware.SPIGOT);

        Player p1 = Bukkit.getPlayer(playerName);
        if (p1 != null) {
            p1.kickPlayer(config.get("bansystem.banmessage")
                    .replaceAll("%REASON%", reason)
                    .replaceAll("%BANNED_BY%", bannedByName)
                    .replaceAll("%REMAINING_TIME%", TimeHelper.getDifference(start, end)));
        }
    }
}