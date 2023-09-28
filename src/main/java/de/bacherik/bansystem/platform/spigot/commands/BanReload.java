package de.bacherik.bansystem.platform.spigot.commands;

import de.bacherik.bansystem.platform.spigot.Main;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class BanReload implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        Main.getInstance().reloadConfigs();
        sender.sendMessage(Main.PREFIX + ChatColor.GREEN + "Config reloaded!");
        return false;
    }
}
