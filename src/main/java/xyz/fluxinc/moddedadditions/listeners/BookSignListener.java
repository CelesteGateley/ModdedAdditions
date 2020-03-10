package xyz.fluxinc.moddedadditions.listeners;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerEditBookEvent;
import org.bukkit.inventory.meta.BookMeta;

import java.util.ArrayList;
import java.util.List;

public class BookSignListener implements Listener {

    @EventHandler
    public void onBookSignEvent(PlayerEditBookEvent event) {
        if (!event.isSigning()) {
            return;
        }
        if (!event.getPlayer().hasPermission("moddedadditions.books.color")) {
            return;
        }

        BookMeta bookMeta = event.getNewBookMeta();
        List<String> newPages = new ArrayList<>();
        for (String page : bookMeta.getPages()) {
            newPages.add(ChatColor.translateAlternateColorCodes('&', page));
        }
        bookMeta.setPages(newPages);
        event.setNewBookMeta(bookMeta);
    }
}
