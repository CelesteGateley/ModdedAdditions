package xyz.fluxinc.moddedadditions;

import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.fluxinc.moddedadditions.commands.PingCommand;
import xyz.fluxinc.moddedadditions.controllers.MagnetInstanceController;
import xyz.fluxinc.moddedadditions.listeners.AnvilListener;
import xyz.fluxinc.moddedadditions.listeners.MagnetListener;
import xyz.fluxinc.moddedadditions.listeners.PingListener;
import xyz.fluxinc.moddedadditions.utils.CustomRecipeUtils;

import xyz.fluxinc.fluxcore.configuration.LanguageManager;

public final class ModdedAdditions extends JavaPlugin {

    private MagnetInstanceController magnetInstanceController;
    private LanguageManager<ModdedAdditions> languageManager;

    @Override
    public void onEnable() {
        languageManager = new LanguageManager<>(this, "lang.yml");

        // Register Ping Listener and Command
        getServer().getPluginManager().registerEvents(new PingListener(), this);
        getCommand("ping").setExecutor(new PingCommand(languageManager));

        // Register Crafting Additions
        getServer().getPluginManager().registerEvents(new CustomRecipeUtils(this), this);

        // Register Colored Anvil Names
        getServer().getPluginManager().registerEvents(new AnvilListener(), this);

        // Setup Magnet Related Tasks
        magnetInstanceController = new MagnetInstanceController(this, getServer().getScheduler());
        getServer().getPluginManager().registerEvents(new MagnetListener(this), this);
    }

    @Override
    public void onDisable() {
        HandlerList.unregisterAll(this);
    }

    public MagnetInstanceController getMagnetInstanceController() { return magnetInstanceController;}

    public LanguageManager getLanguageManager() { return this.languageManager; }


}
