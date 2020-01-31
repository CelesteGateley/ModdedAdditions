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

    public PlayerData setVeinMiner(boolean veinMiner) { this.veinMiner = veinMiner; return this; }

    public PlayerData toggleVeinMiner() { veinMiner = !veinMiner; return this; }

    public boolean sortChests() { return sortChests; }

    public PlayerData setSortChests(boolean sortChests) { this.sortChests = sortChests; return this; }

    public PlayerData toggleSortChests() { sortChests = !sortChests; return this; }

    public int getCurrentMana() { return currentMana; }

    public PlayerData setCurrentMana(int currentMana) { this.currentMana = currentMana; return this; }

    public PlayerData addCurrentMana(int currentMana) { this.currentMana += currentMana; return this; }

    public PlayerData takeCurrentMana(int currentMana) { this.currentMana -= currentMana; return this; }

    public int getMaximumMana() { return maximumMana; }

    public PlayerData setMaximumMana(int maximumMana) { this.maximumMana = maximumMana; return this; }

    public PlayerData addMaximumMana(int maximumMana) { this.maximumMana += maximumMana; return this; }

    public PlayerData takeMaximumMana(int maximumMana) { this.maximumMana -= maximumMana; return this; }

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
