package xyz.fluxinc.moddedadditions.listeners.customitem;

import org.bukkit.Material;
import org.bukkit.block.data.Lightable;
import org.bukkit.block.data.Powerable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class SonicListener implements Listener {

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) { return; }
        if (event.getClickedBlock().getType() != Material.REDSTONE_LAMP) { return; }
        Lightable bData = (Lightable) event.getClickedBlock().getBlockData();
        bData.setLit(true);
        event.getClickedBlock().setBlockData(bData);
    }
}
