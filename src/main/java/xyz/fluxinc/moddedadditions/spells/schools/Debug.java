package xyz.fluxinc.moddedadditions.spells.schools;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import xyz.fluxinc.moddedadditions.spells.Spell;
import xyz.fluxinc.moddedadditions.spells.SpellRecipe;
import xyz.fluxinc.moddedadditions.spells.SpellSchool;
import xyz.fluxinc.moddedadditions.spells.castable.combat.Arrows;
import xyz.fluxinc.moddedadditions.spells.castable.combat.Fireball;
import xyz.fluxinc.moddedadditions.spells.castable.combat.Slowball;
import xyz.fluxinc.moddedadditions.spells.castable.combat.Smite;
import xyz.fluxinc.moddedadditions.spells.castable.debug.*;
import xyz.fluxinc.moddedadditions.spells.recipe.MaterialRecipeIngredient;

import java.util.ArrayList;
import java.util.List;

import static xyz.fluxinc.fluxcore.utils.LoreUtils.addLore;
import static xyz.fluxinc.moddedadditions.ModdedAdditions.KEY_BASE;
import static xyz.fluxinc.moddedadditions.controllers.customitems.SpellBookController.SB_KEY_BASE;

public class Debug extends SpellSchool {

    private final List<Spell> spells = new ArrayList<>();

    public Debug() {
        spells.add(new FillMana());
        spells.add(new IncreaseAll());
        spells.add(new DecreaseAll());
        spells.add(new UnlockAllSchools());
        spells.add(new LockAllSchools());
        spells.add(new IncreaseMaxMana());
        spells.add(new DecreaseMaxMana());
    }

    @Override
    public String getLocalizedName() {
        return "Debug School";
    }

    @Override
    public String getTechnicalName() {
        return "debug";
    }

    @Override
    public int getModelId() {
        return KEY_BASE + SB_KEY_BASE + 4099;
    }

    @Override
    public ItemStack getItemStack() {
        ItemStack itemStack = addLore(new ItemStack(Material.COMMAND_BLOCK), "Debug Spells");
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(this.getLocalizedName());
        itemMeta.setCustomModelData(this.getModelId());
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    @Override
    public String getRiddle() {
        return "";
    }

    @Override
    public SpellRecipe getRecipe() {
        return null;
    }

    @Override
    public List<Spell> getSpells() {
        return spells;
    }
}
