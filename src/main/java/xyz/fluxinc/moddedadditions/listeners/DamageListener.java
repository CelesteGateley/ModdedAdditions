package xyz.fluxinc.moddedadditions.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

import java.net.http.WebSocket;

public class DamageListener implements Listener {

    @EventHandler
    public void onDamageEntity(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player)) { return; }
        event.getDamager().sendMessage("Dealt: " + event.getFinalDamage());
    }
}
