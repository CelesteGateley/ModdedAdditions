package xyz.fluxinc.moddedadditions.common.storage;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import xyz.fluxinc.fluxcore.configuration.ConfigurationManager;
import xyz.fluxinc.moddedadditions.ModdedAdditions;
import xyz.fluxinc.moddedadditions.common.storage.PlayerData;

import java.util.HashMap;
import java.util.Map;

public class PlayerDataController extends ConfigurationManager<ModdedAdditions> implements Listener, Runnable {

    private final Map<Player, PlayerData> cachedData;

    public PlayerDataController(String configuration) {
        super(ModdedAdditions.instance, configuration);
        this.cachedData = new HashMap<>();
        ModdedAdditions.instance.getServer().getScheduler().scheduleSyncRepeatingTask(ModdedAdditions.instance, this, 6000, 6000);
    }

    public PlayerData getPlayerData(Player player) {
        if (cachedData.containsKey(player)) {
            return cachedData.get(player);
        }
        return initializePlayerData(player);
    }

    private PlayerData initializePlayerData(Player player) {
        PlayerData playerData = getGeneric("" + player.getUniqueId());
        if (playerData == null) {
            playerData = new PlayerData();
            getConfiguration().set("" + player.getUniqueId(), playerData);
            saveConfiguration();
            cachedData.put(player, playerData);
        }
        return playerData;
    }

    public void setPlayerData(Player player, PlayerData playerData) {
        getConfiguration().set("" + player.getUniqueId(), playerData);
        cachedData.remove(player);
        cachedData.put(player, playerData);
    }

    public void saveToDisk() {
        for (Player key : cachedData.keySet()) {
            getConfiguration().set(key.getUniqueId() + "", cachedData.get(key));
        }
        saveConfiguration();
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerJoin(PlayerJoinEvent event) {
        cachedData.put(event.getPlayer(), initializePlayerData(event.getPlayer()));
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerLeave(PlayerQuitEvent event) {
        saveToDisk();
        cachedData.remove(event.getPlayer());
    }

    @Override
    public void run() {
        Bukkit.getLogger().fine("Saving Player Data to Disk");
        saveToDisk();
    }
}
