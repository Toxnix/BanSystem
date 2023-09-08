package de.bacherik.bansystem.commands;

import de.bacherik.bansystem.Main;
import de.bacherik.bansystem.config.MessagesConfig;
import de.bacherik.bansystem.utils.UUIDFetcher;
import de.bacherik.bansystem.utils.Util;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.time.LocalDateTime;

public class UnBan extends Command {
    public UnBan(String name, String permission, String... aliases) {
        super(name, permission, aliases);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        MessagesConfig config = Main.getInstance().getMessagesConfig();

        if (args.length == 0) {
            sender.sendMessage(new TextComponent(config.get("bansystem.unban.syntax", true)));
            return;
        }

        String playerName = args[0];

        UUIDFetcher.getUUID(playerName, uuid -> {
            if (uuid == null) {
                sender.sendMessage(new TextComponent(config.get("bansystem.playernotfound", true)
                        .replaceAll("%PLAYER%", playerName)));
                return;
            }
            Main.getInstance().getSql().getBan(uuid.toString(), record -> {
                if (record == null) {
                    sender.sendMessage(new TextComponent(config.get("bansystem.unban.notbanned", true)
                            .replaceAll("%PLAYER%", playerName)));
                    return;
                }

                StringBuilder unbanReason = new StringBuilder();
                for (int i = 1; i < args.length; i++) {
                    unbanReason.append(args[i]).append(" ");
                }


                if (sender instanceof ProxiedPlayer) {
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
                "bansystem.unban");

    }
}
