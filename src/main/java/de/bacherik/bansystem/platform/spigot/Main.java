package de.bacherik.bansystem.platform.spigot;

import de.bacherik.bansystem.platform.spigot.commands.*;
import de.bacherik.bansystem.platform.spigot.listeners.PreLogin;
import de.bacherik.bansystem.platform.spigot.listeners.PreSendMessage;
import de.bacherik.bansystem.utils.MySQL;
import de.bacherik.bansystem.utils.ServerSoftware;
import de.bacherik.bansystem.utils.TimeHelper;
import de.bacherik.bansystem.utils.Util;
import de.bacherik.bansystem.utils.config.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;

public class Main extends JavaPlugin {
    public static final String PREFIX = ChatColor.BLUE + "[" + ChatColor.YELLOW + "BanSystem" + ChatColor.BLUE + "]" + ChatColor.RESET + ": ";
    private static Main instance;
    private SettingsConfig settingsConfig;
    private MessagesConfig msgsConfig;
    private MySQL sql;

    public static Main getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        Bukkit.getConsoleSender().sendMessage(PREFIX + ChatColor.GREEN + "Plugin enabling ...");
        DBConfig dbConfig = new DBConfig("database.yml", "plugins/BanSystem", ServerSoftware.SPIGOT);
        settingsConfig = new SettingsConfig("settings.yml", "plugins/BanSystem", ServerSoftware.SPIGOT);

        MessagesConfig germanConfig = new MessagesGermanConfig("messages_german.yml", "plugins/BanSystem", ServerSoftware.SPIGOT);
        MessagesConfig englishConfig = new MessagesEnglishConfig("messages_english.yml", "plugins/BanSystem", ServerSoftware.SPIGOT);

        if (settingsConfig.getLanguage().equalsIgnoreCase("german")) {
            msgsConfig = germanConfig;
        } else if (settingsConfig.getLanguage().equalsIgnoreCase("english")) {
            msgsConfig = englishConfig;
        }

        sql = new MySQL(dbConfig.getHost(), dbConfig.getPort(), dbConfig.getUsername(), dbConfig.getPassword(), dbConfig.getDatabase(), ServerSoftware.SPIGOT);

        PluginManager pluginManager = Bukkit.getPluginManager();
        // Register commands
        getCommand("ban").setExecutor(new Ban());
        getCommand("unban").setExecutor(new UnBan());
        getCommand("baninfo").setExecutor(new BanInfo());
        getCommand("clearbans").setExecutor(new ClearBans());
        getCommand("bantemplate").setExecutor(new BanTemplate());
        getCommand("banreload").setExecutor(new BanReload());
        getCommand("mute").setExecutor(new Mute());
        getCommand("muteinfo").setExecutor(new MuteInfo());
        getCommand("mutetemplate").setExecutor(new MuteTemplate());
        getCommand("unmute").setExecutor(new UnMute());
        getCommand("clearmutes").setExecutor(new ClearMutes());


        // Register listeners
        pluginManager.registerEvents(new PreLogin(), this);
        pluginManager.registerEvents(new PreSendMessage(), this);

        Bukkit.getConsoleSender().sendMessage(PREFIX + ChatColor.DARK_GREEN + "Plugin enabled!");
        instance = this;
        listen();
    }

    public void reloadConfigs() {
        settingsConfig.reload();

        if (settingsConfig.getLanguage().equalsIgnoreCase("german")) {
            msgsConfig = new MessagesGermanConfig("messages_german.yml", "plugins/BanSystem", ServerSoftware.SPIGOT);
        } else if (settingsConfig.getLanguage().equalsIgnoreCase("english")) {
            msgsConfig = new MessagesEnglishConfig("messages_english.yml", "plugins/BanSystem", ServerSoftware.SPIGOT);
        }
    }

    public SettingsConfig getSettingsConfig() {
        return settingsConfig;
    }

    public MessagesConfig getMessagesConfig() {
        return msgsConfig;
    }

    public MySQL getSql() {
        return sql;
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        Bukkit.getConsoleSender().sendMessage(PREFIX + ChatColor.RED + "Plugin disabling ...");
        sql.close();
        Bukkit.getConsoleSender().sendMessage(PREFIX + ChatColor.DARK_RED + "Plugin disabled!");
    }

    private void listen() {
        getServer().getScheduler().runTaskAsynchronously(this, () -> {
            try {
                ServerSocket serverSocket = new ServerSocket(42069);

                while (true) {
                    Socket socket = serverSocket.accept();

                    InputStream inputStream = socket.getInputStream();
                    byte[] header = new byte[2];
                    inputStream.read(header);
                    int dataLength = ((header[0] & 0xff) << 8) | (header[1] & 0xff);
                    byte[] data = new byte[dataLength];
                    inputStream.read(data);

                    String[] params = new String(data).split(",");
                    String cmd = params[0];

                    if (params.length == 6 && cmd.equals("ban")) {
                        String name = params[1];
                        String bannedByUuid = params[2]; //not used, but might be still useful
                        String bannedBy = params[3];
                        String reason = params[4];
                        LocalDateTime start = LocalDateTime.now();
                        LocalDateTime end = LocalDateTime.parse(params[5], TimeHelper.formatter);

                        Util.broadcastMessage(msgsConfig.get("bansystem.ban.successful")
                                        .replaceAll("%PLAYER%", name)
                                        .replaceAll("%BANNED_BY%", bannedBy)
                                        .replaceAll("%REASON%", reason)
                                        .replaceAll("%TIME%", TimeHelper.getDifference(start, end)),
                                "bansystem.ban", ServerSoftware.SPIGOT);

                        Player p1 = Bukkit.getPlayer(name);
                        if (p1 != null) {
                            p1.kickPlayer(msgsConfig.get("bansystem.banmessage")
                                    .replaceAll("%REASON%", reason)
                                    .replaceAll("%BANNED_BY%", bannedBy)
                                    .replaceAll("%REMAINING_TIME%", TimeHelper.getDifference(start, end)));
                        }
                    } else if (params.length == 5 && cmd.equals("unban")) {
                        String name = params[1];
                        String unbannedByUuid = params[2]; //not used, but might be still useful
                        String unbannedBy = params[3];
                        String reason = params[4];

                        Util.broadcastMessage(msgsConfig.get("bansystem.unban.successful")
                                        .replaceAll("%PLAYER%", name)
                                        .replaceAll("%UNBANNED_BY%", unbannedBy)
                                        .replaceAll("%REASON%", reason),
                                "bansystem.unban", ServerSoftware.SPIGOT);
                    } else {
                        Bukkit.getConsoleSender().sendMessage("§c[BanManagment] Received invalid ban package.");
                    }

                    socket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}