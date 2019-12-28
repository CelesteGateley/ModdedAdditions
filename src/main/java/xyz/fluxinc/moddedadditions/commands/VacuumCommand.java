package xyz.fluxinc.moddedadditions.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import xyz.fluxinc.moddedadditions.controllers.MagnetInstanceController;

public class VacuumCommand implements CommandExecutor {

    private MagnetInstanceController magnetInstanceController;

    public VacuumCommand(MagnetInstanceController magnetInstanceController) { this.magnetInstanceController = magnetInstanceController; }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        return false;
    }
}
