package xyz.fluxinc.moddedadditions.magic.spells.castable.debug;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import xyz.fluxinc.moddedadditions.ModdedAdditions;
import xyz.fluxinc.moddedadditions.common.storage.PlayerData;
import xyz.fluxinc.moddedadditions.magic.controller.SpellBookController;
import xyz.fluxinc.moddedadditions.magic.spells.Spell;
import xyz.fluxinc.moddedadditions.magic.spells.SpellRecipe;

import static xyz.fluxinc.moddedadditions.ModdedAdditions.instance;

public class IncreaseMaxMana extends Spell {

    @Override
    public ItemStack getDefaultItem(World.Environment environment, int level) {
        return new ItemStack(Material.PAPER);
    }

    @Override
    public String getLocalizedName() {
        return "Increase Max Mana by 50";
    }

    @Override
    public String getTechnicalName() {
        return "increase_mana";
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
        return ModdedAdditions.KEY_BASE + SpellBookController.SB_KEY_BASE + 105;
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
        data.setMaximumMana(data.getMaximumMana() + 50);
        instance.getPlayerDataController().setPlayerData(caster, data);
        return true;
    }
}
