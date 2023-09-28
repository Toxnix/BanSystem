package de.bacherik.bansystem.platform.bungee.commands;

import de.bacherik.bansystem.platform.bungee.Main;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;

public class BanReload extends Command {


    public BanReload(String name, String permission, String... aliases) {
        super(name, permission, aliases);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Main.getInstance().reloadConfigs();
        sender.sendMessage(new TextComponent("§aConfig reloaded!"));
    }
}
