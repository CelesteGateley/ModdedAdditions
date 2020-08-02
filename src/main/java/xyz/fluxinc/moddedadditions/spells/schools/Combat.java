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
import xyz.fluxinc.moddedadditions.spells.recipe.MaterialRecipeIngredient;

import java.util.ArrayList;
import java.util.List;

import static xyz.fluxinc.fluxcore.utils.LoreUtils.addLore;
import static xyz.fluxinc.moddedadditions.ModdedAdditions.KEY_BASE;
import static xyz.fluxinc.moddedadditions.controllers.customitems.SpellBookController.SB_KEY_BASE;

public class Combat extends SpellSchool {

    private final List<Spell> spells = new ArrayList<>();

    public Combat() {
        spells.add(new Arrows());
        spells.add(new Fireball());
        spells.add(new Slowball());
        spells.add(new Smite());
    }

    @Override
    public String getLocalizedName() {
        return "Combat School";
    }

    @Override
    public String getTechnicalName() {
        return "combat";
    }

    @Override
    public int getModelId() {
        return KEY_BASE + SB_KEY_BASE + 4001;
    }

    @Override
    public ItemStack getItemStack() {
        ItemStack itemStack = addLore(new ItemStack(Material.DIAMOND_SWORD), "Spells related to dealing damage");
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(this.getLocalizedName());
        itemMeta.setCustomModelData(this.getModelId());
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    @Override
    public String getRiddle() {
        return "The weapons you wield, the range, " +
                "the glimmering blade and projectile, " +
                "and the protection you hold, " +
                "catalysed by the knowledge held within pages";
    }

    @Override
    public SpellRecipe<SpellSchool> getRecipe() {
        return new SpellRecipe<>(this, new MaterialRecipeIngredient(Material.BOOK),
                new MaterialRecipeIngredient(Material.DIAMOND_SWORD), new MaterialRecipeIngredient(Material.SHIELD),
                new MaterialRecipeIngredient(Material.CROSSBOW), new MaterialRecipeIngredient(Material.ARROW));
    }

    @Override
    public List<Spell> getSpells() {
        return spells;
    }
}
