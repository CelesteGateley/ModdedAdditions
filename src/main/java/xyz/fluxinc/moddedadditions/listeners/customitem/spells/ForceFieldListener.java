package xyz.fluxinc.moddedadditions.listeners.customitem.spells;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.util.Vector;
import xyz.fluxinc.moddedadditions.ModdedAdditions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ForceFieldListener implements Runnable, Listener {

    private final Map<Player, Long> activeFields;
    private static final int FIELD_DISTANCE = 4;

    public ForceFieldListener() {
        activeFields = new HashMap<>();
        ModdedAdditions.instance.getServer().getScheduler().scheduleSyncRepeatingTask(ModdedAdditions.instance, this, 20, 20);
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
                if (entity.getType() == EntityType.PLAYER) continue;
                if (!(entity instanceof LivingEntity)) continue;
                Vector distance = entity.getLocation().toVector().subtract(player.getLocation().toVector()).multiply(0.5);
                double x = distance.getX() < 0 ? (-FIELD_DISTANCE + distance.getX()) / 4d : (FIELD_DISTANCE - distance.getX()) / 4d;
                double z = distance.getZ() < 0 ? (-FIELD_DISTANCE + distance.getZ()) / 4d : (FIELD_DISTANCE - distance.getZ()) / 4d;
                entity.setVelocity(new Vector(x, 0.5, z));
            }
        }
        for (Player player : toRemove) {
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("Your force field has lost it's strength"));
            activeFields.remove(player);
        }
    }

    @EventHandler
    public void onProjectileHit(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player) || !activeFields.containsKey(event.getEntity())) return;
        if (activeFields.get(event.getEntity()) < System.currentTimeMillis()) {
            activeFields.remove(event.getEntity());
            ((Player) event.getEntity()).spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("Your force field has lost it's strength"));
            return;
        }
        if (event.getCause() != EntityDamageEvent.DamageCause.PROJECTILE) return;
        event.setCancelled(true);
    }
}
