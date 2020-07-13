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
    private final List<String> technicalNames;

    public SpellRegistry() {
        technicalNames = new ArrayList<>();
        registryById = new LinkedHashMap<>();
        registerAllSpells();

    }

    private void registerAllSpells() {
        // Combat
        registerSpell(new Arrows(), 1);
        registerSpell(new SlowBall(), 2);
        registerSpell(new Fireball(), 3);
        registerSpell(new Smite(), 4);
        // Movement
        registerSpell(new AirJet(), 20);
        registerSpell(new Speed(), 21);
        registerSpell(new Teleport(), 22);
        registerSpell(new LavaWalk(), 23);
        // Support
        registerSpell(new Heal(), 40);
        registerSpell(new Vanish(), 41);
        registerSpell(new MinersBoon(), 43);
        // Tank
        registerSpell(new HardenedForm(), 60);
        registerSpell(new ForceField(), 61);
        registerSpell(new Taunt(), 62);
        // Debug
        //registerSpell(new FillMana(), "fillmana", KEY_BASE + SB_KEY_BASE + 100);
    }

    public void registerSpell(Spell spell, int modelId) {
        registryById.put(KEY_BASE + SB_KEY_BASE + modelId, spell);
        technicalNames.add(spell.getTechnicalName());
    }

    public List<String> getAllTechnicalNames() {
        return technicalNames;
    }

    public List<Spell> getAllSpells() {
        List<Spell> spells = new ArrayList<>();
        for (Integer model : registryById.keySet()) {
            spells.add(registryById.get(model));
        }
        return spells;
    }

    public Spell getSpellById(int modelId) {
        return registryById.getOrDefault(modelId, null);
    }

    public Map<Integer, Spell> getRegistryById() {
        return registryById;
    }


    public String getTechnicalName(int modelId) {
        return registryById.getOrDefault(modelId, null) == null ? null : registryById.get(modelId).getTechnicalName();
    }
}
