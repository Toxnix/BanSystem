package de.bacherik.bansystem;

import de.bacherik.bansystem.commands.*;
import de.bacherik.bansystem.config.*;
import de.bacherik.bansystem.listeners.PreLogin;
import de.bacherik.bansystem.listeners.PreSendMessage;
import de.bacherik.bansystem.utils.MySQL;
import de.bacherik.bansystem.utils.TimeHelper;
import de.bacherik.bansystem.utils.Util;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;

public final class Main extends Plugin {

    private static final String PREFIX = ChatColor.BLUE + "[" + ChatColor.YELLOW + "BanSystem" + ChatColor.BLUE + "]" + ChatColor.RESET + ": ";
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
        System.out.println(ChatColor.DARK_GREEN + PREFIX + "Plugin enabling ...");
        DBConfig dbConfig = new DBConfig("database.yml", "plugins/BanSystem");
        settingsConfig = new SettingsConfig("settings.yml", "plugins/BanSystem");

        MessagesConfig germanConfig = new MessagesGermanConfig("messages_german.yml", "plugins/BanSystem");
        MessagesConfig englishConfig = new MessagesEnglishConfig("messages_english.yml", "plugins/BanSystem");

        if (settingsConfig.getLanguage().equalsIgnoreCase("german")) {
            msgsConfig = germanConfig;
        } else if (settingsConfig.getLanguage().equalsIgnoreCase("english")) {
            msgsConfig = englishConfig;
        }

        sql = new MySQL(dbConfig.getHost(), dbConfig.getPort(), dbConfig.getUsername(), dbConfig.getPassword(), dbConfig.getDatabase());

        PluginManager pluginManager = ProxyServer.getInstance().getPluginManager();
        // Register commands
        pluginManager.registerCommand(this, new Ban("ban", "bansystem.ban", "tempban"));
        pluginManager.registerCommand(this, new UnBan("unban", "bansystem.unban"));
        pluginManager.registerCommand(this, new BanInfo("baninfo", "bansystem.baninfo"));
        pluginManager.registerCommand(this, new ClearBans("clearbans", "bansystem.clearbans", "clearban"));
        pluginManager.registerCommand(this, new BanTemplate("bantemplate", "bansystem.bantemplate"));
        pluginManager.registerCommand(this, new BanReload("banreload", "bansystem.reload", "breload", "banr", "reloadban", "rban", "mrealod", "mreload", "realodmute", "rmute"));
        pluginManager.registerCommand(this, new Mute("mute", "bansystem.mute", "tempmute"));
        pluginManager.registerCommand(this, new MuteInfo("muteinfo", "bansystem.muteinfo"));
        pluginManager.registerCommand(this, new MuteTemplate("mutetemplate", "bansystem.mutetemplate"));
        pluginManager.registerCommand(this, new UnMute("unmute", "bansystem.unmute"));
        pluginManager.registerCommand(this, new ClearMutes("clearmutes", "bansystem.clearmutes", "clearmute"));

        // Register listeners
        pluginManager.registerListener(this, new PreLogin());
        pluginManager.registerListener(this, new PreSendMessage());

        System.out.println(ChatColor.GREEN + PREFIX + "Plugin enabled");
        instance = this;
        listen();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        System.out.println(ChatColor.DARK_RED + PREFIX + "Plugin disabling ...");
        sql.close();
        System.out.println(ChatColor.RED + PREFIX + "Plugin disabled");
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

    public void reloadConfigs() {
        settingsConfig.reload();

        if (settingsConfig.getLanguage().equalsIgnoreCase("german")) {
            msgsConfig = new MessagesGermanConfig("messages_german.yml", "plugins/BanSystem");
        } else if (settingsConfig.getLanguage().equalsIgnoreCase("english")) {
            msgsConfig = new MessagesEnglishConfig("messages_english.yml", "plugins/BanSystem");
        }
    }

    private void listen() {
        getProxy().getScheduler().runAsync(this, () -> {
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
                                "bansystem.ban");

                        ProxiedPlayer p1 = ProxyServer.getInstance().getPlayer(name);
                        if (p1 != null) {
                            p1.disconnect(new TextComponent(msgsConfig.get("bansystem.banmessage")
                                    .replaceAll("%REASON%", reason)
                                    .replaceAll("%BANNED_BY%", bannedBy)
                                    .replaceAll("%REMAINING_TIME%", TimeHelper.getDifference(start, end))));
                        }
                    } else if (params.length == 5 && cmd.equals("unban")) {
                        String name = params[1];
                        String unbannedByUuid = params[2]; //not used, but might be still useful
                        String unbannedBy = params[3];
                        String reason = params[4];

                        Util.broadcastMessage(Main.getInstance().getMessagesConfig().get("bansystem.unban.successful")
                                        .replaceAll("%PLAYER%", name)
                                        .replaceAll("%UNBANNED_BY%", unbannedBy)
                                        .replaceAll("%REASON%", reason),
                                "bansystem.unban");
                    } else {
                        ProxyServer.getInstance().getConsole().sendMessage(new TextComponent("§c[BanManagment] Received invalid ban package."));
                    }

                    socket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
