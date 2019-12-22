package xyz.fluxinc.moddedadditions.listeners;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;
import xyz.fluxinc.moddedadditions.ModdedAdditions;
import xyz.fluxinc.moddedadditions.utils.MagnetUtils;

public class MagnetListener implements Listener {

    private ModdedAdditions instance;
    private MagnetUtils magnetUtils;

    public MagnetListener(ModdedAdditions instance) {
        this.instance = instance;
        this.magnetUtils = instance.getMagnetUtils();
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onInventoryChange(InventoryClickEvent event) {
        instance.getServer().getScheduler().runTaskLater(instance, () -> registerEvent((Player) event.getWhoClicked()), 10L);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPickupEvent(EntityPickupItemEvent event) {
        if (event.getEntity().getType() != EntityType.PLAYER) { return; }
        Player player = (Player) event.getEntity();
        instance.getServer().getScheduler().runTaskLater(instance, () -> registerEvent(player), 10L);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onDropEvent(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        instance.getServer().getScheduler().runTaskLater(instance, () -> registerEvent(player), 10L);
    }

    private void registerEvent(Player player) {
        boolean hasMagnet = false;
        if (player.getInventory().contains(Material.COMPASS)) {
            for (ItemStack iStack : player.getInventory().getContents()) {
                if (magnetUtils.isMagnet(iStack)) { hasMagnet = true; break; }
            }
        }
        if (hasMagnet) { instance.getMagnetInstanceController().registerVacuumInstance(player); }
        else { instance.getMagnetInstanceController().deregisterVacuumInstance(player); }
    }
}