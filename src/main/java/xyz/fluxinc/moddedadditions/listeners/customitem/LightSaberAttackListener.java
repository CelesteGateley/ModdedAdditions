package xyz.fluxinc.moddedadditions.listeners.customitem;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import xyz.fluxinc.fluxcore.utils.LoreUtils;
import xyz.fluxinc.moddedadditions.ModdedAdditions;

public class LightSaberAttackListener implements Listener {

    private ModdedAdditions instance;

    /*
        If the message contains Hello There, respond appropriately
     */

    public LightSaberAttackListener(ModdedAdditions instance) {
        this.instance = instance;
    }

    @EventHandler
    public void onLightSaberHit(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player)) {return;}
        ItemStack itemStack = ((Player) event.getDamager()).getInventory().getItemInMainHand();
        if (itemStack == null || itemStack.getType() != Material.DIAMOND_SWORD) {return;}
        ItemMeta iMeta = itemStack.getItemMeta();
        if (iMeta == null || iMeta.getLore() == null) { return; }
        if (iMeta.getLore().contains(instance.getLanguageManager().getFormattedString("mi-lightsaber"))) {
            ((Player) event.getDamager()).playSound(event.getDamager().getLocation(), Sound.BLOCK_ENCHANTMENT_TABLE_USE, 1, 1);
        }
    }
}