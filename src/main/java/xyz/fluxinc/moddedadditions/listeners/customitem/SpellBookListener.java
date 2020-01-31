package xyz.fluxinc.moddedadditions.listeners.customitem;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;
import xyz.fluxinc.moddedadditions.ModdedAdditions;

public class SpellBookListener implements Listener {

    private ModdedAdditions instance;

    public SpellBookListener(ModdedAdditions instance) {
        this.instance = instance;
    }

    @EventHandler
    public void onSlotChange(PlayerItemHeldEvent event) {
        if (!event.getPlayer().isSneaking()) { return; }
        if (!instance.getSpellBookController().verifySpellBook(event.getPlayer().getInventory().getItemInMainHand())) { return; }

        if (event.getPreviousSlot() > event.getNewSlot() || (event.getPreviousSlot() == 8 && event.getNewSlot() == 0)) {
            return;
        } else if (event.getPreviousSlot() < event.getNewSlot()) {
            System.out.println();
        }
    }
}
