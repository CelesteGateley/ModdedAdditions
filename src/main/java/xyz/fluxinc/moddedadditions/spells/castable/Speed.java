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

public class Speed extends Spell {

    public Speed(ModdedAdditions instance) {
        super(instance);
    }

    @Override
    public String getName() {
        return "Speed";
    }

    @Override
    public ItemStack getItemStack(World.Environment environment, int modelId) {
        ItemStack speed = addLore(new ItemStack(Material.GLOWSTONE_DUST), "Costs: " + getCost(environment) + " Mana");
        ItemMeta iMeta = speed.getItemMeta();
        iMeta.setCustomModelData(modelId);
        iMeta.setDisplayName(ChatColor.WHITE + getName());
        speed.setItemMeta(iMeta);
        return speed;
    }

    @Override
    public int getCost(World.Environment environment) {
        return 50;
    }

    @Override
    public String getRiddle() {
        return "Sweetening your life, this will make you faster than light";
    }

    @Override
    public long getCooldown() {
        return 250;
    }

    @Override
    public boolean enactSpell(Player caster, LivingEntity target) {
        new PotionEffect(PotionEffectType.SPEED, 20 * 20, 4).apply(caster);
        return true;
    }
}
