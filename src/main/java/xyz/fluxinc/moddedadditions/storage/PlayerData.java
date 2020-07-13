package xyz.fluxinc.moddedadditions.storage;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import xyz.fluxinc.moddedadditions.spells.SpellRegistry;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("UnusedReturnValue")
public class PlayerData implements ConfigurationSerializable {

    private boolean veinMiner;
    private boolean sortChests;
    private int currentMana;
    private int maximumMana;
    private int dataVersion;
    private Map<String, Boolean> knownSpellsOld;
    private Map<String, Integer> knownSpells;

    public PlayerData() {
        veinMiner = true;
        sortChests = false;
        currentMana = 0;
        maximumMana = 100;
        initializeSpells();
    }

    @SuppressWarnings("unchecked")
    public PlayerData(Map<String, Object> serializedData) {
        dataVersion = (int) serializedData.getOrDefault("version", 0);
        veinMiner = !serializedData.containsKey("veinMiner") || (boolean) serializedData.get("veinMiner");
        sortChests = serializedData.containsKey("sortChests") && (boolean) serializedData.get("sortChests");
        currentMana = serializedData.containsKey("currentMana") ? (int) serializedData.get("currentMana") : 0;
        maximumMana = serializedData.containsKey("maximumMana") ? (int) serializedData.get("maximumMana") : 100;
        if (dataVersion == 0) {
            knownSpellsOld = serializedData.containsKey("knownSpells") ? (Map<String, Boolean>) serializedData.get("knownSpells") : new HashMap<>();
            upgradeSpellSystem();
            dataVersion = 1;
        } else {
            knownSpells = serializedData.containsKey("knownSpells") ? (Map<String, Integer>) serializedData.get("knownSpells") : new HashMap<>();
        }
        initializeSpells();
    }

    private void initializeSpells() {
        if (knownSpells == null) {
            knownSpells = new HashMap<>();
        }
        SpellRegistry registry = new SpellRegistry();
        for (String spell : registry.getAllTechnicalNames()) {
            knownSpells.putIfAbsent(spell, 0);
        }

    }

    public void evaluateMana() {
        maximumMana = 100;
        for (String spell : knownSpells.keySet()) {
            if (knownSpells.get(spell) > 0) {
                maximumMana += 50;
            }
        }
        currentMana = maximumMana;
    }

    public void upgradeSpellSystem() {
        for (String spell : knownSpellsOld.keySet()) {
            int value = knownSpellsOld.getOrDefault("spell", false) ? 1 : 0;
            knownSpells.put("spell", value);
        }
    }

    public boolean knowsSpell(String spell) {
        return knownSpells.getOrDefault(spell, 0) > 0;
    }

    public int getSpellLevel(String spell) {
        return knownSpells.getOrDefault(spell, 0);
    }

    public PlayerData learnSpell(String spell) {
        knownSpells.put(spell, 1);
        return this;
    }

    public PlayerData setSpell(String spell, int value) {
        knownSpells.put(spell, value);
        return this;
    }

    public boolean veinMiner() {
        return veinMiner;
    }

    public PlayerData setVeinMiner(boolean veinMiner) {
        this.veinMiner = veinMiner;
        return this;
    }

    public PlayerData toggleVeinMiner() {
        veinMiner = !veinMiner;
        return this;
    }

    public boolean sortChests() {
        return sortChests;
    }

    public PlayerData setSortChests(boolean sortChests) {
        this.sortChests = sortChests;
        return this;
    }

    public PlayerData toggleSortChests() {
        sortChests = !sortChests;
        return this;
    }

    public int getCurrentMana() {
        return currentMana;
    }

    public PlayerData setCurrentMana(int currentMana) {
        this.currentMana = currentMana;
        return this;
    }

    public PlayerData addCurrentMana(int mana) {
        if (this.currentMana + mana > this.maximumMana) {
            this.currentMana = this.maximumMana;
        } else {
            this.currentMana += mana;
        }
        return this;
    }

    public PlayerData takeCurrentMana(int mana) {
        if (this.currentMana - mana < 0) {
            this.currentMana = 0;
        } else {
            this.currentMana -= mana;
        }
        return this;
    }

    public int getMaximumMana() {
        return maximumMana;
    }

    public PlayerData setMaximumMana(int maximumMana) {
        this.maximumMana = maximumMana;
        return this;
    }

    public PlayerData addMaximumMana(int maximumMana) {
        this.maximumMana += maximumMana;
        return this;
    }

    public PlayerData takeMaximumMana(int maximumMana) {
        this.maximumMana -= maximumMana;
        return this;
    }

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
