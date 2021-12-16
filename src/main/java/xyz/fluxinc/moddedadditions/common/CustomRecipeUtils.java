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

        //Adding Hammer/Excavator recipe
        makeHammers();
        makeExcavators();

        NamespacedKey magnetKey = new NamespacedKey(instance, "MAGNET");
        recipeKeys.add(magnetKey);

        ShapedRecipe magnetRecipe = new ShapedRecipe(magnetKey, MagnetController.generateNewMagnet());
        magnetRecipe.shape("REL", "IEI", "III");
        magnetRecipe.setIngredient('R', Material.REDSTONE_BLOCK);
        magnetRecipe.setIngredient('E', Material.EMERALD_BLOCK);
        magnetRecipe.setIngredient('I', Material.IRON_BLOCK);
        magnetRecipe.setIngredient('L', Material.LAPIS_BLOCK);
        getServer().addRecipe(magnetRecipe);

        System.out.println("Reached!");
        addLightsaber();
        makeKyberCrystals();
        addSonic();
        addLongFallBoots();
        addHoneyChestPlate();
        addSlimeChestPlate();
        addCopperArmorPlate();
        upgradeLightsaber();
        addElytraRepairKit();
    }

    public static ItemStack generateElytraKit() {
        ItemStack iStack = addLore(new ItemStack(Material.PHANTOM_MEMBRANE), "A set of materials for repairing damaged elytra");
        ItemMeta iMeta = iStack.getItemMeta();
        iMeta.setDisplayName((ChatColor.RESET + "Elytra Repair Kit"));
        iMeta.setCustomModelData(KEY_BASE + 9000 + 3);
        iStack.setItemMeta(iMeta);
        return iStack;
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

    private ShapedRecipe generateNewHammerRecipe(ToolLevel level, String key, Material tool) {
        ShapedRecipe recipe = AreaToolController.generateHammer(level).getRecipe();
        recipeKeys.add(recipe.getKey());
        return recipe;
    }

    private ShapedRecipe generateNewExcavatorRecipe(ToolLevel level, String key, Material tool) {
        ShapedRecipe recipe = AreaToolController.generateExcavator(level).getRecipe();
        recipeKeys.add(recipe.getKey());
        return recipe;
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

    private void addElytraRepairKit() {
        NamespacedKey ElytraRepairKey = new NamespacedKey(instance, "ElytraRepair");
        recipeKeys.add(ElytraRepairKey);
        ItemStack kit = generateElytraKit();
        ShapedRecipe ElytraRepairRecipe = new ShapedRecipe(ElytraRepairKey, kit);
        ElytraRepairRecipe.shape("LLL", "LCL", "LLL");
        ElytraRepairRecipe.setIngredient('L', Material.LEATHER);
        ElytraRepairRecipe.setIngredient('C', Material.CHORUS_FRUIT);
        getServer().addRecipe(ElytraRepairRecipe);
    }

    private void makeHammers() {
        getServer().addRecipe(generateNewHammerRecipe(ToolLevel.WOODEN, "WOODEN_HAMMER", Material.WOODEN_PICKAXE));
        getServer().addRecipe(generateNewHammerRecipe(ToolLevel.STONE, "STONE_HAMMER", Material.STONE_PICKAXE));
        getServer().addRecipe(generateNewHammerRecipe(ToolLevel.IRON, "IRON_HAMMER", Material.IRON_PICKAXE));
        getServer().addRecipe(generateNewHammerRecipe(ToolLevel.GOLD, "GOLDEN_HAMMER", Material.GOLDEN_PICKAXE));
        getServer().addRecipe(generateNewHammerRecipe(ToolLevel.DIAMOND, "DIAMOND_HAMMER", Material.DIAMOND_PICKAXE));
        getServer().addRecipe(generateNewHammerRecipe(ToolLevel.NETHERITE, "NETHERITE_HAMMER", Material.NETHERITE_PICKAXE));
    }

    private void makeExcavators() {
        getServer().addRecipe(generateNewExcavatorRecipe(ToolLevel.WOODEN, "WOODEN_EXCAVATOR", Material.WOODEN_SHOVEL));
        getServer().addRecipe(generateNewExcavatorRecipe(ToolLevel.STONE, "STONE_EXCAVATOR", Material.STONE_SHOVEL));
        getServer().addRecipe(generateNewExcavatorRecipe(ToolLevel.IRON, "IRON_EXCAVATOR", Material.IRON_SHOVEL));
        getServer().addRecipe(generateNewExcavatorRecipe(ToolLevel.GOLD, "GOLDEN_EXCAVATOR", Material.GOLDEN_SHOVEL));
        getServer().addRecipe(generateNewExcavatorRecipe(ToolLevel.DIAMOND, "DIAMOND_EXCAVATOR", Material.DIAMOND_SHOVEL));
        getServer().addRecipe(generateNewHammerRecipe(ToolLevel.NETHERITE, "NETHERITE_EXCAVATOR", Material.NETHERITE_SHOVEL));
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

    private void addSonic() {
        NamespacedKey sonicKey = new NamespacedKey(instance, "SONIC");
        recipeKeys.add(sonicKey);

        ShapedRecipe sonicRecipe = new ShapedRecipe(sonicKey, SonicScrewdriverController.generateNewSonic());
        sonicRecipe.shape("BGB", "IRI", "BEB");
        sonicRecipe.setIngredient('B', Material.IRON_BLOCK);
        sonicRecipe.setIngredient('G', Material.LIME_STAINED_GLASS);
        sonicRecipe.setIngredient('I', Material.IRON_INGOT);
        sonicRecipe.setIngredient('R', Material.REDSTONE_TORCH);
        sonicRecipe.setIngredient('E', Material.EMERALD);
        getServer().addRecipe(sonicRecipe);
    }

    private void addLongFallBoots() {
        NamespacedKey bootsKey = new NamespacedKey(instance, "LONG_FALL_BOOTS");
        recipeKeys.add(bootsKey);
        ItemStack result = generateNewLongFallBoots();
        ShapedRecipe bootsRecipe = new ShapedRecipe(bootsKey, result);
        bootsRecipe.shape("BFB", "BFB", "SIS");
        bootsRecipe.setIngredient('F', Material.FEATHER);
        bootsRecipe.setIngredient('B', Material.IRON_BLOCK);
        bootsRecipe.setIngredient('S', Material.SLIME_BLOCK);
        bootsRecipe.setIngredient('I', Material.IRON_BOOTS);
        getServer().addRecipe(bootsRecipe);
    }

    private void addHoneyChestPlate() {
        NamespacedKey key = new NamespacedKey(instance, "HONEY_CHESTPLATE");
        recipeKeys.add(key);
        ItemStack result = generateHoneyChestplate();
        ShapedRecipe recipe = new ShapedRecipe(key, result);
        recipe.shape("A A", "AAA", "AAA");
        recipe.setIngredient('A', Material.HONEY_BLOCK);
        getServer().addRecipe(recipe);
    }

    private void addSlimeChestPlate() {
        NamespacedKey key = new NamespacedKey(instance, "SLIME_CHESTPLATE");
        recipeKeys.add(key);
        ItemStack result = generateSlimeChestplate();
        ShapedRecipe recipe = new ShapedRecipe(key, result);
        recipe.shape("A A", "AAA", "AAA");
        recipe.setIngredient('A', Material.SLIME_BLOCK);
        getServer().addRecipe(recipe);
    }

    private void addCopperArmorPlate() {
        //Helmet
        NamespacedKey helmet = new NamespacedKey(instance, "COPPER_HELMET");
        recipeKeys.add(helmet);
        ItemStack resultHelm = generateCopperHelmet();
        ShapedRecipe recipeHelm = new ShapedRecipe(helmet, resultHelm);
        recipeHelm.shape("AAA", "A A");
        recipeHelm.setIngredient('A', Material.COPPER_INGOT);
        getServer().addRecipe(recipeHelm);

        //Chestplate
        NamespacedKey chest = new NamespacedKey(instance, "COPPER_CHESTPLATE");
        recipeKeys.add(chest);
        ItemStack resultChest = generateCopperChestplate();
        ShapedRecipe recipeChest = new ShapedRecipe(chest, resultChest);
        recipeChest.shape("A A", "AAA", "AAA");
        recipeChest.setIngredient('A', Material.COPPER_INGOT);
        getServer().addRecipe(recipeChest);

        //Leggings
        NamespacedKey leggings = new NamespacedKey(instance, "COPPER_LEGGINGS");
        recipeKeys.add(leggings);
        ItemStack resultLeggings = generateCopperLeggings();
        ShapedRecipe recipeLeggings = new ShapedRecipe(leggings, resultLeggings);
        recipeLeggings.shape("AAA", "A A", "A A");
        recipeLeggings.setIngredient('A', Material.COPPER_INGOT);
        getServer().addRecipe(recipeLeggings);

        //Boots
        NamespacedKey boots = new NamespacedKey(instance, "COPPER_BOOTS");
        recipeKeys.add(boots);
        ItemStack resultBoots = generateCopperBoots();
        ShapedRecipe recipeBoots = new ShapedRecipe(boots, resultBoots);
        recipeBoots.shape("A A", "A A");
        recipeBoots.setIngredient('A', Material.COPPER_INGOT);
        getServer().addRecipe(recipeBoots);
    }

    @EventHandler
    public void updateKnowledgeBook(PlayerJoinEvent player) {
        player.getPlayer().discoverRecipes(recipeKeys);
    }
}
