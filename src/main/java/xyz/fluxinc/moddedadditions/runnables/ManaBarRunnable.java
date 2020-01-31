package xyz.fluxinc.moddedadditions.runnables;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;

import static xyz.fluxinc.moddedadditions.controllers.customitems.SpellBookController.verifySpellBook;

public class ManaBarRunnable implements Runnable {

    private NamespacedKey bossBar;
    private Player player;

    public ManaBarRunnable(Player player, NamespacedKey bossBar) {
        this.bossBar = bossBar;
        this.player = player;
    }

    @Override
    public void run() {
        if (Bukkit.getServer().getBossBar(bossBar) != null && !verifySpellBook(player.getInventory().getItemInMainHand())) {
            Bukkit.getServer().getBossBar(bossBar).setVisible(false);
        }
    }
}
