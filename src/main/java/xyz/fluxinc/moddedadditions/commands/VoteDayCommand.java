package xyz.fluxinc.moddedadditions.commands;

import net.md_5.bungee.api.chat.ClickEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.w3c.dom.Text;
import xyz.fluxinc.fluxcore.configuration.LanguageManager;

import net.md_5.bungee.api.chat.TextComponent;
import xyz.fluxinc.moddedadditions.ModdedAdditions;

import java.util.ArrayList;
import java.util.List;

public class VoteDayCommand implements CommandExecutor {

    private ModdedAdditions instance;
    private DayVote activeVote = null;
    private LanguageManager languageManager;
    private World dayWorld;
    private int taskId = -1;

    public VoteDayCommand(ModdedAdditions instance, LanguageManager languageManager, World world) {
        this.languageManager = languageManager;
        this.dayWorld = world;
        this.instance = instance;
    }


    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String alias, String[] arguments) {
        if (arguments.length < 1) {
            initiateVote();
            if (commandSender instanceof Player) { activeVote.votedPlayers.add((Player) commandSender); }
            checkVote();
            return true;
        }
        if (activeVote == null) { commandSender.sendMessage(languageManager.generateMessage("dv-noVoteActive")); return true; }
        if (commandSender instanceof Player && activeVote.votedPlayers.contains(commandSender)) {
            commandSender.sendMessage(languageManager.generateMessage("dv-alreadyVoted"));
            return true;
        }
        switch (arguments[0].toLowerCase()) {
            case "yes":
                activeVote.yesVotes++;
                if (commandSender instanceof Player) { activeVote.votedPlayers.add((Player) commandSender); }
                checkVote();
                return true;
            case "no":
                activeVote.noVotes++;
                if (commandSender instanceof Player) { activeVote.votedPlayers.add((Player) commandSender); }
                checkVote();
                return true;
            default:
                commandSender.sendMessage(languageManager.generateMessage("dv-unknownOption"));
        }
        return true;
    }

    private void initiateVote() {
        activeVote = new DayVote();
        TextComponent mainComponent = getVoteComponent();
        for (Player player : instance.getServer().getOnlinePlayers()) { player.spigot().sendMessage(mainComponent); }
        taskId = instance.getServer().getScheduler().scheduleSyncRepeatingTask(instance, () -> {
            if (dayWorld.getTime() > 1000 && dayWorld.getTime() < 13000) {
                activeVote = null;
                for (Player player : instance.getServer().getOnlinePlayers()) { player.sendMessage(languageManager.generateMessage("dv-voteCancelled")); }
                if (taskId != -1) { instance.getServer().getScheduler().cancelTask(taskId); }
            }
        }, 1000L, 1000L);
    }

    private void checkVote() {
        long playerCount = instance.getServer().getOnlinePlayers().size();
        if (activeVote.yesVotes >= playerCount/2L && (playerCount < 2 || activeVote.yesVotes >= 2)) {
            dayWorld.setTime(1000);
            for (Player player : instance.getServer().getOnlinePlayers()) { player.sendMessage(languageManager.generateMessage("dv-voteSuccessful")); }
        } else if (activeVote.noVotes >= playerCount/2L) {
            for (Player player : instance.getServer().getOnlinePlayers()) { player.sendMessage(languageManager.generateMessage("dv-voteFailed")); }
        }
        activeVote = null;
        if (taskId != -1) { instance.getServer().getScheduler().cancelTask(taskId); }
    }

    private TextComponent getVoteComponent() {
        String message = languageManager.generateMessage("dv-dayVote");
        String[] messageArr = message.split("\\$");
        TextComponent mainComponent = new TextComponent(messageArr[0]);
        TextComponent yesComponent = new TextComponent(TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&', languageManager.getKey("dv-voteYes"))));
        TextComponent noComponent = new TextComponent(TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&', languageManager.getKey("dv-voteNo"))));

        yesComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/dayvote yes"));
        noComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/dayvote no"));

        mainComponent.addExtra(yesComponent);
        mainComponent.addExtra(new TextComponent(TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&', messageArr[1]))));
        mainComponent.addExtra(noComponent);
        return mainComponent;
    }

    private static class DayVote {
        private int yesVotes;
        private int noVotes;
        private List<Player> votedPlayers;
        public DayVote() { yesVotes = 0; noVotes = 0; votedPlayers = new ArrayList<>(); }
    }
}
