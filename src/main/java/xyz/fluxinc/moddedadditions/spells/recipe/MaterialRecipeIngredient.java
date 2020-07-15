package xyz.fluxinc.moddedadditions.spells.recipe;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionType;

public class MaterialRecipeIngredient extends RecipeIngredient{

    private final Material type;

    public MaterialRecipeIngredient(Material type) {
        this.type = type;
    }

    @Override
    public boolean verifyItem(ItemStack itemStack) {
        return itemStack.getType() == type;
    }
}
