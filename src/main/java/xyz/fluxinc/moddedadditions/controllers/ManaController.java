package xyz.fluxinc.moddedadditions.controllers;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.KeyedBossBar;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import xyz.fluxinc.moddedadditions.ModdedAdditions;
import xyz.fluxinc.moddedadditions.runnables.ManaBarRunnable;
import xyz.fluxinc.moddedadditions.storage.PlayerData;

import java.util.HashMap;
import java.util.Map;

public class ManaController implements Listener, Runnable {

    private ModdedAdditions instance;
    private Map<Player, NamespacedKey> playerBars;

    public ManaController(ModdedAdditions instance) {
        this.instance = instance;
        playerBars = new HashMap<>();
        instance.getServer().getScheduler().scheduleSyncRepeatingTask(instance, this, 100, 100);
    }

    public void useMana(Player player, int amount) {
        instance.getPlayerDataController().setPlayerData(player, instance.getPlayerDataController().getPlayerData(player).takeCurrentMana(amount));
    }

    public void regenerateMana(Player player, int amount) {
        instance.getPlayerDataController().setPlayerData(player, instance.getPlayerDataController().getPlayerData(player).addCurrentMana(amount));
    }

    public void showManaBar(Player player, int duration) {
        if (Bukkit.getServer().getBossBar(playerBars.get(player)) == null) { generateManaBar(player); }
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(instance, new ManaBarRunnable(playerBars.get(player)), duration*20);
        Bukkit.getServer().getBossBar(playerBars.get(player)).setVisible(true);
    }

    public void showManaBar(Player player) {
        if (Bukkit.getServer().getBossBar(playerBars.get(player)) == null) { generateManaBar(player); }
        Bukkit.getServer().getBossBar(playerBars.get(player)).setVisible(true);
    }

    public void hideManaBar(Player player) {
        if (Bukkit.getServer().getBossBar(playerBars.get(player)) == null) { generateManaBar(player); }
        Bukkit.getServer().getBossBar(playerBars.get(player)).setVisible(false);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerJoin(PlayerJoinEvent event) {
        generateManaBar(event.getPlayer());
        showManaBar(event.getPlayer(), 20);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerLeave(PlayerQuitEvent event) {
        if (Bukkit.getServer().getBossBar(playerBars.get(event.getPlayer())) != null) {
            Bukkit.getServer().removeBossBar(playerBars.get(event.getPlayer()));
        }
    }

    private void generateManaBar(Player player) {
        NamespacedKey key = new NamespacedKey(instance, "mana_bar_" + player.getUniqueId());
        PlayerData data = instance.getPlayerDataController().getPlayerData(player);
        playerBars.put(player, key);
        KeyedBossBar bossBar = Bukkit.createBossBar(key, "Mana: " + data.getCurrentMana() + "/" + data.getMaximumMana(), BarColor.BLUE, BarStyle.SEGMENTED_10);
        bossBar.setProgress((double) data.getCurrentMana() / (double) data.getMaximumMana());
        bossBar.setVisible(false);
    }

    @Override
    public void run() {
        for (Player player : instance.getServer().getOnlinePlayers()) {
            PlayerData data = instance.getPlayerDataController().getPlayerData(player);
            if (data.getCurrentMana() >= data.getMaximumMana()) { data.setCurrentMana(data.getMaximumMana()); }
            else if (data.getMaximumMana() - data.getCurrentMana() < 5) {
                data.setCurrentMana(data.getMaximumMana());
                showManaBar(player, 10);
            } else {
                data.addCurrentMana(5);
                if (data.getCurrentMana() % 50 == 0) { showManaBar(player, 5); }
            }
            instance.getPlayerDataController().setPlayerData(player, data);
        }
    }
}
