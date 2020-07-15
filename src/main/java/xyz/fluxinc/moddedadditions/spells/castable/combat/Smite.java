package xyz.fluxinc.moddedadditions.spells.castable.combat;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
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

public class Smite extends Spell {

    @Override
    public String getLocalizedName() {
        return "Smite";
    }

    @Override
    public String getTechnicalName() {
        return "smite";
    }

    @Override
    public int getModelId() {
        return ModdedAdditions.KEY_BASE + SpellBookController.SB_KEY_BASE + 4;
    }

    @Override
    public ItemStack getItemStack(World.Environment environment, int modelId, int level) {
        ItemStack smite = addLore(new ItemStack(Material.TRIDENT), "Costs: " + getCost(environment, level) + " Mana");
        smite = addLore(smite, "Cooldown: " + getCooldown(level) / 1000d + " Seconds");
        ItemMeta iMeta = smite.getItemMeta();
        iMeta.setCustomModelData(modelId);
        iMeta.setDisplayName(ChatColor.WHITE + getLocalizedName());
        smite.setItemMeta(iMeta);
        return smite;
    }

    @Override
    public int getCost(World.Environment environment, int level) {
        return 50;
    }

    @Override
    public String getRiddle(int level) {
        return "Poseidon strikes at the richest ore held in the highest mountains";
    }

    @Override
    public long getCooldown(int level) {
        return 1000;
    }

    @Override
    public SpellRecipe getRecipe(int level) {
        if (level != 1) return null;
        return new SpellRecipe(new MaterialRecipeIngredient(Material.REDSTONE),
                new MaterialRecipeIngredient(Material.EMERALD_ORE), new MaterialRecipeIngredient(Material.TRIDENT));
    }

    @Override
    public boolean enactSpell(Player caster, LivingEntity target, int level) {
        Block targetBlock = caster.getTargetBlock(null, 50);
        if (targetBlock.getType() == Material.AIR) {
            caster.getWorld().playSound(caster.getLocation(), Sound.BLOCK_FIRE_EXTINGUISH, 1, 1);
            return false;
        }
        caster.getWorld().strikeLightning(targetBlock.getLocation());
        caster.getWorld().playSound(caster.getLocation(), Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 1, 1);
        return true;
    }
}
