package xyz.fluxinc.moddedadditions.spells.castable.movement;

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
import xyz.fluxinc.moddedadditions.spells.recipe.MaterialRecipeIngredient;

import static xyz.fluxinc.fluxcore.utils.LoreUtils.addLore;

public class LavaWalk extends Spell {

    @Override
    public String getLocalizedName() {
        return "Lava Walk";
    }

    @Override
    public String getTechnicalName() {
        return "lavawalk";
    }

    @Override
    public int getModelId() {
        return ModdedAdditions.KEY_BASE + SpellBookController.SB_KEY_BASE + 23;
    }

    @Override
    public ItemStack getItemStack(World.Environment environment, int modelId, int level) {
        ItemStack lavaWalk = addLore(new ItemStack(Material.LAVA_BUCKET), "Costs: " + getCost(environment, level) + " Mana");
        lavaWalk = addLore(lavaWalk, "Cooldown: " + getCooldown(level) / 1000d + " Seconds");
        ItemMeta iMeta = lavaWalk.getItemMeta();
        iMeta.setCustomModelData(modelId);
        iMeta.setDisplayName(ChatColor.WHITE + getLocalizedName());
        lavaWalk.setItemMeta(iMeta);
        return lavaWalk;
    }

    @Override
    public int getCost(World.Environment environment, int level) {
        return 50;
    }

    @Override
    public String getRiddle(int level) {
        return "With an icy step and a blazing heart, even you can cross a blazing desert";
    }

    @Override
    public long getCooldown(int level) {
        return 25000;
    }

    @Override
    public SpellRecipe getRecipe(int level) {
        if (level != 1) return null;
        return new SpellRecipe(new MaterialRecipeIngredient(Material.REDSTONE),
                new MaterialRecipeIngredient(Material.ICE), new MaterialRecipeIngredient(Material.BLAZE_ROD));
    }

    @Override
    public boolean enactSpell(Player caster, LivingEntity target, int level) {
        caster.sendTitle("You can now walk on lava!", "", 10, 70, 20);
        ModdedAdditions.instance.getSpellBookController().addLavaWalk(caster);
        return true;
    }
}
