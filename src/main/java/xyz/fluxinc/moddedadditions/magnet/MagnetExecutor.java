package xyz.fluxinc.moddedadditions.magnet;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import xyz.fluxinc.fluxcore.inventory.CheckExecutor;

import static xyz.fluxinc.moddedadditions.ModdedAdditions.instance;

public class MagnetExecutor extends CheckExecutor {

    @Override
    public boolean verifyItemStack(ItemStack itemStack) {
        return MagnetController.getMagnet().verifyItemStack(itemStack);
    }

    @Override
    public Material getMaterial() {
        return MagnetController.getMagnet().getMaterial();
    }

    @Override
    public void executeIfTrue(Player player) {
        instance.getMagnetController().registerVacuumInstance(player);
    }

    @Override
    public void executeIfFalse(Player player) {
        instance.getMagnetController().deregisterVacuumInstance(player);
    }

    @Override
    public void removeOnLeave(Player player) {
        executeIfFalse(player);
    }
}
