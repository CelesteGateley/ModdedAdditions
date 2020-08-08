package xyz.fluxinc.moddedadditions.spells.castable.support;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
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
import xyz.fluxinc.moddedadditions.spells.recipe.MaterialRecipeIngredient;

import static xyz.fluxinc.fluxcore.utils.LoreUtils.addLore;

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
            default:
                return null;
        }

    }

    @Override
    public long getCooldown(int level) {
        return 30000;
    }

    @Override
    public SpellRecipe getRecipe(int level) {
        switch (level) {
            case 0:
                return new SpellRecipe(this, new MaterialRecipeIngredient(Material.REDSTONE),
                        new MaterialRecipeIngredient(Material.NETHERITE_INGOT), new MaterialRecipeIngredient(Material.HEART_OF_THE_SEA));
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
        new PotionEffect(PotionEffectType.NIGHT_VISION, 20 * 20, 0).apply(target);
        new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 20 * 20, 0).apply(target);
        new PotionEffect(PotionEffectType.FAST_DIGGING, 20 * 20, 1).apply(target);
        return true;
    }
}
