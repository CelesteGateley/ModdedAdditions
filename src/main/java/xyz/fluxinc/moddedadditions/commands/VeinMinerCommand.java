package xyz.fluxinc.moddedadditions.commands;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import xyz.fluxinc.fluxcore.configuration.LanguageManager;
import xyz.fluxinc.moddedadditions.controllers.VeinMinerController;

import java.util.HashMap;
import java.util.Map;

public class VeinMinerCommand implements CommandExecutor {

    private VeinMinerController veinMinerController;
    private LanguageManager languageManager;


    public VeinMinerCommand(VeinMinerController veinMinerController, LanguageManager languageFile) {
        this.veinMinerController = veinMinerController;
        this.languageManager = languageFile;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String alias, String[] arguments) {
        switch (arguments[0].toLowerCase()) {
            case "add":
                // Check if the sender has permission
                if (commandSender instanceof Player && !commandSender.hasPermission("moddedadditions.veinminer.add")) {
                    sendPermissionDenied(commandSender);
                    return true;
                }

                if (arguments.length < 2) { sendNoToolProvided(commandSender); return true; }

                // Check if the sender provided a block
                if (arguments.length < 3) { sendNoBlockProvided(commandSender); return true; }
                Material block;
                try {
                    block = Material.valueOf(arguments[2].toUpperCase());
                } catch (IllegalArgumentException ex) {
                    sendInvalidBlock(commandSender, arguments[2]);
                    return true;
                }

                switch (arguments[1].toLowerCase()) {
                    case "pickaxe":
                        veinMinerController.addPickaxeBlock(block);
                        break;
                    case "axe":
                        veinMinerController.addAxeBlock(block);
                        break;
                    case "shovel":
                        veinMinerController.addShovelBlock(block);
                        break;
                    case "hoe":
                        veinMinerController.addHoeBlock(block);
                        break;
                    case "shears":
                        veinMinerController.addShearsBlock(block);
                        break;
                    case "hand":
                        veinMinerController.addHandBlock(block);
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

                if (arguments.length < 2) { sendNoToolProvided(commandSender); return true; }

                // Check if the sender provided a block
                if (arguments.length < 3) { sendNoBlockProvided(commandSender); return true; }
                Material removeBlock;
                try {
                    removeBlock = Material.valueOf(arguments[2].toUpperCase());
                } catch (IllegalArgumentException ex) {
                    sendInvalidBlock(commandSender, arguments[2]);
                    return true;
                }

                switch (arguments[1].toLowerCase()) {
                    case "pickaxe":
                        veinMinerController.removePickaxeBlock(removeBlock);
                        break;
                    case "axe":
                        veinMinerController.removeAxeBlock(removeBlock);
                        break;
                    case "shovel":
                        veinMinerController.removeShovelBlock(removeBlock);
                        break;
                    case "hoe":
                        veinMinerController.removeHoeBlock(removeBlock);
                        break;
                    case "shears":
                        veinMinerController.removeShearsBlock(removeBlock);
                        break;
                    case "hand":
                        veinMinerController.removeHandBlock(removeBlock);
                        break;
                    default:
                        sendInvalidTool(commandSender, arguments[1]);
                        return true;
                }
                sendBlockRemoved(commandSender, arguments[2], arguments[1]);
                return true;
            case "toggle":
                if (!(commandSender instanceof Player)) { sendMustBePlayer(commandSender); return true; }
                Player player = (Player) commandSender;
                veinMinerController.toggleVeinMiner(player);
                if (veinMinerController.isToggled(player)) {
                    commandSender.sendMessage(languageManager.generateMessage("vm-toggleOff"));
                } else {
                    commandSender.sendMessage(languageManager.generateMessage("vm-toggleOn"));
                }
                return true;
            case "reload":
                if (commandSender instanceof Player && !commandSender.hasPermission("moddedadditions.veinminer.reload")) {
                    sendPermissionDenied(commandSender);
                    return true;
                }
                veinMinerController.loadFromConfiguration();
                commandSender.sendMessage(languageManager.generateMessage("vm-configReloaded"));
                return true;
            case "save":
                if (commandSender instanceof Player && !commandSender.hasPermission("moddedadditions.veinminer.save")) {
                    sendPermissionDenied(commandSender);
                    return true;
                }
                veinMinerController.loadFromConfiguration();
                commandSender.sendMessage(languageManager.generateMessage("vm-configSaved"));
                return true;
            default:
        }
        return false;
    }

    private void sendPermissionDenied(CommandSender sender) {
        sender.sendMessage(languageManager.generateMessage("permissionDenied"));
    }

    private void sendInvalidTool(CommandSender sender, String tool) {
        Map<String, String> messageArgs = new HashMap<>();
        messageArgs.put("tool", tool);
        sender.sendMessage(languageManager.generateMessage("vm-invalidTool", messageArgs));
    }

    private void sendInvalidBlock(CommandSender sender, String block) {
        Map<String, String> messageArgs = new HashMap<>();
        messageArgs.put("block", block);
        sender.sendMessage(languageManager.generateMessage("vm-invalidBlock", messageArgs));
    }

    private void sendNoToolProvided(CommandSender sender) {
        sender.sendMessage(languageManager.generateMessage("vm-noToolProvided"));
    }

    private void sendNoBlockProvided(CommandSender sender) {
        sender.sendMessage(languageManager.generateMessage("vm-noBlockProvided"));
    }

    private void sendBlockAdded(CommandSender sender, String material, String tool) {
        Map<String, String> messageArgs = new HashMap<>();
        messageArgs.put("tool", tool);
        messageArgs.put("block", material);
        sender.sendMessage(languageManager.generateMessage("vm-blockAdded", messageArgs));
    }

    private void sendBlockRemoved(CommandSender sender, String material, String tool) {
        Map<String, String> messageArgs = new HashMap<>();
        messageArgs.put("tool", tool);
        messageArgs.put("block", material);
        sender.sendMessage(languageManager.generateMessage("vm-blockRemoved", messageArgs));
    }

    private void sendMustBePlayer(CommandSender sender) {
        sender.sendMessage(languageManager.generateMessage("mustBePlayer"));
    }

    private void sendUnknownSubCommand(CommandSender sender, String subcommand) {
        Map<String, String> messageArgs = new HashMap<>();
        messageArgs.put("comand", subcommand);
        sender.sendMessage(languageManager.generateMessage("vm-unknownSubCommand", messageArgs));
    }
}
