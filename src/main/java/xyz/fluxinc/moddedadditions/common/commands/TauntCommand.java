package xyz.fluxinc.moddedadditions.common.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.Argument;
import dev.jorel.commandapi.arguments.EntitySelectorArgument;
import dev.jorel.commandapi.arguments.IntegerArgument;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;

import java.util.ArrayList;

public class TauntCommand {

    public static void registerCommands() {
        new CommandAPICommand("taunt")
                .withArguments(new EntitySelectorArgument("Entity", EntitySelectorArgument.EntitySelector.ONE_ENTITY))
                .executes((sender, args) -> {
                    Entity selectedEntity = (Entity) args[0];
                    if (selectedEntity instanceof LivingEntity) {
                        for (Entity entity : selectedEntity.getNearbyEntities(32, 16, 32)) {
                            if (entity instanceof Mob) {
                                ((Mob) entity).setTarget((LivingEntity) selectedEntity);
                            }
                        }
                    }
                }).register();

        new CommandAPICommand("taunt")
                .withArguments(new EntitySelectorArgument("Entity", EntitySelectorArgument.EntitySelector.ONE_ENTITY), new IntegerArgument("Radius"))
                .executes((sender, args) -> {
                    Entity selectedEntity = (Entity) args[0];
                    int radius = ((Integer) args[1]) * 2;
                    if (selectedEntity instanceof LivingEntity) {
                        for (Entity entity : selectedEntity.getNearbyEntities(radius, radius, radius)) {
                            if (entity instanceof Mob) {
                                ((Mob) entity).setTarget((LivingEntity) selectedEntity);
                            }
                        }
                    }
                }).register();
    }
}
