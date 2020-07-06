package xyz.fluxinc.moddedadditions.spells.castable;

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
import xyz.fluxinc.moddedadditions.spells.Spell;

import static xyz.fluxinc.fluxcore.utils.LoreUtils.addLore;

public class MinersBoon extends Spell {

    public MinersBoon(ModdedAdditions instance) {
        super(instance);
    }

    @Override
    public String getName() {
        return "Miners Boon";
    }

    @Override
    public ItemStack getItemStack(World.Environment environment, int modelId) {
        ItemStack minersBoon = addLore(new ItemStack(Material.DIAMOND_PICKAXE), "Costs: " + getCost(environment) + " Mana");
        minersBoon = addLore(minersBoon, "Cooldown: " + getCooldown()/1000 + " Seconds");
        ItemMeta iMeta = minersBoon.getItemMeta();
        iMeta.setCustomModelData(modelId);
        iMeta.setDisplayName(ChatColor.WHITE + getName());
        minersBoon.setItemMeta(iMeta);
        return minersBoon;
    }

    @Override
    public int getCost(World.Environment environment) {
        return 100;
    }

    @Override
    public String getRiddle() {
        return "An ancient metal, and a buried heart, grants one the strength of the earth";
    }

    @Override
    public long getCooldown() {
        return 30000;
    }

    @Override
    public boolean enactSpell(Player caster, LivingEntity target) {
        new PotionEffect(PotionEffectType.NIGHT_VISION, 20 * 20, 0).apply(caster);
        new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 20 * 20, 0).apply(caster);
        new PotionEffect(PotionEffectType.FAST_DIGGING, 20 * 20, 1).apply(caster);
        return true;
    }
}
