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

public class LavaWalk extends Spell {

    public LavaWalk(ModdedAdditions instance) {
        super(instance);
    }

    @Override
    public String getName() {
        return "Lava Walk";
    }

    @Override
    public ItemStack getItemStack(World.Environment environment, int modelId) {
        ItemStack slowball = addLore(new ItemStack(Material.LAVA_BUCKET), "Costs: " + getCost(environment) + " Mana");
        ItemMeta iMeta = slowball.getItemMeta();
        iMeta.setCustomModelData(modelId);
        iMeta.setDisplayName(ChatColor.WHITE + getName());
        slowball.setItemMeta(iMeta);
        return slowball;
    }

    @Override
    public int getCost(World.Environment environment) {
        return 50;
    }

    @Override
    public String getRiddle() {
        return "With an icy step and a blazing heart, even you can cross a blazing desert";
    }

    @Override
    public long getCooldown() {
        return 25000;
    }

    @Override
    public boolean enactSpell(Player caster, LivingEntity target) {
        caster.sendTitle("You can now walk on lava!", "", 10, 70, 20);
        getInstance().getSpellBookController().addLavaWalk(caster);
        return true;
    }
}
