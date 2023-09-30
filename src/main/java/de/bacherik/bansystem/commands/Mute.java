package de.bacherik.bansystem.commands;

import de.bacherik.bansystem.Main;
import de.bacherik.bansystem.config.MessagesConfig;
import de.bacherik.bansystem.utils.TimeHelper;
import de.bacherik.bansystem.utils.UUIDFetcher;
import de.bacherik.bansystem.utils.Util;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Mute extends Command {
    private final MessagesConfig config = Main.getInstance().getMessagesConfig();
    public Mute(String name, String permission, String... aliases) {
        super(name, permission, aliases);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {

        if (args.length == 0 || (args.length == 1 && args[0].equalsIgnoreCase("help"))) {
            sender.sendMessage(new TextComponent(config.get("bansystem.help")));
            return;
        }

        if (args.length < 2) {
            sender.sendMessage(new TextComponent(config.get("bansystem.mute.syntax", true)));
            return;
        }

        String playerName = args[0];
        ProxiedPlayer player = ProxyServer.getInstance().getPlayer(playerName);

        //get uuid async
        UUIDFetcher.getUUID(playerName, uuid -> {

            if (uuid == null) {
                sender.sendMessage(new TextComponent(config.get("bansystem.playernotfound", true)
                        .replaceAll("%PLAYER%", playerName)));
                return;
            }

            Main.getInstance().getSql().getMute(uuid.toString(), muteRecord -> {
                if (muteRecord != null) {
                    sender.sendMessage(new TextComponent(config.get("bansystem.alreadymuted", true)
                            .replaceAll("%PLAYER%", playerName)));
                    return;
                }

                LocalDateTime start = LocalDateTime.now();

                /* Mute Template */
                Pattern pattern = Pattern.compile("^#([1-9]\\d*)$"); //Regex for #<number>
                Matcher matcher = pattern.matcher(args[1]);
                if (args.length == 2 && matcher.matches()) {
                    int templateId = Integer.parseInt(matcher.group(1));
                    Main.getInstance().getSql().getMuteTemplateAsync(templateId, muteTemplate -> {
                        if (muteTemplate == null) {
                            sender.sendMessage(config.get("bansystem.template.add.error"));
                            return;
                        }


                        LocalDateTime end = TimeHelper.addTime(start, muteTemplate.getTime());
                        if (!sender.hasPermission("bansystem.mute.permanent")) {
                            long duration = Duration.between(LocalDateTime.now(), end).getSeconds();
                            if (!Main.getInstance().getSettingsConfig().canMute(sender, duration)) {
                                sender.sendMessage(new TextComponent(config.get("mutesystem.mute.nopermission", true)));
                                return;
                            }
                        }

                        if (sender instanceof ProxiedPlayer) {
                            UUIDFetcher.getUUID(sender.getName(), mutedByUuid -> mute(playerName, uuid.toString(),
                                    sender.getName(), mutedByUuid.toString(), muteTemplate.getReason(), start, end, player, sender));
                        } else {
                            mute(playerName, uuid.toString(), config.get("bansystem.consolename"),
                                    config.get("bansystem.consolename"), muteTemplate.getReason(), start, end, player, sender);
                        }
                    });
                    return;
                }

                LocalDateTime end = TimeHelper.addTime(start, args[1]);

                //No reason speicified
                if (args.length == 2 && end != null) {
                    sender.sendMessage(new TextComponent(config.get("bansystem.mute.syntax", true)));
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
                    end = TimeHelper.addTime(start, "500y"); //permanent mute
                }

                if (!sender.hasPermission("bansystem.mute.permanent")) {
                    long duration = Duration.between(LocalDateTime.now(), end).getSeconds();
                    if (!Main.getInstance().getSettingsConfig().canMute(sender, duration)) {
                        sender.sendMessage(new TextComponent(config.get("bansystem.mute.nopermission", true)));
                        return;
                    }
                }

                //mute
                if (sender instanceof ProxiedPlayer) {
                    LocalDateTime finalEnd = end;
                    UUIDFetcher.getUUID(sender.getName(), mutedByUuid -> mute(playerName, uuid.toString(),
                            sender.getName(), mutedByUuid.toString(), reason, start, finalEnd, player, sender));
                } else {
                    mute(playerName, uuid.toString(), config.get("bansystem.consolename"),
                            config.get("bansystem.consolename"), reason, start, end, player, sender);
                }
            });
        });
    }

    private void mute(String playerName, String playerUuid, String mutedByName,
                      String mutedByUuid, String reason, LocalDateTime start, LocalDateTime end, ProxiedPlayer player, CommandSender sender) {
        if (player.hasPermission("bansystem.mute.bypass")) {
            sender.sendMessage(new TextComponent(config.get("bansystem.mute.bypass", true).replaceAll("%PLAYER%", playerName)));
            return;
        }
        MessagesConfig config = Main.getInstance().getMessagesConfig();

        Main.getInstance().getSql().muteAsync(playerUuid, mutedByUuid, reason, start, end);

        Util.broadcastMessage(config.get("bansystem.mute.successful")
                        .replaceAll("%PLAYER%", playerName)
                        .replaceAll("%MUTED_BY%", mutedByName)
                        .replaceAll("%REASON%", reason)
                        .replaceAll("%TIME%", TimeHelper.getDifference(start, end)),
                "bansystem.mute");

        ProxiedPlayer p1 = ProxyServer.getInstance().getPlayer(playerName);
        if (p1 != null) {
            p1.sendMessage(new TextComponent(config.get("bansystem.mutemessage")
                    .replaceAll("%REASON%", reason)
                    .replaceAll("%BANNED_BY%", mutedByName)
                    .replaceAll("%REMAINING_TIME%", TimeHelper.getDifference(start, end))));
        }
    }
}
