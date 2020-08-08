package xyz.fluxinc.moddedadditions.listeners.customitem.spells;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import static xyz.fluxinc.moddedadditions.spells.castable.combat.Slowball.*;

public class SlowBallListener implements Listener {

    @EventHandler
    public void onSnowballHit(ProjectileHitEvent event) {
        if (event.getEntity().getType() != EntityType.SNOWBALL) {
            return;
        }
        if (event.getEntity().getCustomName() == null) {
            return;
        }
        Entity target = event.getHitEntity();
        if (target instanceof LivingEntity) {
            switch (event.getEntity().getCustomName()) {
                case SLOWBALL_NAME:
                    new PotionEffect(PotionEffectType.SLOW, 10 * 20, 2).apply((LivingEntity) target);
                    new PotionEffect(PotionEffectType.SLOW_DIGGING, 10 * 20, 2).apply((LivingEntity) target);
                    return;
                case LONGER_SLOWBALL_NAME:
                    new PotionEffect(PotionEffectType.SLOW, 10 * 30, 2).apply((LivingEntity) target);
                    new PotionEffect(PotionEffectType.SLOW_DIGGING, 10 * 30, 2).apply((LivingEntity) target);
                    return;
                case POTENT_SLOWBALL_NAME:
                    new PotionEffect(PotionEffectType.SLOW, 10 * 30, 4).apply((LivingEntity) target);
                    new PotionEffect(PotionEffectType.SLOW_DIGGING, 10 * 30, 4).apply((LivingEntity) target);
                    return;
                default:
            }
        }
    }
}
