package de.bacherik.bansystem.config;

import net.md_5.bungee.api.CommandSender;

import java.util.HashMap;

public class SettingsConfig extends Config {

    private final HashMap<String, Integer> permissions;
    private String language;

    public SettingsConfig(String name, String path) {
        super(name, path);

        language = (String) getPathOrSet("bansystem.language", "english", false);

        permissions = new HashMap<>();
        permissions.put("bansystem.ban.supporter", 24 * 60 * 60);
        permissions.put("bansystem.ban.srsupporter", 7 * 24 * 60 * 60);
        permissions.put("bansystem.ban.moderator", 30 * 24 * 60 * 60);
        permissions.put("bansystem.ban.srmoderator", 365 * 24 * 60 * 60);
        permissions.forEach((permission, defaultValue) ->
                permissions.put(permission, (int) getPathOrSet(permission, defaultValue)));
    }

    public String getLanguage() {
        return language;
    }

    public boolean canBan(CommandSender sender, long banDuration) {
        return permissions.entrySet().stream()
                .anyMatch(entry -> sender.hasPermission(entry.getKey()) && banDuration <= entry.getValue());
    }

    public void reload() {
        super.reload();
        language = (String) getPathOrSet("bansystem.language", "german", false);
    }
}
