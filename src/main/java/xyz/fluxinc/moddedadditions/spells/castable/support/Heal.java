package xyz.fluxinc.moddedadditions.spells.castable.support;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.*;
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
        int heal = 2;

        switch (level) {
            case 4:
            case 3:
                ++heal;
            case 2:
                ++heal;
        }
        List<Entity> targets = getNear(caster, target, level);
        System.out.println(heal);
        System.out.println(targets);
        int counter = 0;
        for (Entity E : targets) {
            System.out.println(E.getType());
            if (E instanceof Mob) {
                System.out.println(((Mob) E).getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
                System.out.println(((Mob) E).getHealth());


                if (((Mob) E).getHealth() != ((Mob) E).getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue()){
                    System.out.println("Need Healing");
                    if (((Mob) E).getHealth() > ((Mob) E).getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue() - heal) {
                        System.out.println("Max heal");
                        ((Mob) E).setHealth(((Mob) E).getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
                    }
                    else {
                        System.out.println("Standard heal");
                    ((Mob) E).setHealth(((Mob) E).getHealth()+heal);
                    }
                    counter ++;
                }
                if (E instanceof Player && ((Player) E).getFoodLevel() < 20) {
                    if (((Player) E).getFoodLevel() <= 20 - heal) {
                        ((Player) E).setFoodLevel(((Player) E).getFoodLevel() + heal);
                    } else {
                        ((Player) E).setFoodLevel(20);
                    }
                    counter++;
                }
                continue;
            }
        }
        if (counter > 0) {
            target.getWorld().spawnParticle(Particle.VILLAGER_HAPPY, target.getLocation(), 50, 0.5, 1, 0.5);
            target.getWorld().playSound(target.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
            return true;
        }
        else{
            return false;
        }
    }

    public List<Entity> getNear(Player caster, LivingEntity target, int level) {

        ArrayList<Entity> entities;
        ArrayList<Entity> toremove = new ArrayList<Entity>() {
        };
        if (level == 4) {
            entities = (ArrayList<Entity>) caster.getWorld().getNearbyEntities(caster.getLocation(), 5, 5, 5);
            for (Entity E:entities){
                if (!(((E instanceof Player || E instanceof Mob) && (((LivingEntity) E).getHealth() < ((LivingEntity) E).getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue())))) {
                    toremove.add(E);
                }
                if (E instanceof Monster || E instanceof Flying || E instanceof Slime) {
                    toremove.add(E);
                }
            }
            entities.removeAll(toremove);
        }
        else {
            entities = null;
            if (caster != target) {
                caster.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("You heal your targets wounds"));
                if (target instanceof Player) {
                    ((Player) target).spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("You feel your wounds mend"));
                    entities.add(target);
                }
            } else {
                caster.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("You feel your wounds mend"));
                entities.add(caster);
            }

        }
        return entities;
    }


}