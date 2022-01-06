package xyz.fluxinc.moddedadditions.lightsaber.items;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import xyz.fluxinc.moddedadditions.common.storage.CustomItem;
import xyz.fluxinc.moddedadditions.lightsaber.SaberColor;

import static xyz.fluxinc.fluxcore.utils.StringUtils.toTitleCase;
import static xyz.fluxinc.moddedadditions.ModdedAdditions.KEY_BASE;
import static xyz.fluxinc.moddedadditions.ModdedAdditions.instance;

public class KyberCrystal extends CustomItem {

    public static final int KYBER_CRYSTAL_BASE = 2010;
    private final SaberColor color;

    public KyberCrystal(int modelId, Material material, String keyName, String displayName, String loreKey) {
        super(modelId, material, keyName, displayName, loreKey);
        this.color = null;
    }

    public KyberCrystal(SaberColor color) {
        super(KEY_BASE + KYBER_CRYSTAL_BASE + color.model, Material.EMERALD, color.name() + "_KYBER_CRYSTAL", toTitleCase(color.name()) + " Kyber Crystal", "mi-kybercrystal");
        this.color = color;
    }

    public static boolean isKyberCrystal(ItemStack itemStack) {
        return itemStack.getItemMeta() != null
                && itemStack.getItemMeta().hasCustomModelData()
                && itemStack.getItemMeta().getCustomModelData() >= KEY_BASE + KYBER_CRYSTAL_BASE
                && itemStack.getItemMeta().getCustomModelData() < KEY_BASE + KYBER_CRYSTAL_BASE + 10;
    }

    public static SaberColor getColor(ItemStack itemStack) {
        ItemMeta iMeta = itemStack.getItemMeta();
        if (iMeta == null || !iMeta.hasCustomModelData()) return null;
        if (itemStack.getItemMeta().getCustomModelData() < KEY_BASE + KYBER_CRYSTAL_BASE
                && itemStack.getItemMeta().getCustomModelData() > KEY_BASE + KYBER_CRYSTAL_BASE + 10) return null;
        return SaberColor.getModColor(iMeta.getCustomModelData() - KEY_BASE - KYBER_CRYSTAL_BASE);
    }

    @Override
    public ShapedRecipe getRecipe() {
        NamespacedKey nsKey = new NamespacedKey(instance, this.getKeyName());

        ShapedRecipe result = new ShapedRecipe(nsKey, this.getNewItem());
        result.shape("GEG", "ECE", "GEG");
        result.setIngredient('G', color.component);
        result.setIngredient('E', Material.EMERALD);
        result.setIngredient('C', Material.END_CRYSTAL);
        result.setGroup("Kyber Crystals");
        return result;
    }
}
