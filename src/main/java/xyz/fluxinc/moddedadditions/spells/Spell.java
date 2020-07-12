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

    public abstract String getName();

    public abstract ItemStack getItemStack(World.Environment environment, int modelId);

    public abstract int getCost(World.Environment environment);

    public abstract String getRiddle();

    public abstract long getCooldown();

    public void castSpell(Player caster, LivingEntity target) {
        if (ModdedAdditions.instance.getManaController().getMana(caster) >= getCost(caster.getWorld().getEnvironment())
            && cooldowns.getOrDefault(caster, 0L) + getCooldown() < System.currentTimeMillis()) {
            boolean isCast = enactSpell(caster, target);
            if (isCast) {
                ModdedAdditions.instance.getManaController().useMana(caster, getCost(caster.getWorld().getEnvironment()));
                cooldowns.put(caster, System.currentTimeMillis());
            }
        } else {
            caster.getWorld().playSound(caster.getLocation(), Sound.BLOCK_FIRE_EXTINGUISH, 1, 1);
        }
    }

    public abstract boolean enactSpell(Player caster, LivingEntity target);

}
