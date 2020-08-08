package xyz.fluxinc.moddedadditions.spells.schools;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import xyz.fluxinc.moddedadditions.spells.Spell;
import xyz.fluxinc.moddedadditions.spells.SpellRecipe;
import xyz.fluxinc.moddedadditions.spells.SpellSchool;
import xyz.fluxinc.moddedadditions.spells.castable.support.Heal;
import xyz.fluxinc.moddedadditions.spells.castable.support.MinersBoon;
import xyz.fluxinc.moddedadditions.spells.castable.support.Vanish;
import xyz.fluxinc.moddedadditions.spells.recipe.MaterialRecipeIngredient;

import java.util.ArrayList;
import java.util.List;

import static xyz.fluxinc.fluxcore.utils.LoreUtils.addLore;
import static xyz.fluxinc.moddedadditions.ModdedAdditions.KEY_BASE;
import static xyz.fluxinc.moddedadditions.controllers.customitems.SpellBookController.SB_KEY_BASE;

public class Support extends SpellSchool {

    private final List<Spell> spells = new ArrayList<>();

    public Support() {
        spells.add(new Heal());
        spells.add(new MinersBoon());
        spells.add(new Vanish());
    }

    @Override
    public String getLocalizedName() {
        return "Support School";
    }

    @Override
    public String getTechnicalName() {
        return "support";
    }

    @Override
    public int getModelId() {
        return KEY_BASE + SB_KEY_BASE + 4004;
    }

    @Override
    public ItemStack getItemStack() {
        ItemStack itemStack = addLore(new ItemStack(Material.GOLDEN_APPLE), "Spells related to supporting your travels");
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(this.getLocalizedName());
        itemMeta.setCustomModelData(this.getModelId());
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    @Override
    public String getRiddle() {
        return "A cats wail," +
                " giving you sight to those unseen," +
                " curing your ailments," +
                " and repairing your tools," +
                " catalysed by the knowledge held within pages";
    }

    @Override
    public SpellRecipe getRecipe() {
        return new SpellRecipe(this, new MaterialRecipeIngredient(Material.BOOK),
                new MaterialRecipeIngredient(Material.MUSIC_DISC_CAT), new MaterialRecipeIngredient(Material.GOLDEN_CARROT),
                new MaterialRecipeIngredient(Material.MILK_BUCKET), new MaterialRecipeIngredient(Material.ANVIL));
    }

    @Override
    public List<Spell> getSpells() {
        return spells;
    }
}
