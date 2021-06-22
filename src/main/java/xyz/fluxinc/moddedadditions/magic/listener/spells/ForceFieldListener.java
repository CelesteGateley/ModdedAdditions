package xyz.fluxinc.moddedadditions.magic.listener.spells;

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

    private final Map<Player, ForceFieldInstance> activeFields;

    public ForceFieldListener() {
        activeFields = new HashMap<>();
        ModdedAdditions.instance.getServer().getScheduler().scheduleSyncRepeatingTask(ModdedAdditions.instance, this, 20, 20);
    }

    public void addForceField(Player player, int radius, long duration) {
        activeFields.put(player, new ForceFieldInstance(radius, System.currentTimeMillis() + duration * 1000));
    }

    @Override
    public void run() {
        List<Player> toRemove = new ArrayList<>();
        for (Player player : activeFields.keySet()) {
            if (activeFields.get(player).endTime < System.currentTimeMillis()) {
                toRemove.add(player);
                continue;
            }
            List<Entity> entityList = player.getNearbyEntities(activeFields.get(player).radius, activeFields.get(player).radius, activeFields.get(player).radius);
            for (Entity entity : entityList) {
                if (entity.getType() == EntityType.PLAYER) continue;
                if (!(entity instanceof LivingEntity)) continue;
                Vector distance = entity.getLocation().toVector().subtract(player.getLocation().toVector()).multiply(0.5);
                double x = distance.getX() < 0 ? (-activeFields.get(player).radius + distance.getX()) / 4d : (activeFields.get(player).radius - distance.getX()) / 4d;
                double z = distance.getZ() < 0 ? (-activeFields.get(player).radius + distance.getZ()) / 4d : (activeFields.get(player).radius - distance.getZ()) / 4d;
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
        if (activeFields.get(event.getEntity()).endTime < System.currentTimeMillis()) {
            activeFields.remove(event.getEntity());
            ((Player) event.getEntity()).spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("Your force field has lost it's strength"));
            return;
        }
        if (event.getCause() != EntityDamageEvent.DamageCause.PROJECTILE) return;
        event.setCancelled(true);
    }

    private static class ForceFieldInstance {
        public int radius;
        public long endTime;

        public ForceFieldInstance(int radius, long endTime) {
            this.radius = radius;
            this.endTime = endTime;
        }
    }
}
