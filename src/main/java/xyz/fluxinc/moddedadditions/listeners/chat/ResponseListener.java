package xyz.fluxinc.moddedadditions.listeners.chat;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import xyz.fluxinc.moddedadditions.ModdedAdditions;

import static org.bukkit.Bukkit.getServer;

public class ResponseListener implements Listener {

    private ModdedAdditions instance;

    /*
        If the message contains Hello There, respond appropriately
     */

    public ResponseListener(ModdedAdditions instance) {
        this.instance = instance;
    }

    @EventHandler
    public void onChatEvent(AsyncPlayerChatEvent event) {
        if (!(event.getMessage().toLowerCase().contains("hello there"))) {return;}
        sendResponse(event.getPlayer());
    }

    public void sendResponse(Player player) {
        player.sendTitle("General Kenobi", "You are a bold one!", 10, 70, 20);
    }
}
