package xyz.fluxinc.moddedadditions.spells.castable.combat;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import xyz.fluxinc.moddedadditions.spells.Spell;

import static xyz.fluxinc.fluxcore.utils.LoreUtils.addLore;

public class Fireball extends Spell {

    public Fireball() {
        super();
    }

    @Override
    public String getName() {
        return "Fireball";
    }

    @Override
    public ItemStack getItemStack(World.Environment environment, int modelId) {
        ItemStack fireball = addLore(new ItemStack(Material.FIRE_CHARGE), "Costs: " + getCost(environment) + " Mana");
        fireball = addLore(fireball, "Cooldown: " + getCooldown() / 1000 + " Seconds");
        ItemMeta iMeta = fireball.getItemMeta();
        iMeta.setCustomModelData(modelId);
        iMeta.setDisplayName(ChatColor.WHITE + getName());
        fireball.setItemMeta(iMeta);
        return fireball;
    }

    @Override
    public int getCost(World.Environment environment) {
        return 50;
    }

    @Override
    public String getRiddle() {
        return "The tears of your enemies will burn away as you charge forth";
    }

    @Override
    public long getCooldown() {
        return 1000;
    }

    @Override
    public boolean enactSpell(Player caster, LivingEntity target) {
        org.bukkit.entity.Fireball fireball = caster.launchProjectile(org.bukkit.entity.Fireball.class);
        fireball.setYield(2);
        caster.getWorld().playSound(caster.getLocation(), Sound.ENTITY_GHAST_SHOOT, 1, 1);
        return true;
    }
}
