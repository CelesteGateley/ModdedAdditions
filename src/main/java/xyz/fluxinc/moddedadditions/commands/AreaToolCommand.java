package xyz.fluxinc.moddedadditions.commands;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import xyz.fluxinc.moddedadditions.ModdedAdditions;

import java.util.HashMap;
import java.util.Map;

public class AreaToolCommand implements CommandExecutor {

    private ModdedAdditions instance;


    public AreaToolCommand(ModdedAdditions instance) { this.instance = instance; }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String alias, String[] arguments) {
        if (arguments.length < 1) {
            sendNoSubCommand(commandSender);
            return true;
        }
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
                        instance.getAreaToolController().addHammerBlock(block);
                        break;
                    case "excavator":
                        instance.getAreaToolController().addExcavatorBlock(block);
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
                        instance.getAreaToolController().removeHammerBlock(removeBlock);
                        break;
                    case "excavator":
                        instance.getAreaToolController().removeExcavatorBlock(removeBlock);
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
                instance.getAreaToolController().loadFromConfiguration();
                commandSender.sendMessage(instance.getLanguageManager().generateMessage("vm-configReloaded"));
                return true;
            case "save":
                if (commandSender instanceof Player && !commandSender.hasPermission("moddedadditions.areatool.save")) {
                    sendPermissionDenied(commandSender);
                    return true;
                }
                instance.getAreaToolController().saveConfiguration();
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

    private void sendUnknownSubCommand(CommandSender sender, String subcommand) {
        Map<String, String> messageArgs = new HashMap<>();
        messageArgs.put("command", subcommand);
        sender.sendMessage(instance.getLanguageManager().generateMessage("vm-unknownSubCommand", messageArgs));
    }

    private void sendNoSubCommand(CommandSender sender) {
        sender.sendMessage(instance.getLanguageManager().generateMessage("at-noSubCommand"));
    }
}
