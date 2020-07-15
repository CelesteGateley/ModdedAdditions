package xyz.fluxinc.moddedadditions.spells.castable.support;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionType;
import xyz.fluxinc.moddedadditions.ModdedAdditions;
import xyz.fluxinc.moddedadditions.controllers.customitems.SpellBookController;
import xyz.fluxinc.moddedadditions.spells.Spell;
import xyz.fluxinc.moddedadditions.spells.SpellRecipe;
import xyz.fluxinc.moddedadditions.spells.recipe.MaterialRecipeIngredient;
import xyz.fluxinc.moddedadditions.spells.recipe.PotionRecipeIngredient;

import static xyz.fluxinc.fluxcore.utils.LoreUtils.addLore;

public class Heal extends Spell {

    @Override
    public String getLocalizedName() {
        return "Heal";
    }

    @Override
    public String getTechnicalName() {
        return "heal";
    }

    @Override
    public int getModelId() {
        return ModdedAdditions.KEY_BASE + SpellBookController.SB_KEY_BASE + 40;
    }

    @Override
    public ItemStack getItemStack(World.Environment environment, int modelId, int level) {
        ItemStack heal = addLore(new ItemStack(Material.GOLDEN_APPLE), "Costs: " + getCost(environment, level) + " Mana");
        heal = addLore(heal, "Cooldown: " + getCooldown(level) / 1000d + " Seconds");
        ItemMeta iMeta = heal.getItemMeta();
        iMeta.setCustomModelData(modelId);
        iMeta.setDisplayName(ChatColor.WHITE + getLocalizedName());
        heal.setItemMeta(iMeta);
        return heal;
    }

    @Override
    public int getCost(World.Environment environment, int level) {
        return 25;
    }

    @Override
    public String getRiddle(int level) {
        return "The elixir of life gathered from the forbidden fruit";
    }

    @Override
    public long getCooldown(int level) {
        return 250;
    }

    @Override
    public SpellRecipe getRecipe(int level) {
        if (level != 1) return null;
        return new SpellRecipe(new MaterialRecipeIngredient(Material.REDSTONE),
                new PotionRecipeIngredient(PotionType.INSTANT_HEAL), new MaterialRecipeIngredient(Material.GOLDEN_APPLE));
    }

    @Override
    public boolean enactSpell(Player caster, LivingEntity target, int level) {
        if (caster != target) {
            caster.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("You heal your targets wounds"));
            if (target instanceof Player) {
                ((Player) target).spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("You feel your wounds mend"));
            }
        } else {
            caster.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("You feel your wounds mend"));
        }
        if (target.getHealth() != target.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue()) {
            if (target.getHealth() > target.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue() - 2) {
                target.setHealth(target.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
            } else {
                target.setHealth(target.getHealth() + 2);
            }
            if (target instanceof Player) {
                Player p = (Player) target;
                if (p.getFoodLevel() <= 18) {
                    p.setFoodLevel(p.getFoodLevel() + 2);
                } else {
                    p.setFoodLevel(20);
                }
            }
            target.getWorld().spawnParticle(Particle.VILLAGER_HAPPY, target.getLocation(), 50, 0.5, 1, 0.5);
            target.getWorld().playSound(target.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
            return true;
        } else if (target instanceof Player && ((Player) target).getFoodLevel() < 20) {
            if (((Player) target).getFoodLevel() <= 18) {
                ((Player) target).setFoodLevel(((Player) target).getFoodLevel() + 2);
            } else {
                ((Player) target).setFoodLevel(20);
            }
            target.getWorld().spawnParticle(Particle.VILLAGER_HAPPY, target.getLocation(), 50, 0.5, 1, 0.5);
            target.getWorld().playSound(target.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
            return true;
        } else {
            return false;
        }
    }
}
