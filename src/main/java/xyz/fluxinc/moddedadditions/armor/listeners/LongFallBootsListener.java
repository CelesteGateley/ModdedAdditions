package xyz.fluxinc.moddedadditions.armor.listeners;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

import static xyz.fluxinc.moddedadditions.armor.SpecialArmorUtils.getLongFallBoots;

public class LongFallBootsListener implements Listener {

    @EventHandler
    public void onFallDamage(EntityDamageEvent event) {
        if (event.getCause() != EntityDamageEvent.DamageCause.FALL) return;
        if (event.getEntityType() != EntityType.PLAYER) return;
        Player player = (Player) event.getEntity();
        if (!getLongFallBoots().verifyItemStack(player.getInventory().getBoots())) return;
        event.setCancelled(true);
    }
}
