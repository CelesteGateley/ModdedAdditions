package xyz.fluxinc.moddedadditions.listeners.customitem;

import org.bukkit.block.Dispenser;
import org.bukkit.block.Dropper;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Lightable;
import org.bukkit.block.data.Openable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import static xyz.fluxinc.moddedadditions.ModdedAdditions.instance;

public class SonicScrewdriverListener implements Listener {

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }
        if (event.getItem() == null || !instance.getSonicScrewdriverController().isSonic(event.getItem())) {
            return;
        }
        if (event.getClickedBlock() == null) {
            return;
        }
        if (!instance.getCoreInstance().getBlockAccessController().checkBlockAccess(event.getPlayer(), event.getClickedBlock().getLocation())) {
            return;
        }
        event.setCancelled(true);
        BlockData blockData = event.getClickedBlock().getBlockData();

        if (event.getClickedBlock() instanceof Dispenser && !event.getPlayer().isSneaking()) {
            ((Dispenser)event.getClickedBlock()).dispense();
            return;
        }

        if (event.getClickedBlock() instanceof Dropper && !event.getPlayer().isSneaking()) {
            ((Dropper) event.getClickedBlock()).drop();
            return;
        }

        if (blockData instanceof Lightable) {
            ((Lightable) blockData).setLit(!((Lightable) blockData).isLit());
        } else if (blockData instanceof Openable) {
            ((Openable) blockData).setOpen(!((Openable) blockData).isOpen());
        }

        event.getClickedBlock().setBlockData(blockData);

    }


}
