package xyz.fluxinc.moddedadditions.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import xyz.fluxinc.fluxcore.enums.ToolLevel;
import xyz.fluxinc.moddedadditions.enums.SaberColor;
import xyz.fluxinc.moddedadditions.storage.PlayerData;

import java.util.HashMap;
import java.util.Map;

import static xyz.fluxinc.moddedadditions.ModdedAdditions.instance;
import static xyz.fluxinc.moddedadditions.utils.SpecialArmorUtils.*;

public class ModdedAdditionsCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String alias, String[] arguments) {
        if (arguments.length < 1) {
            sendNoSubCommand(commandSender);
            return true;
        }
        switch (arguments[0].toLowerCase()) {
            case "sort":
                if (commandSender instanceof Player && !commandSender.hasPermission("moddedadditions.sort")) {
                    sendPermissionDenied(commandSender);
                    return true;
                }
                if (!(commandSender instanceof Player)) {
                    sendMustBePlayer(commandSender);
                    return true;
                }
                PlayerData data = instance.getPlayerDataController().getPlayerData((Player) commandSender);
                data.toggleSortChests();
                sendSortInventory((Player) commandSender, data.sortChests());
                instance.getPlayerDataController().setPlayerData((Player) commandSender, data);
                return true;
            case "give":
                if (commandSender instanceof Player && !commandSender.hasPermission("moddedadditions.give")) {
                    sendPermissionDenied(commandSender);
                    return true;
                }
                if (arguments.length < 2) {
                    sendNoItemProvided(commandSender);
                    return true;
                }
                if (!(commandSender instanceof Player)) {
                    sendMustBePlayer(commandSender);
                    return true;
                }
                if (arguments.length < 3) {
                    giveItem(commandSender, arguments[1], null, (Player) commandSender);
                } else {
                    giveItem(commandSender, arguments[1], arguments[2], (Player) commandSender);
                }
                return true;
            case "giveother":
                if (commandSender instanceof Player && !commandSender.hasPermission("moddedadditions.give.others")) {
                    sendPermissionDenied(commandSender);
                    return true;
                }
                if (arguments.length < 2) {
                    sendNoPlayerProvided(commandSender);
                    return true;
                }
                Player recipient = instance.getServer().getPlayer(arguments[1]);
                if (recipient == null) {
                    sendUnknownPlayer(commandSender, arguments[1]);
                    return true;
                }

                if (arguments.length < 3) {
                    sendNoItemProvided(commandSender);
                }
                if (arguments[2].equals("magnet")) {
                    giveItem(commandSender, "magnet", "", recipient);
                } else {
                    if (arguments.length < 4) {
                        giveItem(commandSender, arguments[2], null, recipient);
                        return true;
                    }
                    giveItem(commandSender, arguments[2], arguments[3], recipient);
                }
                return true;
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

    private void sendSortInventory(Player player, boolean sortChests) {
        if (sortChests) {
            player.sendMessage(instance.getLanguageManager().generateMessage("ma-sortOn"));
        } else {
            player.sendMessage(instance.getLanguageManager().generateMessage("ma-sortOff"));
        }
    }

    private void giveItem(CommandSender sender, String item, String type, Player player) {
        switch (item) {
            case "hammer":
                if (type == null) {
                    sendNoTypeProvided(sender);
                    break;
                }
                switch (type) {
                    case "wood":
                        player.getInventory().addItem(instance.getAreaToolController().generateHammer(ToolLevel.WOODEN));
                        break;
                    case "stone":
                        player.getInventory().addItem(instance.getAreaToolController().generateHammer(ToolLevel.STONE));
                        break;
                    case "iron":
                        player.getInventory().addItem(instance.getAreaToolController().generateHammer(ToolLevel.IRON));
                        break;
                    case "gold":
                        player.getInventory().addItem(instance.getAreaToolController().generateHammer(ToolLevel.GOLD));
                        break;
                    case "diamond":
                        player.getInventory().addItem(instance.getAreaToolController().generateHammer(ToolLevel.DIAMOND));
                        break;
                    default:
                        sendInvalidType(sender, type);
                        break;
                }
                break;
            case "excavator":
                if (type == null) {
                    sendNoTypeProvided(sender);
                    break;
                }
                switch (type) {
                    case "wood":
                        player.getInventory().addItem(instance.getAreaToolController().generateExcavator(ToolLevel.WOODEN));
                        break;
                    case "stone":
                        player.getInventory().addItem(instance.getAreaToolController().generateExcavator(ToolLevel.STONE));
                        break;
                    case "iron":
                        player.getInventory().addItem(instance.getAreaToolController().generateExcavator(ToolLevel.IRON));
                        break;
                    case "gold":
                        player.getInventory().addItem(instance.getAreaToolController().generateExcavator(ToolLevel.GOLD));
                        break;
                    case "diamond":
                        player.getInventory().addItem(instance.getAreaToolController().generateExcavator(ToolLevel.DIAMOND));
                        break;
                    default:
                        sendInvalidType(sender, type);
                        break;
                }
                break;
            case "magnet":
                player.getInventory().addItem(instance.getMagnetController().generateNewMagnet());
                break;
            case "sonic":
                player.getInventory().addItem(instance.getSonicScrewdriverController().generateNewSonic());
                break;
            case "spellbook":
                player.getInventory().addItem(instance.getSpellBookController().generateNewSpellBook());
                break;
            case "lightsaber":
                if (type == null) {
                    sendNoColorProvided(sender);
                    break;
                }
                switch (type) {
                    case "blue":
                        player.getInventory().addItem(instance.getLightSaberController().generateNewLightSaber(SaberColor.BLUE));
                        break;
                    case "green":
                        player.getInventory().addItem(instance.getLightSaberController().generateNewLightSaber(SaberColor.GREEN));
                        break;
                    case "purple":
                        player.getInventory().addItem(instance.getLightSaberController().generateNewLightSaber(SaberColor.PURPLE));
                        break;
                    case "red":
                        player.getInventory().addItem(instance.getLightSaberController().generateNewLightSaber(SaberColor.RED));
                        break;
                    case "yellow":
                        player.getInventory().addItem(instance.getLightSaberController().generateNewLightSaber(SaberColor.YELLOW));
                        break;
                    case "orange":
                        player.getInventory().addItem(instance.getLightSaberController().generateNewLightSaber(SaberColor.ORANGE));
                        break;
                    case "white":
                        player.getInventory().addItem(instance.getLightSaberController().generateNewLightSaber(SaberColor.WHITE));
                        break;
                    case "dark":
                        player.getInventory().addItem(instance.getLightSaberController().generateNewLightSaber(SaberColor.DARK));
                        break;
                    default:
                        sendInvalidColor(sender, type);
                }
                break;
            case "kybercrystal":
                if (type == null) {
                    sendNoColorProvided(sender);
                    break;
                }
                switch (type) {
                    case "blue":
                        player.getInventory().addItem(instance.getLightSaberController().generateNewKyberCrystal(SaberColor.BLUE));
                        break;
                    case "green":
                        player.getInventory().addItem(instance.getLightSaberController().generateNewKyberCrystal(SaberColor.GREEN));
                        break;
                    case "purple":
                        player.getInventory().addItem(instance.getLightSaberController().generateNewKyberCrystal(SaberColor.PURPLE));
                        break;
                    case "red":
                        player.getInventory().addItem(instance.getLightSaberController().generateNewKyberCrystal(SaberColor.RED));
                        break;
                    case "yellow":
                        player.getInventory().addItem(instance.getLightSaberController().generateNewKyberCrystal(SaberColor.YELLOW));
                        break;
                    case "orange":
                        player.getInventory().addItem(instance.getLightSaberController().generateNewKyberCrystal(SaberColor.ORANGE));
                        break;
                    case "white":
                        player.getInventory().addItem(instance.getLightSaberController().generateNewKyberCrystal(SaberColor.WHITE));
                        break;
                    case "dark":
                        player.getInventory().addItem(instance.getLightSaberController().generateNewKyberCrystal(SaberColor.DARK));
                        break;
                    default:
                        sendInvalidColor(sender, type);
                }
                break;
            case "longfallboots":
                player.getInventory().addItem(generateNewLongFallBoots());
            case "honeychestplate":
                player.getInventory().addItem(generateHoneyChestplate());
            case "slimechestplate":
                player.getInventory().addItem(generateSlimeChestplate());
            default:
                sendInvalidItem(sender, item);
                break;
        }
    }

    private void sendUnknownPlayer(CommandSender sender, String player) {
        Map<String, String> messageArgs = new HashMap<>();
        messageArgs.put("player", player);
        sender.sendMessage(instance.getLanguageManager().generateMessage("ma-unrecognisedPlayer", messageArgs));
    }

    private void sendNoPlayerProvided(CommandSender sender) {
        sender.sendMessage(instance.getLanguageManager().generateMessage("ma-noPlayerProvided"));
    }

    private void sendNoTypeProvided(CommandSender sender) {
        sender.sendMessage(instance.getLanguageManager().generateMessage("ma-noTypeTool"));
    }

    private void sendNoColorProvided(CommandSender sender) {
        sender.sendMessage(instance.getLanguageManager().generateMessage("ma-noTypeSaber"));
    }

    private void sendInvalidColor(CommandSender sender, String type) {
        Map<String, String> messageArgs = new HashMap<>();
        messageArgs.put("type", type);
        sender.sendMessage(instance.getLanguageManager().generateMessage("ma-unknownTypeSaber", messageArgs));
    }

    private void sendInvalidType(CommandSender sender, String type) {
        Map<String, String> messageArgs = new HashMap<>();
        messageArgs.put("type", type);
        sender.sendMessage(instance.getLanguageManager().generateMessage("ma-unknownTypeTool", messageArgs));
    }

    private void sendNoSubCommand(CommandSender sender) {
        sender.sendMessage(instance.getLanguageManager().generateMessage("ma-noSubCommand"));
    }

    private void sendNoItemProvided(CommandSender sender) {
        sender.sendMessage(instance.getLanguageManager().generateMessage("ma-noItemProvided"));
    }

    private void sendInvalidItem(CommandSender sender, String item) {
        Map<String, String> messageArgs = new HashMap<>();
        messageArgs.put("item", item);
        sender.sendMessage(instance.getLanguageManager().generateMessage("ma-unknownItem", messageArgs));
    }

    private void sendUnknownSubCommand(CommandSender sender, String subcommand) {
        Map<String, String> messageArgs = new HashMap<>();
        messageArgs.put("command", subcommand);
        sender.sendMessage(instance.getLanguageManager().generateMessage("ma-unknownSubCommand", messageArgs));
    }

    private void sendPermissionDenied(CommandSender sender) {
        sender.sendMessage(instance.getLanguageManager().generateMessage("permissionDenied"));
    }

    private void sendMustBePlayer(CommandSender sender) {
        sender.sendMessage(instance.getLanguageManager().generateMessage("mustBePlayer"));
    }
}
