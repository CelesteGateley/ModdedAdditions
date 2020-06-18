package xyz.fluxinc.moddedadditions.commands;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import xyz.fluxinc.moddedadditions.ModdedAdditions;
import xyz.fluxinc.moddedadditions.storage.PlayerData;

import java.util.HashMap;
import java.util.Map;

public class VeinMinerCommand implements CommandExecutor {

    private final ModdedAdditions instance;

    public VeinMinerCommand(ModdedAdditions instance) {
        this.instance = instance;

    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String alias, String[] arguments) {
        if (arguments.length < 1) {
            sendNoSubCommand(commandSender);
            return true;
        }
        switch (arguments[0].toLowerCase()) {
            case "add":
                // Check if the sender has permission
                if (commandSender instanceof Player && !commandSender.hasPermission("moddedadditions.veinminer.add")) {
                    sendPermissionDenied(commandSender);
                    return true;
                }

                if (arguments.length < 2) {
                    sendNoToolProvided(commandSender);
                    return true;
                }

                // Check if the sender provided a block
                if (arguments.length < 3) {
                    sendNoBlockProvided(commandSender);
                    return true;
                }
                Material block;
                try {
                    block = Material.valueOf(arguments[2].toUpperCase());
                } catch (IllegalArgumentException ex) {
                    sendInvalidBlock(commandSender, arguments[2]);
                    return true;
                }

                switch (arguments[1].toLowerCase()) {
                    case "pickaxe":
                        instance.getVeinMinerController().addPickaxeBlock(block);
                        break;
                    case "axe":
                        instance.getVeinMinerController().addAxeBlock(block);
                        break;
                    case "shovel":
                        instance.getVeinMinerController().addShovelBlock(block);
                        break;
                    case "hoe":
                        instance.getVeinMinerController().addHoeBlock(block);
                        break;
                    case "shears":
                        instance.getVeinMinerController().addShearsBlock(block);
                        break;
                    case "hand":
                        instance.getVeinMinerController().addHandBlock(block);
                        break;
                    default:
                        sendInvalidTool(commandSender, arguments[1]);
                        return true;
                }
                sendBlockAdded(commandSender, arguments[2], arguments[1]);
                return true;
            case "remove":
                // Check if the sender has permission
                if (commandSender instanceof Player && !commandSender.hasPermission("moddedadditions.veinminer.remove")) {
                    sendPermissionDenied(commandSender);
                    return true;
                }

                if (arguments.length < 2) {
                    sendNoToolProvided(commandSender);
                    return true;
                }

                // Check if the sender provided a block
                if (arguments.length < 3) {
                    sendNoBlockProvided(commandSender);
                    return true;
                }
                Material removeBlock;
                try {
                    removeBlock = Material.valueOf(arguments[2].toUpperCase());
                } catch (IllegalArgumentException ex) {
                    sendInvalidBlock(commandSender, arguments[2]);
                    return true;
                }

                switch (arguments[1].toLowerCase()) {
                    case "pickaxe":
                        instance.getVeinMinerController().removePickaxeBlock(removeBlock);
                        break;
                    case "axe":
                        instance.getVeinMinerController().removeAxeBlock(removeBlock);
                        break;
                    case "shovel":
                        instance.getVeinMinerController().removeShovelBlock(removeBlock);
                        break;
                    case "hoe":
                        instance.getVeinMinerController().removeHoeBlock(removeBlock);
                        break;
                    case "shears":
                        instance.getVeinMinerController().removeShearsBlock(removeBlock);
                        break;
                    case "hand":
                        instance.getVeinMinerController().removeHandBlock(removeBlock);
                        break;
                    default:
                        sendInvalidTool(commandSender, arguments[1]);
                        return true;
                }
                sendBlockRemoved(commandSender, arguments[2], arguments[1]);
                return true;
            case "toggle":
                if (!(commandSender instanceof Player)) {
                    sendMustBePlayer(commandSender);
                    return true;
                }
                if (!commandSender.hasPermission("moddedadditions.veinminer.toggle")) {
                    sendPermissionDenied(commandSender);
                    return true;
                }
                PlayerData data = instance.getPlayerDataController().getPlayerData((Player) commandSender);
                data.toggleVeinMiner();
                instance.getPlayerDataController().setPlayerData((Player) commandSender, data);
                if (data.veinMiner()) {
                    commandSender.sendMessage(instance.getLanguageManager().generateMessage("vm-toggleOn"));
                } else {
                    commandSender.sendMessage(instance.getLanguageManager().generateMessage("vm-toggleOff"));
                }
                return true;
            case "reload":
                if (commandSender instanceof Player && !commandSender.hasPermission("moddedadditions.veinminer.reload")) {
                    sendPermissionDenied(commandSender);
                    return true;
                }
                instance.getVeinMinerController().loadFromConfiguration();
                commandSender.sendMessage(instance.getLanguageManager().generateMessage("vm-configReloaded"));
                return true;
            case "save":
                if (commandSender instanceof Player && !commandSender.hasPermission("moddedadditions.veinminer.save")) {
                    sendPermissionDenied(commandSender);
                    return true;
                }
                instance.getVeinMinerController().saveConfiguration();
                commandSender.sendMessage(instance.getLanguageManager().generateMessage("vm-configSaved"));
                return true;
            default:
                sendUnknownSubCommand(commandSender, arguments[0]);
                return true;
        }
    }

    private void sendPermissionDenied(CommandSender sender) {
        sender.sendMessage(instance.getLanguageManager().generateMessage("permissionDenied"));
    }

    private void sendInvalidTool(CommandSender sender, String tool) {
        Map<String, String> messageArgs = new HashMap<>();
        messageArgs.put("tool", tool);
        sender.sendMessage(instance.getLanguageManager().generateMessage("vm-invalidTool", messageArgs));
    }

    private void sendInvalidBlock(CommandSender sender, String block) {
        Map<String, String> messageArgs = new HashMap<>();
        messageArgs.put("block", block);
        sender.sendMessage(instance.getLanguageManager().generateMessage("vm-invalidBlock", messageArgs));
    }

    private void sendNoToolProvided(CommandSender sender) {
        sender.sendMessage(instance.getLanguageManager().generateMessage("vm-noToolProvided"));
    }

    private void sendNoBlockProvided(CommandSender sender) {
        sender.sendMessage(instance.getLanguageManager().generateMessage("vm-noBlockProvided"));
    }

    private void sendBlockAdded(CommandSender sender, String material, String tool) {
        Map<String, String> messageArgs = new HashMap<>();
        messageArgs.put("tool", tool);
        messageArgs.put("block", material);
        sender.sendMessage(instance.getLanguageManager().generateMessage("vm-blockAdded", messageArgs));
    }

    private void sendBlockRemoved(CommandSender sender, String material, String tool) {
        Map<String, String> messageArgs = new HashMap<>();
        messageArgs.put("tool", tool);
        messageArgs.put("block", material);
        sender.sendMessage(instance.getLanguageManager().generateMessage("vm-blockRemoved", messageArgs));
    }

    private void sendMustBePlayer(CommandSender sender) {
        sender.sendMessage(instance.getLanguageManager().generateMessage("mustBePlayer"));
    }

    private void sendNoSubCommand(CommandSender sender) {
        sender.sendMessage(instance.getLanguageManager().generateMessage("vm-noSubCommand"));
    }

    private void sendUnknownSubCommand(CommandSender sender, String subcommand) {
        Map<String, String> messageArgs = new HashMap<>();
        messageArgs.put("command", subcommand);
        sender.sendMessage(instance.getLanguageManager().generateMessage("vm-unknownSubCommand", messageArgs));
    }
}
