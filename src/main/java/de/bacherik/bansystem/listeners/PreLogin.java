package de.bacherik.bansystem.listeners;

import de.bacherik.bansystem.Main;
import de.bacherik.bansystem.utils.BanRecord;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class PreLogin implements Listener {

    @EventHandler
    public void onLogin(LoginEvent event) {

        BanRecord record = Main.getInstance().getSql().getBan(event.getConnection().getUniqueId().toString());
        if (record == null) return;

        event.setCancelled(true);
        event.getConnection().disconnect(new TextComponent(Main.getInstance().getMessagesConfig().get("bansystem.banmessage")
                .replaceAll("%REASON%", record.getReason())
                .replaceAll("%REMAINING_TIME%", record.getRemaining())));
    }
}
