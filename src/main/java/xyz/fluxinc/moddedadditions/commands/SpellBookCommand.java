package xyz.fluxinc.moddedadditions.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import xyz.fluxinc.moddedadditions.ModdedAdditions;

import java.util.HashMap;
import java.util.Map;

public class SpellBookCommand implements CommandExecutor {

    private ModdedAdditions instance;

    public SpellBookCommand(ModdedAdditions instance) {
        this.instance = instance;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length < 1) { sendNoSubCommand(sender); return true;}
        Player target;
        if (args.length == 3) {
            Player player = instance.getServer().getPlayer(args[2]);
            if (player == null) { sendUnknownPlayer(sender, args[2]); return true; }
            target = player;
        } else {
            if (!(sender instanceof Player)) { sendMustBePlayer(sender); return true;}
            target = (Player) sender;
        }
        switch (args[0]) {
            case "learn":
                if (args.length < 2) { sendNoSpellProvided(sender); return true; }
                setSpell(target, args[1], true);
                return true;
            case "unlearn":
                if (args.length < 2) { sendNoSpellProvided(sender); return true; }
                setSpell(target, args[1], false);
                return true;
            case "fillmana":
                if (args.length == 2) {
                    Player player = instance.getServer().getPlayer(args[1]);
                    if (player == null) { sendUnknownPlayer(sender, args[1]); return true; }
                    target = player;
                }
                instance.getManaController().regenerateMana(target,
                        instance.getManaController().getMaximumMana(target) - instance.getManaController().getMana(target));
                return true;
            default:
                sendUnknownSubCommand(sender, args[0]);
                return true;
        }
    }

    private void setSpell(Player player, String spell, boolean value) {
        switch (spell) {
            case "fireball": spell = "Fireball"; break;
            case "teleport": spell = "Teleport"; break;
            case "arrows":   spell = "Shoot Arrows"; break;
            case "heal":     spell = "Heal"; break;
            default:         sendInvalidSpell(player, spell); return;
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
