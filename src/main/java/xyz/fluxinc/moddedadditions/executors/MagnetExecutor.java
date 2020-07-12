package xyz.fluxinc.moddedadditions.executors;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import xyz.fluxinc.fluxcore.inventory.CheckExecutor;

import static xyz.fluxinc.moddedadditions.ModdedAdditions.instance;

public class MagnetExecutor extends CheckExecutor {

    @Override
    public boolean verifyItemStack(ItemStack itemStack) {
        return instance.getMagnetController().isMagnet(itemStack)
                || instance.getMagnetController().verifyOldMagnet(itemStack);
    }

    @Override
    public Material getMaterial() {
        return instance.getMagnetController().getMagnetMaterial();
    }

    @Override
    public void executeIfTrue(Player player) {
        if (instance.getMagnetController().verifyOldMagnet(player.getInventory().getItemInMainHand())) {
            player.getInventory().setItemInMainHand(instance.getMagnetController().updateOldMagnet(player.getInventory().getItemInMainHand()));
        }
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
