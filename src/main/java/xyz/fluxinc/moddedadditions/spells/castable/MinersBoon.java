package xyz.fluxinc.moddedadditions.spells.castable;

import org.bukkit.ChatColor;
import org.bukkit.Material;
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
    public ItemStack getItemStack(int modelId) {
        ItemStack speed = addLore(new ItemStack(Material.DIAMOND_PICKAXE), "Costs: " + getCost() + " Mana");
        ItemMeta iMeta = speed.getItemMeta();
        iMeta.setCustomModelData(modelId);
        iMeta.setDisplayName(ChatColor.WHITE + getName());
        speed.setItemMeta(iMeta);
        return speed;
    }

    @Override
    public int getCost() {
        return 75;
    }

    @Override
    public String getRiddle() {
        return "An ancient metal, and a buried heart, grants one the strength of the earth";
    }

    @Override
    public boolean enactSpell(Player caster, LivingEntity target) {
        new PotionEffect(PotionEffectType.NIGHT_VISION, 30 * 20, 0).apply(caster);
        new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 30 * 20, 0).apply(caster);
        new PotionEffect(PotionEffectType.FAST_DIGGING, 30 * 20, 1).apply(caster);
        return true;
    }
}
