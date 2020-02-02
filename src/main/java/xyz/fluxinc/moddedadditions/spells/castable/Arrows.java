package xyz.fluxinc.moddedadditions.spells.castable;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import xyz.fluxinc.moddedadditions.ModdedAdditions;
import xyz.fluxinc.moddedadditions.spells.Spell;

import static xyz.fluxinc.fluxcore.utils.LoreUtils.addLore;

public class Arrows extends Spell {

    public Arrows(ModdedAdditions instance) {
        super(instance);
    }

    @Override
    public String getName() {
        return "Shoot Arrows";
    }

    @Override
    public ItemStack getItemStack(int modelId) {
        ItemStack arrows = addLore(new ItemStack(Material.BOW), "Costs: " + getCost() + " Mana");
        arrows = addLore(arrows, "Costs: 1 Arrow");
        ItemMeta iMeta = arrows.getItemMeta(); iMeta.setCustomModelData(modelId); iMeta.setDisplayName(ChatColor.WHITE + getName());
        arrows.setItemMeta(iMeta);
        return arrows;
    }

    @Override
    public int getCost() {
        return 5;
    }

    @Override
    public void castSpell(Player caster, LivingEntity target) {
        if (caster.getInventory().contains(Material.ARROW) && getInstance().getManaController().getMana(caster) >= getCost()) {
            caster.launchProjectile(Arrow.class);
            caster.getWorld().playSound(caster.getLocation(), Sound.ENTITY_ARROW_SHOOT, 1, 1);
            getInstance().getManaController().useMana(caster, getCost());
            takeArrow(caster);
        } else {caster.getWorld().playSound(caster.getLocation(), Sound.BLOCK_FIRE_EXTINGUISH, 1, 1); }
    }

    private void takeArrow(Player player) {
        if (player.getInventory().contains(Material.ARROW)) {
            int slot = player.getInventory().first(Material.ARROW);
            ItemStack iStack = player.getInventory().getItem(slot);
            if (iStack.getAmount() == 1) { player.getInventory().setItem(slot, null); }
            else { iStack.setAmount(iStack.getAmount() - 1); player.getInventory().setItem(slot, iStack); }
        }
    }
}
