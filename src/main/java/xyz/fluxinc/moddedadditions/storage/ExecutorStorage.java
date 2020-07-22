package xyz.fluxinc.moddedadditions.storage;

import dev.jorel.commandapi.CommandPermission;
import dev.jorel.commandapi.arguments.Argument;
import dev.jorel.commandapi.executors.CommandExecutor;

import java.util.LinkedHashMap;

public class ExecutorStorage {

    private final CommandPermission permission;
    private final CommandExecutor executor;
    private final LinkedHashMap<String, Argument> arguments;

    public ExecutorStorage(CommandExecutor executor, LinkedHashMap<String, Argument> arguments, String permission) {
        this.executor = executor;
        this.arguments = arguments;
        this.permission = CommandPermission.fromString(permission);
    }

    public CommandExecutor getExecutor() {
        return executor;
    }

    public LinkedHashMap<String, Argument> getArguments() {
        return arguments;
    }

    public CommandPermission getPermission() {
        return permission;
    }
}
