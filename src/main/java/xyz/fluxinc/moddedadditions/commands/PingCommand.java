package xyz.fluxinc.moddedadditions.commands;

import xyz.fluxinc.fluxcore.configuration.LanguageManager;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static org.bukkit.Bukkit.getServer;

public class PingCommand implements CommandExecutor {

    private LanguageManager languageManager;

    public PingCommand(LanguageManager languageManager) { this.languageManager = languageManager; }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 1) { sender.sendMessage(languageManager.generateMessage("invalidUsage")); return true; }
        Player target = getServer().getPlayer(args[0]);
        if (target != null) {
            target.playSound(target.getLocation(), Sound.BLOCK_BELL_USE, 50, 1);
            sender.sendMessage(replaceVar(languageManager.generateMessage("dingSender"), sender.getName(), target.getName()));
            target.sendMessage(replaceVar(languageManager.generateMessage("dingTarget"), sender.getName(), target.getName()));
            return true;
        } else {
            sender.sendMessage(languageManager.generateMessage("userNotFound"));
            return true;
        }
    }

    private String replaceVar(String message, String sender, String target) {
        message = message.replaceAll("%sender%", sender);
        message = message.replaceAll("%target%", target);
        return message;
    }
}