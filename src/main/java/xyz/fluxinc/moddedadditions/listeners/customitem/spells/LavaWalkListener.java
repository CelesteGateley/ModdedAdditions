package xyz.fluxinc.moddedadditions.listeners.customitem.spells;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import static xyz.fluxinc.moddedadditions.ModdedAdditions.instance;

public class LavaWalkListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerLavaWalk(PlayerMoveEvent event) {
        if (instance.getSpellBookController().canLavaWalk(event.getPlayer())) {
            Location feet = event.getTo().clone();
            feet.add(0, -1, 0);
            if (feet.getBlock().getType() == Material.LAVA) {
                feet.getBlock().setType(Material.COBBLESTONE);
            }
        }
    }
}
