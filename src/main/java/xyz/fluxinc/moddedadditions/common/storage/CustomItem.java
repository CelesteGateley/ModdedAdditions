package xyz.fluxinc.moddedadditions.common.storage;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import xyz.fluxinc.moddedadditions.ModdedAdditions;

import static xyz.fluxinc.fluxcore.utils.LoreUtils.addLore;

public abstract class CustomItem {

    private final int modelId;
    private final Material material;
    private final String keyName;
    private final String displayName;
    private String lore;

    public CustomItem(int modelId, Material material, String keyName, String displayName, String loreKey) {
        this.modelId = modelId;
        this.material = material;
        this.keyName = keyName;
        this.displayName = displayName;
        this.lore = ModdedAdditions.instance.getLanguageManager().getFormattedString(loreKey);
    }

    public String getKeyName() {
        return this.keyName;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public String getLore() {
        return this.lore;
    }

    public void setLore(String lore) {
        this.lore = lore;
    }

    public ItemStack getNewItem() {
        ItemStack itemStack = addLore(new ItemStack(this.material), this.lore);
        ItemMeta iMeta = itemStack.getItemMeta();
        iMeta.setCustomModelData(this.modelId);
        iMeta.setDisplayName(this.displayName);
        itemStack.setItemMeta(iMeta);
        return itemStack;
    }

    public boolean verifyItemStack(ItemStack itemStack) {
        return itemStack != null &&
                itemStack.getType() == this.material &&
                itemStack.getItemMeta() != null &&
                itemStack.getItemMeta().hasCustomModelData() &&
                itemStack.getItemMeta().getCustomModelData() == this.modelId;
    }

    public Material getMaterial() {
        return this.material;
    }

    public abstract ShapedRecipe getRecipe();
}
