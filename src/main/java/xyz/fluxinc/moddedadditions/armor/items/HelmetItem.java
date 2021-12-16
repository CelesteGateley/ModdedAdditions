package xyz.fluxinc.moddedadditions.armor.items;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ShapedRecipe;
import xyz.fluxinc.moddedadditions.common.storage.CustomItem;

import static xyz.fluxinc.moddedadditions.ModdedAdditions.instance;

public class HelmetItem extends CustomItem {

    private final Material craftingMaterial;

    public HelmetItem(int modelId, Material material, Material craftingMaterial, String keyName, String displayName, String loreKey) {
        super(modelId, material, keyName, displayName, loreKey);
        this.craftingMaterial = craftingMaterial;
    }

    @Override
    public ShapedRecipe getRecipe() {
        ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(instance, this.getKeyName()), this.getNewItem());
        recipe.shape("AAA", "A A");
        recipe.setIngredient('A', this.craftingMaterial);
        return recipe;
    }
}
