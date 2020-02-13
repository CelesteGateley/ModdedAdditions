package xyz.fluxinc.moddedadditions.spells.castable;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;
import xyz.fluxinc.moddedadditions.ModdedAdditions;
import xyz.fluxinc.moddedadditions.spells.Spell;
import xyz.fluxinc.moddedadditions.storage.PlayerData;

import static xyz.fluxinc.fluxcore.utils.LoreUtils.addLore;

public class FillMana extends Spell {

    public FillMana(ModdedAdditions instance) {
        super(instance);
    }

    @Override
    public String getName() {
        return "Fill Mana";
    }

    @Override
    public ItemStack getItemStack(int modelId) {
        ItemStack fillMana = addLore(new ItemStack(Material.EMERALD), "Costs: " + getCost() + " Mana");
        ItemMeta iMeta = fillMana.getItemMeta(); iMeta.setCustomModelData(modelId); iMeta.setDisplayName(ChatColor.WHITE + getName());
        fillMana.setItemMeta(iMeta);
        return fillMana;
    }

    @Override
    public int getCost() {
        return 0;
    }

    @Override
    public boolean enactSpell(Player caster, LivingEntity target) {
        getInstance().getManaController().regenerateMana(caster, 300);
        return true;
    }
}
