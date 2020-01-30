package xyz.fluxinc.moddedadditions.controllers.customitems;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import xyz.fluxinc.moddedadditions.ModdedAdditions;
import xyz.fluxinc.moddedadditions.enums.SaberColor;

import java.util.UUID;

import static xyz.fluxinc.fluxcore.utils.LoreUtils.addLore;
import static xyz.fluxinc.fluxcore.utils.StringUtils.toTitleCase;
import static xyz.fluxinc.moddedadditions.ModdedAdditions.KEY_BASE;
import static xyz.fluxinc.moddedadditions.enums.SaberColor.getChatColor;
import static xyz.fluxinc.moddedadditions.enums.SaberColor.getColorMod;

public class LightSaberController {

    public static final int LS_KEY_BASE = 2020;
    public static final int KC_KEY_BASE = 2010;
    private ModdedAdditions instance;

    public LightSaberController(ModdedAdditions instance) {
        this.instance = instance;
    }

    public ItemStack generateNewLightSaber(SaberColor color) {
        ItemStack itemStack = new ItemStack(Material.DIAMOND_SWORD);
        itemStack = addLore(itemStack, instance.getLanguageManager().getFormattedString("mi-lightsaber"));
        ItemMeta iMeta = itemStack.getItemMeta();
        iMeta.setCustomModelData(KEY_BASE + LS_KEY_BASE + getColorMod(color));
        iMeta.setDisplayName(getChatColor(color) + toTitleCase(color.toString()) + " Saber");
        iMeta.addEnchant(Enchantment.FIRE_ASPECT, 1, false);
        iMeta.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, new AttributeModifier(UUID.randomUUID(), "Attack_damage", 13, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND));
        itemStack.setItemMeta(iMeta);
        return itemStack;
    }

    public ItemStack generateNewKyberCrystal(SaberColor color) {
        ItemStack itemStack = new ItemStack(Material.EMERALD);
        itemStack = addLore(itemStack, instance.getLanguageManager().getFormattedString("mi-kybercrystal"));
        ItemMeta iMeta = itemStack.getItemMeta();
        iMeta.addEnchant(Enchantment.LUCK, 1, true);
        iMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        iMeta.setCustomModelData(KEY_BASE + KC_KEY_BASE + getColorMod(color));
        iMeta.setDisplayName(getChatColor(color) + toTitleCase(color.toString()) + " Kyber Crystal");
        itemStack.setItemMeta(iMeta);
        return itemStack;
    }

    public static boolean verifyLightSaber(ItemStack itemStack) {
        return itemStack.getItemMeta() != null
                && itemStack.getItemMeta().hasCustomModelData()
                && itemStack.getItemMeta().getCustomModelData() > KEY_BASE + LS_KEY_BASE
                && itemStack.getItemMeta().getCustomModelData() < KEY_BASE + LS_KEY_BASE + 10;
    }

    public static boolean verifyKyberCrystal(ItemStack itemStack) {
        return itemStack.getItemMeta() != null
                && itemStack.getItemMeta().hasCustomModelData()
                && itemStack.getItemMeta().getCustomModelData() > KEY_BASE + KC_KEY_BASE
                && itemStack.getItemMeta().getCustomModelData() < KEY_BASE + KC_KEY_BASE + 10;
    }

    public ItemStack getDefaultLightSaber() {
        ItemStack lightSaber = new ItemStack(Material.DIAMOND_SWORD);
        lightSaber = addLore(lightSaber, ChatColor.translateAlternateColorCodes('&', instance.getLanguageManager().getFormattedString("mi-lightsaber")));
        ItemMeta itemMeta = lightSaber.getItemMeta();
        itemMeta.setCustomModelData(KEY_BASE + LS_KEY_BASE);
        lightSaber.setItemMeta(itemMeta);
        return lightSaber;
    }


}
