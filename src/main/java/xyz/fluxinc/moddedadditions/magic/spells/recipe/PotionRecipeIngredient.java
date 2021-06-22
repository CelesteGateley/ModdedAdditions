package xyz.fluxinc.moddedadditions.magic.spells.recipe;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionType;

public class PotionRecipeIngredient extends RecipeIngredient {

    private final PotionType type;

    public PotionRecipeIngredient(PotionType type) {
        this.type = type;
    }

    @Override
    public boolean verifyItem(ItemStack itemStack) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null) return false;
        if (!(itemMeta instanceof PotionMeta)) return false;
        PotionMeta potMeta = (PotionMeta) itemMeta;
        return potMeta.getBasePotionData().getType() == type;
    }
}
