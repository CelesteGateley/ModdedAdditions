package xyz.fluxinc.moddedadditions.listeners;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Objects;

import static org.bukkit.event.inventory.InventoryType.ANVIL;

public class AnvilListener implements Listener {

    @EventHandler
    public void onRemoveAnvilItem(InventoryClickEvent event) {
        if (event.getInventory().getType() == ANVIL
                && event.getSlotType() == InventoryType.SlotType.RESULT
                && event.getWhoClicked().hasPermission("moddedadditions.coloranvil.use")) {
            ItemMeta meta = Objects.requireNonNull(event.getCurrentItem()).getItemMeta();
            if (meta == null) { return; }
            String oldName = meta.getDisplayName();
            if (oldName.contains("&")) {
                String newName = ChatColor.translateAlternateColorCodes('&', oldName);
                meta.setDisplayName(newName);
                event.getCurrentItem().setItemMeta(meta);
            }
        }
    }

    @EventHandler
    public void onAddAnvilItem(PrepareAnvilEvent event) {
        if (event.getInventory().getRenameText() == null || event.getResult() == null) { return; }
        String renText = event.getInventory().getRenameText().replace('§', '&');
        ItemMeta meta = event.getResult().getItemMeta();
        if (meta == null) { return; }
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', renText));
        ItemStack iS = event.getResult();
        iS.setItemMeta(meta);
        event.setResult(iS);
    }
}
