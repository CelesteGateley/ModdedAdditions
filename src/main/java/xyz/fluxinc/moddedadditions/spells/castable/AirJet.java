package xyz.fluxinc.moddedadditions.spells.castable;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;
import xyz.fluxinc.moddedadditions.ModdedAdditions;
import xyz.fluxinc.moddedadditions.spells.Spell;

import static xyz.fluxinc.fluxcore.utils.LoreUtils.addLore;

public class AirJet extends Spell {

    public AirJet(ModdedAdditions instance) {
        super(instance);
    }

    @Override
    public String getName() {
        return "Air Jet";
    }

    @Override
    public ItemStack getItemStack(World.Environment environment, int modelId) {
        ItemStack yeet = addLore(new ItemStack(Material.FEATHER), "Costs: " + getCost(environment) + " Mana");
        ItemMeta iMeta = yeet.getItemMeta();
        iMeta.setCustomModelData(modelId);
        iMeta.setDisplayName(ChatColor.WHITE + getName());
        yeet.setItemMeta(iMeta);
        return yeet;
    }

    @Override
    public int getCost(World.Environment environment) {
        return 15;
    }

    @Override
    public String getRiddle() {
        return "Tied up tight, the chicken may not fly free";
    }

    @Override
    public boolean enactSpell(Player caster, LivingEntity target) {
        Vector newVector = caster.getEyeLocation().getDirection().multiply(new Vector(2, -2, 2));
        if (newVector.getY() < 0) {
            newVector.multiply(new Vector(1, -1, 1));
        }
        caster.setVelocity(newVector);
        return true;
    }
}
