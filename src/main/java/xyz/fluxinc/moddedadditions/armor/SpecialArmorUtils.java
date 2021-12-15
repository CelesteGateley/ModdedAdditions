package xyz.fluxinc.moddedadditions.armor;

import org.bukkit.ChatColor;
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
        iMeta.setDisplayName(ChatColor.RESET + "Long Fall Boots");
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

    public static ItemStack generateHoneyChestplate() {
        ItemStack iStack = addLore(new ItemStack(Material.CHAINMAIL_CHESTPLATE), "Leaves a sticky residue on attackers, slowing them down!");
        ItemMeta iMeta = iStack.getItemMeta();
        iMeta.setCustomModelData(KEY_BASE + ARMOR_KEY + 2);
        iMeta.setDisplayName(ChatColor.RESET + "Honey Chestplate");
        iStack.setItemMeta(iMeta);
        return iStack;
    }

    public static boolean verifyHoneyChestplate(ItemStack iStack) {
        return iStack != null &&
                iStack.getType() == Material.CHAINMAIL_CHESTPLATE &&
                iStack.getItemMeta() != null &&
                iStack.getItemMeta().hasCustomModelData() &&
                iStack.getItemMeta().getCustomModelData() == KEY_BASE + ARMOR_KEY + 2;
    }

    public static ItemStack generateSlimeChestplate() {
        ItemStack iStack = addLore(new ItemStack(Material.CHAINMAIL_CHESTPLATE), "A bouncy chestplate, knocking back attackers!");
        ItemMeta iMeta = iStack.getItemMeta();
        iMeta.setDisplayName(ChatColor.RESET + "Slime Chestplate");
        iMeta.setCustomModelData(KEY_BASE + ARMOR_KEY + 3);
        iStack.setItemMeta(iMeta);
        return iStack;
    }

    public static boolean verifySlimeChestplate(ItemStack iStack) {
        return iStack != null &&
                iStack.getType() == Material.CHAINMAIL_CHESTPLATE &&
                iStack.getItemMeta() != null &&
                iStack.getItemMeta().hasCustomModelData() &&
                iStack.getItemMeta().getCustomModelData() == KEY_BASE + ARMOR_KEY + 3;
    }

    public static ItemStack generateCopperHelmet() {
        ItemStack iStack = addLore(new ItemStack(Material.IRON_HELMET), "Finally a use for Copper!");
        ItemMeta iMeta = iStack.getItemMeta();
        iMeta.setCustomModelData(KEY_BASE + ARMOR_KEY + 4);
        iMeta.setDisplayName(ChatColor.RESET + "Copper Helmet");
        iStack.setItemMeta(iMeta);
        return iStack;
    }

    public static boolean verifyCopperHelmet(ItemStack iStack) {
        return iStack != null &&
                iStack.getType() == Material.IRON_HELMET &&
                iStack.getItemMeta() != null &&
                iStack.getItemMeta().hasCustomModelData() &&
                iStack.getItemMeta().getCustomModelData() == KEY_BASE + ARMOR_KEY + 4;
    }

    public static ItemStack generateCopperChestplate() {
        ItemStack iStack = addLore(new ItemStack(Material.IRON_CHESTPLATE), "Finally a use for Copper!");
        ItemMeta iMeta = iStack.getItemMeta();
        iMeta.setCustomModelData(KEY_BASE + ARMOR_KEY + 5);
        iMeta.setDisplayName(ChatColor.RESET + "Copper Chestplate");
        iStack.setItemMeta(iMeta);
        return iStack;
    }

    public static boolean verifyCopperChestplate(ItemStack iStack) {
        return iStack != null &&
                iStack.getType() == Material.IRON_CHESTPLATE &&
                iStack.getItemMeta() != null &&
                iStack.getItemMeta().hasCustomModelData() &&
                iStack.getItemMeta().getCustomModelData() == KEY_BASE + ARMOR_KEY + 5;
    }

    public static boolean verifyCopperLeggings(ItemStack iStack) {
        return iStack != null &&
                iStack.getType() == Material.IRON_LEGGINGS &&
                iStack.getItemMeta() != null &&
                iStack.getItemMeta().hasCustomModelData() &&
                iStack.getItemMeta().getCustomModelData() == KEY_BASE + ARMOR_KEY + 6;
    }

    public static ItemStack generateCopperLeggings() {
        ItemStack iStack = addLore(new ItemStack(Material.IRON_LEGGINGS), "Finally a use for Copper!");
        ItemMeta iMeta = iStack.getItemMeta();
        iMeta.setCustomModelData(KEY_BASE + ARMOR_KEY + 6);
        iMeta.setDisplayName(ChatColor.RESET + "Copper Leggings");
        iStack.setItemMeta(iMeta);
        return iStack;
    }

    public static boolean verifyCopperBoots(ItemStack iStack) {
        return iStack != null &&
                iStack.getType() == Material.IRON_BOOTS &&
                iStack.getItemMeta() != null &&
                iStack.getItemMeta().hasCustomModelData() &&
                iStack.getItemMeta().getCustomModelData() == KEY_BASE + ARMOR_KEY + 7;
    }

    public static ItemStack generateCopperBoots() {
        ItemStack iStack = addLore(new ItemStack(Material.IRON_BOOTS), "Finally a use for Copper!");
        ItemMeta iMeta = iStack.getItemMeta();
        iMeta.setCustomModelData(KEY_BASE + ARMOR_KEY + 7);
        iMeta.setDisplayName(ChatColor.RESET + "Copper Leggings");
        iStack.setItemMeta(iMeta);
        return iStack;
    }

}
