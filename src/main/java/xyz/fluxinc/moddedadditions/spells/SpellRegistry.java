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

    private final Map<Integer, Spell> registryById;
    private final Map<String, Spell> registryByName;
    private final Map<Integer, String> registryToString;
    private final List<String> technicalNames;

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
        registerSpell(new Fireball(instance), "fireball", 1);
        registerSpell(new Teleport(instance), "teleport", 2);
        registerSpell(new Arrows(instance), "arrows", 3);
        registerSpell(new Heal(instance), "heal", 4);
        registerSpell(new AirJet(instance), "airjet", 5);
        //registerSpell(new FillMana(instance), "fillmana", KEY_BASE + SB_KEY_BASE + 100);
        registerSpell(new Smite(instance), "smite", 6);
        registerSpell(new Speed(instance), "speed", 7);
        registerSpell(new SlowBall(instance), "slowball", 8);
        registerSpell(new MinersBoon(instance), "minersboon", 9);
        registerSpell(new LavaWalk(instance), "lavawalk", 10);
        registerSpell(new ForceField(instance), "forcefield", 11);
    }

    public void registerSpell(Spell spell, String technicalName, int modelId) {
        technicalNames.add(technicalName);
        registryByName.put(technicalName, spell);
        registryById.put(KEY_BASE + SB_KEY_BASE + modelId, spell);
        registryToString.put(KEY_BASE + SB_KEY_BASE + modelId, technicalName);
    }

    public List<String> getAllTechnicalNames() {
        return technicalNames;
    }

    public List<Spell> getAllSpells() {
        List<Spell> spells = new ArrayList<>();
        for (String name : registryByName.keySet()) {
            spells.add(registryByName.get(name));
        }
        return spells;
    }

    public Spell getSpellById(int modelId) {
        return registryById.getOrDefault(modelId, null);
    }

    public Map<Integer, Spell> getRegistryById() {
        return registryById;
    }

    public Map<String, Spell> getRegistryByName() {
        return registryByName;
    }

    public String getTechnicalName(int modelId) {
        return registryToString.getOrDefault(modelId, null);
    }
}
