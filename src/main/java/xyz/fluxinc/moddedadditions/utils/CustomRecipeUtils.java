package xyz.fluxinc.moddedadditions.utils;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import xyz.fluxinc.moddedadditions.ModdedAdditions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;

import static org.bukkit.Bukkit.getServer;
import static xyz.fluxinc.fluxcore.utils.LoreUtils.addLore;
import static xyz.fluxinc.moddedadditions.storage.AdditionalRecipeStorage.*;

public class CustomRecipeUtils implements Listener {

    private Collection<NamespacedKey> recipeKeys;
    private ModdedAdditions instance;

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

        NamespacedKey nsKey = new NamespacedKey(this.instance, "WOODEN_HAMMER");
        recipeKeys.add(nsKey);
        hammerRecipe = new ShapedRecipe(nsKey, instance.getAreaToolController().generateHammer(ToolLevel.WOODEN));
        hammerRecipe.shape("PPP", " S ", " S ");
        hammerRecipe.setIngredient('S', Material.STICK);
        hammerRecipe.setIngredient('P', Material.WOODEN_PICKAXE);
        getServer().addRecipe(hammerRecipe);

        nsKey = new NamespacedKey(this.instance, "STONE_HAMMER");
        recipeKeys.add(nsKey);
        hammerRecipe = new ShapedRecipe(nsKey, instance.getAreaToolController().generateHammer(ToolLevel.STONE));
        hammerRecipe.shape("PPP", " S ", " S ");
        hammerRecipe.setIngredient('S', Material.STICK);
        hammerRecipe.setIngredient('P', Material.STONE_PICKAXE);
        getServer().addRecipe(hammerRecipe);

        nsKey = new NamespacedKey(this.instance, "IRON_HAMMER");
        recipeKeys.add(nsKey);
        hammerRecipe = new ShapedRecipe(nsKey, instance.getAreaToolController().generateHammer(ToolLevel.IRON));
        hammerRecipe.shape("PPP", " S ", " S ");
        hammerRecipe.setIngredient('S', Material.STICK);
        hammerRecipe.setIngredient('P', Material.IRON_PICKAXE);
        getServer().addRecipe(hammerRecipe);

        nsKey = new NamespacedKey(this.instance, "GOLDEN_HAMMER");
        recipeKeys.add(nsKey);
        hammerRecipe = new ShapedRecipe(nsKey, instance.getAreaToolController().generateHammer(ToolLevel.GOLD));
        hammerRecipe.shape("PPP", " S ", " S ");
        hammerRecipe.setIngredient('S', Material.STICK);
        hammerRecipe.setIngredient('P', Material.GOLDEN_PICKAXE);
        getServer().addRecipe(hammerRecipe);

        nsKey = new NamespacedKey(this.instance, "DIAMOND_HAMMER");
        recipeKeys.add(nsKey);
        hammerRecipe = new ShapedRecipe(nsKey, instance.getAreaToolController().generateHammer(ToolLevel.DIAMOND));
        hammerRecipe.shape("PPP", " S ", " S ");
        hammerRecipe.setIngredient('S', Material.STICK);
        hammerRecipe.setIngredient('P', Material.DIAMOND_PICKAXE);
        getServer().addRecipe(hammerRecipe);

    }

    private void makeExcavators() {
        ShapedRecipe excavatorRecipe;

        NamespacedKey nsKey = new NamespacedKey(this.instance, "WOODEN_EXCAVATOR");
        recipeKeys.add(nsKey);
        excavatorRecipe = new ShapedRecipe(nsKey, instance.getAreaToolController().generateExcavator(ToolLevel.WOODEN));
        excavatorRecipe.shape("PPP", " S ", " S ");
        excavatorRecipe.setIngredient('S', Material.STICK);
        excavatorRecipe.setIngredient('P', Material.WOODEN_SHOVEL);
        getServer().addRecipe(excavatorRecipe);

        nsKey = new NamespacedKey(this.instance, "STONE_EXCAVATOR");
        recipeKeys.add(nsKey);
        excavatorRecipe = new ShapedRecipe(nsKey, instance.getAreaToolController().generateExcavator(ToolLevel.WOODEN));
        excavatorRecipe.shape("PPP", " S ", " S ");
        excavatorRecipe.setIngredient('S', Material.STICK);
        excavatorRecipe.setIngredient('P', Material.STONE_SHOVEL);
        getServer().addRecipe(excavatorRecipe);

        nsKey = new NamespacedKey(this.instance, "IRON_EXCAVATOR");
        recipeKeys.add(nsKey);
        excavatorRecipe = new ShapedRecipe(nsKey, instance.getAreaToolController().generateExcavator(ToolLevel.WOODEN));
        excavatorRecipe.shape("PPP", " S ", " S ");
        excavatorRecipe.setIngredient('S', Material.STICK);
        excavatorRecipe.setIngredient('P', Material.IRON_SHOVEL);
        getServer().addRecipe(excavatorRecipe);

        nsKey = new NamespacedKey(this.instance, "GOLDEN_EXCAVATOR");
        recipeKeys.add(nsKey);
        excavatorRecipe = new ShapedRecipe(nsKey, instance.getAreaToolController().generateExcavator(ToolLevel.WOODEN));
        excavatorRecipe.shape("PPP", " S ", " S ");
        excavatorRecipe.setIngredient('S', Material.STICK);
        excavatorRecipe.setIngredient('P', Material.GOLDEN_SHOVEL);
        getServer().addRecipe(excavatorRecipe);

        nsKey = new NamespacedKey(this.instance, "DIAMOND_EXCAVATOR");
        recipeKeys.add(nsKey);
        excavatorRecipe = new ShapedRecipe(nsKey, instance.getAreaToolController().generateExcavator(ToolLevel.WOODEN));
        excavatorRecipe.shape("PPP", " S ", " S ");
        excavatorRecipe.setIngredient('S', Material.STICK);
        excavatorRecipe.setIngredient('P', Material.DIAMOND_SHOVEL);
        getServer().addRecipe(excavatorRecipe);
    }

    public void addLightsaber() {
        NamespacedKey lightSaberKey = new NamespacedKey(this.instance, "LIGHTSABER");
        recipeKeys.add(lightSaberKey);
        ItemStack lightSaber = new ItemStack(Material.DIAMOND_SWORD);
        lightSaber = addLore(lightSaber, ChatColor.translateAlternateColorCodes('&', instance.getLanguageManager().getConfig().getString("mi-lightsaber")));
        ItemMeta itemMeta = lightSaber.getItemMeta();
        itemMeta.setDisplayName(ChatColor.WHITE + "Lightsaber");
        itemMeta.addEnchant(Enchantment.FIRE_ASPECT, 1, false);
        itemMeta.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, new AttributeModifier(UUID.randomUUID(), "Attack_damage", 13, AttributeModifier.Operation.ADD_SCALAR, EquipmentSlot.HAND));
        lightSaber.setItemMeta(itemMeta);

        ShapedRecipe lightSaberRecipe = new ShapedRecipe(lightSaberKey, lightSaber);
        lightSaberRecipe.shape("IGI", "ICI", "III");
        lightSaberRecipe.setIngredient('G', Material.GLASS_PANE);
        lightSaberRecipe.setIngredient('I', Material.IRON_BLOCK);
        lightSaberRecipe.setIngredient('C', Material.END_CRYSTAL);
        getServer().addRecipe(lightSaberRecipe);
    }

    @EventHandler
    public void updateKnowledgeBook(PlayerJoinEvent player) {
        player.getPlayer().discoverRecipes(recipeKeys);
    }
}
