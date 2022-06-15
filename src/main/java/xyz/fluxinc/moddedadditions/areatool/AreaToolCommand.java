package xyz.fluxinc.moddedadditions.areatool;

import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.*;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import xyz.fluxinc.moddedadditions.common.storage.ExecutorStorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static xyz.fluxinc.moddedadditions.ModdedAdditions.instance;

public class AreaToolCommand {

    private static List<String> getAllMaterials() {
        List<String> materials = new ArrayList<>();
        for (Material material : Material.values()) {
            String val = material.toString().toLowerCase();
            if (!(val.contains("legacy"))) {
                materials.add(val);
            }
        }
        return materials;
    }


    private static HashMap<String, ExecutorStorage> getCommands() {
        HashMap<String, ExecutorStorage> returnVal = new HashMap<>();
        ListArgument<String> materialArgument = new ListArgumentBuilder<String>("material").withList(getAllMaterials()).withStringMapper().build();
        // Add Command
        List<Argument> arguments = new ArrayList<>();
        arguments.add(new LiteralArgument("add"));
        arguments.add(new MultiLiteralArgument("hammer", "excavator"));
        arguments.add(materialArgument);
        returnVal.put("add", new ExecutorStorage((sender, args) -> {
            if (!(sender.hasPermission("moddedadditions.areatool.add"))) {
                sendPermissionDenied(sender);
                return;
            }
            String tool = (String) args[0];
            if (tool.equals("hammer")) {
                instance.getAreaToolController().addHammerBlock(Material.valueOf(((String) args[1]).toUpperCase()));
            } else if (tool.equals("excavator")) {
                instance.getAreaToolController().addExcavatorBlock(Material.valueOf(((String) args[1]).toUpperCase()));
            } else {
                CommandAPI.fail("Invalid Tool Provided");
                return;
            }
            sendBlockAdded(sender, (String) args[1], tool);
        }, arguments));

        // Remove Command
        arguments = new ArrayList<>();
        arguments.add(new LiteralArgument("remove"));
        arguments.add(new MultiLiteralArgument("hammer", "excavator"));
        arguments.add(materialArgument);
        returnVal.put("remove", new ExecutorStorage((sender, args) -> {
            if (!(sender.hasPermission("moddedadditions.areatool.remove"))) {
                sendPermissionDenied(sender);
                return;
            }
            String tool = (String) args[0];
            if (tool.equals("hammer")) {
                instance.getAreaToolController().removeHammerBlock(Material.valueOf(((String) args[1]).toUpperCase()));
            } else if (tool.equals("excavator")) {
                instance.getAreaToolController().removeExcavatorBlock(Material.valueOf(((String) args[1]).toUpperCase()));
            } else {
                CommandAPI.fail("Invalid Tool Provided");
                return;
            }
            sendBlockRemoved(sender, (String) args[1], tool);
        }, arguments));

        // Save Command
        arguments = new ArrayList<>();
        arguments.add(new LiteralArgument("save"));
        returnVal.put("save", new ExecutorStorage((sender, args) -> {
            if (!(sender.hasPermission("moddedadditions.areatool.save"))) {
                sendPermissionDenied(sender);
                return;
            }
            instance.getAreaToolController().saveConfiguration();
            sender.sendMessage(instance.getLanguageManager().generateMessage("vm-configSaved"));
        }, arguments));

        // Reload Command
        arguments = new ArrayList<>();
        arguments.add(new LiteralArgument("reload"));
        returnVal.put("reload", new ExecutorStorage((sender, args) -> {
            if (!(sender.hasPermission("moddedadditions.areatool.reload"))) {
                sendPermissionDenied(sender);
                return;
            }
            instance.getAreaToolController().loadFromConfiguration();
            sender.sendMessage(instance.getLanguageManager().generateMessage("vm-configReloaded"));
        }, arguments));

        return returnVal;
    }

    public static void registerCommands() {
        HashMap<String, ExecutorStorage> commands = getCommands();
        for (String key : commands.keySet()) {
            CommandAPICommand command = new CommandAPICommand("areatool").withAliases("at", "atool", "areat");
            for (Argument argument : commands.get(key).getArguments()) {
                command.withArguments(argument);
            }
            command.executes(commands.get(key).getExecutor());
            command.register();
        }
    }

    private static void sendPermissionDenied(CommandSender sender) {
        sender.sendMessage(instance.getLanguageManager().generateMessage("permissionDenied"));
    }

    private static void sendBlockAdded(CommandSender sender, String material, String tool) {
        Map<String, String> messageArgs = new HashMap<>();
        messageArgs.put("tool", tool);
        messageArgs.put("block", material);
        sender.sendMessage(instance.getLanguageManager().generateMessage("vm-blockAdded", messageArgs));
    }

    private static void sendBlockRemoved(CommandSender sender, String material, String tool) {
        Map<String, String> messageArgs = new HashMap<>();
        messageArgs.put("tool", tool);
        messageArgs.put("block", material);
        sender.sendMessage(instance.getLanguageManager().generateMessage("vm-blockRemoved", messageArgs));
    }

}
