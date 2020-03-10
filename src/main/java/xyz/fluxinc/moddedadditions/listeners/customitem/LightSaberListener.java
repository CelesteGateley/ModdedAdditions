package xyz.fluxinc.moddedadditions.listeners.customitem;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import xyz.fluxinc.moddedadditions.ModdedAdditions;
import xyz.fluxinc.moddedadditions.enums.SaberColor;

import static xyz.fluxinc.moddedadditions.ModdedAdditions.KEY_BASE;
import static xyz.fluxinc.moddedadditions.controllers.customitems.LightSaberController.*;

public class LightSaberListener implements Listener {

    private ModdedAdditions instance;

    public LightSaberListener(ModdedAdditions instance) {
        this.instance = instance;
    }

    @EventHandler
    public void onLightSaberHit(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player)) {return;}
        ItemStack itemStack = ((Player) event.getDamager()).getInventory().getItemInMainHand();
        if (itemStack.getType() != Material.DIAMOND_SWORD) {return;}
        ItemMeta iMeta = itemStack.getItemMeta();
        if (iMeta == null || iMeta.getLore() == null) { return; }
        if (verifyLightSaber(itemStack)) {
            ((Player) event.getDamager()).playSound(event.getDamager().getLocation(), Sound.BLOCK_ENCHANTMENT_TABLE_USE, 1, 1);
        }
    }

    @EventHandler
    public void onLightSaberCraft(PrepareItemCraftEvent event) {
        boolean containsKyberCrystal = false;
        SaberColor color = null;

        if (event.getRecipe() != null
                && event.getRecipe().getResult().getItemMeta() != null
                && event.getRecipe().getResult().getItemMeta().hasCustomModelData()
                && event.getRecipe().getResult().getItemMeta().getCustomModelData() == KEY_BASE + LS_KEY_BASE) {
            for (ItemStack item : event.getInventory().getMatrix()) {
                if (item != null && item.getType().equals(Material.EMERALD)) {
                    if (item.getItemMeta() != null && item.getItemMeta().hasCustomModelData()) {
                        if (verifyKyberCrystal(item)){
                            containsKyberCrystal = true;
                            color = SaberColor.getModColor(item.getItemMeta().getCustomModelData() - KC_KEY_BASE - KEY_BASE);
                            break;
                        }
                    }
                }
            }
            if (!containsKyberCrystal) {
                event.getInventory().setResult(null);
            } else {
                event.getInventory().setResult(instance.getLightSaberController().generateNewLightSaber(color));
            }
        }
    }
}