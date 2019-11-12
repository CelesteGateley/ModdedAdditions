package xyz.fluxinc.moddedadditions.controllers;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import xyz.fluxinc.fluxcore.configuration.ConfigurationManager;
import xyz.fluxinc.moddedadditions.ModdedAdditions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
        hammerBlocks = processArray(areaToolConfiguration.getStringList(HAMMER_CONFIG_KEY));
        excavatorBlocks = processArray(areaToolConfiguration.getStringList(EXCAVATOR_CONFIG_KEY));
    }

    private List<Material> processArray(List<String> configArray) {
        List<Material> materials = new ArrayList<>();
        for (String str : configArray) {
            Material mat = Material.getMaterial(str);
            if (mat != null) { materials.add(mat); }
        }
        return materials;
    }

    public boolean checkHammer(Material material) { return hammerBlocks.contains(material); }

    public boolean checkExcavator(Material material) { return excavatorBlocks.contains(material); }

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
            Bukkit.getLogger().severe("An error occurred whilst saving " + CONFIG_NAME +": " + e.getMessage());
            Bukkit.getServer().getPluginManager().disablePlugin(this.instance);
        }
    }
}
