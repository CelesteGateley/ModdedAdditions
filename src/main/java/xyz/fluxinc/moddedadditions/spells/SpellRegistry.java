package xyz.fluxinc.moddedadditions.spells;

import xyz.fluxinc.moddedadditions.spells.castable.combat.Arrows;
import xyz.fluxinc.moddedadditions.spells.castable.combat.Fireball;
import xyz.fluxinc.moddedadditions.spells.castable.combat.SlowBall;
import xyz.fluxinc.moddedadditions.spells.castable.combat.Smite;
import xyz.fluxinc.moddedadditions.spells.castable.movement.AirJet;
import xyz.fluxinc.moddedadditions.spells.castable.movement.LavaWalk;
import xyz.fluxinc.moddedadditions.spells.castable.movement.Speed;
import xyz.fluxinc.moddedadditions.spells.castable.movement.Teleport;
import xyz.fluxinc.moddedadditions.spells.castable.support.Heal;
import xyz.fluxinc.moddedadditions.spells.castable.support.MinersBoon;
import xyz.fluxinc.moddedadditions.spells.castable.support.Vanish;
import xyz.fluxinc.moddedadditions.spells.castable.tank.ForceField;
import xyz.fluxinc.moddedadditions.spells.castable.tank.HardenedForm;
import xyz.fluxinc.moddedadditions.spells.castable.tank.Taunt;

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

    public SpellRegistry() {
        technicalNames = new ArrayList<>();
        registryById = new LinkedHashMap<>();
        registryByName = new LinkedHashMap<>();
        registryToString = new LinkedHashMap<>();

        registerAllSpells();

    }

    private void registerAllSpells() {
        // Combat
        registerSpell(new Arrows(), "arrows", 1);
        registerSpell(new SlowBall(), "slowball", 2);
        registerSpell(new Fireball(), "fireball", 3);
        registerSpell(new Smite(), "smite", 4);
        // Movement
        registerSpell(new AirJet(), "airjet", 20);
        registerSpell(new Speed(), "speed", 21);
        registerSpell(new Teleport(), "teleport", 22);
        registerSpell(new LavaWalk(), "lavawalk", 23);
        // Support
        registerSpell(new Heal(), "heal", 40);
        registerSpell(new Vanish(), "vanish", 41);
        registerSpell(new MinersBoon(), "minersboon", 43);
        // Tank
        registerSpell(new HardenedForm(), "hardenedform", 60);
        registerSpell(new ForceField(), "forcefield", 61);
        registerSpell(new Taunt(), "taunt", 62);
        // Debug
        //registerSpell(new FillMana(), "fillmana", KEY_BASE + SB_KEY_BASE + 100);
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
