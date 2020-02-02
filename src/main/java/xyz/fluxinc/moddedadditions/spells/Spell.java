package xyz.fluxinc.moddedadditions.spells;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import xyz.fluxinc.moddedadditions.ModdedAdditions;

public abstract class Spell {

    private ModdedAdditions instance;


    public Spell(ModdedAdditions instance) {
        this.instance = instance;
    }

    public ModdedAdditions getInstance() { return instance; }

    public abstract String getName();

    public abstract ItemStack getItemStack(int modelId);

    public abstract int getCost();

    public abstract void castSpell(Player caster, LivingEntity target);


}
