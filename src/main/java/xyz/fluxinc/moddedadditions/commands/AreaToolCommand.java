package xyz.fluxinc.moddedadditions.commands;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import xyz.fluxinc.fluxcore.configuration.LanguageManager;
import xyz.fluxinc.moddedadditions.controllers.AreaToolController;
import xyz.fluxinc.moddedadditions.controllers.VeinMinerController;

import java.util.HashMap;
import java.util.Map;

public class AreaToolCommand implements CommandExecutor {

    private AreaToolController areaToolController;
    private LanguageManager languageManager;


    public AreaToolCommand(AreaToolController areaToolController, LanguageManager languageFile) {
        this.areaToolController = areaToolController;
        this.languageManager = languageFile;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String alias, String[] arguments) {
        switch (arguments[0].toLowerCase()) {
            case "add":
                // Check if the sender has permission
                if (commandSender instanceof Player && !commandSender.hasPermission("moddedadditions.areatool.add")) {
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
                    case "hammer":
                        areaToolController.addHammerBlock(block);
                        break;
                    case "excavator":
                        areaToolController.addExcavatorBlock(block);
                        break;
                    default:
                        sendInvalidTool(commandSender, arguments[1]);
                        return true;
                }
                sendBlockAdded(commandSender, arguments[2], arguments[1]);
                return true;
            case "remove":
                // Check if the sender has permission
                if (commandSender instanceof Player && !commandSender.hasPermission("moddedadditions.areatool.remove")) {
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
                    case "hammer":
                        areaToolController.addHammerBlock(removeBlock);
                        break;
                    case "excavator":
                        areaToolController.addExcavatorBlock(removeBlock);
                        break;
                    default:
                        sendInvalidTool(commandSender, arguments[1]);
                        return true;
                }
                sendBlockRemoved(commandSender, arguments[2], arguments[1]);
                return true;
            case "reload":
                if (commandSender instanceof Player && !commandSender.hasPermission("moddedadditions.areatool.reload")) {
                    sendPermissionDenied(commandSender);
                    return true;
                }
                areaToolController.loadFromConfiguration();
                commandSender.sendMessage(languageManager.generateMessage("vm-configReloaded"));
                return true;
            case "save":
                if (commandSender instanceof Player && !commandSender.hasPermission("moddedadditions.areatool.save")) {
                    sendPermissionDenied(commandSender);
                    return true;
                }
                areaToolController.saveConfiguration();
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
