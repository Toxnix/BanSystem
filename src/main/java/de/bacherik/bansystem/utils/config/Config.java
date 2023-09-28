package de.bacherik.bansystem.utils.config;

import de.bacherik.bansystem.utils.ServerSoftware;
import de.leonhard.storage.Yaml;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class Config {

    private final File file;

    private Configuration bugeeConfiguration;
    private ServerSoftware serverSoftware;

    public Config(String name, String path, ServerSoftware serverSoftware) {
        this.serverSoftware = serverSoftware;
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

        if (serverSoftware.equals(ServerSoftware.BUNGEECORD)) {
            try {
                bugeeConfiguration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    protected void save() {
        if (serverSoftware.equals(ServerSoftware.BUNGEECORD)) {
            try {
                ConfigurationProvider.getProvider(YamlConfiguration.class).save(bugeeConfiguration, file);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    protected void reload() {
        if (serverSoftware.equals(ServerSoftware.BUNGEECORD)) {
            bugeeConfiguration = null;
            try {
                bugeeConfiguration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public Object getPathOrSet(String path, Object defaultValue) {
        return getPathOrSet(path, defaultValue, true);
    }

    public Object getPathOrSet(String key, Object defaultValue, boolean translateColors) {
        if (serverSoftware.equals(ServerSoftware.BUNGEECORD)) {
            if (bugeeConfiguration.get(key) == null) {
                bugeeConfiguration.set(key, defaultValue);
            save();
        }
            return translateColors ? translateColors(bugeeConfiguration.get(key)) : bugeeConfiguration.get(key);
        } else if (serverSoftware.equals(ServerSoftware.SPIGOT)) {
            Yaml yaml = new Yaml(new Yaml(file));
            yaml.getOrSetDefault(key, defaultValue);
            return translateColors ? translateColors(yaml.get(key)) : yaml.get(key);
        }
        return "something went wrong";
    }

    public Object translateColors(Object value) {
        if (value instanceof String) {
            return ((String) value).replaceAll("&", "§");
        } else if (value instanceof Integer) {
            return value;
        }
        return value;
    }
}
