package xyz.fluxinc.moddedadditions.listeners.chat;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ResponseListener implements Listener {

    @EventHandler
    public void onChatEvent(AsyncPlayerChatEvent event) {
        if (!(event.getMessage().toLowerCase().contains("hello there"))) {
            return;
        }
        event.getPlayer().sendTitle("General Kenobi", "You are a bold one!", 10, 70, 20);
    }

}
