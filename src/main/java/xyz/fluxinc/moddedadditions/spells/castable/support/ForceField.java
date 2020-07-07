package xyz.fluxinc.moddedadditions.spells.castable.support;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import xyz.fluxinc.moddedadditions.ModdedAdditions;
import xyz.fluxinc.moddedadditions.spells.Spell;

import static xyz.fluxinc.fluxcore.utils.LoreUtils.addLore;

public class ForceField extends Spell {

    public ForceField(ModdedAdditions instance) {
        super(instance);
    }

    @Override
    public String getName() {
        return "ForceField";
    }

    @Override
    public ItemStack getItemStack(World.Environment environment, int modelId) {
        ItemStack forcefield = addLore(new ItemStack(Material.GLASS), "Costs: " + getCost(environment) + " Mana");
        forcefield = addLore(forcefield, "Cooldown: " + getCooldown()/1000d + " Seconds");
        ItemMeta iMeta = forcefield.getItemMeta();
        iMeta.setCustomModelData(modelId);
        iMeta.setDisplayName(ChatColor.WHITE + getName());
        forcefield.setItemMeta(iMeta);
        return forcefield;
    }

    @Override
    public int getCost(World.Environment environment) {
        return 100;
    }

    @Override
    public String getRiddle() {
        return "An invisible shield protects you, and pushes away all those who come close";
    }

    @Override
    public long getCooldown() {
        return 30*1000;
    }

    @Override
    public boolean enactSpell(Player caster, LivingEntity target) {
        getInstance().getForceFieldListener().addForceField(caster, 30);
        return true;
    }
}
