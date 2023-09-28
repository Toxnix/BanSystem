package de.bacherik.bansystem.utils;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;

import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class Util {

    /* Executor pool for async methods */
    public static final ExecutorService pool = Executors.newCachedThreadPool();

    /**
     * Helper method to broadcast a message
     *
     * @param message    the message to be broadcasted
     * @param permission the permission needed to recieve the message
     */
    public static void broadcastMessage(String message, String permission, ServerSoftware software) {
        if (software.equals(ServerSoftware.BUNGEECORD)) {
            ProxyServer.getInstance().getPlayers().stream().filter(player -> player.hasPermission(permission))
                    .forEach(player -> player.sendMessage(new TextComponent(message)));
            System.out.println(message);
        } else if (software.equals(ServerSoftware.SPIGOT)) {
            Bukkit.getOnlinePlayers().stream().filter(player -> player.hasPermission(permission))
                    .forEach(player -> player.sendMessage(message));
            Bukkit.getConsoleSender().sendMessage(message);
        }

    }

    /**
     * Try to resolve an uuid to a name async
     *
     * @param uuid     the uuid
     * @param acceptor the name acceptor
     */
    public static void UUIDtoName(String uuid, Consumer<String> acceptor) {
        if (uuid == null || uuid.isEmpty()) {
            acceptor.accept(null);
        } else {
            try {
                UUIDFetcher.getName(UUID.fromString(uuid), acceptor);
            } catch (IllegalArgumentException ex) {
                acceptor.accept(uuid);
            }
        }
    }
}
