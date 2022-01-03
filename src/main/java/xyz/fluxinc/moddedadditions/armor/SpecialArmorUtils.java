package xyz.fluxinc.moddedadditions.armor;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ShapedRecipe;
import xyz.fluxinc.fluxcore.enums.ArmorLevel;
import xyz.fluxinc.moddedadditions.armor.items.ArmorSet;
import xyz.fluxinc.moddedadditions.armor.items.ChestplateItem;
import xyz.fluxinc.moddedadditions.common.storage.CustomItem;

import static xyz.fluxinc.moddedadditions.ModdedAdditions.KEY_BASE;
import static xyz.fluxinc.moddedadditions.ModdedAdditions.instance;

public class SpecialArmorUtils {

    private static final int ARMOR_KEY = 4000;

    public static CustomItem getLongFallBoots() {
        return new CustomItem(KEY_BASE + ARMOR_KEY + 1, Material.IRON_BOOTS, "LONG_FALL_BOOTS", "Long Fall Boots", "mi-longfallboots") {
            @Override
            public ShapedRecipe getRecipe() {
                ShapedRecipe bootsRecipe = new ShapedRecipe(new NamespacedKey(instance, this.getKeyName()), this.getNewItem());
                bootsRecipe.shape("BFB", "BFB", "SIS");
                bootsRecipe.setIngredient('F', Material.FEATHER);
                bootsRecipe.setIngredient('B', Material.IRON_BLOCK);
                bootsRecipe.setIngredient('S', Material.SLIME_BLOCK);
                bootsRecipe.setIngredient('I', Material.IRON_BOOTS);
                return bootsRecipe;
            }
        };
    }

    public static CustomItem getHoneyChestplate() {
        return new ChestplateItem(KEY_BASE + ARMOR_KEY + 2, Material.CHAINMAIL_CHESTPLATE, Material.HONEY_BLOCK,
                "HONEY_CHESTPLATE", "Honey Chestplate", "mi-honeychestplate");
    }

    public static CustomItem getSlimeChestplate() {
        return new ChestplateItem(KEY_BASE + ARMOR_KEY + 3, Material.CHAINMAIL_CHESTPLATE, Material.SLIME_BLOCK,
                "SLIME_CHESTPLATE", "Slime Chestplate", "mi-slimechestplate");
    }

    public static ArmorSet getCopperArmor() {
        return new ArmorSet(ArmorLevel.IRON, KEY_BASE + ARMOR_KEY + 4, Material.COPPER_INGOT, "COPPER", "Copper", "mi-copperArmor");
    }
}
