package xyz.fluxinc.moddedadditions.spells.castable;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import xyz.fluxinc.moddedadditions.ModdedAdditions;
import xyz.fluxinc.moddedadditions.spells.Spell;

import static xyz.fluxinc.fluxcore.utils.LoreUtils.addLore;

public class SlowBall extends Spell {

    public static final String SLOWBALL_NAME = "abXYZoasDGAdgiQXVSAadsgasnNEFA";

    public SlowBall(ModdedAdditions instance) {
        super(instance);
    }

    @Override
    public String getName() {
        return "Slow Ball";
    }

    @Override
    public ItemStack getItemStack(World.Environment environment, int modelId) {
        ItemStack slowball = addLore(new ItemStack(Material.SNOWBALL), "Costs: " + getCost(environment) + " Mana");
        slowball = addLore(slowball, "Cooldown: " + getCooldown()/1000d + " Seconds");
        ItemMeta iMeta = slowball.getItemMeta();
        iMeta.setCustomModelData(modelId);
        iMeta.setDisplayName(ChatColor.WHITE + getName());
        slowball.setItemMeta(iMeta);
        return slowball;
    }

    @Override
    public int getCost(World.Environment environment) {
        return 15;
    }

    @Override
    public String getRiddle() {
        return "One strike from winter's wrath slows your very soul to a crawl";
    }

    @Override
    public long getCooldown() {
        return 2000;
    }

    @Override
    public boolean enactSpell(Player caster, LivingEntity target) {
        Entity entity = caster.launchProjectile(Snowball.class);
        entity.setCustomName(SLOWBALL_NAME);
        return true;
    }
}
