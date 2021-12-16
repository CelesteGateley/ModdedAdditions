package xyz.fluxinc.moddedadditions.areatool;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ShapedRecipe;
import xyz.fluxinc.moddedadditions.common.storage.CustomItem;

import static xyz.fluxinc.moddedadditions.ModdedAdditions.instance;

public class AreaToolItem extends CustomItem {

    public AreaToolItem(int modelId, Material material, String keyName, String displayName, String loreKey) {
        super(modelId, material, keyName, displayName, loreKey);
    }

    @Override
    public ShapedRecipe getRecipe() {
        NamespacedKey nsKey = new NamespacedKey(instance, this.getKeyName().toUpperCase());
        ShapedRecipe result = new ShapedRecipe(nsKey, this.getNewItem());
        result.shape("PPP", " S ", " S ");
        result.setIngredient('S', Material.STICK);
        result.setIngredient('P', this.getMaterial());
        return result;
    }
}
