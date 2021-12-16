package xyz.fluxinc.moddedadditions.armor.listeners;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import static xyz.fluxinc.moddedadditions.armor.SpecialArmorUtils.*;

public class CopperArmorListener implements Listener {

    @EventHandler
    public void onFallDamage(EntityDamageByEntityEvent event) {
        if (event.getEntityType() != EntityType.PLAYER) return;

        if (!verifyCopperHelmet(((Player) event.getEntity()).getInventory().getHelmet())) return;

        if (!verifyCopperChestplate(((Player) event.getEntity()).getInventory().getChestplate())) return;

        if (!verifyCopperLeggings(((Player) event.getEntity()).getInventory().getLeggings())) return;

        if (!verifyCopperBoots(((Player) event.getEntity()).getInventory().getBoots())) return;

        new PotionEffect(PotionEffectType.SLOW, 50, 3).apply(((Player) event.getEntity())); //Needs correction
    }

}
