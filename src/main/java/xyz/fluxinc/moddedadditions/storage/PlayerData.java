package xyz.fluxinc.moddedadditions.storage;

import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.util.HashMap;
import java.util.Map;

public class PlayerData implements ConfigurationSerializable {

    private boolean veinMiner;
    private boolean sortChests;
    private int currentMana;
    private int maximumMana;

    public PlayerData() {
        veinMiner = true;
        sortChests = false;
        currentMana = 0;
        maximumMana = 300;
    }

    public PlayerData(Map<String, Object> serializedData) {
        veinMiner = !serializedData.containsKey("veinMiner") || (boolean) serializedData.get("veinMiner");
        sortChests = serializedData.containsKey("sortChests") && (boolean) serializedData.get("sortChests");
        currentMana = serializedData.containsKey("currentMana") ? (int) serializedData.get("currentMana") : 0;
        maximumMana = serializedData.containsKey("maximumMana") ? (int) serializedData.get("maximumMana") : 300;
    }

    public boolean veinMiner() { return veinMiner; }

    public void setVeinMiner(boolean veinMiner) { this.veinMiner = veinMiner; }

    public boolean toggleVeinMiner() { veinMiner = !veinMiner; return veinMiner; }

    public boolean sortChests() { return sortChests; }

    public void setSortChests(boolean sortChests) { this.sortChests = sortChests; }

    public boolean toggleSortChests() { sortChests = !sortChests; return sortChests; }

    public int getCurrentMana() { return currentMana; }

    public void setCurrentMana(int currentMana) { this.currentMana = currentMana; }

    public int getMaximumMana() { return maximumMana; }

    public void setMaximumMana(int maximumMana) { this.maximumMana = maximumMana; }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("veinMiner", veinMiner);
        map.put("sortChests", sortChests);
        map.put("currentMana", currentMana);
        map.put("maximumMana", maximumMana);
        return map;
    }
}
