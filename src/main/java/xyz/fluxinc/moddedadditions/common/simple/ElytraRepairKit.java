package xyz.fluxinc.moddedadditions.common.simple;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import xyz.fluxinc.moddedadditions.common.storage.CustomItem;

import static org.bukkit.Bukkit.getServer;
import static xyz.fluxinc.moddedadditions.ModdedAdditions.KEY_BASE;
import static xyz.fluxinc.moddedadditions.ModdedAdditions.instance;

public class ElytraRepairKit extends CustomItem {

    public static CustomItem getElytraRepairKit() {
        return new ElytraRepairKit(KEY_BASE + 9000 + 3, Material.PHANTOM_MEMBRANE, "ELYTRA_REPAIR_KIT", "Elytra Repair Kit", "mi-elytraKit");
    }

    public ElytraRepairKit(int modelId, Material material, String keyName, String displayName, String loreKey) {
        super(modelId, material, keyName, displayName, loreKey);
    }

    @Override
    public ShapedRecipe getRecipe() {
        NamespacedKey key = new NamespacedKey(instance, this.getKeyName());
        ShapedRecipe recipe = new ShapedRecipe(key, this.getNewItem());
        recipe.shape("LLL", "LCL", "LLL");
        recipe.setIngredient('L', Material.LEATHER);
        recipe.setIngredient('C', Material.CHORUS_FRUIT);
        return recipe;
    }
}
