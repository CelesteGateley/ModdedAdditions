package xyz.fluxinc.moddedadditions.runnables;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.function.Predicate;

public class MagnetRunnable implements Runnable {

    private static final int DEFAULT_RADIUS = 8;
    private final Player player;
    private final int xRadius;
    private final int yRadius;
    private final int zRadius;

    public MagnetRunnable(Player player) {
        this.player = player;
        this.xRadius = DEFAULT_RADIUS;
        this.yRadius = DEFAULT_RADIUS;
        this.zRadius = DEFAULT_RADIUS;
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
        Predicate<Entity> predicate = entity -> (entity.getType() == EntityType.DROPPED_ITEM);
        Collection<Entity> nearbyEntities = player.getWorld().getNearbyEntities(pFeet, xRadius, yRadius, zRadius, predicate);
        for (Entity entity : nearbyEntities) {
            Item item = (Item) entity;
            if (item.getPickupDelay() <= 0) {
                item.teleport(player);
            }
        }
    }
}
