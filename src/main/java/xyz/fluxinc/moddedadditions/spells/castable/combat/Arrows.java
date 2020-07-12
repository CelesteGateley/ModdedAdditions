package xyz.fluxinc.moddedadditions.spells.castable.combat;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.AbstractArrow;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import xyz.fluxinc.moddedadditions.ModdedAdditions;
import xyz.fluxinc.moddedadditions.spells.Spell;

import static xyz.fluxinc.fluxcore.utils.LoreUtils.addLore;

public class Arrows extends Spell {

    public Arrows() {
        super();
    }

    @Override
    public String getName() {
        return "Shoot Arrows";
    }

    @Override
    public ItemStack getItemStack(World.Environment environment, int modelId) {
        ItemStack arrows = addLore(new ItemStack(Material.BOW), "Costs: " + getCost(environment) + " Mana");
        arrows = addLore(arrows, "Cooldown: " + getCooldown()/1000d + " Seconds");
        ItemMeta iMeta = arrows.getItemMeta();
        iMeta.setCustomModelData(modelId);
        iMeta.setDisplayName(ChatColor.WHITE + getName());
        arrows.setItemMeta(iMeta);
        return arrows;
    }

    @Override
    public int getCost(World.Environment environment) {
        return 5;
    }

    @Override
    public String getRiddle() {
        return "The eyes of a spectre cause those shot with your firearm to glow to the heavens";
    }

    @Override
    public long getCooldown() {
        return 250;
    }

    @Override
    public boolean enactSpell(Player caster, LivingEntity target) {
        Arrow arrow = caster.launchProjectile(Arrow.class);
        arrow.setPickupStatus(AbstractArrow.PickupStatus.CREATIVE_ONLY);
        caster.getWorld().playSound(caster.getLocation(), Sound.ENTITY_ARROW_SHOOT, 1, 1);
        return true;
    }

}
