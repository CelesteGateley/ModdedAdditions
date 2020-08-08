package xyz.fluxinc.moddedadditions.spells.castable.combat;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionType;
import xyz.fluxinc.moddedadditions.ModdedAdditions;
import xyz.fluxinc.moddedadditions.controllers.customitems.SpellBookController;
import xyz.fluxinc.moddedadditions.spells.Spell;
import xyz.fluxinc.moddedadditions.spells.SpellRecipe;
import xyz.fluxinc.moddedadditions.spells.recipe.EnchantedBookRecipeIngredient;
import xyz.fluxinc.moddedadditions.spells.recipe.MaterialRecipeIngredient;
import xyz.fluxinc.moddedadditions.spells.recipe.PotionRecipeIngredient;

import static xyz.fluxinc.fluxcore.utils.LoreUtils.addLore;

public class Fireball extends Spell {

    @Override
    public ItemStack getDefaultItem(World.Environment environment, int level) {
        return new ItemStack(Material.FIRE_CHARGE);
    }

    @Override
    public String getLocalizedName() {
        return "Fireball";
    }

    @Override
    public String getTechnicalName() {
        return "fireball";
    }

    @Override
    public String getDescription(int level) {
        switch (level) {
            case 1: return "Increases the explosion radius";
            case 2: return "Increases the speed of the fireball";
            case 3: return "Increases the power of the explosion";
            default: return "Throw a fireball forward!";
        }
    }

    @Override
    public int getModelId() {
        return ModdedAdditions.KEY_BASE + SpellBookController.SB_KEY_BASE + 3;
    }

    @Override
    public int getCost(World.Environment environment, int level) {
        return 50;
    }

    @Override
    public String getRiddle(int level) {
        switch (level) {
            case 0:
                return "&9Catalyst: &4Redstone\n\n&9The tears of your enemies will burn away as you charge forth";
            case 1:
                return "&9Catalyst: &cGlowstone Dust\n\n&9Blazing a trail, your magic will break all chains, stronger than the hardest metal, and overcoming any resistance to the blast";
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
                        new MaterialRecipeIngredient(Material.FIRE_CHARGE), new MaterialRecipeIngredient(Material.GHAST_TEAR));
            case 1:
                return new SpellRecipe(this, new MaterialRecipeIngredient(Material.GLOWSTONE_DUST),
                        new MaterialRecipeIngredient(Material.BLAZE_ROD), new MaterialRecipeIngredient(Material.CHAIN),
                        new MaterialRecipeIngredient(Material.NETHERITE_INGOT), new EnchantedBookRecipeIngredient(Enchantment.PROTECTION_EXPLOSIONS));
            default:
                return null;
        }
    }

    @Override
    public boolean enactSpell(Player caster, LivingEntity target, int level) {
        org.bukkit.entity.Fireball fireball = caster.launchProjectile(org.bukkit.entity.Fireball.class);
        caster.getWorld().playSound(caster.getLocation(), Sound.ENTITY_GHAST_SHOOT, 1, 1);
        switch (level) {
            case 1:
                fireball.setYield(2);
                return true;
            case 2:
                fireball.setYield(3);
                return true;
            case 3:
                fireball.getVelocity().multiply(2);
                fireball.setYield(3);
                return true;
            case 4:
                fireball.getVelocity().multiply(2);
                fireball.setYield(4);
                return true;
        }
        return true;
    }
}
