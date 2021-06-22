package xyz.fluxinc.moddedadditions.magic.spells.recipe;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class MaterialRecipeIngredient extends RecipeIngredient {

    private final Material type;

    public MaterialRecipeIngredient(Material type) {
        this.type = type;
    }

    @Override
    public boolean verifyItem(ItemStack itemStack) {
        return itemStack.getType() == type;
    }
}
