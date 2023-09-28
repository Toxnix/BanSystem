package de.bacherik.bansystem.platform.spigot.commands;

import de.bacherik.bansystem.platform.bungee.Main;
import de.bacherik.bansystem.utils.TimeHelper;
import de.bacherik.bansystem.utils.config.MessagesConfig;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class BanTemplate implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        MessagesConfig config = Main.getInstance().getMessagesConfig();

        if (args.length == 0) {
            sender.sendMessage(config.get("bansystem.ban.template.syntax"));
            return false;
        }
        String action = args[0];
        if (action.equalsIgnoreCase("list")) {
            sender.sendMessage(config.get("bansystem.ban.template.list.header"));
            sender.sendMessage((String.format(config.get("bansystem.ban.template.list.format.head"),
                    config.get("bansystem.ban.template.list.id.head"),
                    config.get("bansystem.ban.template.list.time.head"),
                    config.get("bansystem.ban.template.list.reason.head"))));

            Main.getInstance().getSql().getBanTemplates().forEach(banTemplate ->
                    sender.sendMessage(String.format(config.get("bansystem.ban.template.list.format.content"),
                            config.get("bansystem.ban.template.list.id.content").replaceAll("%ID%", String.valueOf(banTemplate.getId())),
                            config.get("bansystem.ban.template.list.time.content").replaceAll("%TIME%", TimeHelper.formatTime(banTemplate.getTime())),
                            config.get("bansystem.ban.template.list.reason.content").replaceAll("%REASON%", banTemplate.getReason()))));
            sender.sendMessage(config.get("bansystem.ban.template.list.footer"));
            return false;

        } else if (action.equalsIgnoreCase("add")) {
            if (args.length > 2) {
                StringBuilder reason = new StringBuilder();
                for (int i = 2; i < args.length; i++)
                    reason.append(args[i]).append(" ");

                Main.getInstance().getSql().addBanTemplate(args[1], reason.toString());
                sender.sendMessage(config.get("bansystem.ban.template.add.successful"));
                return false;
            }
        } else if (action.equalsIgnoreCase("edit")) {
            if (args.length > 3) {
                StringBuilder reason = new StringBuilder();
                for (int i = 3; i < args.length; i++)
                    reason.append(args[i]).append(" ");

                Main.getInstance().getSql().editBanTemplate(Integer.parseInt(args[1]), args[2], reason.toString());
                sender.sendMessage(config.get("bansystem.ban.template.edit.successful"));
                return false;
            }
        } else if (action.equalsIgnoreCase("remove")) {
            if (args.length == 2) {
                Main.getInstance().getSql().removeBanTemplate(Integer.parseInt(args[1]));
                sender.sendMessage(config.get("bansystem.ban.template.remove.successful"));
                return false;
            }
        }
        sender.sendMessage(config.get("bansystem.ban.template.syntax"));
        return false;
    }
}