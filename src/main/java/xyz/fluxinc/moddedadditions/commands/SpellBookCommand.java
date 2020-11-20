package xyz.fluxinc.moddedadditions.commands;

import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.Argument;
import dev.jorel.commandapi.arguments.EntitySelectorArgument;
import dev.jorel.commandapi.arguments.LiteralArgument;
import dev.jorel.commandapi.arguments.StringArgument;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import xyz.fluxinc.moddedadditions.spells.Spell;
import xyz.fluxinc.moddedadditions.storage.ExecutorStorage;
import xyz.fluxinc.moddedadditions.storage.PlayerData;
import xyz.fluxinc.moddedadditions.utils.registries.SpellRegistry;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static xyz.fluxinc.moddedadditions.ModdedAdditions.instance;

public class SpellBookCommand {

    private static final int LEARN_SPELL_LEVEL = 1;

    private static List<String> getSpellList() {
        List<String> spells = new ArrayList<>();
        spells.add("all");
        for (Spell spell : SpellRegistry.getAllSpells()) {
            spells.add(spell.getTechnicalName());
        }
        return spells;
    }

    private static HashMap<String, ExecutorStorage> getCommands() {
        HashMap<String, ExecutorStorage> returnVal = new HashMap<>();
        // Learn Command
        List<Argument> arguments = new ArrayList<>();
        arguments.add(new LiteralArgument("learn"));
        arguments.add(new EntitySelectorArgument("player", EntitySelectorArgument.EntitySelector.MANY_PLAYERS));
        arguments.add(new StringArgument("spell").overrideSuggestions(getSpellList().toArray(new String[arguments.size()])));
        returnVal.put("learn", new ExecutorStorage((sender, args) -> {
            if (!sender.hasPermission("moddedadditions.spells.learn")) {
                sendPermissionDenied(sender);
                return;
            }
            Collection<Player> targets = new ArrayList<>();
            if (args[0] == null && sender instanceof Player) {
                targets.add((Player) sender);
            } else if (args[0] == null) {
                CommandAPI.fail("Sender must be a player");
            } else if (args[0] instanceof List) {
                targets = (Collection<Player>) args[0];
            } else {
                CommandAPI.fail("Invalid List of Players");
            }
            if (args[1].equals("all")) {
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
                    playerData.setSpell((String) args[1], LEARN_SPELL_LEVEL);
                    sendLearnSpell(sender, (String) args[1]);
                    instance.getPlayerDataController().setPlayerData(player, playerData);
                }
            }
        }, arguments));

        // Unlearn Command
        arguments = new ArrayList<>();
        arguments.add(new LiteralArgument("unlearn"));
        arguments.add(new EntitySelectorArgument("player", EntitySelectorArgument.EntitySelector.MANY_PLAYERS));
        arguments.add(new StringArgument("spell").overrideSuggestions(getSpellList().toArray(new String[arguments.size()])));
        returnVal.put("unlearn", new ExecutorStorage((sender, args) -> {
            if (!sender.hasPermission("moddedadditions.spells.unlearn")) {
                sendPermissionDenied(sender);
                return;
            }
            Collection<Player> targets = new ArrayList<>();
            if (args[0] == null && sender instanceof Player) {
                targets.add((Player) sender);
            } else if (args[0] == null) {
                CommandAPI.fail("Sender must be a player");
            } else if (args[0] instanceof List) {
                targets = (Collection<Player>) args[0];
            } else {
                CommandAPI.fail("Invalid List of Players");
            }
            if (args[1].equals("all")) {
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
                    playerData.setSpell((String) args[1], 0);
                    sendUnlearnSpell(sender, (String) args[1]);
                    instance.getPlayerDataController().setPlayerData(player, playerData);
                }
            }
        }, arguments));

        // FillMana
        arguments = new ArrayList<>();
        arguments.add(new LiteralArgument("fillmana"));
        arguments.add(new EntitySelectorArgument("player", EntitySelectorArgument.EntitySelector.MANY_PLAYERS));
        returnVal.put("fillmana", new ExecutorStorage((sender, args) -> {
            if (!sender.hasPermission("moddedadditions.spells.fillmana")) {
                sendPermissionDenied(sender);
                return;
            }
            Collection<Player> targets = new ArrayList<>();
            if (args[0] == null && sender instanceof Player) {
                targets.add((Player) sender);
            } else if (args[0] == null) {
                CommandAPI.fail("Sender must be a player");
            } else if (args[0] instanceof List) {
                targets = (Collection<Player>) args[0];
            } else {
                CommandAPI.fail("Invalid List of Players");
            }
            for (Player player : targets) {
                PlayerData playerData = instance.getPlayerDataController().getPlayerData(player);
                playerData.setCurrentMana(playerData.getMaximumMana());
                instance.getPlayerDataController().setPlayerData(player, playerData);
            }
        }, arguments));

        // Evaluate
        arguments = new ArrayList<>();
        arguments.add(new LiteralArgument("evaluate"));
        arguments.add(new EntitySelectorArgument("player", EntitySelectorArgument.EntitySelector.MANY_PLAYERS));
        returnVal.put("evaluate", new ExecutorStorage((sender, args) -> {
            if (!sender.hasPermission("moddedadditions.spells.evaluate")) {
                sendPermissionDenied(sender);
                return;
            }
            Collection<Player> targets = new ArrayList<>();
            if (args[0] == null && sender instanceof Player) {
                targets.add((Player) sender);
            } else if (args[0] == null) {
                CommandAPI.fail("Sender must be a player");
            } else if (args[0] instanceof List) {
                targets = (Collection<Player>) args[0];
            } else {
                CommandAPI.fail("Invalid List of Players");
            }
            for (Player player : targets) {
                PlayerData playerData = instance.getPlayerDataController().getPlayerData(player);
                playerData.upgradeSpellSystem();
                playerData.evaluateMana();
                instance.getPlayerDataController().setPlayerData(player, playerData);
            }
        }, arguments));

        // Evaluate All
        arguments = new ArrayList<>();
        arguments.add(new LiteralArgument("evaluate"));
        arguments.add(new EntitySelectorArgument("player", EntitySelectorArgument.EntitySelector.MANY_PLAYERS));
        returnVal.put("evaluate", new ExecutorStorage((sender, args) -> {
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
        }, arguments));

        return returnVal;
    }

    public static void registerCommands() {
        HashMap<String, ExecutorStorage> commands = getCommands();
        for (String key : commands.keySet()) {
            new CommandAPICommand("spellbook")
                    .withAliases("sb", "sbook", "spellb")
                    .withArguments(commands.get(key).getArguments())
                    .executes(commands.get(key).getExecutor())
                    .register();
        }
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
