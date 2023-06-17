package xyz.fluxinc.moddedadditions.common.commands;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import xyz.fluxinc.fluxcore.command.Command;

import java.util.ArrayList;

public class TauntCommand {

    public static void registerCommands() {
        new Command("taunt")
            .entity("entity")
            .integer("radius")
            .executor((sender, args) -> {
                Entity selectedEntity = (Entity) args.get(0);
                int radius = 16;
                try {
                    radius = (args.get(1) == null ? 16 : ((Integer) args.get(1))) * 2;
                } catch (NullPointerException ignored) {}
                if (selectedEntity instanceof LivingEntity) {
                    for (Entity entity : selectedEntity.getNearbyEntities(radius, radius, radius)) {
                        if (entity instanceof Mob) {
                            ((Mob) entity).setTarget((LivingEntity) selectedEntity);
                        }
                    }
                }
            })
            .register();
    }
}
