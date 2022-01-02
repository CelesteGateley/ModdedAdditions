package xyz.fluxinc.moddedadditions.armor.listeners;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import xyz.fluxinc.moddedadditions.armor.SpecialArmorUtils;

import static xyz.fluxinc.moddedadditions.armor.SpecialArmorUtils.*;

public class CopperArmorListener implements Listener {

    @EventHandler
    public void onFallDamage(EntityDamageByEntityEvent event) {
        if (event.getEntityType() != EntityType.PLAYER) return;
        if (!SpecialArmorUtils.getCopperArmor().isWearingFull((HumanEntity) event.getEntity())) return;

        new PotionEffect(PotionEffectType.SLOW, 50, 3).apply(((Player) event.getEntity())); //Needs correction
    }

}
