package xyz.fluxinc.moddedadditions.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import xyz.fluxinc.moddedadditions.ModdedAdditions;

public class VacuumCommand implements CommandExecutor {

    private ModdedAdditions instance;

    public VacuumCommand(ModdedAdditions instance) {
        this.instance = instance;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        return false;
    }
}
