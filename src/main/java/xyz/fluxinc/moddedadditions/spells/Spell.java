package xyz.fluxinc.moddedadditions.spells;

import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import xyz.fluxinc.moddedadditions.ModdedAdditions;

public abstract class Spell {

    private final ModdedAdditions instance;


    public Spell(ModdedAdditions instance) {
        this.instance = instance;
    }

    public ModdedAdditions getInstance() {
        return instance;
    }

    public abstract String getName();

    public abstract ItemStack getItemStack(World.Environment environment, int modelId);

    public abstract int getCost(World.Environment environment);

    public abstract String getRiddle();

    public void castSpell(Player caster, LivingEntity target) {
        if (getInstance().getManaController().getMana(caster) >= getCost(caster.getWorld().getEnvironment())) {
            boolean isCast = enactSpell(caster, target);
            if (isCast) {
                getInstance().getManaController().useMana(caster, getCost(caster.getWorld().getEnvironment()));
            }
        } else {
            caster.getWorld().playSound(caster.getLocation(), Sound.BLOCK_FIRE_EXTINGUISH, 1, 1);
        }
    }

    public abstract boolean enactSpell(Player caster, LivingEntity target);

}
