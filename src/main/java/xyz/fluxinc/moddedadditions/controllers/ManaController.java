package xyz.fluxinc.moddedadditions.controllers;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import xyz.fluxinc.moddedadditions.ModdedAdditions;
import xyz.fluxinc.moddedadditions.storage.PlayerData;

public class ManaController implements Listener, Runnable {

    private ModdedAdditions instance;

    public ManaController(ModdedAdditions instance) {
        this.instance = instance;
        instance.getServer().getScheduler().scheduleSyncRepeatingTask(instance, this, 100, 100);
    }

    public void useMana(Player player, int amount) {
        instance.getPlayerDataController().setPlayerData(player, instance.getPlayerDataController().getPlayerData(player).takeCurrentMana(amount));
    }

    public void regenerateMana(Player player, int amount) {
        instance.getPlayerDataController().setPlayerData(player, instance.getPlayerDataController().getPlayerData(player).addCurrentMana(amount));
    }

    public void showManaBar(Player player, int duration) {
    }

    public void showManaBar(Player player) {}

    public void hideManaBar(Player player) {}


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
