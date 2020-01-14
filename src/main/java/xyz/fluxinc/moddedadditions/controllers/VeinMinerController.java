package xyz.fluxinc.moddedadditions.controllers;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import xyz.fluxinc.fluxcore.configuration.ConfigurationManager;
import xyz.fluxinc.moddedadditions.ModdedAdditions;

import java.util.ArrayList;
import java.util.List;

import static xyz.fluxinc.fluxcore.utils.BlockUtils.convertStringToMaterial;

@SuppressWarnings("unused")
public class VeinMinerController {

    private ModdedAdditions instance;
    private ConfigurationManager<ModdedAdditions> vmConfiguration;

    private List<Material> pickaxeBlocks;
    private List<Material> axeBlocks;
    private List<Material> shovelBlocks;
    private List<Material> hoeBlocks;
    private List<Material> shearsBlocks;
    private List<Material> handBlocks;
    private List<Player> toggledPlayers;

    private static final String CONFIG_NAME = "veinminer.yml";
    private static final String PICKAXE_CONFIG_KEY = "pickaxe";
    private static final String AXE_CONFIG_KEY = "axe";
    private static final String SHOVEL_CONFIG_KEY = "shovel";
    private static final String HOE_CONFIG_KEY = "hoe";
    private static final String SHEARS_CONFIG_KEY = "shears";
    private static final String HAND_CONFIG_KEY = "hand";

    public VeinMinerController(ModdedAdditions instance) {
        this.instance = instance;
        vmConfiguration = new ConfigurationManager<>(this.instance, CONFIG_NAME);
        loadFromConfiguration();
        toggledPlayers = new ArrayList<>();
    }

    public boolean checkPickaxe(Material material) {
        return pickaxeBlocks.contains(material);
    }

    public boolean checkAxe(Material material) {
        return axeBlocks.contains(material);
    }

    public boolean checkShovel(Material material) {
        return shovelBlocks.contains(material);
    }

    public boolean checkHoe(Material material) {
        return hoeBlocks.contains(material);
    }

    public boolean checkShears(Material material) {
        return shearsBlocks.contains(material);
    }

    public boolean checkHand(Material material) {
        return handBlocks.contains(material);
    }

    public void addPickaxeBlock(Material material) {
        pickaxeBlocks.add(material);
        vmConfiguration.getConfiguration().set(PICKAXE_CONFIG_KEY, fromMaterialToString(pickaxeBlocks).toArray());
        saveConfiguration();
    }

    public void addAxeBlock(Material material) {
        axeBlocks.add(material);
        vmConfiguration.getConfiguration().set(AXE_CONFIG_KEY, fromMaterialToString(axeBlocks).toArray());
        saveConfiguration();
    }

    public void addShovelBlock(Material material) {
        shovelBlocks.add(material);
        vmConfiguration.getConfiguration().set(SHOVEL_CONFIG_KEY, fromMaterialToString(shovelBlocks).toArray());
        saveConfiguration();
    }

    public void addHoeBlock(Material material) {
        hoeBlocks.add(material);
        vmConfiguration.getConfiguration().set(HOE_CONFIG_KEY, fromMaterialToString(hoeBlocks).toArray());
        saveConfiguration();
    }

    public void addShearsBlock(Material material) {
        shearsBlocks.add(material);
        vmConfiguration.getConfiguration().set(SHEARS_CONFIG_KEY, fromMaterialToString(shearsBlocks).toArray());
        saveConfiguration();
    }

    public void addHandBlock(Material material) {
        handBlocks.add(material);
        vmConfiguration.getConfiguration().set(HAND_CONFIG_KEY, fromMaterialToString(handBlocks).toArray());
        saveConfiguration();
    }

    public void removePickaxeBlock(Material material) {
        pickaxeBlocks.remove(material);
        vmConfiguration.getConfiguration().set(PICKAXE_CONFIG_KEY, fromMaterialToString(pickaxeBlocks).toArray());
        saveConfiguration();
    }

    public void removeAxeBlock(Material material) {
        axeBlocks.remove(material);
        vmConfiguration.getConfiguration().set(AXE_CONFIG_KEY, fromMaterialToString(axeBlocks).toArray());
        saveConfiguration();
    }

    public void removeShovelBlock(Material material) {
        shovelBlocks.remove(material);
        vmConfiguration.getConfiguration().set(SHOVEL_CONFIG_KEY, fromMaterialToString(shovelBlocks).toArray());
        saveConfiguration();
    }

    public void removeHoeBlock(Material material) {
        hoeBlocks.remove(material);
        vmConfiguration.getConfiguration().set(HOE_CONFIG_KEY, fromMaterialToString(hoeBlocks).toArray());
        saveConfiguration();
    }

    public void removeShearsBlock(Material material) {
        shearsBlocks.remove(material);
        vmConfiguration.getConfiguration().set(SHEARS_CONFIG_KEY, fromMaterialToString(shearsBlocks).toArray());
        saveConfiguration();
    }

    public void removeHandBlock(Material material) {
        handBlocks.remove(material);
        vmConfiguration.getConfiguration().set(HAND_CONFIG_KEY, fromMaterialToString(handBlocks).toArray());
        saveConfiguration();
    }

    public boolean isToggled(Player player) {
        return toggledPlayers.contains(player);
    }

    public void toggleVeinMiner(Player player) {
        if (toggledPlayers.contains(player)) {
            toggledPlayers.remove(player);
        } else {
            toggledPlayers.add(player);
        }
    }

    public int getMaxBlocks() {
        return vmConfiguration.getInt("max-blocks");
    }

    public boolean getAllowInCreative() {
        return vmConfiguration.getBoolean("allow-in-creative");
    }

    public void saveConfiguration() {
       vmConfiguration.saveConfiguration();
    }

    public void loadFromConfiguration() {
        pickaxeBlocks = convertStringToMaterial(vmConfiguration.getGeneric(PICKAXE_CONFIG_KEY));
        axeBlocks = convertStringToMaterial(vmConfiguration.getGeneric(AXE_CONFIG_KEY));
        shovelBlocks = convertStringToMaterial(vmConfiguration.getGeneric(SHOVEL_CONFIG_KEY));
        hoeBlocks = convertStringToMaterial(vmConfiguration.getGeneric(HOE_CONFIG_KEY));
        shearsBlocks = convertStringToMaterial(vmConfiguration.getGeneric(SHEARS_CONFIG_KEY));
        handBlocks = convertStringToMaterial(vmConfiguration.getGeneric(HAND_CONFIG_KEY));
    }

    private List<String> fromMaterialToString(List<Material> materials) {
        List<String> strings = new ArrayList<>();
        for (Material material : materials) { strings.add(material.name()); }
        return strings;
    }
}
