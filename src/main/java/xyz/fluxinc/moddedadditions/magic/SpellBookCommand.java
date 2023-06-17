package xyz.fluxinc.moddedadditions.magic;

import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.*;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import xyz.fluxinc.fluxcore.command.Command;
import xyz.fluxinc.moddedadditions.common.storage.ExecutorStorage;
import xyz.fluxinc.moddedadditions.common.storage.PlayerData;
import xyz.fluxinc.moddedadditions.magic.spells.Spell;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static xyz.fluxinc.moddedadditions.ModdedAdditions.instance;

public class SpellBookCommand {

    private static final int LEARN_SPELL_LEVEL = 1;

    private static final String cmd = "spellbook";
    private static final String[] aliases = {"sb", "spells",};

    private static List<String> getSpellList() {
        List<String> spells = new ArrayList<>();
        spells.add("all");
        for (Spell spell : SpellRegistry.getAllSpells()) {
            spells.add(spell.getTechnicalName());
        }
        return spells;
    }

    public static Command getLearnCommand() {
        Command command = new Command(cmd, aliases)
                .literal("learn")
                .players("player")
                .string("spell", getSpellList().toArray(new String[0]));
        return command.executor((sender, args) -> {
            if (!sender.hasPermission("moddedadditions.spells.learn")) {
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
            if (args.get(1).equals("all")) {
                for (Player player : targets) {
                    PlayerData playerData = instance.getPlayerDataController().getPlayerData(player);
                    for (String spell : getSpellList()) {
                        if (spell.equals("all")) continue;
                        playerData.setSpell(spell, LEARN_SPELL_LEVEL);
                        sendLearnSpell(sender, spell);
                    }
                    instance.getPlayerDataController().setPlayerData(player, playerData);
                }
            } else {
                for (Player player : targets) {
                    PlayerData playerData = instance.getPlayerDataController().getPlayerData(player);
                    playerData.setSpell((String) args.get(1), LEARN_SPELL_LEVEL);
                    sendLearnSpell(sender, (String) args.get(1));
                    instance.getPlayerDataController().setPlayerData(player, playerData);
                }
            }
        });
    }
    public static Command getUnlearnCommand() {
        Command command = new Command(cmd, aliases)
                .literal("unlearn")
                .players("player")
                .string("spell", getSpellList().toArray(new String[0]));
        return command.executor((sender, args) -> {
            if (!sender.hasPermission("moddedadditions.spells.unlearn")) {
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
            if (args.get(1).equals("all")) {
                for (Player player : targets) {
                    PlayerData playerData = instance.getPlayerDataController().getPlayerData(player);
                    for (String spell : getSpellList()) {
                        if (spell.equals("all")) continue;
                        playerData.setSpell(spell, 0);
                        sendUnlearnSpell(sender, spell);
                    }
                    instance.getPlayerDataController().setPlayerData(player, playerData);
                }
            } else {
                for (Player player : targets) {
                    PlayerData playerData = instance.getPlayerDataController().getPlayerData(player);
                    playerData.setSpell((String) args.get(1), 0);
                    sendUnlearnSpell(sender, (String) args.get(1));
                    instance.getPlayerDataController().setPlayerData(player, playerData);
                }
            }
        });
    }

    public static Command getFillCommand() {
        Command command = new Command(cmd, aliases)
                .literal("fillmana")
                .players("player");
        return command.executor((sender, args) -> {
            if (!sender.hasPermission("moddedadditions.spells.fillmana")) {
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
            for (Player player : targets) {
                PlayerData playerData = instance.getPlayerDataController().getPlayerData(player);
                playerData.setCurrentMana(playerData.getMaximumMana());
                instance.getPlayerDataController().setPlayerData(player, playerData);
            }
        });
    }

    public static Command getEvaluateCommand() {
        Command command = new Command(cmd, aliases)
                .literal("evaluate")
                .players("player");
        return command.executor((sender, args) -> {
            if (!sender.hasPermission("moddedadditions.spells.evaluate")) {
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
            for (Player player : targets) {
                PlayerData playerData = instance.getPlayerDataController().getPlayerData(player);
                playerData.upgradeSpellSystem();
                playerData.evaluateMana();
                instance.getPlayerDataController().setPlayerData(player, playerData);
            }
        });
    }
    public static Command getEvaluateAllCommand() {
        Command command = new Command(cmd, aliases)
                .literal("evaluate-all");
        return command.executor((sender, args) -> {
            if (!sender.hasPermission("moddedadditions.spells.evaluateall")) {
                sendPermissionDenied(sender);
                return;
            }
            YamlConfiguration dataConfig = instance.getPlayerDataController().getConfiguration();
            for (String key : dataConfig.getKeys(false)) {
                PlayerData data = (PlayerData) dataConfig.get(key);
                data.evaluateMana();
                data.upgradeSpellSystem();
                dataConfig.set(key, data);
            }
            try {
                dataConfig.save(new File(instance.getDataFolder(), "storage.yml"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
    public static void registerCommands() {
        getLearnCommand().register();
        getUnlearnCommand().register();
        getFillCommand().register();
        getEvaluateCommand().register();
        getEvaluateAllCommand().register();
    }

    private static void sendLearnSpell(CommandSender sender, String spell) {
        Map<String, String> messageArgs = new HashMap<>();
        messageArgs.put("spell", spell);
        sender.sendMessage(instance.getLanguageManager().generateMessage("sb-learnSpell", messageArgs));
    }

    private static void sendUnlearnSpell(CommandSender sender, String spell) {
        Map<String, String> messageArgs = new HashMap<>();
        messageArgs.put("spell", spell);
        sender.sendMessage(instance.getLanguageManager().generateMessage("sb-unlearnSpell", messageArgs));
    }

    private static void sendPermissionDenied(CommandSender sender) {
        sender.sendMessage(instance.getLanguageManager().generateMessage("permissionDenied"));
    }
}
