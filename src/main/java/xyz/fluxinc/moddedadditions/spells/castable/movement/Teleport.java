package xyz.fluxinc.moddedadditions.spells.castable.movement;

import org.bukkit.*;
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

public class Teleport extends Spell {

    @Override
    public String getLocalizedName() {
        return "Teleport";
    }

    @Override
    public String getTechnicalName() {
        return "teleport";
    }

    @Override
    public int getModelId() {
        return ModdedAdditions.KEY_BASE + SpellBookController.SB_KEY_BASE + 22;
    }

    @Override
    public ItemStack getItemStack(World.Environment environment, int modelId, int level) {
        ItemStack teleport = addLore(new ItemStack(Material.ENDER_PEARL), "Costs: " + getCost(environment, level) + " Mana");
        teleport = addLore(teleport, "Cooldown: " + getCooldown(level) / 1000d + " Seconds");
        ItemMeta iMeta = teleport.getItemMeta();
        iMeta.setCustomModelData(modelId);
        iMeta.setDisplayName(ChatColor.WHITE + getLocalizedName());
        teleport.setItemMeta(iMeta);
        return teleport;
    }

    @Override
    public int getCost(World.Environment environment, int level) {
        return 50;
    }

    @Override
    public String getRiddle(int level) {
        return "The portal's key holds the truth as to how the tall ones move";
    }

    @Override
    public long getCooldown(int level) {
        return 500;
    }

    @Override
    public SpellRecipe getRecipe(int level) {
        if (level != 1) return null;
        return new SpellRecipe(new MaterialRecipeIngredient(Material.REDSTONE),
                new MaterialRecipeIngredient(Material.ENDER_PEARL), new MaterialRecipeIngredient(Material.ENDER_EYE));
    }

    @Override
    public boolean enactSpell(Player caster, LivingEntity target, int level) {
        Block targetBlock = caster.getTargetBlock(null, 100);
        if (targetBlock.getType() == Material.AIR) {
            caster.getWorld().playSound(caster.getLocation(), Sound.BLOCK_FIRE_EXTINGUISH, 1, 1);
            return false;
        }
        Location targetLocation = targetBlock.getLocation().add(0, 1, 0);
        if (caster.getWorld().getBlockAt(targetLocation).getType() != Material.AIR) {
            return false;
        }
        targetLocation.setPitch(caster.getLocation().getPitch());
        targetLocation.setYaw(caster.getLocation().getYaw());
        caster.teleport(targetLocation);
        caster.getWorld().playSound(caster.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1, 1);
        return true;
    }
}
