package xyz.fluxinc.moddedadditions.common.listeners.chat;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import static org.bukkit.Bukkit.getServer;

public class PingListener implements Listener {

    @EventHandler
    public void onChatMessage(AsyncPlayerChatEvent event) {
        if (!event.getMessage().contains("@")) {
            return;
        }

        Player p = null;

        for (Player player : getServer().getOnlinePlayers()) {
            if (event.getMessage().contains("@" + player.getName()) || event.getMessage().contains(player.getName() + "@")) {
                p = player;
                break;
            }
        }

        if (p != null) {
            p.playSound(p.getLocation(), Sound.BLOCK_BELL_USE, 10, 1);
        }
    }

}
