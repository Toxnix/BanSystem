package de.bacherik.bansystem.listeners;

import de.bacherik.bansystem.Main;
import de.bacherik.bansystem.utils.MuteRecord;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;


public class PreSendMessage implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onMessage(ChatEvent event) {
        if (event.isCancelled()) {
            return; // ignore already canceled messages
        }

        if (event.getSender() instanceof ProxiedPlayer) {
            ProxiedPlayer player = (ProxiedPlayer) event.getSender();

            // Check if the player sending the message has a mute record
            MuteRecord record = Main.getInstance().getSql().getMute(player.getUniqueId().toString());

            if (record != null) { // If the player is muted
                event.setCancelled(true); // Cancel the chat message event
                player.sendMessage(new TextComponent(Main.getInstance().getMessagesConfig().get("bansystem.mutemessage")
                        .replaceAll("%REASON%", record.getReason())
                        .replaceAll("%REMAINING_TIME%", record.getRemaining())));
            }
        }
    }
}
