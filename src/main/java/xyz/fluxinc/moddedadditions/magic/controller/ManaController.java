package xyz.fluxinc.moddedadditions.magic.controller;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.boss.KeyedBossBar;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import xyz.fluxinc.moddedadditions.magic.ManaBarRunnable;
import xyz.fluxinc.moddedadditions.common.storage.PlayerData;

import java.util.HashMap;
import java.util.Map;

import static xyz.fluxinc.moddedadditions.ModdedAdditions.instance;

public class ManaController implements Listener, Runnable {

    private Map<Player, NamespacedKey> playerBars;

    public ManaController() {
        playerBars = new HashMap<>();
        instance.getServer().getScheduler().scheduleSyncRepeatingTask(instance, this, 40, 40);
    }

    public int getMana(Player player) {
        return instance.getPlayerDataController().getPlayerData(player).getCurrentMana();
    }

    public int getMaximumMana(Player player) {
        return instance.getPlayerDataController().getPlayerData(player).getMaximumMana();
    }

    public void useMana(Player player, int amount) {
        instance.getPlayerDataController().setPlayerData(player, instance.getPlayerDataController().getPlayerData(player).takeCurrentMana(amount));
        updateManaBar(player);
    }

    public void regenerateMana(Player player, int amount) {
        instance.getPlayerDataController().setPlayerData(player, instance.getPlayerDataController().getPlayerData(player).addCurrentMana(amount));
        updateManaBar(player);
    }

    public void regeneratePercentMana(Player player, int amount) {
        PlayerData data = instance.getPlayerDataController().getPlayerData(player);
        int regenAmount = (data.getMaximumMana() / 100) * amount;
        if (data.getCurrentMana() + regenAmount >= data.getMaximumMana()) {
            instance.getPlayerDataController().setPlayerData(player, data.setCurrentMana(data.getMaximumMana()));
        } else {
            instance.getPlayerDataController().setPlayerData(player, data.addCurrentMana(regenAmount));
        }

        updateManaBar(player);
    }

    private void updateManaBar(Player player) {
        if (Bukkit.getServer().getBossBar(playerBars.get(player)) == null) {
            generateManaBar(player);
        }
        BossBar bar = Bukkit.getServer().getBossBar(playerBars.get(player));
        PlayerData data = instance.getPlayerDataController().getPlayerData(player);
        bar.setTitle("Mana: " + data.getCurrentMana() + "/" + data.getMaximumMana());
        bar.setProgress(Math.min(Math.max((double) data.getCurrentMana() / (double) data.getMaximumMana(), 0), 1));
    }

    public void showManaBar(Player player, int duration) {
        showManaBar(player);
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(instance, new ManaBarRunnable(player, playerBars.get(player)), duration * 20);
    }

    public void showManaBar(Player player) {
        if (Bukkit.getServer().getBossBar(playerBars.get(player)) == null) {
            generateManaBar(player);
        }
        updateManaBar(player);
        Bukkit.getServer().getBossBar(playerBars.get(player)).setVisible(true);
    }

    public void hideManaBar(Player player) {
        if (Bukkit.getServer().getBossBar(playerBars.get(player)) == null) {
            generateManaBar(player);
        }
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

    public void initializeAllManaBars() {
        playerBars = new HashMap<>();
        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            generateManaBar(player);
        }
    }

    private void generateManaBar(Player player) {
        NamespacedKey key = new NamespacedKey(instance, "mana_bar_" + player.getUniqueId());
        PlayerData data = instance.getPlayerDataController().getPlayerData(player);
        playerBars.put(player, key);
        KeyedBossBar bossBar = Bukkit.createBossBar(key, "Mana: " + data.getCurrentMana() + "/" + data.getMaximumMana(), BarColor.BLUE, BarStyle.SEGMENTED_10);
        bossBar.setProgress(Math.min(Math.max((double) data.getCurrentMana() / (double) data.getMaximumMana(), 0), 1));
        bossBar.setVisible(false);
        bossBar.addPlayer(player);
    }

    @Override
    public void run() {
        for (Player player : instance.getServer().getOnlinePlayers()) {
            regeneratePercentMana(player, 4);
            updateManaBar(player);
        }
    }
}
