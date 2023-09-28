package de.bacherik.bansystem.utils.config;

import de.bacherik.bansystem.utils.ServerSoftware;
import de.leonhard.storage.Yaml;

import java.io.File;

public class DBConfig extends Config {

    private final String host;
    private final int port;
    private final String username;
    private final String password;
    private final String database;

    public DBConfig(String name, String path, ServerSoftware serverSoftware) {
        super(name, path, serverSoftware);

        this.host = (String) getPathOrSet("mysql.host", "localhost");
        if (serverSoftware.equals(ServerSoftware.BUNGEECORD)) this.port = (int) getPathOrSet("mysql.port", 3306);
        else if (serverSoftware.equals(ServerSoftware.SPIGOT)) {
            Yaml config = new Yaml(new File(path + "/" + name));
            this.port = config.getOrSetDefault("mysql.port", 3306);
        } else {
            this.port = (int) getPathOrSet("mysql.port", 3306);
        }
        this.username = (String) getPathOrSet("mysql.username", "root");
        this.password = (String) getPathOrSet("mysql.password", "password");
        this.database = (String) getPathOrSet("mysql.database", "ban");
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getDatabase() {
        return database;
    }
}
