package xyz.fluxinc.moddedadditions.spells.recipe;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class EnchantedBookRecipeIngredient extends RecipeIngredient {

    private final Enchantment type;

    public EnchantedBookRecipeIngredient(Enchantment type) {
        this.type = type;
    }

    @Override
    public boolean verifyItem(ItemStack itemStack) {
        if (itemStack.getType() != Material.ENCHANTED_BOOK) return false;
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null) return false;
        return itemMeta.getEnchants().containsKey(type);
    }
}
