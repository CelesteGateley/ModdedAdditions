package xyz.fluxinc.moddedadditions.spells.castable.support;

import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
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

import java.util.ArrayList;
import java.util.List;

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
        switch (level) {
            case 0:
                return "&9Catalyst: &4Redstone\n\n&9The elixir of life gathered from the forbidden fruit";
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
                        new PotionRecipeIngredient(PotionType.INSTANT_HEAL), new MaterialRecipeIngredient(Material.GOLDEN_APPLE));
            default:
                return null;
        }
    }

    @Override
    public boolean enactSpell(Player caster, LivingEntity target, int level) {
        int heal = 2;

        switch (level) {
            case 4:
            case 3:
                ++heal;
            case 2:
                ++heal;
        }
        List<LivingEntity> targets = level == 4 ? getNearbyEntities(target, 5) : new ArrayList<>();
        targets.add(target);

        int counter = 0;
        for (LivingEntity entity : targets) {
            boolean hungerCheck = true;
            if (entity instanceof Player) hungerCheck = ((Player) entity).getFoodLevel() >= 20;
            if (entity.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue() == entity.getHealth() && hungerCheck)
                continue;
            if (healTarget(entity, heal)) counter++;
        }
        if (counter > 0) {
            target.getWorld().spawnParticle(Particle.VILLAGER_HAPPY, target.getLocation(), 50, 0.5, 1, 0.5);
            target.getWorld().playSound(target.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
            return true;
        } else {
            return false;
        }
    }

    private List<LivingEntity> getNearbyEntities(LivingEntity target, int distance) {
        List<LivingEntity> entities = new ArrayList<>();
        for (Entity entity : target.getNearbyEntities(distance, distance, distance)) {
            if (!(entity instanceof LivingEntity)) continue;
            if (entity instanceof Monster) continue;
            entities.add((LivingEntity) entity);
        }
        return entities;
    }

    private boolean healTarget(LivingEntity target, int amount) {
        boolean doneHunger = true;
        boolean doneHealth = true;

        if (target instanceof Player) {
            if (((Player) target).getFoodLevel() == 20) {
                doneHunger = false;
            } else if (((Player) target).getFoodLevel() >= 20 - amount) {
                ((Player) target).setFoodLevel(20);
            } else {
                ((Player) target).setFoodLevel(((Player) target).getFoodLevel() + amount);
            }
        }

        if (target.getHealth() == target.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue()) {
            doneHealth = false;
        } else if (target.getHealth() >= target.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue() - amount) {
            target.setHealth(target.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
        } else {
            target.setHealth(target.getHealth() + amount);
        }

        return doneHealth || doneHunger;

    }


}