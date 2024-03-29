package xyz.fluxinc.moddedadditions.areatool;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.configuration.file.YamlConfiguration;
import xyz.fluxinc.fluxcore.configuration.ConfigurationManager;
import xyz.fluxinc.fluxcore.enums.ToolLevel;
import xyz.fluxinc.fluxcore.utils.StringUtils;
import xyz.fluxinc.moddedadditions.common.storage.CustomItem;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static xyz.fluxinc.fluxcore.utils.BlockUtils.convertStringToMaterial;
import static xyz.fluxinc.moddedadditions.ModdedAdditions.KEY_BASE;
import static xyz.fluxinc.moddedadditions.ModdedAdditions.instance;

@SuppressWarnings("BooleanMethodIsAlwaysInverted")
public class AreaToolController {

    public static final int AT_KEY_BASE = 1000;
    private static final String CONFIG_NAME = "areatool.yml";
    private static final String HAMMER_CONFIG_KEY = "hammer";
    private static final String EXCAVATOR_CONFIG_KEY = "excavator";
    private final YamlConfiguration areaToolConfiguration;
    private List<Material> hammerBlocks;
    private List<Material> excavatorBlocks;


    public AreaToolController() {
        areaToolConfiguration = new ConfigurationManager<>(instance, CONFIG_NAME).getConfiguration();
        loadFromConfiguration();
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

    public static CustomItem generateHammer(ToolLevel level) {
        Material toolMaterial;
        int modelId;
        switch (level) {
            case STONE:
                toolMaterial = Material.STONE_PICKAXE;
                modelId = 2;
                break;
            case IRON:
                toolMaterial = Material.IRON_PICKAXE;
                modelId = 3;
                break;
            case GOLD:
                toolMaterial = Material.GOLDEN_PICKAXE;
                modelId = 4;
                break;
            case DIAMOND:
                toolMaterial = Material.DIAMOND_PICKAXE;
                modelId = 5;
                break;
            case NETHERITE:
                toolMaterial = Material.NETHERITE_PICKAXE;
                modelId = 6;
                break;
            default:
                toolMaterial = Material.WOODEN_PICKAXE;
                modelId = 1;
                break;
        }

        return new AreaToolItem(KEY_BASE + AT_KEY_BASE + 10 + modelId, toolMaterial,
                level + "_HAMMER", StringUtils.toTitleCase(level.toString()) + " Hammer", "mi-hammer");
    }

    public static CustomItem generateExcavator(ToolLevel level) {
        Material toolMaterial;
        int modelId;
        switch (level) {
            case STONE:
                toolMaterial = Material.STONE_SHOVEL;
                modelId = 2;
                break;
            case IRON:
                toolMaterial = Material.IRON_SHOVEL;
                modelId = 3;
                break;
            case GOLD:
                toolMaterial = Material.GOLDEN_SHOVEL;
                modelId = 4;
                break;
            case DIAMOND:
                toolMaterial = Material.DIAMOND_SHOVEL;
                modelId = 5;
                break;
            case NETHERITE:
                toolMaterial = Material.NETHERITE_SHOVEL;
                modelId = 6;
                break;
            default:
                toolMaterial = Material.WOODEN_SHOVEL;
                modelId = 1;
                break;
        }

        return new AreaToolItem(KEY_BASE + AT_KEY_BASE + 20 + modelId, toolMaterial,
                level + "_EXCAVATOR", StringUtils.toTitleCase(level.toString()) + " Excavator", "mi-excavator");
    }

    public boolean checkHammer(Material material) {
        return hammerBlocks.contains(material);
    }

    public boolean checkExcavator(Material material) {
        return excavatorBlocks.contains(material);
    }

    public void addHammerBlock(Material material) {
        hammerBlocks.add(material);
        areaToolConfiguration.set(HAMMER_CONFIG_KEY, fromMaterialToString(hammerBlocks).toArray());
        saveConfiguration();
    }

    public void removeHammerBlock(Material material) {
        hammerBlocks.remove(material);
        areaToolConfiguration.set(HAMMER_CONFIG_KEY, fromMaterialToString(hammerBlocks).toArray());
        saveConfiguration();
    }

    public void addExcavatorBlock(Material material) {
        excavatorBlocks.add(material);
        areaToolConfiguration.set(EXCAVATOR_CONFIG_KEY, fromMaterialToString(excavatorBlocks).toArray());
        saveConfiguration();
    }

    public void removeExcavatorBlock(Material material) {
        excavatorBlocks.remove(material);
        areaToolConfiguration.set(EXCAVATOR_CONFIG_KEY, fromMaterialToString(excavatorBlocks).toArray());
        saveConfiguration();
    }

    public List<Material> getHammerList() {
        return this.hammerBlocks;
    }

    public List<Material> getExcavatorList() {
        return this.excavatorBlocks;
    }

    public void saveConfiguration() {
        try {
            areaToolConfiguration.save(new File(instance.getDataFolder(), CONFIG_NAME));
        } catch (IOException e) {
            Bukkit.getLogger().severe("An error occurred whilst saving " + CONFIG_NAME + ": " + e.getMessage());
            Bukkit.getServer().getPluginManager().disablePlugin(instance);
        }
    }

    public void loadFromConfiguration() {
        hammerBlocks = convertStringToMaterial(areaToolConfiguration.getStringList(HAMMER_CONFIG_KEY));
        excavatorBlocks = convertStringToMaterial(areaToolConfiguration.getStringList(EXCAVATOR_CONFIG_KEY));
    }

    private List<String> fromMaterialToString(List<Material> materials) {
        List<String> strings = new ArrayList<>();
        for (Material material : materials) {
            strings.add(material.name());
        }
        return strings;
    }

}
