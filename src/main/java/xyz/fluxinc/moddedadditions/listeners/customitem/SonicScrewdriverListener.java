package xyz.fluxinc.moddedadditions.listeners.customitem;

import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Lightable;
import org.bukkit.block.data.Openable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import xyz.fluxinc.moddedadditions.ModdedAdditions;

public class SonicScrewdriverListener implements Listener {

    private final ModdedAdditions instance;

    public SonicScrewdriverListener(ModdedAdditions instance) {
        this.instance = instance;
    }

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

        if (blockData instanceof Lightable) {
            ((Lightable) blockData).setLit(!((Lightable) blockData).isLit());
        } else if (blockData instanceof Openable) {
            ((Openable) blockData).setOpen(!((Openable) blockData).isOpen());
        }

        event.getClickedBlock().setBlockData(blockData);

    }


}
