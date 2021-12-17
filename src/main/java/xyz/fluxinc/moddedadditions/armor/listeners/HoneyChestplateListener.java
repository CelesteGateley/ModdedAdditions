package xyz.fluxinc.moddedadditions.armor.listeners;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import static xyz.fluxinc.moddedadditions.armor.SpecialArmorUtils.getHoneyChestplate;


public class HoneyChestplateListener implements Listener {

    @EventHandler
    public void onFallDamage(EntityDamageByEntityEvent event) {
        if (event.getEntityType() != EntityType.PLAYER) return;
        if (event.getCause() != EntityDamageEvent.DamageCause.ENTITY_ATTACK) return;
        if (!(event.getDamager() instanceof LivingEntity)) return;
        if (!getHoneyChestplate().verifyItemStack(((Player) event.getEntity()).getInventory().getChestplate())) return;

        new PotionEffect(PotionEffectType.SLOW, 100, 2).apply((LivingEntity) event.getDamager());
    }
}
