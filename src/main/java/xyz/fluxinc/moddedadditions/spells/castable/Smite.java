package xyz.fluxinc.moddedadditions.spells.castable;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import xyz.fluxinc.moddedadditions.ModdedAdditions;
import xyz.fluxinc.moddedadditions.spells.Spell;

import static xyz.fluxinc.fluxcore.utils.LoreUtils.addLore;

public class Smite extends Spell {
    public Smite(ModdedAdditions instance) {
        super(instance);
    }

    @Override
    public String getName() {
        return "Smite";
    }

    @Override
    public ItemStack getItemStack(int modelId) {
        ItemStack smite = addLore(new ItemStack(Material.TRIDENT), "Costs: " + getCost() + " Mana");
        ItemMeta iMeta = smite.getItemMeta();
        iMeta.setCustomModelData(modelId);
        iMeta.setDisplayName(ChatColor.WHITE + getName());
        smite.setItemMeta(iMeta);
        return smite;
    }

    @Override
    public int getCost() {
        return 50;
    }

    @Override
    public String getRiddle() {
        return "Poseidon strikes at the riches held in the highest mountains";
    }

    @Override
    public boolean enactSpell(Player caster, LivingEntity target) {
        Block targetBlock = caster.getTargetBlock(null, 50);
        if (targetBlock.getType() == Material.AIR) {
            caster.getWorld().playSound(caster.getLocation(), Sound.BLOCK_FIRE_EXTINGUISH, 1, 1);
            return false;
        }
        caster.getWorld().strikeLightning(targetBlock.getLocation());
        caster.getWorld().playSound(caster.getLocation(), Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 1, 1);
        return true;
    }
}
