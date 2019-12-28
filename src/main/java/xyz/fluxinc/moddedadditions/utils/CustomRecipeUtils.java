package xyz.fluxinc.moddedadditions.utils;

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
import xyz.fluxinc.moddedadditions.ModdedAdditions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import static org.bukkit.Bukkit.getServer;
import static xyz.fluxinc.fluxcore.utils.LoreUtils.addLore;
import static xyz.fluxinc.moddedadditions.storage.AdditionalRecipeStorage.*;

public class CustomRecipeUtils implements Listener {

    private Collection<NamespacedKey> recipeKeys;
    private ModdedAdditions instance;

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


        /*
         * Furnace Recipe takes 5 Arguments:
         * NamespacedKey (Unique Identifier)
         * Result (ItemStack)
         * Source (Material)
         * XP Granted (0.15 for Charcoal, 0.2 for Cactus, 0.35 for meats)
         * Cooking Time (200 ticks by default)
         */
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
        ItemStack magnet = new ItemStack(Material.COMPASS);
        magnet = addLore(magnet, ChatColor.translateAlternateColorCodes('&', instance.getLanguageManager().getConfig().getString("mi-magnet")));
        ItemMeta itemMeta = magnet.getItemMeta();
        itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&9Item &cMagnet"));
        magnet.setItemMeta(itemMeta);

