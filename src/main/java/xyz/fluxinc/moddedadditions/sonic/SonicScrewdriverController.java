package xyz.fluxinc.moddedadditions.sonic;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ShapedRecipe;
import xyz.fluxinc.moddedadditions.common.storage.CustomItem;

import static xyz.fluxinc.moddedadditions.ModdedAdditions.KEY_BASE;
import static xyz.fluxinc.moddedadditions.ModdedAdditions.instance;

public class SonicScrewdriverController {

    private static final int SONIC_MODEL_KEY = KEY_BASE + 9002;

    public static CustomItem getSonic() {
        return new CustomItem(SONIC_MODEL_KEY, Material.STICK, "SONIC_SCREWDRIVER", ChatColor.WHITE + "Sonic Screwdriver", "mi-sonic") {
            @Override
            public ShapedRecipe getRecipe() {
                NamespacedKey sonicKey = new NamespacedKey(instance, this.getKeyName());

                ShapedRecipe sonicRecipe = new ShapedRecipe(sonicKey, this.getNewItem());
                sonicRecipe.shape("BGB", "IRI", "BEB");
                sonicRecipe.setIngredient('B', Material.IRON_BLOCK);
                sonicRecipe.setIngredient('G', Material.LIME_STAINED_GLASS);
                sonicRecipe.setIngredient('I', Material.IRON_INGOT);
                sonicRecipe.setIngredient('R', Material.REDSTONE_TORCH);
                sonicRecipe.setIngredient('E', Material.EMERALD);
                return sonicRecipe;
            }
        };
    }
}
