package xyz.fluxinc.moddedadditions.executors;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import xyz.fluxinc.fluxcore.inventory.CheckExecutor;
import xyz.fluxinc.moddedadditions.ModdedAdditions;

public class MagnetExecutor extends CheckExecutor {

    private ModdedAdditions instance;

    public MagnetExecutor(ModdedAdditions instance) {
        this.instance = instance;
    }

    @Override
    public boolean verifyItemStack(ItemStack itemStack) { return instance.getMagnetController().isMagnet(itemStack); }

    @Override
    public Material getMaterial() { return instance.getMagnetController().getMagnetMaterial(); }

    @Override
    public void executeIfTrue(Player player) { instance.getMagnetController().registerVacuumInstance(player); }

    @Override
    public void executeIfFalse(Player player) { instance.getMagnetController().deregisterVacuumInstance(player); }

    @Override
    public void removeOnLeave(Player player) { executeIfFalse(player); }
}
