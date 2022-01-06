package xyz.fluxinc.moddedadditions.common;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import xyz.fluxinc.fluxcore.enums.ToolLevel;
import xyz.fluxinc.moddedadditions.areatool.AreaToolController;
import xyz.fluxinc.moddedadditions.armor.SpecialArmorUtils;
import xyz.fluxinc.moddedadditions.armor.items.ArmorSet;
import xyz.fluxinc.moddedadditions.common.simple.ElytraRepairKit;
import xyz.fluxinc.moddedadditions.common.storage.CustomItem;
import xyz.fluxinc.moddedadditions.lightsaber.SaberColor;
import xyz.fluxinc.moddedadditions.lightsaber.items.DarkSaber;
import xyz.fluxinc.moddedadditions.lightsaber.items.KyberCrystal;
import xyz.fluxinc.moddedadditions.lightsaber.items.LightSaber;
import xyz.fluxinc.moddedadditions.magnet.MagnetController;
import xyz.fluxinc.moddedadditions.sonic.SonicScrewdriverController;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import static org.bukkit.Bukkit.getServer;
import static xyz.fluxinc.moddedadditions.ModdedAdditions.instance;
import static xyz.fluxinc.moddedadditions.common.storage.AdditionalRecipeStorage.*;

public class CustomRecipeUtils implements Listener {

    private final Collection<NamespacedKey> recipeKeys;

    /*
     * Furnace Recipe takes 5 Arguments:
     * NamespacedKey (Unique Identifier)
     * Result (ItemStack)
     * Source (Material)
     * XP Granted (0.15 for Charcoal, 0.2 for Cactus, 0.35 for meats)
     * Cooking Time (200 ticks by default)
     */

    public CustomRecipeUtils() {
        recipeKeys = new ArrayList<>();

        SLAB_TO_BLOCK.forEach((slab, block) -> {
            NamespacedKey nsKey = new NamespacedKey(instance, slab.toString() + "_BACK_CONVERSION");
            recipeKeys.add(nsKey);
            ShapedRecipe slabToBlock = new ShapedRecipe(nsKey, new ItemStack(block));
            slabToBlock.shape("S", "S");
            slabToBlock.setIngredient('S', slab);
            getServer().addRecipe(slabToBlock);
        });

        processDyes(STAINED_GLASS_TO_DYE, STAINED_GLASS);
        processDyes(STAINED_GLASS_PANE_TO_DYE, STAINED_GLASS_PANE);
        processDyes(TERRACOTTA_TO_DYE, TERRACOTTA);
        processDyes(GLAZED_TERRACOTTA_TO_DYE, GLAZED_TERRACOTTA);
        processDyes(CONCRETE_TO_DYE, CONCRETE);
        processDyes(CONCRETE_POWDER_TO_DYE, CONCRETE_POWDER);

        FURNACE_RECIPE.forEach((material, result) -> {
            FurnaceRecipe smeltingRecipe = new FurnaceRecipe(
                    new NamespacedKey(instance, material.toString() + "_TO_" + result.toString()),
                    new ItemStack(result),
                    material,
                    (float) 0.2,
                    200);
            getServer().addRecipe(smeltingRecipe);
        });

        addCustomItem(MagnetController.getMagnet());
        addCustomItem(SonicScrewdriverController.getSonic());
        addCustomItem(ElytraRepairKit.getElytraRepairKit());

        addCustomItem(SpecialArmorUtils.getHoneyChestplate());
        addCustomItem(SpecialArmorUtils.getSlimeChestplate());
        addCustomItem(SpecialArmorUtils.getLongFallBoots());
        addArmorSet(SpecialArmorUtils.getCopperArmor());

        for (ToolLevel level : ToolLevel.values()) {
            addCustomItem(AreaToolController.generateExcavator(level));
            addCustomItem(AreaToolController.generateHammer(level));
        }

        for (SaberColor color : SaberColor.values()) {
            addCustomItem(new KyberCrystal(color));
        }

        addCustomItem(new LightSaber(SaberColor.DEPLETED));
        addCustomItem(new DarkSaber(SaberColor.DEPLETED));
    }

    public void addCustomItem(CustomItem customItem) {
        ShapedRecipe recipe = customItem.getRecipe();
        recipeKeys.add(recipe.getKey());
        getServer().addRecipe(recipe);
    }

    public void addArmorSet(ArmorSet armorSet) {
        addCustomItem(armorSet.getHelmet());
        addCustomItem(armorSet.getChestplate());
        addCustomItem(armorSet.getLeggings());
        addCustomItem(armorSet.getBoots());
    }

    private void processDyes(HashMap<Material, Material> dyeMap, ArrayList<Material> blockList) {
        dyeMap.forEach((block, dye) -> {
            for (Material originBlock : blockList) {
                NamespacedKey nsKey = new NamespacedKey(instance, dye.toString() + "_WITH_" + originBlock.toString());
                recipeKeys.add(nsKey);
                ShapedRecipe dyeToBlock = new ShapedRecipe(nsKey, new ItemStack(block, 8));
                dyeToBlock.shape("BBB", "BDB", "BBB");
                dyeToBlock.setIngredient('B', originBlock);
                dyeToBlock.setIngredient('D', dye);
                getServer().addRecipe(dyeToBlock);
            }
        });
    }

    @EventHandler
    public void updateKnowledgeBook(PlayerJoinEvent player) {
        player.getPlayer().discoverRecipes(recipeKeys);
    }
}
