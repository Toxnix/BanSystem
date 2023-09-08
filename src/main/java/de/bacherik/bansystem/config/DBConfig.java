package de.bacherik.bansystem.config;

public class DBConfig extends Config {

    private final String host;
    private final int port;
    private final String username;
    private final String password;
    private final String database;

    public DBConfig(String name, String path) {
        super(name, path);

        this.host = (String) getPathOrSet("mysql.host", "localhost");
        this.port = (int) getPathOrSet("mysql.port", 3306);
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
