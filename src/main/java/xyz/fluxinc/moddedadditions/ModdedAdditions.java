package xyz.fluxinc.moddedadditions;

import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.fluxinc.moddedadditions.controllers.MagnetInstanceController;
import xyz.fluxinc.moddedadditions.listeners.MagnetListener;
import xyz.fluxinc.moddedadditions.utils.CustomRecipeUtils;

/* import xyz.fluxinc.fluxcore.configuration.LanguageManager; */

public final class ModdedAdditions extends JavaPlugin {

    private MagnetInstanceController magnetInstanceController;

    @Override
    public void onEnable() {
        //LanguageManager<ModdedAdditions> languageManager = new LanguageManager<>(this, "lang.yml");

        /*
            Register Crafting Additions
         */
        getServer().getPluginManager().registerEvents(new CustomRecipeUtils(this), this);

        /*
            Setup Magnet Related Tasks
         */
        magnetInstanceController = new MagnetInstanceController(this, getServer().getScheduler());
        getServer().getPluginManager().registerEvents(new MagnetListener(this), this);
    }

    @Override
    public void onDisable() {
        HandlerList.unregisterAll(this);
    }

    public MagnetInstanceController getMagnetInstanceController() { return magnetInstanceController;}


}
