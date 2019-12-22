package xyz.fluxinc.moddedadditions.controllers;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import xyz.fluxinc.fluxcore.configuration.ConfigurationManager;
import xyz.fluxinc.moddedadditions.ModdedAdditions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class VeinMinerController {

    private ModdedAdditions instance;
    private YamlConfiguration vmConfiguration;
    private List<Material> pickaxeBlocks;
    private List<Material> axeBlocks;
    private List<Material> shovelBlocks;
    private List<Material> hoeBlocks;
    private List<Material> shearsBlocks;
    private List<Material> handBlocks;
    private static final String CONFIG_NAME = "veinminer.yml";
    private static final String PICKAXE_CONFIG_KEY = "pickaxe";
    private static final String AXE_CONFIG_KEY = "axe";
    private static final String SHOVEL_CONFIG_KEY = "shovel";
    private static final String HOE_CONFIG_KEY = "hoe";
    private static final String SHEARS_CONFIG_KEY = "shears";
    private static final String HAND_CONFIG_KEY = "hand";


    public VeinMinerController(ModdedAdditions instance) {
        this.instance = instance;
        vmConfiguration = new ConfigurationManager<>(this.instance, CONFIG_NAME).getConfig();
        pickaxeBlocks = processArray(vmConfiguration.getStringList(PICKAXE_CONFIG_KEY));
        axeBlocks = processArray(vmConfiguration.getStringList(AXE_CONFIG_KEY));
        shovelBlocks = processArray(vmConfiguration.getStringList(SHOVEL_CONFIG_KEY));
        hoeBlocks = processArray(vmConfiguration.getStringList(HOE_CONFIG_KEY));
        shearsBlocks = processArray(vmConfiguration.getStringList(SHEARS_CONFIG_KEY));
        handBlocks = processArray(vmConfiguration.getStringList(HAND_CONFIG_KEY));
        for (Material mat : pickaxeBlocks) {
            Bukkit.getServer().getLogger().fine("" + mat);
        }
    }

    private List<Material> processArray(List<String> configArray) {
        List<Material> materials = new ArrayList<>();
        for (String str : configArray) {
            Material mat = Material.getMaterial(str);
            if (mat != null) {
                materials.add(mat);
            }
        }
        return materials;
    }

    public boolean checkPickaxe(Material material) { return pickaxeBlocks.contains(material); }

    public boolean checkAxe(Material material) { return axeBlocks.contains(material); }

    public boolean checkShovel(Material material) { return shovelBlocks.contains(material); }

    public boolean checkHoe(Material material) { return hoeBlocks.contains(material); }

    public boolean checkShears(Material material) { return shearsBlocks.contains(material); }

    public boolean checkHand(Material material) { return handBlocks.contains(material); }

    public void addPickaxeBlock(Material material) {
        pickaxeBlocks.add(material);
        vmConfiguration.set(PICKAXE_CONFIG_KEY, pickaxeBlocks.toArray());
        saveConfiguration();
    }

    public void addAxeBlock(Material material) {
        axeBlocks.add(material);
        vmConfiguration.set(AXE_CONFIG_KEY, axeBlocks.toArray());
        saveConfiguration();
    }

    public void addShovelBlock(Material material) {
        shovelBlocks.add(material);
        vmConfiguration.set(SHOVEL_CONFIG_KEY, shovelBlocks.toArray());
        saveConfiguration();
    }

    public void addHoeBlock(Material material) {
        hoeBlocks.add(material);
        vmConfiguration.set(HOE_CONFIG_KEY, hoeBlocks.toArray());
        saveConfiguration();
    }

    public void addShearsBlock(Material material) {
        shearsBlocks.add(material);
        vmConfiguration.set(SHEARS_CONFIG_KEY, shearsBlocks.toArray());
        saveConfiguration();
    }

    public void addHandBlock(Material material) {
       handBlocks.add(material);
        vmConfiguration.set(HAND_CONFIG_KEY, handBlocks.toArray());
        saveConfiguration();
    }

    public void removePickaxeBlock(Material material) {
        pickaxeBlocks.remove(material);
        vmConfiguration.set(PICKAXE_CONFIG_KEY, pickaxeBlocks.toArray());
        saveConfiguration();
    }

    public void removeAxeBlock(Material material) {
        axeBlocks.remove(material);
        vmConfiguration.set(AXE_CONFIG_KEY, axeBlocks.toArray());
        saveConfiguration();
    }

    public void removeShovelBlock(Material material) {
        shovelBlocks.remove(material);
        vmConfiguration.set(SHOVEL_CONFIG_KEY, shovelBlocks.toArray());
        saveConfiguration();
    }

    public void removeHoeBlock(Material material) {
        hoeBlocks.remove(material);
        vmConfiguration.set(HOE_CONFIG_KEY, hoeBlocks.toArray());
        saveConfiguration();
    }

    public void removeShearsBlock(Material material) {
        shearsBlocks.remove(material);
        vmConfiguration.set(SHEARS_CONFIG_KEY, shearsBlocks.toArray());
        saveConfiguration();
    }

    public void removeHandBlock(Material material) {
        handBlocks.remove(material);
        vmConfiguration.set(HAND_CONFIG_KEY, handBlocks.toArray());
        saveConfiguration();
    }


    private void saveConfiguration() {
        try {
            vmConfiguration.save(CONFIG_NAME);
        } catch (IOException e) {
            Bukkit.getLogger().severe("An error occurred whilst saving " + CONFIG_NAME + ": " + e.getMessage());
            Bukkit.getServer().getPluginManager().disablePlugin(this.instance);
        }
    }

    public int getMaxBlocks() {
        return vmConfiguration.getInt("max-blocks");
    }

    public boolean getAllowInCreative() { return vmConfiguration.getBoolean("allow-in-creative"); }
}