        ShapedRecipe magnetRecipe = new ShapedRecipe(magnetKey, magnet);
        magnetRecipe.shape("REL", "IEI", "III");
        magnetRecipe.setIngredient('R', Material.REDSTONE_BLOCK);
        magnetRecipe.setIngredient('E', Material.EMERALD_BLOCK);
        magnetRecipe.setIngredient('I', Material.IRON_BLOCK);
        magnetRecipe.setIngredient('L', Material.LAPIS_BLOCK);
        getServer().addRecipe(magnetRecipe);
    }

    @EventHandler
    public void updateKnowledgeBook(PlayerJoinEvent player) {
        player.getPlayer().discoverRecipes(recipeKeys);
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

    private void makeHammers() {
        ShapedRecipe hammerRecipe;
        ItemStack woodenHammer;
        ItemStack stoneHammer;
        ItemStack ironHammer;
        ItemStack goldHammer;
        ItemStack diamondHammer;

        NamespacedKey nsKey = new NamespacedKey(this.instance, "WOODEN_HAMMER");
        recipeKeys.add(nsKey);
        woodenHammer = addLore(new ItemStack(Material.WOODEN_PICKAXE), ChatColor.translateAlternateColorCodes('&', instance.getLanguageManager().getConfig().getString("mi-hammer")));
        ItemMeta iMeta = woodenHammer.getItemMeta();
        iMeta.setDisplayName("Wooden Hammer");
        woodenHammer.setItemMeta(iMeta);
        hammerRecipe = new ShapedRecipe(nsKey, woodenHammer);
        hammerRecipe.shape("PPP", " S ", " S ");
        hammerRecipe.setIngredient('S', Material.STICK);
        hammerRecipe.setIngredient('P', Material.WOODEN_PICKAXE);
        getServer().addRecipe(hammerRecipe);

        nsKey = new NamespacedKey(this.instance, "STONE_HAMMER");
        recipeKeys.add(nsKey);
        stoneHammer = addLore(new ItemStack(Material.STONE_PICKAXE), ChatColor.translateAlternateColorCodes('&', instance.getLanguageManager().getConfig().getString("mi-hammer")));
        iMeta = stoneHammer.getItemMeta();
        iMeta.setDisplayName("Stone Hammer");
        stoneHammer.setItemMeta(iMeta);
        hammerRecipe = new ShapedRecipe(nsKey, stoneHammer);
        hammerRecipe.shape("PPP", " S ", " S ");
        hammerRecipe.setIngredient('S', Material.STICK);
        hammerRecipe.setIngredient('P', Material.STONE_PICKAXE);
        getServer().addRecipe(hammerRecipe);

        nsKey = new NamespacedKey(this.instance, "IRON_HAMMER");
        recipeKeys.add(nsKey);
        ironHammer = addLore(new ItemStack(Material.IRON_PICKAXE), ChatColor.translateAlternateColorCodes('&', instance.getLanguageManager().getConfig().getString("mi-hammer")));
        iMeta = ironHammer.getItemMeta();
        iMeta.setDisplayName("Iron Hammer");
        ironHammer.setItemMeta(iMeta);
        hammerRecipe = new ShapedRecipe(nsKey, ironHammer);
        hammerRecipe.shape("PPP", " S ", " S ");
        hammerRecipe.setIngredient('S', Material.STICK);
        hammerRecipe.setIngredient('P', Material.IRON_PICKAXE);
        getServer().addRecipe(hammerRecipe);

        nsKey = new NamespacedKey(this.instance, "GOLDEN_HAMMER");
        recipeKeys.add(nsKey);
        goldHammer = addLore(new ItemStack(Material.GOLDEN_PICKAXE), ChatColor.translateAlternateColorCodes('&', instance.getLanguageManager().getConfig().getString("mi-hammer")));
        iMeta = goldHammer.getItemMeta();
        iMeta.setDisplayName("Golden Hammer");
        goldHammer.setItemMeta(iMeta);
        hammerRecipe = new ShapedRecipe(nsKey, goldHammer);
        hammerRecipe.shape("PPP", " S ", " S ");
        hammerRecipe.setIngredient('S', Material.STICK);
        hammerRecipe.setIngredient('P', Material.GOLDEN_PICKAXE);
        getServer().addRecipe(hammerRecipe);

        nsKey = new NamespacedKey(this.instance, "DIAMOND_HAMMER");
        recipeKeys.add(nsKey);
        diamondHammer = addLore(new ItemStack(Material.DIAMOND_PICKAXE), ChatColor.translateAlternateColorCodes('&', instance.getLanguageManager().getConfig().getString("mi-hammer")));
        iMeta = diamondHammer.getItemMeta();
        iMeta.setDisplayName("Diamond Hammer");
        diamondHammer.setItemMeta(iMeta);
        hammerRecipe = new ShapedRecipe(nsKey, diamondHammer);
        hammerRecipe.shape("PPP", " S ", " S ");
        hammerRecipe.setIngredient('S', Material.STICK);
        hammerRecipe.setIngredient('P', Material.DIAMOND_PICKAXE);
        getServer().addRecipe(hammerRecipe);

    }

    private void makeExcavators() {
        ShapedRecipe excavatorRecipe;
        ItemStack woodenExcavator;
        ItemStack stoneExcavator;
        ItemStack ironExcavator;
        ItemStack goldExcavator;
        ItemStack diamondExcavator;

        NamespacedKey nsKey = new NamespacedKey(this.instance, "WOODEN_EXCAVATOR");
        recipeKeys.add(nsKey);
        woodenExcavator = addLore(new ItemStack(Material.WOODEN_SHOVEL), ChatColor.translateAlternateColorCodes('&', instance.getLanguageManager().getConfig().getString("mi-excavator")));
        ItemMeta iMeta = woodenExcavator.getItemMeta();
        iMeta.setDisplayName("Wooden Excavator");
        woodenExcavator.setItemMeta(iMeta);
        excavatorRecipe = new ShapedRecipe(nsKey, woodenExcavator);
        excavatorRecipe.shape("PPP", " S ", " S ");
        excavatorRecipe.setIngredient('S', Material.STICK);
        excavatorRecipe.setIngredient('P', Material.WOODEN_SHOVEL);
        getServer().addRecipe(excavatorRecipe);

        nsKey = new NamespacedKey(this.instance, "STONE_EXCAVATOR");
        recipeKeys.add(nsKey);
        stoneExcavator = addLore(new ItemStack(Material.STONE_SHOVEL), ChatColor.translateAlternateColorCodes('&', instance.getLanguageManager().getConfig().getString("mi-excavator")));
        iMeta = stoneExcavator.getItemMeta();
        iMeta.setDisplayName("Stone Excavator");
        stoneExcavator.setItemMeta(iMeta);
        excavatorRecipe = new ShapedRecipe(nsKey, stoneExcavator);
        excavatorRecipe.shape("PPP", " S ", " S ");
        excavatorRecipe.setIngredient('S', Material.STICK);
        excavatorRecipe.setIngredient('P', Material.STONE_SHOVEL);
        getServer().addRecipe(excavatorRecipe);

        nsKey = new NamespacedKey(this.instance, "IRON_EXCAVATOR");
        recipeKeys.add(nsKey);
        ironExcavator = addLore(new ItemStack(Material.IRON_SHOVEL), ChatColor.translateAlternateColorCodes('&', instance.getLanguageManager().getConfig().getString("mi-excavator")));
        iMeta = ironExcavator.getItemMeta();
        iMeta.setDisplayName("Iron Excavator");
        ironExcavator.setItemMeta(iMeta);
        excavatorRecipe = new ShapedRecipe(nsKey, ironExcavator);
        excavatorRecipe.shape("PPP", " S ", " S ");
        excavatorRecipe.setIngredient('S', Material.STICK);
        excavatorRecipe.setIngredient('P', Material.IRON_SHOVEL);
        getServer().addRecipe(excavatorRecipe);

        nsKey = new NamespacedKey(this.instance, "GOLDEN_EXCAVATOR");
        recipeKeys.add(nsKey);
        goldExcavator = addLore(new ItemStack(Material.GOLDEN_SHOVEL), ChatColor.translateAlternateColorCodes('&', instance.getLanguageManager().getConfig().getString("mi-excavator")));
        iMeta = goldExcavator.getItemMeta();
        iMeta.setDisplayName("Golden Excavator");
        goldExcavator.setItemMeta(iMeta);
        excavatorRecipe = new ShapedRecipe(nsKey, goldExcavator);
        excavatorRecipe.shape("PPP", " S ", " S ");
        excavatorRecipe.setIngredient('S', Material.STICK);
        excavatorRecipe.setIngredient('P', Material.GOLDEN_SHOVEL);
        getServer().addRecipe(excavatorRecipe);

        nsKey = new NamespacedKey(this.instance, "DIAMOND_EXCAVATOR");
        recipeKeys.add(nsKey);
        diamondExcavator = addLore(new ItemStack(Material.DIAMOND_SHOVEL), ChatColor.translateAlternateColorCodes('&', instance.getLanguageManager().getConfig().getString("mi-excavator")));
        iMeta = diamondExcavator.getItemMeta();
        iMeta.setDisplayName("Diamond Excavator");
        diamondExcavator.setItemMeta(iMeta);
        excavatorRecipe = new ShapedRecipe(nsKey, diamondExcavator);
        excavatorRecipe.shape("PPP", " S ", " S ");
        excavatorRecipe.setIngredient('S', Material.STICK);
        excavatorRecipe.setIngredient('P', Material.DIAMOND_SHOVEL);
        getServer().addRecipe(excavatorRecipe);

    }
}
