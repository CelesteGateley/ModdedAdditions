package xyz.fluxinc.moddedadditions.spells.castable;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import xyz.fluxinc.moddedadditions.ModdedAdditions;
import xyz.fluxinc.moddedadditions.controllers.customitems.SpellBookController;
import xyz.fluxinc.moddedadditions.spells.Spell;
import xyz.fluxinc.moddedadditions.spells.SpellRecipe;

import static xyz.fluxinc.fluxcore.utils.LoreUtils.addLore;

public class FillMana extends Spell {

    @Override
    public ItemStack getDefaultItem(World.Environment environment, int level) {
        return new ItemStack(Material.EMERALD);
    }

    @Override
    public String getLocalizedName() {
        return "Fill Mana";
    }

    @Override
    public String getTechnicalName() {
        return "fillmana";
    }

    @Override
    public int getModelId() {
        return ModdedAdditions.KEY_BASE + SpellBookController.SB_KEY_BASE + 100;
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
        ModdedAdditions.instance.getManaController().regenerateMana(caster, 300);
        return true;
    }
}
