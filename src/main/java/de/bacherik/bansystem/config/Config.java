package de.bacherik.bansystem.config;

import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class Config {

    private final File file;

    private Configuration fileConfiguration;

    public Config(String name, String path) {
        File folder = new File(path);
        if (!folder.exists()) folder.mkdir();

        file = new File(path, name);

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        try {
            fileConfiguration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected void save() {
        try {
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(fileConfiguration, file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected void reload() {
        fileConfiguration = null;
        try {
            fileConfiguration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Object getPathOrSet(String path, Object defaultValue) {
        return getPathOrSet(path, defaultValue, true);
    }

    public Object getPathOrSet(String path, Object defaultValue, boolean translateColors) {
        if (fileConfiguration.get(path) == null) {
            fileConfiguration.set(path, defaultValue);
            save();
        }

        return translateColors ? translateColors(fileConfiguration.get(path)) : fileConfiguration.get(path);
    }

    public Object translateColors(Object value) {
        if (value instanceof String) {
            return ((String) value).replaceAll("&", "§");
        }
        return value;
    }
}
