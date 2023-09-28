package de.bacherik.bansystem.platform.spigot.listeners;

import de.bacherik.bansystem.platform.spigot.Main;
import de.bacherik.bansystem.utils.BanRecord;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

public class PreLogin implements Listener {

    @EventHandler
    public void onLogin(PlayerLoginEvent event) {
        BanRecord record = Main.getInstance().getSql().getBan(event.getPlayer().getUniqueId().toString());
        if (record == null) return;

        event.setKickMessage(Main.getInstance().getMessagesConfig().get("bansystem.banmessage")
                .replaceAll("%REASON%", record.getReason())
                .replaceAll("%REMAINING_TIME%", record.getRemaining()));
    }
}
