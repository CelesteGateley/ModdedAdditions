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
        ArrayList<Argument> arguments = new ArrayList<>();

        arguments.add(new EntitySelectorArgument("Entity", EntitySelectorArgument.EntitySelector.ONE_ENTITY));
        new CommandAPICommand("taunt")
                .withArguments(arguments)
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

        arguments.add(new IntegerArgument("Radius"));
        new CommandAPICommand("taunt")
                .withArguments(arguments)
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
