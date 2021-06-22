package xyz.fluxinc.moddedadditions.magic.spells.recipe;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class CustomItemRecipeIngredient extends RecipeIngredient {

    private final Material type;
    private final int modelId;

    public CustomItemRecipeIngredient(ItemStack template) {
        this.type = template.getType();
        this.modelId = template.getItemMeta().getCustomModelData();
    }

    @Override
    public boolean verifyItem(ItemStack itemStack) {
        return itemStack.getType() == type &&
                itemStack.getItemMeta() != null &&
                itemStack.getItemMeta().hasCustomModelData() &&
                itemStack.getItemMeta().getCustomModelData() == modelId;
    }
}
