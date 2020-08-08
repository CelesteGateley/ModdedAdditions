package xyz.fluxinc.moddedadditions.spells.castable.combat;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Snowball;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionType;
import xyz.fluxinc.moddedadditions.ModdedAdditions;
import xyz.fluxinc.moddedadditions.controllers.customitems.SpellBookController;
import xyz.fluxinc.moddedadditions.spells.Spell;
import xyz.fluxinc.moddedadditions.spells.SpellRecipe;
import xyz.fluxinc.moddedadditions.spells.recipe.EnchantedBookRecipeIngredient;
import xyz.fluxinc.moddedadditions.spells.recipe.MaterialRecipeIngredient;
import xyz.fluxinc.moddedadditions.spells.recipe.PotionRecipeIngredient;

import static xyz.fluxinc.fluxcore.utils.LoreUtils.addLore;

public class Slowball extends Spell {

    public static final String SLOWBALL_NAME = "abXYZoasDGAdgiQXVSAadsgasnNEFA";
    public static final String LONGER_SLOWBALL_NAME = "abXYZBBDEXZsadfDGAdgiQXadsgasnNEFA";
    public static final String POTENT_SLOWBALL_NAME = "sadfasZFFDGVEasdeNEFA";

    @Override
    public ItemStack getDefaultItem(World.Environment environment, int level) {
        return new ItemStack(Material.SNOWBALL);
    }

    @Override
    public String getLocalizedName() {
        return "Slow Ball";
    }

    @Override
    public String getTechnicalName() {
        return "slowball";
    }

    @Override
    public String getDescription(int level) {
        switch (level) {
            case 1: return "Increases slowball speed!";
            case 2: return "Increases the slow duration!";
            case 3: return "Increases the amount slowed by!";
            default: return "Flings a snowball to slow your target down!";
        }
    }

    @Override
    public int getModelId() {
        return ModdedAdditions.KEY_BASE + SpellBookController.SB_KEY_BASE + 2;
    }

    @Override
    public int getCost(World.Environment environment, int level) {
        return 15;
    }

    @Override
    public String getRiddle(int level) {
        switch (level) {
            case 0:
                return "&9Catalyst: &4Redstone\n\n&9One strike from winter's wrath slows your very soul to a crawl";
            case 1:
                return "&9Catalyst: &cGlowstone Dust\n\n&9With increased strength, you can channel more power into your throw, slowing the enemies flesh quicker";
            default:
                return null;
        }
    }

    @Override
    public long getCooldown(int level) {
        return 2000;
    }

    @Override
    public SpellRecipe getRecipe(int level) {
        switch (level) {
            case 0:
                return new SpellRecipe(this, new MaterialRecipeIngredient(Material.REDSTONE),
                        new MaterialRecipeIngredient(Material.SNOWBALL), new MaterialRecipeIngredient(Material.SOUL_SAND));
            case 1:
                return new SpellRecipe(this, new MaterialRecipeIngredient(Material.GLOWSTONE_DUST),
                        new PotionRecipeIngredient(PotionType.STRENGTH), new EnchantedBookRecipeIngredient(Enchantment.CHANNELING),
                        new EnchantedBookRecipeIngredient(Enchantment.ARROW_DAMAGE), new MaterialRecipeIngredient(Material.ROTTEN_FLESH));
            default:
                return null;
        }
    }

    @Override
    public boolean enactSpell(Player caster, LivingEntity target, int level) {
        Projectile entity = caster.launchProjectile(Snowball.class);
        entity.setCustomName(SLOWBALL_NAME);
        switch (level) {
            case 1:
                return true;
            case 2:
                entity.getVelocity().multiply(2);
                return true;
            case 3:
                entity.getVelocity().multiply(2);
                entity.setCustomName(LONGER_SLOWBALL_NAME);
                return true;
            case 4:
                entity.getVelocity().multiply(2);
                entity.setCustomName(POTENT_SLOWBALL_NAME);
                return true;
            default:
                return false;
        }
    }
}
