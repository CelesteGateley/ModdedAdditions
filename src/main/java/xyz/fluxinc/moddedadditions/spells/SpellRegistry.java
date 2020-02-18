package xyz.fluxinc.moddedadditions.spells;

import xyz.fluxinc.moddedadditions.ModdedAdditions;
import xyz.fluxinc.moddedadditions.spells.castable.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static xyz.fluxinc.moddedadditions.ModdedAdditions.KEY_BASE;
import static xyz.fluxinc.moddedadditions.controllers.customitems.SpellBookController.SB_KEY_BASE;

public class SpellRegistry {

    private Map<Integer, Spell> registryById;
    private Map<String, Spell> registryByName;
    private Map<Integer, String> registryToString;
    private List<String> technicalNames;

    public SpellRegistry(ModdedAdditions instance) {
        technicalNames = new ArrayList<>();
        registryById = new LinkedHashMap<>();
        registryByName = new LinkedHashMap<>();
        registryToString = new LinkedHashMap<>();

        registerAllSpells(instance);

    }

    public SpellRegistry() {
        technicalNames = new ArrayList<>();
        registryById = new LinkedHashMap<>();
        registryByName = new LinkedHashMap<>();
        registryToString = new LinkedHashMap<>();

        registerAllSpells(null);
    }

    private void registerAllSpells(ModdedAdditions instance) {
        registerSpell(new Fireball(instance), "fireball", KEY_BASE + SB_KEY_BASE + 1);
        registerSpell(new Teleport(instance), "teleport", KEY_BASE + SB_KEY_BASE + 2);
        registerSpell(new Arrows(instance), "arrows", KEY_BASE + SB_KEY_BASE + 3);
        registerSpell(new Heal(instance), "heal", KEY_BASE + SB_KEY_BASE + 4);
        registerSpell(new AirJet(instance), "airjet", KEY_BASE + SB_KEY_BASE + 5);
        registerSpell(new FillMana(instance), "fillmana", KEY_BASE + SB_KEY_BASE + 100);
        registerSpell(new Smite(instance), "smite", KEY_BASE + SB_KEY_BASE + 6);
    }

    public void registerSpell(Spell spell, String technicalName, int modelId) {
        technicalNames.add(technicalName);
        registryByName.put(technicalName, spell);
        registryById.put(modelId, spell);
        registryToString.put(modelId, technicalName);
    }

    public List<String> getAllTechnicalNames() {
        return technicalNames;
    }

    public List<Spell> getAllSpells() {
        List<Spell> spells = new ArrayList<>();
        for (String name : registryByName.keySet()) { spells.add(registryByName.get(name)); }
        return spells;
    }

    public Spell getSpellById(int modelId) {
        return registryById.getOrDefault(modelId, null);
    }

    public Map<Integer, Spell> getRegistryById() { return registryById; }

    public Map<String, Spell> getRegistryByName() { return registryByName; }

    public String getTechnicalName(int modelId) { return registryToString.getOrDefault(modelId, null); }
}
