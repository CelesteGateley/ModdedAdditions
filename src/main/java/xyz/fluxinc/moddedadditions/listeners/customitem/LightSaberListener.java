package xyz.fluxinc.moddedadditions.listeners.customitem;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import xyz.fluxinc.fluxcore.utils.ToolUtils;
import xyz.fluxinc.moddedadditions.enums.SaberColor;

import java.util.ArrayList;

import static xyz.fluxinc.fluxcore.utils.LoreUtils.addLore;
import static xyz.fluxinc.fluxcore.utils.StringUtils.toTitleCase;
import static xyz.fluxinc.moddedadditions.ModdedAdditions.KEY_BASE;
import static xyz.fluxinc.moddedadditions.ModdedAdditions.instance;
import static xyz.fluxinc.moddedadditions.controllers.customitems.LightSaberController.*;
import static xyz.fluxinc.moddedadditions.enums.SaberColor.getChatColor;

@SuppressWarnings("ConstantConditions")
public class LightSaberListener implements Listener {


    @EventHandler
    public void onLightSaberBlockHit(BlockBreakEvent event) {
        ItemStack iStack = event.getPlayer().getInventory().getItemInMainHand();
        if (!verifyLightSaber(iStack) && !verifyDCSaber(iStack)) return;
        ItemMeta iMeta = iStack.getItemMeta();
        if (iMeta instanceof Damageable) {
            if (((Damageable) iMeta).getDamage() + 2 >= iStack.getType().getMaxDurability()) {
                iMeta.setLore(new ArrayList<>());
                iMeta.setDisplayName(ChatColor.GRAY + "Depleted Saber");
                iMeta.setUnbreakable(true);
                iMeta.setCustomModelData(verifyLightSaber(iStack) ? KEY_BASE + LS_KEY_BASE : KEY_BASE + DC_KEY_BASE);
                iStack.setItemMeta(iMeta);
                addLore(iStack, instance.getLanguageManager().getFormattedString("mi-depletedsaber"));
            }
        }
    }


    @EventHandler
    public void onLightSaberHit(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player)) {
            return;
        }
        ItemStack itemStack = ((Player) event.getDamager()).getInventory().getItemInMainHand();
        if (!verifyLightSaber(itemStack) && !verifyDCSaber(itemStack)) return;
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta.getCustomModelData() == KEY_BASE + LS_KEY_BASE || itemMeta.getCustomModelData() == KEY_BASE + DC_KEY_BASE) {
            event.setCancelled(true);
            return;
        }
        if (itemMeta instanceof Damageable) {
            if (((Damageable) itemMeta).getDamage() + 1 == itemStack.getType().getMaxDurability()) {
                itemMeta.setLore(new ArrayList<>());
                itemMeta.setDisplayName(ChatColor.GRAY + "Depleted " + ChatColor.WHITE + "Saber");
                itemMeta.setUnbreakable(true);
                itemMeta.setCustomModelData(verifyLightSaber(itemStack) ? KEY_BASE + LS_KEY_BASE : KEY_BASE + DC_KEY_BASE);
                itemStack.setItemMeta(itemMeta);
                addLore(itemStack, instance.getLanguageManager().getFormattedString("mi-depletedsaber"));
            }
        }


        if (itemStack.getType() != Material.DIAMOND_SWORD) {
            return;
        }

        ItemMeta iMeta = itemStack.getItemMeta();
        if (iMeta == null || iMeta.getLore() == null) {
            return;
        }
        if (verifyLightSaber(itemStack)) {
            if (itemStack.getType() == Material.DIAMOND_SWORD) itemStack.setType(Material.NETHERITE_SWORD);
            ((Player) event.getDamager()).playSound(event.getDamager().getLocation(), Sound.BLOCK_ENCHANTMENT_TABLE_USE, 1, 1);
        }
    }

    @EventHandler
    public void onLightSaberRepair(PrepareAnvilEvent event) {
        if (event.getInventory().getItem(0) == null || event.getInventory().getItem(1) == null) return;
        if (!verifyLightSaber(event.getInventory().getItem(0)) && !verifyDCSaber(event.getInventory().getItem(0)))
            return;
        if (event.getInventory().getItem(1).getType() == Material.NETHERITE_SWORD) {
            event.setResult(ToolUtils.transferEnchantments(event.getInventory().getItem(1), event.getInventory().getItem(0)));
            return;
        }
        if (event.getInventory().getItem(1).getType() == Material.ENCHANTED_BOOK) return;
        if (!verifyKyberCrystal(event.getInventory().getItem(1))) return;
        SaberColor saberColor = SaberColor.getModColor(event.getInventory().getItem(1).getItemMeta().getCustomModelData() - KC_KEY_BASE - KEY_BASE);
        ItemStack saber = event.getInventory().getItem(0).clone();
        event.getInventory().setRepairCost(verifyLightSaber(saber) ? 20 : 30);
        ItemMeta iMeta = saber.getItemMeta();
        iMeta.setCustomModelData(verifyLightSaber(saber) ? KEY_BASE + LS_KEY_BASE + SaberColor.getColorMod(saberColor)
                : KEY_BASE + DC_KEY_BASE + SaberColor.getColorMod(saberColor));
        iMeta.setDisplayName(verifyLightSaber(saber) ? getChatColor(saberColor) + toTitleCase(saberColor.toString()) + " Saber"
                : ChatColor.DARK_GRAY + "Black-Cored " + getChatColor(saberColor) + toTitleCase(saberColor.toString()) + " Saber");
        iMeta.setUnbreakable(false);
        iMeta.setLore(new ArrayList<>());
        ((Damageable) iMeta).setDamage(0);
        saber.setItemMeta(iMeta);
        addLore(saber, instance.getLanguageManager().getFormattedString("mi-lightsaber"));
        event.setResult(saber);
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
                        if (verifyKyberCrystal(item)) {
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
                event.getInventory().setResult(generateNewLightSaber(color));
            }
        }
    }


    @EventHandler
    public void onLightSaberUpgrade(PrepareItemCraftEvent event) {
        if (event.getInventory().getItem(4) == null) return;
        if (event.getRecipe() != null
                && event.getRecipe().getResult().getItemMeta() != null
                && event.getRecipe().getResult().getItemMeta().hasCustomModelData()
                && event.getRecipe().getResult().getItemMeta().getCustomModelData() == KEY_BASE + DC_KEY_BASE) {
            if (!verifyLightSaber(event.getInventory().getMatrix()[4])) {
                event.getInventory().setResult(null);
                return;
            }
            System.out.println("Reached");
            SaberColor color = SaberColor.getModColor(event.getInventory().getMatrix()[4].getItemMeta().getCustomModelData() - LS_KEY_BASE - KEY_BASE);
            event.getInventory().setResult(upgradeSaber(event.getInventory().getMatrix()[4], color));
        }
    }

}