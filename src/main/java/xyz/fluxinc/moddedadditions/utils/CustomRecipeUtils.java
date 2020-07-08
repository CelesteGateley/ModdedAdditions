package xyz.fluxinc.moddedadditions.utils;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import xyz.fluxinc.fluxcore.enums.ToolLevel;
import xyz.fluxinc.moddedadditions.ModdedAdditions;
import xyz.fluxinc.moddedadditions.enums.SaberColor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import static org.bukkit.Bukkit.getServer;
import static xyz.fluxinc.moddedadditions.ModdedAdditions.KEY_BASE;
import static xyz.fluxinc.moddedadditions.controllers.customitems.SpellBookController.SB_KEY_BASE;
import static xyz.fluxinc.moddedadditions.storage.AdditionalRecipeStorage.*;
import static xyz.fluxinc.moddedadditions.utils.SpecialArmorUtils.*;

public class CustomRecipeUtils implements Listener {

    private final Collection<NamespacedKey> recipeKeys;
    private final ModdedAdditions instance;

    /*
     * Furnace Recipe takes 5 Arguments:
     * NamespacedKey (Unique Identifier)
     * Result (ItemStack)
     * Source (Material)
     * XP Granted (0.15 for Charcoal, 0.2 for Cactus, 0.35 for meats)
     * Cooking Time (200 ticks by default)
     */

    public CustomRecipeUtils(ModdedAdditions instance) {
        recipeKeys = new ArrayList<>();
        this.instance = instance;

        SLAB_TO_BLOCK.forEach((slab, block) -> {
            NamespacedKey nsKey = new NamespacedKey(this.instance, slab.toString() + "_BACK_CONVERSION");
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
                    new NamespacedKey(this.instance, material.toString() + "_TO_" + result.toString()),
                    new ItemStack(result),
                    material,
                    (float) 0.2,
                    200);
            getServer().addRecipe(smeltingRecipe);
        });

        //Adding Hammer/Excavator recipe
        makeHammers();
        makeExcavators();

        NamespacedKey magnetKey = new NamespacedKey(this.instance, "MAGNET");
        recipeKeys.add(magnetKey);

        ShapedRecipe magnetRecipe = new ShapedRecipe(magnetKey, instance.getMagnetController().generateNewMagnet());
        magnetRecipe.shape("REL", "IEI", "III");
        magnetRecipe.setIngredient('R', Material.REDSTONE_BLOCK);
        magnetRecipe.setIngredient('E', Material.EMERALD_BLOCK);
        magnetRecipe.setIngredient('I', Material.IRON_BLOCK);
        magnetRecipe.setIngredient('L', Material.LAPIS_BLOCK);
        getServer().addRecipe(magnetRecipe);

