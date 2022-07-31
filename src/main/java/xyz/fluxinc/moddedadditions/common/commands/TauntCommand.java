package xyz.fluxinc.moddedadditions.common.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.Argument;
import dev.jorel.commandapi.arguments.EntitySelector;
import dev.jorel.commandapi.arguments.EntitySelectorArgument;
import dev.jorel.commandapi.arguments.IntegerArgument;
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
                Entity selectedEntity = (Entity) args[0];
                int radius = (args[1] == null ? 16 : ((Integer) args[1])) * 2;
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
