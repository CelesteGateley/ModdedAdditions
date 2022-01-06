package xyz.fluxinc.moddedadditions.lightsaber.items;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import xyz.fluxinc.moddedadditions.common.storage.CustomItem;
import xyz.fluxinc.moddedadditions.lightsaber.SaberColor;

import java.util.ArrayList;
import java.util.UUID;

import static xyz.fluxinc.fluxcore.utils.LoreUtils.addLore;
import static xyz.fluxinc.fluxcore.utils.StringUtils.toTitleCase;
import static xyz.fluxinc.moddedadditions.ModdedAdditions.KEY_BASE;
import static xyz.fluxinc.moddedadditions.ModdedAdditions.instance;

public class DarkSaber extends CustomItem {

    public static final int DARK_SABER_BASE = 2030;
    private final SaberColor color;

    public DarkSaber(int modelId, Material material, String keyName, String displayName, String loreKey) {
        super(modelId, material, keyName, displayName, loreKey);
        this.color = null;
    }

    public DarkSaber(SaberColor color) {
        super(KEY_BASE + DARK_SABER_BASE + color.model, Material.NETHERITE_SWORD, color.name() + "_DARK_SABER",
                ChatColor.DARK_GRAY + "Black-Cored " + color.chatColor + toTitleCase(color.name()) + " Saber", "mi-lightsaber");
        this.color = color;
    }

    public static boolean isDarkSaber(ItemStack itemStack) {
        return itemStack.getItemMeta() != null
                && itemStack.getItemMeta().hasCustomModelData()
                && itemStack.getItemMeta().getCustomModelData() >= KEY_BASE + DARK_SABER_BASE
                && itemStack.getItemMeta().getCustomModelData() < KEY_BASE + DARK_SABER_BASE + 10;
    }

    public static SaberColor getColor(ItemStack itemStack) {
        ItemMeta iMeta = itemStack.getItemMeta();
        if (iMeta == null || iMeta.hasCustomModelData()) return null;
        if (itemStack.getItemMeta().getCustomModelData() >= KEY_BASE + DARK_SABER_BASE
                && itemStack.getItemMeta().getCustomModelData() < KEY_BASE + DARK_SABER_BASE + 10) return null;
        return SaberColor.getModColor(iMeta.getCustomModelData() - KEY_BASE - DARK_SABER_BASE);
    }

    public static ItemStack upgradeFromLightSaber(ItemStack saber) {
        SaberColor color = LightSaber.getColor(saber);
        if (color == null) return saber;
        ItemStack itemStack = saber.clone();
        ItemMeta iMeta = itemStack.getItemMeta();
        iMeta.setCustomModelData(KEY_BASE + DarkSaber.DARK_SABER_BASE + color.model);
        iMeta.setDisplayName(ChatColor.DARK_GRAY + "Black-Cored " + color.chatColor + toTitleCase(color.toString()) + " Saber");
        iMeta.removeAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE);
        iMeta.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, new AttributeModifier(UUID.randomUUID(), "Attack_damage", 15, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND));
        itemStack.setItemMeta(iMeta);
        return itemStack;
    }

    public static ItemStack depleteSaber(ItemStack itemStack) {
        ItemMeta iMeta = itemStack.getItemMeta();
        iMeta.setLore(new ArrayList<>());
        iMeta.setDisplayName(ChatColor.GRAY + "Depleted Saber");
        iMeta.setUnbreakable(true);
        iMeta.setCustomModelData(KEY_BASE + DARK_SABER_BASE + SaberColor.DEPLETED.model);
        itemStack.setItemMeta(iMeta);
        addLore(itemStack, instance.getLanguageManager().getFormattedString("mi-depletedsaber"));
        return itemStack;
    }

    public static ItemStack repairSaber(ItemStack itemStack, SaberColor color) {
        ItemMeta iMeta = itemStack.getItemMeta();
        iMeta.setCustomModelData(KEY_BASE + DARK_SABER_BASE + color.model);
        iMeta.setDisplayName(ChatColor.DARK_GRAY + "Black-Cored " + color.chatColor + toTitleCase(color.toString()) + " Saber");
        iMeta.setUnbreakable(false);
        iMeta.setLore(new ArrayList<>());
        ((Damageable) iMeta).setDamage(0);
        itemStack.setItemMeta(iMeta);
        addLore(itemStack, instance.getLanguageManager().getFormattedString("mi-lightsaber"));
        return itemStack;
    }

    @Override
    public ShapedRecipe getRecipe() {
        NamespacedKey key = new NamespacedKey(instance, this.getKeyName());

        ShapedRecipe recipe = new ShapedRecipe(key, this.getNewItem());
        recipe.shape("SSS", "SLS", "SSS");
        recipe.setIngredient('S', Material.NETHERITE_SCRAP);
        recipe.setIngredient('L', Material.NETHERITE_SWORD);
        return recipe;
    }

    @Override
    public ItemStack modifyItemStack(ItemStack itemStack) {
        ItemMeta iMeta = itemStack.getItemMeta();
        iMeta.addEnchant(Enchantment.FIRE_ASPECT, 2, false);
        iMeta.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, new AttributeModifier(UUID.randomUUID(), "Attack_damage", 15, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND));
        itemStack.setItemMeta(iMeta);
        return itemStack;
    }
}
