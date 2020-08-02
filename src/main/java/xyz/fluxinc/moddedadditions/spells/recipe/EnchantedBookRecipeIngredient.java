package xyz.fluxinc.moddedadditions.spells.recipe;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;

public class EnchantedBookRecipeIngredient extends RecipeIngredient {

    private final Enchantment type;

    public EnchantedBookRecipeIngredient(Enchantment type) {
        this.type = type;
    }

    @Override
    public boolean verifyItem(ItemStack itemStack) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta.getEnchants().containsKey(type)) return true;
        if (!(itemMeta instanceof EnchantmentStorageMeta)) return false;
        return ((EnchantmentStorageMeta) itemMeta).getStoredEnchants().containsKey(type);
    }
}
