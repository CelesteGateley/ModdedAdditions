package xyz.fluxinc.moddedadditions.listeners;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryPickupItemEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import xyz.fluxinc.moddedadditions.ModdedAdditions;

import static xyz.fluxinc.moddedadditions.utils.MagnetUtils.isMagnet;

public class MagnetListener implements Listener {

    private ModdedAdditions instance;

    public MagnetListener(ModdedAdditions instance) {
        this.instance = instance;
    }

    @EventHandler
    public void onInventoryChange(InventoryMoveItemEvent event) {
        if (event.getDestination().getType() != InventoryType.PLAYER) { return; }
        PlayerInventory playerInventory = (PlayerInventory)event.getDestination();
        if (playerInventory.getHolder() != null) {
            registerEvent((Player)playerInventory.getHolder());
        }
    }

    @EventHandler
    public void onPickupEvent(InventoryPickupItemEvent event) {
        if (event.getInventory().getType() != InventoryType.PLAYER) { return; }
        PlayerInventory playerInventory = (PlayerInventory)event.getInventory();
        Player player = playerInventory.getHolder() != null ? (Player)playerInventory.getHolder() : null;
        if (player == null) { return; }
        registerEvent(player);
    }

    @EventHandler
    public void onDropEvent(PlayerDropItemEvent event) {
        if (!instance.getMagnetInstanceController().hasVacuumInstance(event.getPlayer())) { return; }
        registerEvent(event.getPlayer());
    }

    private void registerEvent(Player player) {
        boolean hasMagnet = false;
        if (player.getInventory().contains(Material.COMPASS)) {
            for (ItemStack iStack : player.getInventory().getContents()) { if (isMagnet(iStack)) { hasMagnet = true; break; }}
        }
        if (hasMagnet) { instance.getMagnetInstanceController().registerVacuumInstance(player); }
        else { instance.getMagnetInstanceController().deregisterVacuumInstance(player); }
    }
}