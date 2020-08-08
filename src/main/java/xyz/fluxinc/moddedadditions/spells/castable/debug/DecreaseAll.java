package xyz.fluxinc.moddedadditions.spells.castable.debug;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import xyz.fluxinc.moddedadditions.ModdedAdditions;
import xyz.fluxinc.moddedadditions.controllers.customitems.SpellBookController;
import xyz.fluxinc.moddedadditions.spells.Spell;
import xyz.fluxinc.moddedadditions.spells.SpellRecipe;
import xyz.fluxinc.moddedadditions.storage.PlayerData;
import xyz.fluxinc.moddedadditions.utils.registries.SpellRegistry;

import static xyz.fluxinc.moddedadditions.ModdedAdditions.instance;

public class DecreaseAll extends Spell {

    @Override
    public ItemStack getDefaultItem(World.Environment environment, int level) {
        return new ItemStack(Material.ROTTEN_FLESH);
    }

    @Override
    public String getLocalizedName() {
        return "Decrease Spells by 1";
    }

    @Override
    public String getTechnicalName() {
        return "decrease_spells";
    }

    @Override
    public String getDescription(int level) {
        switch (level) {
            default: return "";
        }
    }

    @Override
    public int getModelId() {
        return ModdedAdditions.KEY_BASE + SpellBookController.SB_KEY_BASE + 102;
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
        for (Spell spell : SpellRegistry.getAllSpells()) {
            int newLevel = data.getSpellLevel(spell.getTechnicalName()) == 0 ? 0 : data.getSpellLevel(spell.getTechnicalName()) - 1;
            data.setSpell(spell.getTechnicalName(), newLevel);
        }
        instance.getPlayerDataController().setPlayerData(caster, data);
        return true;
    }
}
