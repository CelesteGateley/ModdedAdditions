package xyz.fluxinc.moddedadditions.utils;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import static xyz.fluxinc.fluxcore.utils.LoreUtils.addLore;
import static xyz.fluxinc.moddedadditions.ModdedAdditions.KEY_BASE;

public class SpecialArmorUtils {

    private static final int ARMOR_KEY = 4000;

    public static ItemStack generateNewLongFallBoots() {
        ItemStack iStack = addLore(new ItemStack(Material.IRON_BOOTS), "Boots that prevent the wearer from receiving fall damage!");
        ItemMeta iMeta = iStack.getItemMeta();
        iMeta.setCustomModelData(KEY_BASE + ARMOR_KEY + 1);
        iStack.setItemMeta(iMeta);
        return iStack;
    }

    public static boolean verifyLongFallBoots(ItemStack iStack) {
        return iStack != null &&
                iStack.getType() == Material.IRON_BOOTS &&
                iStack.getItemMeta() != null &&
                iStack.getItemMeta().hasCustomModelData() &&
                iStack.getItemMeta().getCustomModelData() == KEY_BASE + ARMOR_KEY + 1;
    }
}
