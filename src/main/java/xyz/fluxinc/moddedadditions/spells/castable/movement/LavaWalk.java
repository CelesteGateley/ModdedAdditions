package xyz.fluxinc.moddedadditions.spells.castable.movement;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import xyz.fluxinc.moddedadditions.ModdedAdditions;
import xyz.fluxinc.moddedadditions.spells.Spell;

import static xyz.fluxinc.fluxcore.utils.LoreUtils.addLore;

public class LavaWalk extends Spell {

    public LavaWalk() {
        super();
    }

    @Override
    public String getName() {
        return "Lava Walk";
    }

    @Override
    public ItemStack getItemStack(World.Environment environment, int modelId, int level) {
        ItemStack lavaWalk = addLore(new ItemStack(Material.LAVA_BUCKET), "Costs: " + getCost(environment, level) + " Mana");
        lavaWalk = addLore(lavaWalk, "Cooldown: " + getCooldown(level) / 1000d + " Seconds");
        ItemMeta iMeta = lavaWalk.getItemMeta();
        iMeta.setCustomModelData(modelId);
        iMeta.setDisplayName(ChatColor.WHITE + getName());
        lavaWalk.setItemMeta(iMeta);
        return lavaWalk;
    }

    @Override
    public int getCost(World.Environment environment, int level) {
        return 50;
    }

    @Override
    public String getRiddle(int level) {
        return "With an icy step and a blazing heart, even you can cross a blazing desert";
    }

    @Override
    public long getCooldown(int level) {
        return 25000;
    }

    @Override
    public boolean enactSpell(Player caster, LivingEntity target, int level) {
        caster.sendTitle("You can now walk on lava!", "", 10, 70, 20);
        ModdedAdditions.instance.getSpellBookController().addLavaWalk(caster);
        return true;
    }
}
