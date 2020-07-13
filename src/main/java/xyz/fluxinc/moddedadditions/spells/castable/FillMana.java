package xyz.fluxinc.moddedadditions.spells.castable;

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

public class FillMana extends Spell {

    public FillMana() {
        super();
    }

    @Override
    public String getName() {
        return "Fill Mana";
    }

    @Override
    public ItemStack getItemStack(World.Environment environment, int modelId, int level) {
        ItemStack fillMana = addLore(new ItemStack(Material.EMERALD), "Costs: " + getCost(environment, level) + " Mana");
        ItemMeta iMeta = fillMana.getItemMeta();
        iMeta.setCustomModelData(modelId);
        iMeta.setDisplayName(ChatColor.WHITE + getName());
        fillMana.setItemMeta(iMeta);
        return fillMana;
    }

    @Override
    public int getCost(World.Environment environment, int level) {
        return 0;
    }

    @Override
    public String getRiddle(int level) {
        return "";
    }

    @Override
    public long getCooldown(int level) {
        return 0;
    }

    @Override
    public boolean enactSpell(Player caster, LivingEntity target, int level) {
        ModdedAdditions.instance.getManaController().regenerateMana(caster, 300);
        return true;
    }
}
