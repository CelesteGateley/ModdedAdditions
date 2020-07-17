package xyz.fluxinc.moddedadditions.spells;

import xyz.fluxinc.moddedadditions.spells.castable.combat.Arrows;
import xyz.fluxinc.moddedadditions.spells.castable.combat.Fireball;
import xyz.fluxinc.moddedadditions.spells.castable.combat.Slowball;
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
import java.util.List;

public class SpellRegistry {

    private final List<Spell> spells;

    public SpellRegistry() {
        spells = new ArrayList<>();
        registerAllSpells();
    }

    private void registerAllSpells() {
        // Combat 
        spells.add(new Arrows());
        spells.add(new Fireball());
        spells.add(new Slowball());
        spells.add(new Smite());
        // Movement
        spells.add(new AirJet());
        spells.add(new Speed());
        spells.add(new Teleport());
        spells.add(new LavaWalk());
        // Support
        spells.add(new Heal());
        spells.add(new Vanish());
        spells.add(new MinersBoon());
        // Tank
        spells.add(new HardenedForm());
        spells.add(new ForceField());
        spells.add(new Taunt());
        // Debug
        //registerSpell(new FillMana(), "fillmana", KEY_BASE + SB_KEY_BASE + 100);
    }

    public List<Spell> getAllSpells() {
        return spells;
    }

    public Spell getSpellById(int modelId) {
        for (Spell spell : spells) {
            if (spell.getModelId() == modelId) {
                return spell;
            }
        }
        return null;
    }

    public String getTechnicalName(int modelId) {
        for (Spell spell : spells) {
            if (spell.getModelId() == modelId) {
                return spell.getTechnicalName();
            }
        }
        return null;
    }
}
