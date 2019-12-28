package xyz.fluxinc.moddedadditions.controllers;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import xyz.fluxinc.fluxcore.configuration.ConfigurationManager;
import xyz.fluxinc.moddedadditions.ModdedAdditions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static xyz.fluxinc.fluxcore.utils.BlockUtils.convertStringToMaterial;

public class AreaToolController {

    private ModdedAdditions instance;
    private YamlConfiguration areaToolConfiguration;
    private List<Material> hammerBlocks;
    private List<Material> excavatorBlocks;
    private static final String CONFIG_NAME = "areatool.yml";
    private static final String HAMMER_CONFIG_KEY = "hammer";
    private static final String EXCAVATOR_CONFIG_KEY = "excavator";


    public AreaToolController(ModdedAdditions instance) {
        this.instance = instance;
        areaToolConfiguration = new ConfigurationManager<>(this.instance, CONFIG_NAME).getConfig();
        hammerBlocks = convertStringToMaterial(areaToolConfiguration.getStringList(HAMMER_CONFIG_KEY));
        excavatorBlocks = convertStringToMaterial(areaToolConfiguration.getStringList(EXCAVATOR_CONFIG_KEY));
    }

    public boolean checkHammer(Material material) {
        return hammerBlocks.contains(material);
    }

    public boolean checkExcavator(Material material) {
        return excavatorBlocks.contains(material);
    }

    public void addHammerBlock(Material material) {
        hammerBlocks.add(material);
        areaToolConfiguration.set(HAMMER_CONFIG_KEY, hammerBlocks.toArray());
        saveConfiguration();
    }

    public void removeHammerBlock(Material material) {
        hammerBlocks.remove(material);
        areaToolConfiguration.set(HAMMER_CONFIG_KEY, hammerBlocks.toArray());
        saveConfiguration();
    }

    public void addExcavatorBlock(Material material) {
        excavatorBlocks.add(material);
        areaToolConfiguration.set(EXCAVATOR_CONFIG_KEY, excavatorBlocks.toArray());
        saveConfiguration();
    }

    public void removeExcavatorBlock(Material material) {
        excavatorBlocks.remove(material);
        areaToolConfiguration.set(EXCAVATOR_CONFIG_KEY, excavatorBlocks.toArray());
        saveConfiguration();
    }

    private void saveConfiguration() {
        try {
            areaToolConfiguration.save(CONFIG_NAME);
        } catch (IOException e) {
            Bukkit.getLogger().severe("An error occurred whilst saving " + CONFIG_NAME + ": " + e.getMessage());
            Bukkit.getServer().getPluginManager().disablePlugin(this.instance);
        }
    }

    public static List<Block> getBlockList(Block startingBlock, BlockFace face) {
        // Extract the X Y Z coordinates and world for easy access
        int x = startingBlock.getX();
        int y = startingBlock.getY();
        int z = startingBlock.getZ();
        World world = startingBlock.getWorld();
        // Create a list for the extra blocks
        List<Block> extraBlocks = new ArrayList<>();

        // Switch to get the surrounding blocks based on the block face
        switch (face) {
            case UP:
            case DOWN:
                for (int xMod = -1; xMod < 2; xMod++) {
                    for (int zMod = -1; zMod < 2; zMod++) {
                        extraBlocks.add(world.getBlockAt(x + xMod, y, z + zMod));
                    }
                }
                break;
            case EAST:
            case WEST:
                for (int yMod = -1; yMod < 2; yMod++) {
                    for (int zMod = -1; zMod < 2; zMod++) {
                        extraBlocks.add(world.getBlockAt(x, y + yMod, z + zMod));
                    }
                }
                break;
            case NORTH:
            case SOUTH:
                for (int yMod = -1; yMod < 2; yMod++) {
                    for (int xMod = -1; xMod < 2; xMod++) {
                        extraBlocks.add(world.getBlockAt(x + xMod, y + yMod, z));
                    }
                }
                break;
            default:
                break;
        }
        return extraBlocks;
    }

    public static void takeDurability(Player player, ItemStack tool) {
        Random random = new Random();
        double chance = (100D / (getUnbreakingLevel(tool) + 1));
        ItemMeta iMeta = tool.getItemMeta();
        if (iMeta instanceof Damageable) {
            Damageable damageable = (Damageable) iMeta;
            for (int i = 0; i < 3; i++) {
                double rand = random.nextInt(99) + 1;
                if (rand >= chance) {
                    continue;
                }
                damageable.setDamage(damageable.getDamage() + 1);
                if (damageable.getDamage() > tool.getType().getMaxDurability()) {
                    player.getInventory().remove(tool);
                    return;
                }
            }
            tool.setItemMeta((ItemMeta) damageable);
        }
    }

    private static int getUnbreakingLevel(ItemStack tool) {
        ItemMeta itemMeta = tool.getItemMeta();
        if (itemMeta == null) {
            return 0;
        }
        return itemMeta.getEnchantLevel(Enchantment.DURABILITY);
    }


}
