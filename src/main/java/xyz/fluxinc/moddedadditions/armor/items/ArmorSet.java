package xyz.fluxinc.moddedadditions.armor.items;

import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.ItemStack;
import xyz.fluxinc.fluxcore.enums.ArmorLevel;
import xyz.fluxinc.moddedadditions.ModdedAdditions;
import xyz.fluxinc.moddedadditions.common.storage.CustomItem;

public class ArmorSet {

    private final ArmorLevel armorLevel;
    private final int modelIdBase;
    private final Material craftingItem;
    private final String keyPrefix;
    private final String displayPrefix;
    private final String lore;

    public ArmorSet(ArmorLevel armorLevel, int modelIdBase, Material craftingItem, String keyPrefix, String displayPrefix, String loreKey) {
        this.armorLevel = armorLevel;
        this.modelIdBase = modelIdBase;
        this.craftingItem = craftingItem;
        this.keyPrefix = keyPrefix;
        this.displayPrefix = displayPrefix;
        this.lore = loreKey;
    }

    public CustomItem getHelmet() {
        return new HelmetItem(this.modelIdBase, this.armorLevel.helmet, this.craftingItem, this.keyPrefix + "_HELMET", this.displayPrefix + " Helmet", this.lore) {
            @Override
            public ItemStack modifyItemStack(ItemStack itemStack) {
                return modifyHelmet(itemStack);
            }
        };
    }

    public CustomItem getChestplate() {
        return new ChestplateItem(this.modelIdBase, this.armorLevel.chestplate, this.craftingItem, this.keyPrefix + "_CHESTPLATE", this.displayPrefix + " Chestplate", this.lore) {
            @Override
            public ItemStack modifyItemStack(ItemStack itemStack) {
                return modifyChestplate(itemStack);
            }
        };
    }

    public CustomItem getLeggings() {
        return new LeggingsItem(this.modelIdBase, this.armorLevel.leggings, this.craftingItem, this.keyPrefix + "_LEGGINGS", this.displayPrefix + " Leggings", this.lore) {
            @Override
            public ItemStack modifyItemStack(ItemStack itemStack) {
                return modifyLeggings(itemStack);
            }
        };
    }

    public CustomItem getBoots() {
        return new BootsItem(this.modelIdBase, this.armorLevel.boots, this.craftingItem, this.keyPrefix + "_BOOTS", this.displayPrefix + " Boots", this.lore) {
            @Override
            public ItemStack modifyItemStack(ItemStack itemStack) {
                return modifyBoots(itemStack);
            }
        };
    }

    public ItemStack modifyHelmet(ItemStack itemStack) {
        return itemStack;
    }

    public ItemStack modifyChestplate(ItemStack itemStack) {
        return itemStack;
    }

    public ItemStack modifyLeggings(ItemStack itemStack) {
        return itemStack;
    }

    public ItemStack modifyBoots(ItemStack itemStack) {
        return itemStack;
    }

    public boolean isWearingHelmet(HumanEntity entity) {
        return getHelmet().verifyItemStack(entity.getInventory().getHelmet());
    }

    public boolean isWearingChestplate(HumanEntity entity) {
        return getChestplate().verifyItemStack(entity.getInventory().getChestplate());
    }

    public boolean isWearingLeggings(HumanEntity entity) {
        return getLeggings().verifyItemStack(entity.getInventory().getLeggings());
    }

    public boolean isWearingBoots(HumanEntity entity) {
        return getBoots().verifyItemStack(entity.getInventory().getBoots());
    }

    public boolean isWearingAny(HumanEntity entity) {
        return isWearingHelmet(entity) || isWearingChestplate(entity) || isWearingLeggings(entity) || isWearingBoots(entity);
    }

    public boolean isWearingFull(HumanEntity entity) {
        return isWearingHelmet(entity) && isWearingChestplate(entity) && isWearingLeggings(entity) && isWearingBoots(entity);
    }
}
