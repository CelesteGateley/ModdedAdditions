package xyz.fluxinc.moddedadditions.common.commands;

import dev.jorel.commandapi.CommandAPI;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import xyz.fluxinc.fluxcore.command.Command;
import xyz.fluxinc.moddedadditions.common.ItemRegistry;
import xyz.fluxinc.moddedadditions.common.storage.PlayerData;

import java.util.*;

import static xyz.fluxinc.moddedadditions.ModdedAdditions.instance;

public class ModdedAdditionsCommand {

    private static final String cmd = "moddedadditions";
    private static final String[] aliases = {"ma", "madditions", "moddeda", "mad"};

    public static Command getGiveCommand(){
        Command command = new Command(cmd, aliases)
                .literal("give")
                .players("player")
                .string("item", ItemRegistry.getAllItemNames().toArray(new String[]{}));
        return command.executor((sender, args) -> {
            if (!(sender.hasPermission("moddedadditions.give"))) {
                sendPermissionDenied(sender);
                return;
            }
            Collection<Player> targets = new ArrayList<>();
            if (args.get(0) == null && sender instanceof Player) {
                targets.add((Player) sender);
            } else if (args.get(0) == null) {
                CommandAPI.failWithString("Sender must be a player");
            } else if (args.get(0) instanceof List) {
                targets = (Collection<Player>) args.get(0);
            } else {
                CommandAPI.failWithString("Invalid List of Players");
            }

            ItemStack itemStack = ItemRegistry.getItem((String) args.get(1));
            if (itemStack == null) {
                sendInvalidItem(sender, (String) args.get(1));
            } else {
                for (Player target : targets) {
                    target.getInventory().addItem(itemStack);
                }
            }
        });
    }

    public static Command getSortCommand() {
        Command command = new Command(cmd, aliases).literal("sort");
        return command.executor((sender, args) -> {
            if (!(sender.hasPermission("moddedadditions.sort"))) {
                sendPermissionDenied(sender);
                return;
            }
            if (!(sender instanceof Player)) {
                sendMustBePlayer(sender);
                return;
            }
            PlayerData data = instance.getPlayerDataController().getPlayerData((Player) sender);
            data.toggleSortChests();
            sendSortInventory((Player) sender, data.sortChests());
            instance.getPlayerDataController().setPlayerData((Player) sender, data);
        });
    }

    public static Command getDumpCommand() {
        Command command = new Command(cmd, aliases).literal("dump");
        return command.executor((sender, args) -> {
            if (!(sender.hasPermission("moddedadditions.dump"))) {
                sendPermissionDenied(sender);
                return;
            }
            boolean success = ItemRegistry.dumpToFile("items.txt");
            if (success) {
                sender.sendMessage(instance.getLanguageManager().generateMessage("ma-dumpSuccessful"));
            } else {
                sender.sendMessage(instance.getLanguageManager().generateMessage("ma-dumpFailed"));
            }
        });
    }

    public static Command getReloadCommand() {
        Command command = new Command(cmd, aliases).literal("reload");
        return command.executor((sender, args) -> {
            if (!(sender.hasPermission("moddedadditions.reload"))) {
                sendPermissionDenied(sender);
                return;
            }
            instance.reloadConfiguration();
            sender.sendMessage(instance.getLanguageManager().generateMessage("configurationReloaded"));
        });
    }

    public static void registerCommands() {
        getGiveCommand().register();
        getSortCommand().register();
        getDumpCommand().register();
        getReloadCommand().register();
    }

    private static void sendPermissionDenied(CommandSender sender) {
        sender.sendMessage(instance.getLanguageManager().generateMessage("permissionDenied"));
    }

    private static void sendInvalidItem(CommandSender sender, String item) {
        Map<String, String> messageArgs = new HashMap<>();
        messageArgs.put("item", item);
        sender.sendMessage(instance.getLanguageManager().generateMessage("ma-unknownItem", messageArgs));
    }

    private static void sendMustBePlayer(CommandSender sender) {
        sender.sendMessage(instance.getLanguageManager().generateMessage("mustBePlayer"));
    }

    private static void sendSortInventory(Player player, boolean sortChests) {
        if (sortChests) {
            player.sendMessage(instance.getLanguageManager().generateMessage("ma-sortOn"));
        } else {
            player.sendMessage(instance.getLanguageManager().generateMessage("ma-sortOff"));
        }
    }

}
