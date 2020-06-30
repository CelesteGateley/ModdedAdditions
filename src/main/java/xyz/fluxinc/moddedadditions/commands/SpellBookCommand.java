package xyz.fluxinc.moddedadditions.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import xyz.fluxinc.moddedadditions.ModdedAdditions;
import xyz.fluxinc.moddedadditions.storage.PlayerData;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SpellBookCommand implements CommandExecutor {

    private final ModdedAdditions instance;

    public SpellBookCommand(ModdedAdditions instance) {
        this.instance = instance;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length < 1) {
            sendNoSubCommand(sender);
            return true;
        }
        Player target;
        if (args.length == 3) {
            Player player = instance.getServer().getPlayer(args[2]);
            if (player == null) {
                sendUnknownPlayer(sender, args[2]);
                return true;
            }
            target = player;
        } else {
            if (!(sender instanceof Player)) {
                sendMustBePlayer(sender);
                return true;
            }
            target = (Player) sender;
        }
        switch (args[0]) {
            case "learn":
                if (!sender.hasPermission("moddedadditions.spells.learn")) { sendPermissionDenied(sender); return true; }
                if (target != sender && !sender.hasPermission("moddedadditions.spells.learn.others")) { sendPermissionDenied(sender); return true; }
                if (args.length < 2) {
                    sendNoSpellProvided(sender);
                    return true;
                }
                if (args[1].equals("*")) {
                    for (String spell : instance.getSpellBookController().getSpellRegistry().getAllTechnicalNames()) {
                        setSpell(target, spell, true);
                    }
                    return true;
                }
                setSpell(target, args[1], true);
                return true;
            case "unlearn":
                if (!sender.hasPermission("moddedadditions.spells.unlearn")) { sendPermissionDenied(sender); return true; }
                if (target != sender && !sender.hasPermission("moddedadditions.spells.unlearn.others")) { sendPermissionDenied(sender); return true; }
                if (args.length < 2) {
                    sendNoSpellProvided(sender);
                    return true;
                }
                if (args[1].equals("*")) {
                    for (String spell : instance.getSpellBookController().getSpellRegistry().getAllTechnicalNames()) {
                        setSpell(target, spell, false);
                    }
                    return true;
                }
                setSpell(target, args[1], false);
                return true;
            case "fillmana":
                if (!sender.hasPermission("moddedadditions.fillmana")) { sendPermissionDenied(sender); return true; }
                if (args.length == 2) {
                    Player player = instance.getServer().getPlayer(args[1]);
                    if (!sender.hasPermission("moddedadditions.fillmana.others")) { sendPermissionDenied(sender); return true; }
                    if (player == null) {
                        sendUnknownPlayer(sender, args[1]);
                        return true;
                    }
                    target = player;
                }
                instance.getManaController().regenerateMana(target,
                        instance.getManaController().getMaximumMana(target) - instance.getManaController().getMana(target));
                return true;
            case "evaluateall":
                if (!sender.isOp()) { sendUnknownSubCommand(sender, "evaluateall"); return true; }
                YamlConfiguration dataConfig = instance.getPlayerDataController().getConfiguration();
                for (String key : dataConfig.getKeys(false)) {
                    PlayerData data = (PlayerData) dataConfig.get(key);
                    data.evaluateMana();
                    dataConfig.set(key, data);
                }
                try {
                    dataConfig.save(new File(instance.getDataFolder(), "storage.yml"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return true;
            default:
                sendUnknownSubCommand(sender, args[0]);
                return true;
        }
    }

    private void setSpell(Player player, String spell, boolean value) {
        List<String> spells = instance.getSpellBookController().getSpellRegistry().getAllTechnicalNames();
        if (!spells.contains(spell)) {
            sendInvalidSpell(player, spell);
            return;
        }
        instance.getPlayerDataController().setPlayerData(player, instance.getPlayerDataController().getPlayerData(player).setSpell(spell, value));
    }

    private void sendNoSubCommand(CommandSender sender) {
        sender.sendMessage(instance.getLanguageManager().generateMessage("sb-noSubCommand"));
    }

    private void sendNoSpellProvided(CommandSender sender) {
        sender.sendMessage(instance.getLanguageManager().generateMessage("sb-noSpellProvided"));
    }

    private void sendInvalidSpell(CommandSender sender, String spell) {
        Map<String, String> messageArgs = new HashMap<>();
        messageArgs.put("spell", spell);
        sender.sendMessage(instance.getLanguageManager().generateMessage("sb-unknownSpell", messageArgs));
    }

    private void sendUnknownSubCommand(CommandSender sender, String subcommand) {
        Map<String, String> messageArgs = new HashMap<>();
        messageArgs.put("command", subcommand);
        sender.sendMessage(instance.getLanguageManager().generateMessage("sb-unknownSubCommand", messageArgs));
    }

    private void sendPermissionDenied(CommandSender sender) {
        sender.sendMessage(instance.getLanguageManager().generateMessage("permissionDenied"));
    }

    private void sendMustBePlayer(CommandSender sender) {
        sender.sendMessage(instance.getLanguageManager().generateMessage("mustBePlayer"));
    }

    private void sendUnknownPlayer(CommandSender sender, String player) {
        Map<String, String> messageArgs = new HashMap<>();
        messageArgs.put("player", player);
        sender.sendMessage(instance.getLanguageManager().generateMessage("ma-unrecognisedPlayer", messageArgs));
    }

}
