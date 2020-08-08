package xyz.fluxinc.moddedadditions.spells;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import xyz.fluxinc.moddedadditions.ModdedAdditions;

import java.util.HashMap;
import java.util.Map;

import static xyz.fluxinc.fluxcore.utils.LoreUtils.addLore;

public abstract class Spell extends Magic {

    private final Map<Player, Long> cooldowns;

    public Spell() {
        cooldowns = new HashMap<>();
    }

    public abstract ItemStack getDefaultItem(World.Environment environment, int level);

    public abstract String getLocalizedName();

    public abstract String getTechnicalName();

    public abstract int getModelId();

    public abstract String getDescription(int level);

    public ItemStack getItemStack(World.Environment environment, int level) {
        ItemStack itemStack = addLore(getDefaultItem(environment, level), ChatColor.BLUE + getDescription(0));
        itemStack = addLore(itemStack, ChatColor.BLUE + "Costs: " + getCost(environment, level) + " Mana");
        itemStack = addLore(itemStack, ChatColor.BLUE + "Cooldown: " + getCooldown(level) / 1000d + " Seconds");
        if (getRecipe(level) != null) {
            itemStack = addLore(itemStack, ChatColor.translateAlternateColorCodes('&', "\n\n&9Next Level: " + getDescription(level)));
            itemStack = addLore(itemStack, "\n" + ChatColor.translateAlternateColorCodes('&', getRiddle(level)));
        }
        ItemMeta iMeta = itemStack.getItemMeta();
        iMeta.setCustomModelData(getModelId());
        iMeta.setDisplayName(ChatColor.WHITE + getLocalizedName());
        itemStack.setItemMeta(iMeta);
        return itemStack;
    }

    public abstract int getCost(World.Environment environment, int level);

    public abstract String getRiddle(int level);

    public abstract long getCooldown(int level);

    public abstract SpellRecipe getRecipe(int level);

    public void castSpell(Player caster, LivingEntity target, int level) {
        if (ModdedAdditions.instance.getManaController().getMana(caster) >= getCost(caster.getWorld().getEnvironment(), level)
                && cooldowns.getOrDefault(caster, 0L) + getCooldown(level) < System.currentTimeMillis()) {
            boolean isCast = enactSpell(caster, target, level);
            if (isCast) {
                ModdedAdditions.instance.getManaController().useMana(caster, getCost(caster.getWorld().getEnvironment(), level));
                cooldowns.put(caster, System.currentTimeMillis());
            }
        } else {
            caster.getWorld().playSound(caster.getLocation(), Sound.BLOCK_FIRE_EXTINGUISH, 1, 1);
        }
    }

    public abstract boolean enactSpell(Player caster, LivingEntity target, int level);

}
