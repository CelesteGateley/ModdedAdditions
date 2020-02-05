package xyz.fluxinc.moddedadditions.storage;

import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.util.HashMap;
import java.util.Map;

public class PlayerData implements ConfigurationSerializable {

    private boolean veinMiner;
    private boolean sortChests;
    private int currentMana;
    private int maximumMana;
    private Map<String, Boolean> knownSpells;

    public PlayerData() {
        veinMiner = true;
        sortChests = false;
        currentMana = 0;
        maximumMana = 300;
        initializeSpells();
    }

    @SuppressWarnings("unchecked")
    public PlayerData(Map<String, Object> serializedData) {
        veinMiner = !serializedData.containsKey("veinMiner") || (boolean) serializedData.get("veinMiner");
        sortChests = serializedData.containsKey("sortChests") && (boolean) serializedData.get("sortChests");
        currentMana = serializedData.containsKey("currentMana") ? (int) serializedData.get("currentMana") : 0;
        maximumMana = serializedData.containsKey("maximumMana") ? (int) serializedData.get("maximumMana") : 300;
        knownSpells = serializedData.containsKey("knownSpells") ? (Map<String, Boolean>) serializedData.get("knownSpells") : new HashMap<>();
        initializeSpells();
    }

    private void initializeSpells() {
        if (knownSpells == null) { knownSpells = new HashMap<>(); }

        knownSpells.putIfAbsent("Fireball", false);
        knownSpells.putIfAbsent("Teleport", false);
        knownSpells.putIfAbsent("Shoot Arrows", false);
        knownSpells.putIfAbsent("Heal", false);
        knownSpells.putIfAbsent("Air Jet", true);

    }

    public boolean knowsSpell(String spell) {
        return knownSpells.getOrDefault(spell, false);
    }

    public PlayerData learnSpell(String spell) { knownSpells.put(spell, true); return this; }

    public PlayerData setSpell(String spell, boolean value) { knownSpells.put(spell, value); return this; }

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
        map.put("knownSpells", knownSpells);
        return map;
    }
}
