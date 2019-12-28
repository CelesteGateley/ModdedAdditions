package xyz.fluxinc.moddedadditions.runnables;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;

import java.util.Collection;

public class MagnetRunnable implements Runnable {

    private Player player;
    private int xRadius;
    private int yRadius;
    private int zRadius;

    public MagnetRunnable(Player player) {
        this.player = player;
        this.xRadius = 16;
        this.yRadius = 16;
        this.zRadius = 16;
    }

    public MagnetRunnable(Player player, int radius) {
        this.player = player;
        this.xRadius = radius;
        this.yRadius = radius;
        this.zRadius = radius;
    }

    public MagnetRunnable(Player player, int xRadius, int yRadius, int zRadius) {
        this.player = player;
        this.xRadius = xRadius;
        this.yRadius = yRadius;
        this.zRadius = zRadius;
    }

    @Override
    public void run() {
        Location pFeet = player.getLocation();
        Collection<Entity> nearbyEntities = player.getWorld().getNearbyEntities(pFeet, xRadius, yRadius, zRadius);
        for (Entity entity : nearbyEntities) {
            if (entity.getType() != EntityType.DROPPED_ITEM) {
                continue;
            }
            Item item = (Item) entity;
            if (item.getPickupDelay() <= 0) {
                item.teleport(player);
            }
        }
    }
}
