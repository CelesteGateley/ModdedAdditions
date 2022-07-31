package xyz.fluxinc.moddedadditions.areatool;

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

public class AreaToolCommand {

    private static final String cmd = "areatool";
    private static final String[] aliases = {"at", "atool", "areat",};

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
                .raw(new MultiLiteralArgument("hammer", "excavator"))
                .string("material", getAllMaterials().toArray(new String[0]));
        return command.executor((sender, args) -> {
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
        });
    }

    public static Command getRemoveCommand() {
        Command command = new Command(cmd, aliases)
                .literal("remove")
                .raw(new MultiLiteralArgument("hammer", "excavator"))
                .string("material", getAllMaterials().toArray(new String[0]));
        return command.executor((sender, args) -> {
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
        });
    }

    public static Command getSaveCommand() {
        Command command = new Command(cmd, aliases).literal("save");
        return command.executor((sender, args) -> {
            if (!(sender.hasPermission("moddedadditions.areatool.save"))) {
                sendPermissionDenied(sender);
                return;
            }
            instance.getAreaToolController().saveConfiguration();
            sender.sendMessage(instance.getLanguageManager().generateMessage("vm-configSaved"));
        });
    }

    public static Command getReloadCommand() {
        Command command = new Command(cmd, aliases).literal("reload");
        return command.executor((sender, args) -> {
            if (!(sender.hasPermission("moddedadditions.areatool.reload"))) {
                sendPermissionDenied(sender);
                return;
            }
            instance.getAreaToolController().loadFromConfiguration();
            sender.sendMessage(instance.getLanguageManager().generateMessage("vm-configReloaded"));
        });
    }

    public static void registerCommands() {
        getAddCommand().register();
        getRemoveCommand().register();
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

}
