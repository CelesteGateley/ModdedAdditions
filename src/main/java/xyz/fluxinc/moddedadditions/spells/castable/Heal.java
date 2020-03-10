package xyz.fluxinc.moddedadditions.spells.castable;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
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
    public ItemStack getItemStack(int modelId) {
        ItemStack heal = addLore(new ItemStack(Material.GOLDEN_APPLE), "Costs: " + getCost() + " Mana");
        ItemMeta iMeta = heal.getItemMeta();
        iMeta.setCustomModelData(modelId);
        iMeta.setDisplayName(ChatColor.WHITE + getName());
        heal.setItemMeta(iMeta);
        return heal;
    }

    @Override
    public int getCost() {
        return 25;
    }

    @Override
    public boolean enactSpell(Player caster, LivingEntity target) {
        if (target.getHealth() != target.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue()) {
            if (target.getHealth() > target.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue() - 2) {
                target.setHealth(target.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
            } else {
                target.setHealth(target.getHealth() + 2);
            }
            target.getWorld().spawnParticle(Particle.VILLAGER_HAPPY, target.getLocation(), 50, 0.5, 1, 0.5);
            target.getWorld().playSound(target.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
            return true;
        } else {
            return false;
        }
    }
}
