package xyz.fluxinc.moddedadditions.controllers;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;
import xyz.fluxinc.moddedadditions.ModdedAdditions;
import xyz.fluxinc.moddedadditions.runnables.MagnetRunnable;

import java.util.HashMap;
import java.util.Map;

public class MagnetInstanceController {

    private BukkitScheduler taskScheduler;
    private ModdedAdditions instance;
    private Map<Player, Integer> vacuumInstances;

    public MagnetInstanceController(ModdedAdditions instance, BukkitScheduler taskScheduler) {
        this.taskScheduler = taskScheduler;
        this.instance = instance;
        vacuumInstances = new HashMap<>();
    }

    public boolean hasVacuumInstance(Player player) {
        return vacuumInstances.containsKey(player);
    }

    public void registerVacuumInstance(Player player) {
        if (!hasVacuumInstance(player)) {
            int taskId = taskScheduler.scheduleSyncRepeatingTask(instance, new MagnetRunnable(player), 0L, 20L);
            vacuumInstances.put(player, taskId);
        }
    }

    public void deregisterVacuumInstance(Player player) {
        if (hasVacuumInstance(player)) {
            taskScheduler.cancelTask(vacuumInstances.get(player));
            vacuumInstances.remove(player);
        }
    }

}
