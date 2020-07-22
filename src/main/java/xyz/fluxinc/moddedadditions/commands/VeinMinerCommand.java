package xyz.fluxinc.moddedadditions.commands;

import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.Argument;
import dev.jorel.commandapi.arguments.LiteralArgument;
import dev.jorel.commandapi.arguments.StringArgument;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import xyz.fluxinc.moddedadditions.storage.ExecutorStorage;

import java.util.*;

import static xyz.fluxinc.moddedadditions.ModdedAdditions.instance;

public class VeinMinerCommand {

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
        String[] materials = getAllMaterials().toArray(new String[0]);
        // Add Command
        LinkedHashMap<String, Argument> arguments = new LinkedHashMap<>();
        arguments.put("add", new LiteralArgument("add"));
        arguments.put("tool", new StringArgument().overrideSuggestions("pickaxe","axe","shovel","hoe","shears","hand"));
        arguments.put("material", new StringArgument().overrideSuggestions(materials));
        returnVal.put("add", new ExecutorStorage((sender, args) -> {
            if (!(sender.hasPermission("moddedadditions.veinminer.add"))) { sendPermissionDenied(sender); return; }
            String tool = (String) args[0];
            switch (tool) {
                case "pickaxe":
                    instance.getVeinMinerController().addPickaxeBlock(Material.valueOf(((String) args[1]).toUpperCase()));
                    break;
                case "axe":
                    instance.getVeinMinerController().addAxeBlock(Material.valueOf(((String) args[1]).toUpperCase()));
                    break;
                case "shovel":
                    instance.getVeinMinerController().addShovelBlock(Material.valueOf(((String) args[1]).toUpperCase()));
                    break;
                case "hoe":
                    instance.getVeinMinerController().addHoeBlock(Material.valueOf(((String) args[1]).toUpperCase()));
                    break;
                case "shears":
                    instance.getVeinMinerController().addShearsBlock(Material.valueOf(((String) args[1]).toUpperCase()));
                    break;
                case "hand":
                    instance.getVeinMinerController().addHandBlock(Material.valueOf(((String) args[1]).toUpperCase()));
                    break;
                default:
                    CommandAPI.fail("Invalid Tool Provided");
                    return;
            }
            sendBlockAdded(sender, (String) args[1], tool);
        }, arguments));

        // Remove Command
        arguments = new LinkedHashMap<>();
        arguments.put("remove", new LiteralArgument("remove"));
        arguments.put("tool", new StringArgument().overrideSuggestions("pickaxe","axe","shovel","hoe","shears","hand"));
        arguments.put("material", new StringArgument().overrideSuggestions(materials));
        returnVal.put("remove", new ExecutorStorage((sender, args) -> {
            if (!(sender.hasPermission("moddedadditions.veinminer.remove"))) { sendPermissionDenied(sender); return; }
            String tool = (String) args[0];
            switch (tool) {
                case "pickaxe":
                    instance.getVeinMinerController().removePickaxeBlock(Material.valueOf(((String) args[1]).toUpperCase()));
                    break;
                case "axe":
                    instance.getVeinMinerController().removeAxeBlock(Material.valueOf(((String) args[1]).toUpperCase()));
                    break;
                case "shovel":
                    instance.getVeinMinerController().removeShovelBlock(Material.valueOf(((String) args[1]).toUpperCase()));
                    break;
                case "hoe":
                    instance.getVeinMinerController().removeHoeBlock(Material.valueOf(((String) args[1]).toUpperCase()));
                    break;
                case "shears":
                    instance.getVeinMinerController().removeShearsBlock(Material.valueOf(((String) args[1]).toUpperCase()));
                    break;
                case "hand":
                    instance.getVeinMinerController().removeHandBlock(Material.valueOf(((String) args[1]).toUpperCase()));
                    break;
                default:
                    CommandAPI.fail("Invalid Tool Provided");
                    return;
            }
            sendBlockRemoved(sender, (String) args[1], tool);
        }, arguments));

        // Save Command
        arguments = new LinkedHashMap<>();
        arguments.put("save", new LiteralArgument("save"));
        returnVal.put("save", new ExecutorStorage((sender, args) -> {
            if (!(sender.hasPermission("moddedadditions.veinminer.save"))) { sendPermissionDenied(sender); return; }
            instance.getVeinMinerController().saveConfiguration();
            sender.sendMessage(instance.getLanguageManager().generateMessage("vm-configSaved"));
        }, arguments));

        // Reload Command
        arguments = new LinkedHashMap<>();
        arguments.put("reload", new LiteralArgument("reload"));
        returnVal.put("reload", new ExecutorStorage((sender, args) -> {
            if (!(sender.hasPermission("moddedadditions.veinminer.reload"))) { sendPermissionDenied(sender); return; }
            instance.getVeinMinerController().loadFromConfiguration();
            sender.sendMessage(instance.getLanguageManager().generateMessage("vm-configReloaded"));
        }, arguments));

        return returnVal;
    }

    public static void registerCommands() {
        HashMap<String, ExecutorStorage> commands = getCommands();
        for (String key : commands.keySet()) {
            new CommandAPICommand("veinminer")
                    .withAliases("vm", "vminer", "veinm")
                    .withArguments(commands.get(key).getArguments())
                    .executes(commands.get(key).getExecutor())
                    .register();
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
