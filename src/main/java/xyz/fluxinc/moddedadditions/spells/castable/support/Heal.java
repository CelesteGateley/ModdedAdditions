package xyz.fluxinc.moddedadditions.spells.castable.support;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import xyz.fluxinc.moddedadditions.ModdedAdditions;
import xyz.fluxinc.moddedadditions.spells.Spell;

import static xyz.fluxinc.fluxcore.utils.LoreUtils.addLore;

public class Heal extends Spell {

    public Heal(ModdedAdditions instance) {
        super(instance);
    }

    @Override
    public String getName() {
        return "Heal";
    }

    @Override
    public ItemStack getItemStack(World.Environment environment, int modelId) {
        ItemStack heal = addLore(new ItemStack(Material.GOLDEN_APPLE), "Costs: " + getCost(environment) + " Mana");
        heal = addLore(heal, "Cooldown: " + getCooldown()/1000d + " Seconds");
        ItemMeta iMeta = heal.getItemMeta();
        iMeta.setCustomModelData(modelId);
        iMeta.setDisplayName(ChatColor.WHITE + getName());
        heal.setItemMeta(iMeta);
        return heal;
    }

    @Override
    public int getCost(World.Environment environment) {
        return 25;
    }

    @Override
    public String getRiddle() {
        return "The elixir of life gathered from the forbidden fruit";
    }

    @Override
    public long getCooldown() {
        return 250;
    }

    @Override
    public boolean enactSpell(Player caster, LivingEntity target) {
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
