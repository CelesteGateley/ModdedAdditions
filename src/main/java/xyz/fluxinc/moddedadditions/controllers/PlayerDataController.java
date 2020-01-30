package xyz.fluxinc.moddedadditions.controllers;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.fluxinc.fluxcore.configuration.ConfigurationManager;
import xyz.fluxinc.moddedadditions.ModdedAdditions;
import xyz.fluxinc.moddedadditions.storage.PlayerData;

public class PlayerDataController extends ConfigurationManager implements Listener {

    private ModdedAdditions instance;

    public PlayerDataController(ModdedAdditions instance, String configuration) {
        super(instance, configuration);
        this.instance = instance;
    }

    public PlayerData getPlayerData(Player player) {
        PlayerData playerData = (PlayerData) getGeneric("" + player.getUniqueId());
        if (playerData == null) {
            playerData = new PlayerData();
            getConfiguration().set("" + player.getUniqueId(), playerData);
            saveConfiguration();
        }
        return playerData;
    }

    public void setPlayerData(Player player, PlayerData playerData) {
        getConfiguration().set("" + player.getUniqueId(), playerData);
        saveConfiguration();
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerJoin(PlayerJoinEvent event) {
        PlayerData data = getPlayerData(event.getPlayer());
        instance.getVeinMinerController();
    }
}
