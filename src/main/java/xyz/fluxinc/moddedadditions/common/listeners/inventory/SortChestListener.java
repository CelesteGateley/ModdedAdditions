package xyz.fluxinc.moddedadditions.common.listeners.inventory;

import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import static xyz.fluxinc.fluxcore.utils.InventoryUtils.sortItemStacks;
import static xyz.fluxinc.moddedadditions.ModdedAdditions.instance;

public class SortChestListener implements Listener {

    @EventHandler
    public void onOpenInventoryEvent(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK || event.getClickedBlock() == null) {
            return;
        }
        if (event.getClickedBlock().getType() != Material.CHEST && event.getClickedBlock().getType() != Material.BARREL) {
            return;
        }
        if (!(instance.getPlayerDataController().getPlayerData(event.getPlayer()).sortChests())) {
            return;
        }
        Chest chest = (Chest) event.getClickedBlock().getState();
        if (chest.getLootTable() != null) return;
        ItemStack[] sorted = sortItemStacks(chest.getInventory().getContents());
        chest.getInventory().clear();
        for (ItemStack itemStack : sorted) {
            if (itemStack == null) {
                continue;
            }
            chest.getInventory().addItem(itemStack);
        }
    }


}
