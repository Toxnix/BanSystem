package de.bacherik.bansystem.platform.spigot.listeners;

import de.bacherik.bansystem.platform.spigot.Main;
import de.bacherik.bansystem.utils.MuteRecord;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;

public class PreSendMessage implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onMessage(PlayerChatEvent event) {
        if (event.isCancelled()) {
            return; // ignore already cancelled messages
        }

        Player player = event.getPlayer();
        String message = event.getMessage();

        MuteRecord record = Main.getInstance().getSql().getMute(player.getUniqueId().toString());
        if (record == null) return;

        event.setCancelled(true);
        player.sendMessage(Main.getInstance().getMessagesConfig().get("bansystem.mutemessage")
                .replaceAll("%REASON%", record.getReason())
                .replaceAll("%REMAINING_TIME%", record.getRemaining()));
    }
}
