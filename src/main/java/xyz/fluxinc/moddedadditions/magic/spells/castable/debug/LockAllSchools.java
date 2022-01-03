package xyz.fluxinc.moddedadditions.magic.spells.castable.debug;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import xyz.fluxinc.moddedadditions.ModdedAdditions;
import xyz.fluxinc.moddedadditions.common.storage.PlayerData;
import xyz.fluxinc.moddedadditions.magic.SpellRegistry;
import xyz.fluxinc.moddedadditions.magic.controller.SpellBookController;
import xyz.fluxinc.moddedadditions.magic.spells.Spell;
import xyz.fluxinc.moddedadditions.magic.spells.SpellRecipe;
import xyz.fluxinc.moddedadditions.magic.spells.SpellSchool;

import static xyz.fluxinc.moddedadditions.ModdedAdditions.instance;

public class LockAllSchools extends Spell {

    @Override
    public ItemStack getDefaultItem(World.Environment environment, int level) {
        return new ItemStack(Material.BARRIER);
    }

    @Override
    public String getLocalizedName() {
        return "Lock All Schools";
    }

    @Override
    public String getTechnicalName() {
        return "lock_schools";
    }

    @Override
    public String getDescription(int level) {
        switch (level) {
            default:
                return "";
        }
    }

    @Override
    public int getModelId() {
        return ModdedAdditions.KEY_BASE + SpellBookController.SB_KEY_BASE + 104;
    }

    @Override
    public int getCost(World.Environment environment, int level) {
        return 0;
    }

    @Override
    public String getRiddle(int level) {
        return "";
    }

    @Override
    public long getCooldown(int level) {
        return 0;
    }

    @Override
    public SpellRecipe getRecipe(int level) {
        return null;
    }

    @Override
    public boolean enactSpell(Player caster, LivingEntity target, int level) {
        PlayerData data = instance.getPlayerDataController().getPlayerData(caster);
        for (SpellSchool school : SpellRegistry.getAllSchools()) {
            data.setSchool(school.getTechnicalName(), false);
        }
        instance.getPlayerDataController().setPlayerData(caster, data);
        return true;
    }
}
