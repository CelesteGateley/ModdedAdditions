package xyz.fluxinc.moddedadditions.listeners.customitem.armor;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.util.Vector;

import static xyz.fluxinc.moddedadditions.utils.SpecialArmorUtils.verifySlimeChestplate;

public class SlimeChestplateListener implements Listener {

    @EventHandler
    public void onFallDamage(EntityDamageByEntityEvent event) {
        if (event.getEntityType() != EntityType.PLAYER) { return; }
        if (event.getCause() != EntityDamageEvent.DamageCause.ENTITY_ATTACK)
        if (!(event.getDamager() instanceof LivingEntity)) { return; }
        if (!verifySlimeChestplate(((Player) event.getEntity()).getInventory().getChestplate())) { return; }
        LivingEntity entity = (LivingEntity) event.getDamager();
        entity.setVelocity(entity.getEyeLocation().getDirection().multiply(new Vector(-1, 0, -1)).multiply(new Vector(2, 1, 2)));
    }
}
