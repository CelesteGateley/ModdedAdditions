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
    public boolean verifyItemStack(ItemStack itemStack) { return instance.getMagnetUtils().isMagnet(itemStack); }

    @Override
    public Material getMaterial() { return Material.COMPASS; }

    @Override
    public void executeIfTrue(Player player) { instance.getMagnetInstanceController().registerVacuumInstance(player); }

    @Override
    public void executeIfFalse(Player player) { instance.getMagnetInstanceController().deregisterVacuumInstance(player); }

    @Override
    public void removeOnLeave(Player player) { executeIfFalse(player); }
}