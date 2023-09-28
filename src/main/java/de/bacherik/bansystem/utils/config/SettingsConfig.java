package de.bacherik.bansystem.utils.config;

import de.bacherik.bansystem.utils.ServerSoftware;
import net.md_5.bungee.api.CommandSender;

import java.util.HashMap;

public class SettingsConfig extends Config {

    private final HashMap<String, Integer> banPermissions;
    private final HashMap<String, Integer> mutePermissions;
    private String language;

    public SettingsConfig(String name, String path, ServerSoftware serverSoftware) {
        super(name, path, serverSoftware);

        language = (String) getPathOrSet("bansystem.language", "english", false);

        banPermissions = new HashMap<>();
        banPermissions.put("bansystem.ban.supporter", 24 * 60 * 60);
        banPermissions.put("bansystem.ban.srsupporter", 7 * 24 * 60 * 60);
        banPermissions.put("bansystem.ban.moderator", 30 * 24 * 60 * 60);
        banPermissions.put("bansystem.ban.srmoderator", 365 * 24 * 60 * 60);
        banPermissions.forEach((permission, defaultValue) ->
                banPermissions.put(permission, (int) getPathOrSet(permission, defaultValue)));

        mutePermissions = new HashMap<>();
        mutePermissions.put("bansystem.mute.supporter", 12 * 60 * 60);
        mutePermissions.put("bansystem.mute.srsupporter", 3 * 24 * 60 * 60);
        mutePermissions.put("bansystem.mute.moderator", 15 * 24 * 60 * 60);
        mutePermissions.put("bansystem.mute.srmoderator", 180 * 24 * 60 * 60);
        mutePermissions.forEach((permission, defaultValue) ->
                mutePermissions.put(permission, (int) getPathOrSet(permission, defaultValue)));
    }

    public String getLanguage() {
        return language;
    }

    public boolean canBan(CommandSender sender, long banDuration) {
        return banPermissions.entrySet().stream()
                .anyMatch(entry -> sender.hasPermission(entry.getKey()) && banDuration <= entry.getValue());
    }

    public boolean canBan(org.bukkit.command.CommandSender player, long banDuration) {
        return banPermissions.entrySet().stream()
                .anyMatch(entry -> player.hasPermission(entry.getKey()) && banDuration <= entry.getValue());
    }

    public boolean canMute(CommandSender sender, long muteDuration) {
        return mutePermissions.entrySet().stream()
                .anyMatch(entry -> sender.hasPermission(entry.getKey()) && muteDuration <= entry.getValue());
    }

    public boolean canMute(org.bukkit.command.CommandSender player, long muteDuration) {
        return mutePermissions.entrySet().stream()
                .anyMatch(entry -> player.hasPermission(entry.getKey()) && muteDuration <= entry.getValue());
    }

    public void reload() {
        super.reload();
        language = (String) getPathOrSet("bansystem.language", "german", false);
    }
}
