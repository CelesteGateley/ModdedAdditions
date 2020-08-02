package xyz.fluxinc.moddedadditions.spells;

import org.bukkit.inventory.ItemStack;

import java.util.List;

public abstract class SpellSchool {

    public abstract String getLocalizedName();

    public abstract String getTechnicalName();

    public abstract int getModelId();

    public abstract ItemStack getItemStack();

    public abstract String getRiddle();

    public abstract SpellRecipe getRecipe();

    public abstract List<Spell> getSpells();

}
