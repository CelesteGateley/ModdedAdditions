package xyz.fluxinc.moddedadditions.spells.schools;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionType;
import xyz.fluxinc.moddedadditions.spells.Spell;
import xyz.fluxinc.moddedadditions.spells.SpellRecipe;
import xyz.fluxinc.moddedadditions.spells.SpellSchool;
import xyz.fluxinc.moddedadditions.spells.castable.movement.AirJet;
import xyz.fluxinc.moddedadditions.spells.castable.movement.LavaWalk;
import xyz.fluxinc.moddedadditions.spells.castable.movement.Speed;
import xyz.fluxinc.moddedadditions.spells.castable.movement.Teleport;
import xyz.fluxinc.moddedadditions.spells.recipe.MaterialRecipeIngredient;
import xyz.fluxinc.moddedadditions.spells.recipe.PotionRecipeIngredient;

import java.util.ArrayList;
import java.util.List;

import static xyz.fluxinc.fluxcore.utils.LoreUtils.addLore;
import static xyz.fluxinc.moddedadditions.ModdedAdditions.KEY_BASE;
import static xyz.fluxinc.moddedadditions.controllers.customitems.SpellBookController.SB_KEY_BASE;

public class Movement extends SpellSchool {

    private final List<Spell> spells = new ArrayList<>();

    public Movement() {
        spells.add(new AirJet());
        spells.add(new Speed());
        spells.add(new Teleport());
        spells.add(new LavaWalk());
    }

    @Override
    public String getLocalizedName() {
        return "Movement School";
    }

    @Override
    public String getTechnicalName() {
        return "movement";
    }

    @Override
    public int getModelId() {
        return KEY_BASE + SB_KEY_BASE + 4002;
    }

    @Override
    public ItemStack getItemStack() {
        ItemStack itemStack = addLore(new ItemStack(Material.FEATHER), "Spells related to moving about");
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(this.getLocalizedName());
        itemMeta.setCustomModelData(this.getModelId());
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    @Override
    public String getRiddle() {
        return "Flying into the sky, " +
                "swifter than the wind, " +
                "bouncing towards the heavens, " +
                "with boots that glisten in the sunlight, " +
                "catalysed by the knowledge held within pages";
    }

    @Override
    public SpellRecipe getRecipe() {
        return new SpellRecipe(this, new MaterialRecipeIngredient(Material.BOOK),
                new MaterialRecipeIngredient(Material.SHULKER_SHELL), new PotionRecipeIngredient(PotionType.SPEED),
                new MaterialRecipeIngredient(Material.SLIME_BLOCK), new MaterialRecipeIngredient(Material.DIAMOND_BOOTS));
    }

    @Override
    public List<Spell> getSpells() {
        return spells;
    }
}
