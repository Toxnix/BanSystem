package de.bacherik.bansystem.commands;

import de.bacherik.bansystem.Main;
import de.bacherik.bansystem.config.MessagesConfig;
import de.bacherik.bansystem.utils.TimeHelper;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;

public class BanTemplate extends Command {

    public BanTemplate(String name, String permission, String... aliases) {
        super(name, permission, aliases);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        MessagesConfig config = Main.getInstance().getMessagesConfig();

        if (args.length == 0) {
            sender.sendMessage(new TextComponent(config.get("bansystem.template.syntax")));
            return;
        }
        String action = args[0];
        if (action.equalsIgnoreCase("list")) {
            sender.sendMessage(new TextComponent(config.get("bansystem.template.list.header")));
            sender.sendMessage(new TextComponent(String.format(config.get("bansystem.template.list.format.head"),
                    config.get("bansystem.template.list.id.head"),
                    config.get("bansystem.template.list.time.head"),
                    config.get("bansystem.template.list.reason.head"))));

            Main.getInstance().getSql().getBanTemplates().forEach(banTemplate ->
                    sender.sendMessage(new TextComponent(String.format(config.get("bansystem.template.list.format.content"),
                            config.get("bansystem.template.list.id.content").replaceAll("%ID%", String.valueOf(banTemplate.getId())),
                            config.get("bansystem.template.list.time.content").replaceAll("%TIME%", TimeHelper.formatTime(banTemplate.getTime())),
                            config.get("bansystem.template.list.reason.content").replaceAll("%REASON%", banTemplate.getReason())))));
            sender.sendMessage(new TextComponent(config.get("bansystem.template.list.footer")));
            return;

        } else if (action.equalsIgnoreCase("add")) {
            if (args.length > 2) {
                StringBuilder reason = new StringBuilder();
                for (int i = 2; i < args.length; i++)
                    reason.append(args[i]).append(" ");

                Main.getInstance().getSql().addBanTemplate(args[1], reason.toString());
                sender.sendMessage(new TextComponent(config.get("bansystem.template.add.successful")));
                return;
            }
        } else if (action.equalsIgnoreCase("edit")) {
            if (args.length > 3) {
                StringBuilder reason = new StringBuilder();
                for (int i = 3; i < args.length; i++)
                    reason.append(args[i]).append(" ");

                Main.getInstance().getSql().editBanTemplate(Integer.parseInt(args[1]), args[2], reason.toString());
                sender.sendMessage(new TextComponent(config.get("bansystem.template.edit.successful")));
                return;
            }
        } else if (action.equalsIgnoreCase("remove")) {
            if (args.length == 2) {
                Main.getInstance().getSql().removeBanTemplate(Integer.parseInt(args[1]));
                sender.sendMessage(new TextComponent(config.get("bansystem.template.remove.successful")));
                return;
            }
        }
        sender.sendMessage(new TextComponent(config.get("bansystem.template.syntax")));
    }
}
