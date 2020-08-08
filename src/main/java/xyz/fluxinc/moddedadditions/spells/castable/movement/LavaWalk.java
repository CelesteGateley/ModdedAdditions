package xyz.fluxinc.moddedadditions.spells.castable.movement;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import xyz.fluxinc.moddedadditions.ModdedAdditions;
import xyz.fluxinc.moddedadditions.controllers.customitems.SpellBookController;
import xyz.fluxinc.moddedadditions.spells.Spell;
import xyz.fluxinc.moddedadditions.spells.SpellRecipe;
import xyz.fluxinc.moddedadditions.spells.recipe.EnchantedBookRecipeIngredient;
import xyz.fluxinc.moddedadditions.spells.recipe.MaterialRecipeIngredient;

import static xyz.fluxinc.fluxcore.utils.LoreUtils.addLore;

public class LavaWalk extends Spell {

    @Override
    public ItemStack getDefaultItem(World.Environment environment, int level) {
        return new ItemStack(Material.LAVA_BUCKET);
    }

    @Override
    public String getLocalizedName() {
        return "Lava Walk";
    }

    @Override
    public String getTechnicalName() {
        return "lavawalk";
    }

    @Override
    public String getDescription(int level) {
        switch (level) {
            case 1: return "Increases the duration!";
            case 2: return "Decreases the cooldown!";
            case 3: return "Grants Fire Resistance for the duration!";
            default: return "Leaves a cobblestone trail at your feet when walking over lava!";
        }
    }

    @Override
    public int getModelId() {
        return ModdedAdditions.KEY_BASE + SpellBookController.SB_KEY_BASE + 23;
    }

    @Override
    public int getCost(World.Environment environment, int level) {
        return 50;
    }

    @Override
    public String getRiddle(int level) {
        switch (level) {
            case 0:
                return "&9Catalyst: &4Redstone\n\n&9With an icy step and a blazing heart, even you can cross a blazing desert";
            case 1:
                return "&9Catalyst: &cGlowstone Dust\n\n&9Your frozen step, walking with a wet soul, forming rock below, given life by the souls of the damned";
            default:
                return null;
        }

    }

    @Override
    public long getCooldown(int level) {
        return level >= 3 ? 15000 : 25000;
    }

    @Override
    public SpellRecipe getRecipe(int level) {
        switch (level) {
            case 0:
                return new SpellRecipe(this, new MaterialRecipeIngredient(Material.REDSTONE),
                        new MaterialRecipeIngredient(Material.ICE), new MaterialRecipeIngredient(Material.BLAZE_ROD));
            case 1:
                return new SpellRecipe(this, new MaterialRecipeIngredient(Material.GLOWSTONE_DUST),
                        new MaterialRecipeIngredient(Material.SOUL_SAND), new MaterialRecipeIngredient(Material.WATER_BUCKET),
                        new EnchantedBookRecipeIngredient(Enchantment.FROST_WALKER), new MaterialRecipeIngredient(Material.COBBLESTONE));
            default:
                return null;
        }
    }

    @Override
    public boolean enactSpell(Player caster, LivingEntity target, int level) {
        caster.sendTitle("You can now walk on lava!", "", 10, 70, 20);
        ModdedAdditions.instance.getSpellBookController().addLavaWalk(caster, level >= 2 ? 60 : 30);
        if (level >= 4) new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 60 * 20, 0).apply(caster);
        return true;
    }
}
