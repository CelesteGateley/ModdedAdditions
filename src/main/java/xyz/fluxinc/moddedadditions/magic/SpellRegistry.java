package xyz.fluxinc.moddedadditions.magic;

import org.bukkit.entity.Player;
import xyz.fluxinc.moddedadditions.ModdedAdditions;
import xyz.fluxinc.moddedadditions.common.storage.PlayerData;
import xyz.fluxinc.moddedadditions.magic.spells.Spell;
import xyz.fluxinc.moddedadditions.magic.spells.SpellRecipe;
import xyz.fluxinc.moddedadditions.magic.spells.SpellSchool;
import xyz.fluxinc.moddedadditions.magic.spells.schools.*;

import java.util.ArrayList;
import java.util.List;

public class SpellRegistry {

    private static final List<SpellSchool> schools = new ArrayList<>();
    private static final Debug debug = new Debug();

    static {
        schools.add(new Combat());
        schools.add(new Movement());
        schools.add(new Tank());
        schools.add(new Support());
        //registerSpell(new FillMana(), "fillmana", KEY_BASE + SB_KEY_BASE + 100);
    }

    public static List<SpellRecipe> getAvailableRecipes(Player player) {
        List<SpellRecipe> recipes = new ArrayList<>();
        PlayerData playerData = ModdedAdditions.instance.getPlayerDataController().getPlayerData(player);
        for (SpellSchool school : schools) {
            if (!playerData.checkSchool(school.getTechnicalName())) {
                recipes.add(school.getRecipe());
            }
        }
        for (Spell spell : getAllSpells()) {
            SpellRecipe recipe = spell.getRecipe(playerData.getSpellLevel(spell.getTechnicalName()));
            if (recipe != null) recipes.add(recipe);
        }
        return recipes;
    }

    public static List<Spell> getAllSpells() {
        List<Spell> spells = new ArrayList<>();
        for (SpellSchool school : schools) {
            spells.addAll(school.getSpells());
        }
        return spells;
    }

    public static Spell getSpellById(int modelId) {
        for (Spell spell : getAllSpells()) {
            if (spell.getModelId() == modelId) {
                return spell;
            }
        }
        for (Spell spell : debug.getSpells()) {
            if (spell.getModelId() == modelId) {
                return spell;
            }
        }
        return null;
    }

    public static SpellSchool getSchoolById(int modelId) {
        if (modelId == debug.getModelId()) return debug;
        for (SpellSchool school : schools) {
            if (school.getModelId() == modelId) {
                return school;
            }
        }
        return null;
    }

    public static String getTechnicalName(int modelId) {
        for (Spell spell : getAllSpells()) {
            if (spell.getModelId() == modelId) {
                return spell.getTechnicalName();
            }
        }
        return null;
    }

    public static List<SpellSchool> getAllSchools() {
        return schools;
    }
}
