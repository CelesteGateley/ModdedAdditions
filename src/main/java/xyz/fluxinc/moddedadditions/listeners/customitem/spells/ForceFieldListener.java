package xyz.fluxinc.moddedadditions.listeners.customitem.spells;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.util.Vector;
import xyz.fluxinc.moddedadditions.ModdedAdditions;

import java.util.*;

public class ForceFieldListener implements Runnable, Listener {

    private final ModdedAdditions instance;
    private final Map<Player, Long> activeFields;
    private static final int FIELD_DISTANCE = 8;

    public ForceFieldListener(ModdedAdditions instance) {
        this.instance = instance;
        activeFields = new HashMap<>();
        instance.getServer().getScheduler().scheduleSyncRepeatingTask(instance, this, 20, 20);
    }

    public void addForceField(Player player, long duration) {
        activeFields.put(player, System.currentTimeMillis() + duration * 1000);
    }

    @Override
    public void run() {
        List<Player> toRemove = new ArrayList<>();
        for (Player player : activeFields.keySet()) {
            if (activeFields.get(player) < System.currentTimeMillis()) {
                toRemove.add(player);
                continue;
            }
            List<Entity> entityList = player.getNearbyEntities(FIELD_DISTANCE, FIELD_DISTANCE, FIELD_DISTANCE);
            for (Entity entity : entityList) {
                Vector distance = entity.getLocation().toVector().subtract(player.getLocation().toVector()).multiply(0.5);
                entity.setVelocity(distance);
            }
        }
        for (Player player : toRemove) {
            activeFields.remove(player);
        }
    }

    @EventHandler
    public void onProjectileHit(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player) || !activeFields.containsKey(event.getEntity())) return;
        if (activeFields.get(event.getEntity()) < System.currentTimeMillis()) {
            activeFields.remove(event.getEntity());
            return;
        }
        if (event.getCause() != EntityDamageEvent.DamageCause.PROJECTILE) return;
        event.setCancelled(true);
    }
}