        addLightsaber();
        makeKyberCrystals();
        addSonic();
        addSpells();
        addLongFallBoots();
        addHoneyChestPlate();
        addSlimeChestPlate();
    }

    private void processDyes(HashMap<Material, Material> dyeMap, ArrayList<Material> blockList) {
        dyeMap.forEach((block, dye) -> {
            for (Material originBlock : blockList) {
                NamespacedKey nsKey = new NamespacedKey(this.instance, dye.toString() + "_WITH_" + originBlock.toString());
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
        NamespacedKey nsKey = new NamespacedKey(this.instance, key);
        recipeKeys.add(nsKey);
        ShapedRecipe result = new ShapedRecipe(nsKey, instance.getAreaToolController().generateHammer(level));
        result.shape("PPP", " S ", " S ");
        result.setIngredient('S', Material.STICK);
        result.setIngredient('P', tool);
        return result;
    }

    private ShapedRecipe generateNewExcavatorRecipe(ToolLevel level, String key, Material tool) {
        NamespacedKey nsKey = new NamespacedKey(this.instance, key);
        recipeKeys.add(nsKey);
        ShapedRecipe result = new ShapedRecipe(nsKey, instance.getAreaToolController().generateExcavator(level));
        result.shape("PPP", " S ", " S ");
        result.setIngredient('S', Material.STICK);
        result.setIngredient('P', tool);
        return result;
    }

    private ShapedRecipe generateKyberCrystalRecipe(SaberColor color, String key) {
        NamespacedKey nsKey = new NamespacedKey(this.instance, key);
        recipeKeys.add(nsKey);

        ShapedRecipe result = new ShapedRecipe(nsKey, instance.getLightSaberController().generateNewKyberCrystal(color));
        result.shape("GEG", "ECE", "GEG");
        result.setIngredient('G', SaberColor.getStainedGlass(color));
        result.setIngredient('E', Material.EMERALD);
        result.setIngredient('C', Material.END_CRYSTAL);
        return result;
    }

    private void makeHammers() {
        getServer().addRecipe(generateNewHammerRecipe(ToolLevel.WOODEN, "WOODEN_HAMMER", Material.WOODEN_PICKAXE));
        getServer().addRecipe(generateNewHammerRecipe(ToolLevel.STONE, "STONE_HAMMER", Material.STONE_PICKAXE));
        getServer().addRecipe(generateNewHammerRecipe(ToolLevel.IRON, "IRON_HAMMER", Material.IRON_PICKAXE));
        getServer().addRecipe(generateNewHammerRecipe(ToolLevel.GOLD, "GOLDEN_HAMMER", Material.GOLDEN_PICKAXE));
        getServer().addRecipe(generateNewHammerRecipe(ToolLevel.DIAMOND, "DIAMOND_HAMMER", Material.DIAMOND_PICKAXE));
        getServer().addRecipe(generateNewHammerRecipe(ToolLevel.DIAMOND, "NETHERITE_HAMMER", Material.NETHERITE_PICKAXE));
    }

    private void makeExcavators() {
        getServer().addRecipe(generateNewExcavatorRecipe(ToolLevel.WOODEN, "WOODEN_EXCAVATOR", Material.WOODEN_SHOVEL));
        getServer().addRecipe(generateNewExcavatorRecipe(ToolLevel.STONE, "STONE_EXCAVATOR", Material.STONE_SHOVEL));
        getServer().addRecipe(generateNewExcavatorRecipe(ToolLevel.IRON, "IRON_EXCAVATOR", Material.IRON_SHOVEL));
        getServer().addRecipe(generateNewExcavatorRecipe(ToolLevel.GOLD, "GOLDEN_EXCAVATOR", Material.GOLDEN_SHOVEL));
        getServer().addRecipe(generateNewExcavatorRecipe(ToolLevel.DIAMOND, "DIAMOND_EXCAVATOR", Material.DIAMOND_SHOVEL));
        getServer().addRecipe(generateNewHammerRecipe(ToolLevel.DIAMOND, "NETHERITE_EXCAVATOR", Material.NETHERITE_SHOVEL));
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
        NamespacedKey lightSaberKey = new NamespacedKey(this.instance, "LIGHTSABER");
        recipeKeys.add(lightSaberKey);

        ShapedRecipe lightSaberRecipe = new ShapedRecipe(lightSaberKey, instance.getLightSaberController().getDefaultLightSaber());
        lightSaberRecipe.shape("IGI", "ICI", "III");
        lightSaberRecipe.setIngredient('G', Material.GLASS_PANE);
        lightSaberRecipe.setIngredient('I', Material.IRON_BLOCK);
        lightSaberRecipe.setIngredient('C', Material.EMERALD);
        getServer().addRecipe(lightSaberRecipe);
    }

    private void addSonic() {
        NamespacedKey sonicKey = new NamespacedKey(this.instance, "SONIC");
        recipeKeys.add(sonicKey);

        ShapedRecipe sonicRecipe = new ShapedRecipe(sonicKey, instance.getSonicScrewdriverController().generateNewSonic());
        sonicRecipe.shape("BGB", "IRI", "BEB");
        sonicRecipe.setIngredient('B', Material.IRON_BLOCK);
        sonicRecipe.setIngredient('G', Material.LIME_STAINED_GLASS);
        sonicRecipe.setIngredient('I', Material.IRON_INGOT);
        sonicRecipe.setIngredient('R', Material.REDSTONE_TORCH);
        sonicRecipe.setIngredient('E', Material.EMERALD);
        getServer().addRecipe(sonicRecipe);
    }

    private void addSpell(int spellId, Material item1, Material item2) {
        NamespacedKey spellKey = new NamespacedKey(this.instance, "SPELL_" + (KEY_BASE + SB_KEY_BASE + spellId));
        ItemStack result = instance.getSpellBookController().setSpell(KEY_BASE + SB_KEY_BASE + spellId, instance.getSpellBookController().generateNewSpellBook());
        ShapedRecipe spellRecipe = new ShapedRecipe(spellKey, result);
        spellRecipe.shape("ABA", "BSB", "ABA");
        spellRecipe.setIngredient('A', item1);
        spellRecipe.setIngredient('B', item2);
        spellRecipe.setIngredient('S', Material.BOOK);
        getServer().addRecipe(spellRecipe);
    }

    private void addSpells() {
        // FireArrow
        addSpell(1, Material.CROSSBOW, Material.SPECTRAL_ARROW);
        // Slowball
        addSpell(2, Material.SNOWBALL, Material.SOUL_SAND);
        // Fireball
        addSpell(3, Material.FIRE_CHARGE, Material.GHAST_TEAR);
        // Smite
        addSpell(4, Material.EMERALD_ORE, Material.TRIDENT);

        // AirJet
        addSpell(20, Material.FEATHER, Material.STRING);
        // Speed
        addSpell(21, Material.GLOWSTONE_DUST, Material.SUGAR);
        // Teleport
        addSpell(22, Material.ENDER_PEARL, Material.ENDER_EYE);
        // Lava Walk
        addSpell(23, Material.ICE, Material.BLAZE_ROD);

        // Heal (Instant Heal Potion)
        addSpell(40, Material.GOLDEN_APPLE, Material.POTION);
        // HardenedForm
        addSpell(41, Material.OBSIDIAN, Material.COBWEB);
        // ForceField
        addSpell(42, Material.SHIELD, Material.PISTON);
        // Miners Boon
        addSpell(43, Material.NETHERITE_INGOT, Material.HEART_OF_THE_SEA);

        // Vanish
        addSpell(60, Material.GOLDEN_CARROT, Material.BLACKSTONE);
    }

    private void addLongFallBoots() {
        NamespacedKey bootsKey = new NamespacedKey(this.instance, "LONG_FALL_BOOTS");
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
        NamespacedKey key = new NamespacedKey(this.instance, "HONEY_CHESTPLATE");
        recipeKeys.add(key);
        ItemStack result = generateHoneyChestplate();
        ShapedRecipe recipe = new ShapedRecipe(key, result);
        recipe.shape("A A", "AAA", "AAA");
        recipe.setIngredient('A', Material.HONEY_BLOCK);
        getServer().addRecipe(recipe);
    }

    private void addSlimeChestPlate() {
        NamespacedKey key = new NamespacedKey(this.instance, "SLIME_CHESTPLATE");
        recipeKeys.add(key);
        ItemStack result = generateSlimeChestplate();
        ShapedRecipe recipe = new ShapedRecipe(key, result);
        recipe.shape("A A", "AAA", "AAA");
        recipe.setIngredient('A', Material.SLIME_BLOCK);
        getServer().addRecipe(recipe);
    }

    @EventHandler
    public void updateKnowledgeBook(PlayerJoinEvent player) {
        player.getPlayer().discoverRecipes(recipeKeys);
    }
}
