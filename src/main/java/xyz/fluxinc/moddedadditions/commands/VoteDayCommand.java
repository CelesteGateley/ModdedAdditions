package xyz.fluxinc.moddedadditions.commands;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;
import xyz.fluxinc.moddedadditions.ModdedAdditions;

import java.util.ArrayList;
import java.util.List;

public class VoteDayCommand implements CommandExecutor, Listener {

    private ModdedAdditions instance;
    private DayVote activeVote = null;
    private World dayWorld;
    private int taskId = -1;

    public VoteDayCommand(ModdedAdditions instance, World world) {
        this.dayWorld = world;
        this.instance = instance;
    }


    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String alias, String[] arguments) {
        if (arguments.length < 1) {
            if (dayWorld.getTime() > 1000 && dayWorld.getTime() < 13000) { commandSender.sendMessage(instance.getLanguageManager().generateMessage("dv-alreadyDay")); return true; }
            initiateVote();
            if (commandSender instanceof Player) { activeVote.votedPlayers.add((Player) commandSender); activeVote.yesVotes++; }
            checkVote();
            return true;
        }
        if (activeVote == null) { commandSender.sendMessage(instance.getLanguageManager().generateMessage("dv-noVoteActive")); return true; }
        if (commandSender instanceof Player && activeVote.votedPlayers.contains(commandSender)) {
            commandSender.sendMessage(instance.getLanguageManager().generateMessage("dv-alreadyVoted"));
            return true;
        }
        switch (arguments[0].toLowerCase()) {
            case "yes":
                activeVote.yesVotes++;
                if (commandSender instanceof Player) { activeVote.votedPlayers.add((Player) commandSender); }
                checkVote();
                commandSender.sendMessage(instance.getLanguageManager().generateMessage("dv-voteRegistered"));
                return true;
            case "no":
                activeVote.noVotes++;
                if (commandSender instanceof Player) { activeVote.votedPlayers.add((Player) commandSender); }
                commandSender.sendMessage(instance.getLanguageManager().generateMessage("dv-voteRegistered"));
                checkVote();
                return true;
            default:
                commandSender.sendMessage(instance.getLanguageManager().generateMessage("dv-unknownOption"));
        }
        return true;
    }

    @EventHandler
    public void onSleepEvent(PlayerBedEnterEvent event) {
        if (dayWorld.getTime() > 1000 && dayWorld.getTime() < 13000) { return; }
        if (activeVote == null) { initiateVote(); }
        activeVote.yesVotes++;
        activeVote.votedPlayers.add(event.getPlayer());
    }

    private void initiateVote() {
        activeVote = new DayVote();
        TextComponent mainComponent = getVoteComponent();
        for (Player player : instance.getServer().getOnlinePlayers()) { player.spigot().sendMessage(mainComponent); }
        taskId = instance.getServer().getScheduler().scheduleSyncRepeatingTask(instance, () -> {
            if (dayWorld.getTime() > 1000 && dayWorld.getTime() < 13000 && activeVote != null) {
                activeVote = null;
                for (Player player : instance.getServer().getOnlinePlayers()) { player.sendMessage(instance.getLanguageManager().generateMessage("dv-voteCancelled")); }
                if (taskId != -1) { instance.getServer().getScheduler().cancelTask(taskId); }
            }
        }, 100L, 100L);
    }

    private void checkVote() {
        long playerCount = instance.getServer().getOnlinePlayers().size();
        if (activeVote.yesVotes >= playerCount/2L && (playerCount < 2 || activeVote.yesVotes >= 2)) {
            dayWorld.setTime(1000);
            for (Player player : instance.getServer().getOnlinePlayers()) { player.sendMessage(instance.getLanguageManager().generateMessage("dv-voteSuccessful")); }
            activeVote = null;
            if (taskId != -1) { instance.getServer().getScheduler().cancelTask(taskId); }
        } else if (activeVote.noVotes >= playerCount/2L ) {
            for (Player player : instance.getServer().getOnlinePlayers()) { player.sendMessage(instance.getLanguageManager().generateMessage("dv-voteFailed")); }
            activeVote = null;
            if (taskId != -1) { instance.getServer().getScheduler().cancelTask(taskId); }
        }

    }

    private TextComponent getVoteComponent() {
        String message = instance.getLanguageManager().generateMessage("dv-dayVote");
        String[] messageArr = message.split("\\$");
        TextComponent mainComponent = new TextComponent(messageArr[0]);
        TextComponent yesComponent = new TextComponent(TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&', instance.getLanguageManager().getFormattedString("dv-voteYes"))));
        TextComponent noComponent = new TextComponent(TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&', instance.getLanguageManager().getFormattedString("dv-voteNo"))));

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
