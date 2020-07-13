package xyz.fluxinc.moddedadditions.spells;

import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import xyz.fluxinc.moddedadditions.ModdedAdditions;

import java.util.HashMap;
import java.util.Map;

public abstract class Spell {

    private final Map<Player, Long> cooldowns;

    public Spell() {
        cooldowns = new HashMap<>();
    }

    public abstract String getLocalizedName();

    public abstract String getTechnicalName();

    public abstract ItemStack getItemStack(World.Environment environment, int modelId, int level);

    public abstract int getCost(World.Environment environment, int level);

    public abstract String getRiddle(int level);

    public abstract long getCooldown(int level);

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
