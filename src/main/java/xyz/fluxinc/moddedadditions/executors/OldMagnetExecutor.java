package xyz.fluxinc.moddedadditions.executors;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import xyz.fluxinc.fluxcore.inventory.CheckExecutor;
import xyz.fluxinc.moddedadditions.controllers.customitems.MagnetController;

import static xyz.fluxinc.moddedadditions.ModdedAdditions.instance;

public class OldMagnetExecutor extends CheckExecutor {

    @Override
    public boolean verifyItemStack(ItemStack itemStack) {
        return MagnetController.verifyOldMagnet(itemStack);
    }

    @Override
    public Material getMaterial() {
        return Material.COMPASS;
    }

    @Override
    public void executeIfTrue(Player player) {
        for (int i = 0; i < player.getInventory().getSize(); i++) {
            if (player.getInventory().getItem(i) != null && verifyItemStack(player.getInventory().getItem(i))) {
                player.getInventory().setItem(i, MagnetController.updateOldMagnet(player.getInventory().getItem(i)));
            }
        }
    }

    @Override
    public void executeIfFalse(Player player) {
    }

    @Override
    public void removeOnLeave(Player player) {
    }
}
