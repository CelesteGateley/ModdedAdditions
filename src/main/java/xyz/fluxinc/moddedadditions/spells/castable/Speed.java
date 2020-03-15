package xyz.fluxinc.moddedadditions.spells.castable;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;
import xyz.fluxinc.moddedadditions.ModdedAdditions;
import xyz.fluxinc.moddedadditions.spells.Spell;

import static xyz.fluxinc.fluxcore.utils.LoreUtils.addLore;

public class Speed extends Spell {

    public Speed(ModdedAdditions instance) {
        super(instance);
    }

    @Override
    public String getName() {
        return "Speed";
    }

    @Override
    public ItemStack getItemStack(int modelId) {
        ItemStack speed = addLore(new ItemStack(Material.GLOWSTONE_DUST), "Costs: " + getCost() + " Mana");
        ItemMeta iMeta = speed.getItemMeta();
        iMeta.setCustomModelData(modelId);
        iMeta.setDisplayName(ChatColor.WHITE + getName());
        speed.setItemMeta(iMeta);
        return speed;
    }

    @Override
    public int getCost() {
        return 50;
    }

    @Override
    public boolean enactSpell(Player caster, LivingEntity target) {
        new PotionEffect(PotionEffectType.SPEED, 20*20, 4).apply(caster);
        return true;
    }
}
