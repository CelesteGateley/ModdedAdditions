package xyz.fluxinc.moddedadditions.common;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import xyz.fluxinc.fluxcore.enums.ToolLevel;
import xyz.fluxinc.moddedadditions.areatool.AreaToolController;
import xyz.fluxinc.moddedadditions.armor.SpecialArmorUtils;
import xyz.fluxinc.moddedadditions.armor.items.ArmorSet;
import xyz.fluxinc.moddedadditions.common.simple.ElytraRepairKit;
import xyz.fluxinc.moddedadditions.common.storage.CustomItem;
import xyz.fluxinc.moddedadditions.lightsaber.LightSaberController;
import xyz.fluxinc.moddedadditions.magnet.MagnetController;
import xyz.fluxinc.moddedadditions.sonic.SonicScrewdriverController;
import xyz.fluxinc.moddedadditions.lightsaber.SaberColor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import static org.bukkit.Bukkit.getServer;
import static xyz.fluxinc.fluxcore.utils.LoreUtils.addLore;
import static xyz.fluxinc.moddedadditions.ModdedAdditions.KEY_BASE;
import static xyz.fluxinc.moddedadditions.ModdedAdditions.instance;
import static xyz.fluxinc.moddedadditions.lightsaber.LightSaberController.getDefaultDCLightSaber;
import static xyz.fluxinc.moddedadditions.common.storage.AdditionalRecipeStorage.*;
import static xyz.fluxinc.moddedadditions.armor.SpecialArmorUtils.*;

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

        addAreaTools();

        addCustomItem(MagnetController.getMagnet());
        addCustomItem(SonicScrewdriverController.getSonic());
        addCustomItem(ElytraRepairKit.getElytraRepairKit());

        ShapedRecipe sonicRecipe = SonicScrewdriverController.getSonic().getRecipe();
        recipeKeys.add(sonicRecipe.getKey());
        getServer().addRecipe(sonicRecipe);

        addCustomItem(SpecialArmorUtils.getHoneyChestplate());
        addCustomItem(SpecialArmorUtils.getSlimeChestplate());
        addCustomItem(SpecialArmorUtils.getLongFallBoots());
        addArmorSet(SpecialArmorUtils.getCopperArmor());

        addLightsaber();
        makeKyberCrystals();
        upgradeLightsaber();
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

    public void addAreaTools() {
        for (ToolLevel level : ToolLevel.values()) {
            addCustomItem(AreaToolController.generateExcavator(level));
            addCustomItem(AreaToolController.generateHammer(level));
        }
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

    private ShapedRecipe generateKyberCrystalRecipe(SaberColor color, String key) {
        NamespacedKey nsKey = new NamespacedKey(instance, key);
        recipeKeys.add(nsKey);

        ShapedRecipe result = new ShapedRecipe(nsKey, LightSaberController.generateNewKyberCrystal(color));
        result.shape("GEG", "ECE", "GEG");
        result.setIngredient('G', SaberColor.getStainedGlass(color));
        result.setIngredient('E', Material.EMERALD);
        result.setIngredient('C', Material.END_CRYSTAL);
        result.setGroup("Kyber Crystals");
        return result;
    }

    private void makeKyberCrystals() {
        getServer().addRecipe(generateKyberCrystalRecipe(SaberColor.BLUE, "BLUE_KYBER_CRYSTAL"));
        getServer().addRecipe(generateKyberCrystalRecipe(SaberColor.GREEN, "GREEN_KYBER_CRYSTAL"));
        getServer().addRecipe(generateKyberCrystalRecipe(SaberColor.PURPLE, "PURPLE_KYBER_CRYSTAL"));
        getServer().addRecipe(generateKyberCrystalRecipe(SaberColor.RED, "RED_KYBER_CRYSTAL"));
        getServer().addRecipe(generateKyberCrystalRecipe(SaberColor.YELLOW, "YELLOW_KYBER_CRYSTAL"));
        getServer().addRecipe(generateKyberCrystalRecipe(SaberColor.ORANGE, "ORANGE_KYBER_CRYSTAL"));
        getServer().addRecipe(generateKyberCrystalRecipe(SaberColor.WHITE, "WHITE_KYBER_CRYSTAL"));
    }

    private void addLightsaber() {
        NamespacedKey lightSaberKey = new NamespacedKey(instance, "LIGHTSABER");
        recipeKeys.add(lightSaberKey);

        ShapedRecipe lightSaberRecipe = new ShapedRecipe(lightSaberKey, LightSaberController.getDefaultLightSaber());
        lightSaberRecipe.shape("IGI", "ICI", "III");
        lightSaberRecipe.setIngredient('G', Material.GLASS_PANE);
        lightSaberRecipe.setIngredient('I', Material.IRON_BLOCK);
        lightSaberRecipe.setIngredient('C', Material.EMERALD);
        getServer().addRecipe(lightSaberRecipe);
    }

    private void upgradeLightsaber() {
        NamespacedKey darkCoreKey = new NamespacedKey(instance, "DCSABER");
        recipeKeys.add(darkCoreKey);

        ShapedRecipe darkCoreRecipe = new ShapedRecipe(darkCoreKey, getDefaultDCLightSaber());
        darkCoreRecipe.shape("SSS", "SLS", "SSS");
        darkCoreRecipe.setIngredient('S', Material.NETHERITE_SCRAP);
        darkCoreRecipe.setIngredient('L', Material.NETHERITE_SWORD);
        getServer().addRecipe(darkCoreRecipe);
    }

    @EventHandler
    public void updateKnowledgeBook(PlayerJoinEvent player) {
        player.getPlayer().discoverRecipes(recipeKeys);
    }
}
