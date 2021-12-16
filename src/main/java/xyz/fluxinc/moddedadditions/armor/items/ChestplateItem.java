package xyz.fluxinc.moddedadditions.armor.items;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import xyz.fluxinc.moddedadditions.common.storage.CustomItem;

import static org.bukkit.Bukkit.getServer;
import static xyz.fluxinc.moddedadditions.ModdedAdditions.instance;
import static xyz.fluxinc.moddedadditions.armor.SpecialArmorUtils.generateHoneyChestplate;

public class ChestplateItem extends CustomItem {

    private final Material craftingMaterial;

    public ChestplateItem(int modelId, Material material, Material craftingMaterial, String keyName, String displayName, String loreKey) {
        super(modelId, material, keyName, displayName, loreKey);
        this.craftingMaterial = craftingMaterial;
    }

    @Override
    public ShapedRecipe getRecipe() {
        ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(instance, this.getKeyName()), this.getNewItem());
        recipe.shape("A A", "AAA", "AAA");
        recipe.setIngredient('A', this.craftingMaterial);
        return recipe;
    }
}
