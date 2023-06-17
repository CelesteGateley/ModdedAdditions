package xyz.fluxinc.moddedadditions.veinminer;

import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.*;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import xyz.fluxinc.fluxcore.command.Command;
import xyz.fluxinc.moddedadditions.common.storage.ExecutorStorage;
import xyz.fluxinc.moddedadditions.common.storage.PlayerData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static xyz.fluxinc.moddedadditions.ModdedAdditions.instance;

public class VeinMinerCommand {

    private static final String cmd = "veinminer";
    private static final String[] aliases = {"vm", "vminer", "veinm",};
    
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

    public static Command getAddCommand() {
        Command command = new Command(cmd, aliases)
                .literal("add")
                .raw(new MultiLiteralArgument("tool", List.of("pickaxe", "axe", "shovel", "hoe", "shears", "hand")))
                .string("material", getAllMaterials().toArray(new String[0]));
        return command.executor((sender, args) -> {
            if (!(sender.hasPermission("moddedadditions.veinminer.add"))) {
                sendPermissionDenied(sender);
                return;
            }
            String tool = (String) args.get(0);
            switch (tool) {
                case "pickaxe":
                    instance.getVeinMinerController().addPickaxeBlock(Material.valueOf(((String) args.get(1)).toUpperCase()));
                    break;
                case "axe":
                    instance.getVeinMinerController().addAxeBlock(Material.valueOf(((String) args.get(1)).toUpperCase()));
                    break;
                case "shovel":
                    instance.getVeinMinerController().addShovelBlock(Material.valueOf(((String) args.get(1)).toUpperCase()));
                    break;
                case "hoe":
                    instance.getVeinMinerController().addHoeBlock(Material.valueOf(((String) args.get(1)).toUpperCase()));
                    break;
                case "shears":
                    instance.getVeinMinerController().addShearsBlock(Material.valueOf(((String) args.get(1)).toUpperCase()));
                    break;
                case "hand":
                    instance.getVeinMinerController().addHandBlock(Material.valueOf(((String) args.get(1)).toUpperCase()));
                    break;
                default:
                    CommandAPI.failWithString("Invalid Tool Provided");
                    return;
            }
            sendBlockAdded(sender, (String) args.get(1), tool);
        });
    }
    
    public static Command getRemoveCommand() {
        Command command = new Command(cmd, aliases)
                .literal("remove")
                .raw(new MultiLiteralArgument("tool", List.of("pickaxe", "axe", "shovel", "hoe", "shears", "hand")))
                .string("material", getAllMaterials().toArray(new String[0]));
        return command.executor((sender, args) -> {
            if (!(sender.hasPermission("moddedadditions.veinminer.remove"))) {
                sendPermissionDenied(sender);
                return;
            }
            String tool = (String) args.get(0);
            switch (tool) {
                case "pickaxe":
                    instance.getVeinMinerController().removePickaxeBlock(Material.valueOf(((String) args.get(1)).toUpperCase()));
                    break;
                case "axe":
                    instance.getVeinMinerController().removeAxeBlock(Material.valueOf(((String) args.get(1)).toUpperCase()));
                    break;
                case "shovel":
                    instance.getVeinMinerController().removeShovelBlock(Material.valueOf(((String) args.get(1)).toUpperCase()));
                    break;
                case "hoe":
                    instance.getVeinMinerController().removeHoeBlock(Material.valueOf(((String) args.get(1)).toUpperCase()));
                    break;
                case "shears":
                    instance.getVeinMinerController().removeShearsBlock(Material.valueOf(((String) args.get(1)).toUpperCase()));
                    break;
                case "hand":
                    instance.getVeinMinerController().removeHandBlock(Material.valueOf(((String) args.get(1)).toUpperCase()));
                    break;
                default:
                    CommandAPI.failWithString("Invalid Tool Provided");
                    return;
            }
            sendBlockAdded(sender, (String) args.get(1), tool);
        });
    }

    public static Command getToggleCommand() {
        Command command = new Command(cmd, aliases).literal("toggle");
        return command.executor((sender, args) -> {
            if (!(sender instanceof Player)) {
                sendMustBePlayer(sender);
                return;
            }
            if (!sender.hasPermission("moddedadditions.veinminer.toggle")) {
                sendPermissionDenied(sender);
                return;
            }
            PlayerData data = instance.getPlayerDataController().getPlayerData((Player) sender);
            data.toggleVeinMiner();
            instance.getPlayerDataController().setPlayerData((Player) sender, data);
            if (data.veinMiner()) {
                sender.sendMessage(instance.getLanguageManager().generateMessage("vm-toggleOn"));
            } else {
                sender.sendMessage(instance.getLanguageManager().generateMessage("vm-toggleOff"));
            }
        });
    }

    public static Command getSaveCommand() {
        Command command = new Command(cmd, aliases).literal("save");
        return command.executor((sender, args) -> {
            if (!(sender.hasPermission("moddedadditions.veinminer.save"))) {
                sendPermissionDenied(sender);
                return;
            }
            instance.getVeinMinerController().saveConfiguration();
            sender.sendMessage(instance.getLanguageManager().generateMessage("vm-configSaved"));
        });
    }

    public static Command getReloadCommand() {
        Command command = new Command(cmd, aliases).literal("reload");
        return command.executor((sender, args) -> {
            if (!(sender.hasPermission("moddedadditions.veinminer.reload"))) {
                sendPermissionDenied(sender);
                return;
            }
            instance.getVeinMinerController().loadFromConfiguration();
            sender.sendMessage(instance.getLanguageManager().generateMessage("vm-configReloaded"));
        });
    }

    public static void registerCommands() {
        getAddCommand().register();
        getRemoveCommand().register();
        getToggleCommand().register();
        getSaveCommand().register();
        getReloadCommand().register();
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

    private static void sendMustBePlayer(CommandSender sender) {
        sender.sendMessage(instance.getLanguageManager().generateMessage("mustBePlayer"));
    }

}
