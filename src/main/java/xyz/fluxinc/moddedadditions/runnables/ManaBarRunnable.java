package xyz.fluxinc.moddedadditions.runnables;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;

public class ManaBarRunnable implements Runnable {

    private NamespacedKey bossBar;

    public ManaBarRunnable(NamespacedKey bossBar) {
        this.bossBar = bossBar;
    }

    @Override
    public void run() {
        Bukkit.getServer().getBossBar(bossBar).setVisible(false);
    }
}
