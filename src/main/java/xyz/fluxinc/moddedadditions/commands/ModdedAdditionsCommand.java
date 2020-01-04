package xyz.fluxinc.moddedadditions.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import xyz.fluxinc.moddedadditions.ModdedAdditions;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("SwitchStatementWithTooFewBranches")
public class ModdedAdditionsCommand implements CommandExecutor {

    private ModdedAdditions instance;

    public ModdedAdditionsCommand(ModdedAdditions instance) {
        this.instance = instance;
    }
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String alias, String[] arguments) {
        if (arguments.length < 1) { sendNoSubCommand(commandSender); return true; }
        switch (arguments[0].toLowerCase()) {
            case "reload":
                if (commandSender instanceof Player && !commandSender.hasPermission("moddedadditions.reload")) {
                    sendPermissionDenied(commandSender);
                    return true;
                }
                instance.reloadConfiguration();
                commandSender.sendMessage(instance.getLanguageManager().generateMessage("configurationReloaded"));
                return true;
            default:
                sendUnknownSubCommand(commandSender, arguments[0]);
                return true;
        }
    }

    private void sendNoSubCommand(CommandSender sender) {
        sender.sendMessage(instance.getLanguageManager().generateMessage("vm-noSubCommand"));
    }

    private void sendUnknownSubCommand(CommandSender sender, String subcommand) {
        Map<String, String> messageArgs = new HashMap<>();
        messageArgs.put("comand", subcommand);
        sender.sendMessage(instance.getLanguageManager().generateMessage("vm-unknownSubCommand", messageArgs));
    }

    private void sendPermissionDenied(CommandSender sender) {
        sender.sendMessage(instance.getLanguageManager().generateMessage("permissionDenied"));
    }
}
