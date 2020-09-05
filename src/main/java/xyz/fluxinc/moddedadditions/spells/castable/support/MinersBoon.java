package xyz.fluxinc.moddedadditions.spells.castable.support;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
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

public class MinersBoon extends Spell {

    @Override
    public ItemStack getDefaultItem(World.Environment environment, int level) {
        return new ItemStack(Material.DIAMOND_PICKAXE);
    }

    @Override
    public String getLocalizedName() {
        return "Miners Boon";
    }

    @Override
    public String getTechnicalName() {
        return "minersboon";
    }

    @Override
    public String getDescription(int level) {
        switch (level) {
            case 1:
                return "Increases the duration!";
            case 2:
                return "Decreases the cooldown!";
            case 3:
                return "Increases the potency!";
            default:
                return "Grants buffs to help with mining!";
        }
    }


    @Override
    public int getModelId() {
        return ModdedAdditions.KEY_BASE + SpellBookController.SB_KEY_BASE + 43;
    }

    @Override
    public int getCost(World.Environment environment, int level) {
        return 100;
    }

    @Override
    public String getRiddle(int level) {
        switch (level) {
            case 0:
                return "&9Catalyst: &4Redstone\n\n&9An ancient metal, and a buried heart, grants one the strength of the earth";
            case 1:
                return "&9Catalyst: &cGlowstone Dust\n\n&9An ancient pickaxe, efficient and everlasting, self repairing for eternity";
            case 2:
                return "&9Catalyst: &bDiamond\n\n&9A star fallen from the heavens imbues you with the strength of a god, the speed of a cheetah, and the eyes of a cat";
            default:
                return null;
        }
    }

    @Override
    public long getCooldown(int level) {
        return level >= 3 ? 15000 : 30000;
    }

    @Override
    public SpellRecipe getRecipe(int level) {
        switch (level) {
            case 0:
                return new SpellRecipe(this, new MaterialRecipeIngredient(Material.REDSTONE),
                        new MaterialRecipeIngredient(Material.NETHERITE_INGOT), new MaterialRecipeIngredient(Material.HEART_OF_THE_SEA));
            case 1:
                return new SpellRecipe(this, new MaterialRecipeIngredient(Material.GLOWSTONE_DUST),
                        new MaterialRecipeIngredient(Material.NETHERITE_PICKAXE), new EnchantedBookRecipeIngredient(Enchantment.DIG_SPEED),
                        new EnchantedBookRecipeIngredient(Enchantment.DURABILITY), new EnchantedBookRecipeIngredient(Enchantment.MENDING));
            case 2:
                return new SpellRecipe(this, new MaterialRecipeIngredient(Material.GLOWSTONE_DUST),
                        new MaterialRecipeIngredient(Material.NETHER_STAR), new PotionRecipeIngredient(PotionType.SPEED),
                        new PotionRecipeIngredient(PotionType.STRENGTH), new PotionRecipeIngredient(PotionType.NIGHT_VISION));
            default:
                return null;
        }
    }

    @Override
    public boolean enactSpell(Player caster, LivingEntity target, int level) {
        if (caster != target) {
            caster.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("You grant your target the power of the earth"));
            if (target instanceof Player) {
                ((Player) target).spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("You feel the earth's power flow through you"));
            }
        } else {
            caster.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("You feel the earth's power flow through you"));
        }
        new PotionEffect(PotionEffectType.NIGHT_VISION, (level >= 2 ? 40 : 20) * 20, level >= 4 ? 1 : 0).apply(target);
        new PotionEffect(PotionEffectType.FIRE_RESISTANCE, (level >= 2 ? 40 : 20) * 20, level >= 4 ? 1 : 0).apply(target);
        new PotionEffect(PotionEffectType.FAST_DIGGING, (level >= 2 ? 40 : 20) * 20, level >= 4 ? 2 : 1).apply(target);
        return true;
    }
}
