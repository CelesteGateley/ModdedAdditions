package xyz.fluxinc.moddedadditions.spells.castable.combat;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import xyz.fluxinc.moddedadditions.spells.Spell;

import static xyz.fluxinc.fluxcore.utils.LoreUtils.addLore;

public class SlowBall extends Spell {

    public static final String SLOWBALL_NAME = "abXYZoasDGAdgiQXVSAadsgasnNEFA";

    @Override
    public String getLocalizedName() {
        return "Slow Ball";
    }

    @Override
    public String getTechnicalName() {
        return "slowball";
    }

    @Override
    public ItemStack getItemStack(World.Environment environment, int modelId, int level) {
        ItemStack slowball = addLore(new ItemStack(Material.SNOWBALL), "Costs: " + getCost(environment, level) + " Mana");
        slowball = addLore(slowball, "Cooldown: " + getCooldown(level) / 1000d + " Seconds");
        ItemMeta iMeta = slowball.getItemMeta();
        iMeta.setCustomModelData(modelId);
        iMeta.setDisplayName(ChatColor.WHITE + getLocalizedName());
        slowball.setItemMeta(iMeta);
        return slowball;
    }

    @Override
    public int getCost(World.Environment environment, int level) {
        return 15;
    }

    @Override
    public String getRiddle(int level) {
        return "One strike from winter's wrath slows your very soul to a crawl";
    }

    @Override
    public long getCooldown(int level) {
        return 2000;
    }

    @Override
    public boolean enactSpell(Player caster, LivingEntity target, int level) {
        Entity entity = caster.launchProjectile(Snowball.class);
        entity.setCustomName(SLOWBALL_NAME);
        return true;
    }
}
