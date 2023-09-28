package de.bacherik.bansystem.platform.spigot.commands;

import de.bacherik.bansystem.platform.bungee.Main;
import de.bacherik.bansystem.utils.TimeHelper;
import de.bacherik.bansystem.utils.config.MessagesConfig;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class MuteTemplate implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        MessagesConfig config = Main.getInstance().getMessagesConfig();

        if (args.length == 0) {
            sender.sendMessage(config.get("bansystem.mute.template.syntax"));
            return false;
        }
        String action = args[0];
        if (action.equalsIgnoreCase("list")) {
            sender.sendMessage(config.get("bansystem.mute.template.list.header"));
            sender.sendMessage(String.format(config.get("bansystem.mute.template.list.format.head"),
                    config.get("bansystem.mute.template.list.id.head"),
                    config.get("bansystem.mute.template.list.time.head"),
                    config.get("bansystem.mute.template.list.reason.head")));

            Main.getInstance().getSql().getMuteTemplates().forEach(muteTemplate ->
                    sender.sendMessage(String.format(config.get("bansystem.mute.template.list.format.content"),
                            config.get("bansystem.mute.template.list.id.content").replaceAll("%ID%", String.valueOf(muteTemplate.getId())),
                            config.get("bansystem.mute.template.list.time.content").replaceAll("%TIME%", TimeHelper.formatTime(muteTemplate.getTime())),
                            config.get("bansystem.mute.template.list.reason.content").replaceAll("%REASON%", muteTemplate.getReason()))));
            sender.sendMessage(config.get("bansystem.mute.template.list.footer"));
            return false;

        } else if (action.equalsIgnoreCase("add")) {
            if (args.length > 2) {
                StringBuilder reason = new StringBuilder();
                for (int i = 2; i < args.length; i++)
                    reason.append(args[i]).append(" ");

                Main.getInstance().getSql().addMuteTemplate(args[1], reason.toString());
                sender.sendMessage(config.get("bansystem.mute.template.add.successful"));
                return false;
            }
        } else if (action.equalsIgnoreCase("edit")) {
            if (args.length > 3) {
                StringBuilder reason = new StringBuilder();
                for (int i = 3; i < args.length; i++)
                    reason.append(args[i]).append(" ");

                Main.getInstance().getSql().editMuteTemplate(Integer.parseInt(args[1]), args[2], reason.toString());
                sender.sendMessage(config.get("bansystem.mute.template.edit.successful"));
                return false;
            }
        } else if (action.equalsIgnoreCase("remove")) {
            if (args.length == 2) {
                Main.getInstance().getSql().removeMuteTemplate(Integer.parseInt(args[1]));
                sender.sendMessage(config.get("bansystem.mute.template.remove.successful"));
                return false;
            }
        }
        sender.sendMessage(config.get("bansystem.mute.template.syntax"));
        return false;
    }
}
