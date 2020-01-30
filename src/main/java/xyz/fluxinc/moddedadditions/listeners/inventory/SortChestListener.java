package xyz.fluxinc.moddedadditions.listeners.inventory;

import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import xyz.fluxinc.moddedadditions.ModdedAdditions;

import static xyz.fluxinc.fluxcore.utils.InventoryUtils.sortItemStacks;

public class SortChestListener implements Listener {

    private ModdedAdditions instance;

    public SortChestListener(ModdedAdditions instance) {
        this.instance = instance;
    }

    @EventHandler
    public void onOpenInventoryEvent(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK || event.getClickedBlock() == null) { return; }
        if (event.getClickedBlock().getType() != Material.CHEST) { return; }
        if (!(instance.getPlayerDataController().getPlayerData(event.getPlayer()).sortChests())) { return; }
        Chest chest = (Chest) event.getClickedBlock().getState();
        ItemStack[] sorted = sortItemStacks(chest.getInventory().getContents());
        chest.getInventory().clear();
        for (ItemStack itemStack : sorted) {
            if (itemStack == null) { continue; }
            chest.getInventory().addItem(itemStack);
        }
    }



}
