package xyz.fluxinc.moddedadditions.magic.spells.schools;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import xyz.fluxinc.moddedadditions.magic.spells.Spell;
import xyz.fluxinc.moddedadditions.magic.spells.SpellRecipe;
import xyz.fluxinc.moddedadditions.magic.spells.SpellSchool;
import xyz.fluxinc.moddedadditions.magic.spells.castable.tank.ForceField;
import xyz.fluxinc.moddedadditions.magic.spells.castable.tank.HardenedForm;
import xyz.fluxinc.moddedadditions.magic.spells.castable.tank.Taunt;
import xyz.fluxinc.moddedadditions.magic.spells.recipe.EnchantedBookRecipeIngredient;
import xyz.fluxinc.moddedadditions.magic.spells.recipe.MaterialRecipeIngredient;

import java.util.ArrayList;
import java.util.List;

import static xyz.fluxinc.fluxcore.utils.LoreUtils.addLore;
import static xyz.fluxinc.moddedadditions.ModdedAdditions.KEY_BASE;
import static xyz.fluxinc.moddedadditions.magic.controller.SpellBookController.SB_KEY_BASE;

public class Tank extends SpellSchool {

    private final List<Spell> spells = new ArrayList<>();

    public Tank() {
        spells.add(new ForceField());
        spells.add(new HardenedForm());
        spells.add(new Taunt());
    }

    @Override
    public String getLocalizedName() {
        return "Tank School";
    }

    @Override
    public String getTechnicalName() {
        return "tank";
    }

    @Override
    public int getModelId() {
        return KEY_BASE + SB_KEY_BASE + 4003;
    }

    @Override
    public ItemStack getItemStack() {
        ItemStack itemStack = addLore(new ItemStack(Material.SHIELD), "Spells related to preventing damage");
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(this.getLocalizedName());
        itemMeta.setCustomModelData(this.getModelId());
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    @Override
    public String getRiddle() {
        return "The hardest rocks," +
                " protecting shell of the young," +
                " resisting the damage," +
                " and dealing it back," +
                " catalysed by the knowledge held within pages";
    }

    @Override
    public SpellRecipe getRecipe() {
        return new SpellRecipe(this, new MaterialRecipeIngredient(Material.BOOK),
                new MaterialRecipeIngredient(Material.OBSIDIAN), new MaterialRecipeIngredient(Material.EGG),
                new EnchantedBookRecipeIngredient(Enchantment.PROTECTION_ENVIRONMENTAL), new EnchantedBookRecipeIngredient(Enchantment.THORNS));
    }

    @Override
    public List<Spell> getSpells() {
        return spells;
    }
}
