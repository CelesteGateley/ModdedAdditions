package xyz.fluxinc.moddedadditions.spells.castable.combat;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.AbstractArrow;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import xyz.fluxinc.moddedadditions.ModdedAdditions;
import xyz.fluxinc.moddedadditions.controllers.customitems.SpellBookController;
import xyz.fluxinc.moddedadditions.spells.Spell;
import xyz.fluxinc.moddedadditions.spells.SpellRecipe;
import xyz.fluxinc.moddedadditions.spells.recipe.EnchantedBookRecipeIngredient;
import xyz.fluxinc.moddedadditions.spells.recipe.MaterialRecipeIngredient;
import xyz.fluxinc.moddedadditions.spells.recipe.PotionRecipeIngredient;

public class Arrows extends Spell {

    @Override
    public ItemStack getDefaultItem(World.Environment environment, int level) {
        return new ItemStack(Material.BOW);
    }

    @Override
    public String getLocalizedName() {
        return "Shoot Arrows";
    }

    @Override
    public String getTechnicalName() {
        return "arrows";
    }

    @Override
    public int getModelId() {
        return ModdedAdditions.KEY_BASE + SpellBookController.SB_KEY_BASE + 1;
    }

    @Override
    public String getDescription(int level) {
        switch (level) {
            case 1:
                return "Doubles the damage of the arrow!";
            case 2:
                return "Gives the arrow Spectral properties!";
            case 3:
                return "Poisons those hit by the arrow!";
            default:
                return "Fires an Arrow at your crosshair!";
        }
    }

    @Override
    public int getCost(World.Environment environment, int level) {
        return 5;
    }

    @Override
    public String getRiddle(int level) {
        switch (level) {
            case 0:
                return "&9Catalyst: &4Redstone\n\n&9The eyes of a spectre cause those shot with your firearm to glow to the heavens";
            case 1:
                return "&9Catalyst: &cGlowstone Dust\n\n&9With great power comes more damage, leading you to be scattered amongst the bones, fallen to the ground below core";
            case 2:
                return "&9Catalyst: &bDiamond\n\n&9By tipping the arrow, you can damage the target, slow them to a crawl, and bring home the bacon!";
            default:
                return null;
        }
    }

    @Override
    public long getCooldown(int level) {
        return 250;
    }

    @Override
    public SpellRecipe getRecipe(int level) {
        switch (level) {
            case 0:
                return new SpellRecipe(this, new MaterialRecipeIngredient(Material.REDSTONE),
                        new MaterialRecipeIngredient(Material.SPECTRAL_ARROW), new MaterialRecipeIngredient(Material.CROSSBOW));
            case 1:
                return new SpellRecipe(this, new MaterialRecipeIngredient(Material.GLOWSTONE_DUST),
                        new EnchantedBookRecipeIngredient(Enchantment.ARROW_DAMAGE), new MaterialRecipeIngredient(Material.LEAD),
                        new MaterialRecipeIngredient(Material.BONE), new MaterialRecipeIngredient(Material.NETHERRACK));
            case 2:
                return new SpellRecipe(this, new MaterialRecipeIngredient(Material.DIAMOND),
                        new PotionRecipeIngredient(PotionType.SLOWNESS), new PotionRecipeIngredient(PotionType.INSTANT_DAMAGE),
                        new MaterialRecipeIngredient(Material.COOKED_PORKCHOP), new MaterialRecipeIngredient(Material.TIPPED_ARROW));
            default:
                return null;
        }
    }

    @Override
    public boolean enactSpell(Player caster, LivingEntity target, int level) {
        caster.getWorld().playSound(caster.getLocation(), Sound.ENTITY_ARROW_SHOOT, 1, 1);
        Arrow arrow = caster.launchProjectile(Arrow.class);
        arrow.setPickupStatus(AbstractArrow.PickupStatus.CREATIVE_ONLY);
        switch (level) {
            case 1:
                return true;
            case 4:
                arrow.setBasePotionData(new PotionData(PotionType.POISON));
            case 3:
                arrow.addCustomEffect(new PotionEffect(PotionEffectType.GLOWING, 100, 1), true);
            case 2:
                arrow.setDamage(arrow.getDamage() * 2);
                return true;
        }
        return true;
    }

}
