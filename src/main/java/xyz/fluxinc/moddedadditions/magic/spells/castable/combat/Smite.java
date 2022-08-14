package xyz.fluxinc.moddedadditions.magic.spells.castable.combat;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import xyz.fluxinc.moddedadditions.ModdedAdditions;
import xyz.fluxinc.moddedadditions.magic.controller.SpellBookController;
import xyz.fluxinc.moddedadditions.magic.spells.Spell;
import xyz.fluxinc.moddedadditions.magic.spells.SpellRecipe;
import xyz.fluxinc.moddedadditions.magic.spells.recipe.EnchantedBookRecipeIngredient;
import xyz.fluxinc.moddedadditions.magic.spells.recipe.MaterialRecipeIngredient;

public class Smite extends Spell {

    @Override
    public ItemStack getDefaultItem(World.Environment environment, int level) {
        return new ItemStack(Material.TRIDENT);
    }

    @Override
    public String getLocalizedName() {
        return "Smite";
    }

    @Override
    public String getTechnicalName() {
        return "smite";
    }

    @Override
    public String getDescription(int level) {
        switch (level) {
            case 1:
                return "Decreases the mana cost to cast!";
            case 2:
                return "Targets mobs in a 3x3 area around the target!";
            case 3:
                return "Increases radius to 5x5!";
            default:
                return "Strike lightning where you are looking!";
        }
    }

    @Override
    public int getModelId() {
        return ModdedAdditions.KEY_BASE + SpellBookController.SB_KEY_BASE + 4;
    }

    @Override
    public int getCost(World.Environment environment, int level) {
        return level < 2 ? 50 : 25;
    }

    @Override
    public String getRiddle(int level) {
        switch (level) {
            case 0:
                return "&9Catalyst: &4Redstone\n\n&9Poseidon strikes at the richest ore held in the highest mountains";
            case 1:
                return "&9Catalyst: &cGlowstone Dust\n\n&9Exploding with a crash, the target will more easily be set ablaze, and charged quicker to death";
            case 2:
                return "&9Catalyst: &bDiamond\n\n&9Channeling your strength through the drowned shells, the lightning seeks out the light of the soul, and the shot hits all in range";
            default:
                return null;
        }
    }

    @Override
    public long getCooldown(int level) {
        return 1000;
    }

    @Override
    public SpellRecipe getRecipe(int level) {
        switch (level) {
            case 0:
                return new SpellRecipe(this, new MaterialRecipeIngredient(Material.REDSTONE),
                        new MaterialRecipeIngredient(Material.EMERALD_ORE), new MaterialRecipeIngredient(Material.TRIDENT));
            case 1:
                return new SpellRecipe(this, new MaterialRecipeIngredient(Material.GLOWSTONE_DUST),
                        new MaterialRecipeIngredient(Material.TNT), new MaterialRecipeIngredient(Material.TARGET),
                        new EnchantedBookRecipeIngredient(Enchantment.QUICK_CHARGE), new MaterialRecipeIngredient(Material.BLAZE_ROD));
            case 2:
                return new SpellRecipe(this, new MaterialRecipeIngredient(Material.DIAMOND),
                        new MaterialRecipeIngredient(Material.NAUTILUS_SHELL), new MaterialRecipeIngredient(Material.SOUL_TORCH),
                        new EnchantedBookRecipeIngredient(Enchantment.MULTISHOT), new EnchantedBookRecipeIngredient(Enchantment.CHANNELING));
            default:
                return null;
        }
    }

    @Override
    public boolean enactSpell(Player caster, LivingEntity target, int level) {
        Block targetBlock = caster.getTargetBlock(null, 50);
        if (targetBlock.getType() == Material.AIR) {
            caster.getWorld().playSound(caster.getLocation(), Sound.BLOCK_FIRE_EXTINGUISH, 1, 1);
            return false;
        }
        switch (level) {
            case 1:
            case 2:
                caster.getWorld().strikeLightning(targetBlock.getLocation());
                caster.getWorld().playSound(caster.getLocation(), Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 1, 1);
                return true;
            case 3:
                caster.getWorld().strikeLightning(targetBlock.getLocation());
                for (Entity entity : caster.getWorld().getNearbyEntities(targetBlock.getLocation(), 6, 3, 6)) {
                    if (entity instanceof LivingEntity) {
                        entity.getWorld().strikeLightning(entity.getLocation());
                    }
                }
                return true;
            case 4:
                caster.getWorld().strikeLightning(targetBlock.getLocation());
                for (Entity entity : caster.getWorld().getNearbyEntities(targetBlock.getLocation(), 10, 5, 10)) {
                    if (entity instanceof LivingEntity) {
                        if (entity instanceof Player && entity.equals(caster)) continue;
                        entity.getWorld().strikeLightning(entity.getLocation());
                    }
                }
                return true;
            default:
                return false;
        }
    }
}
