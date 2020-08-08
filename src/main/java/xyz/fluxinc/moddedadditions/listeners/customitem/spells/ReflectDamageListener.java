package xyz.fluxinc.moddedadditions.listeners.customitem.spells;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.HashMap;
import java.util.Map;

public class ReflectDamageListener implements Listener {

    private final Map<Player, Long> reflected;

    public ReflectDamageListener() {
        reflected = new HashMap<>();
    }

    public void addReflected(Player player, int seconds) {
        reflected.put(player, System.currentTimeMillis() + (seconds*1000));
    }

    @EventHandler
    public void onDamageTaken(EntityDamageByEntityEvent event) {
        long time = System.currentTimeMillis();
        if (event.getEntity() instanceof Player) {
            if (!reflected.containsKey(event.getEntity())) return;
            if (reflected.get(event.getEntity()) > time) {
                event.setCancelled(true);
                if (event.getDamager() instanceof LivingEntity) {
                    ((LivingEntity) event.getDamager()).damage(event.getFinalDamage(), event.getEntity());
                }
            }
        }
    }
}
