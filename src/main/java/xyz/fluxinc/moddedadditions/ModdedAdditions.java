package xyz.fluxinc.moddedadditions;

import org.bukkit.ChatColor;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.fluxinc.fluxcore.FluxCore;
import xyz.fluxinc.fluxcore.configuration.ConfigurationManager;
import xyz.fluxinc.fluxcore.configuration.LanguageManager;
import xyz.fluxinc.fluxcore.security.BlockAccessController;
import xyz.fluxinc.moddedadditions.commands.NotifyCommand;
import xyz.fluxinc.moddedadditions.controllers.AreaToolController;
import xyz.fluxinc.moddedadditions.controllers.MagnetInstanceController;
import xyz.fluxinc.moddedadditions.controllers.VeinMinerController;
import xyz.fluxinc.moddedadditions.listeners.*;
import xyz.fluxinc.moddedadditions.utils.CustomRecipeUtils;
import xyz.fluxinc.moddedadditions.utils.MagnetUtils;

public final class ModdedAdditions extends JavaPlugin {

    private MagnetInstanceController magnetInstanceController;
    private LanguageManager<ModdedAdditions> languageManager;
    private ConfigurationManager<ModdedAdditions> configurationManager;
    private FluxCore fluxCore;
    private BlockAccessController blockAccessController;
    private VeinMinerController veinMinerController;
    private AreaToolController areaToolController;
    private MagnetUtils magnetUtils;

    @Override
    public void onEnable() {
        // Register Core Plugin
        fluxCore = (FluxCore)getServer().getPluginManager().getPlugin("FluxCore");
        if (fluxCore == null) { getLogger().severe("FluxCore not initialised, disabling!"); getServer().getPluginManager().disablePlugin(this); return; }

        // Register Language and Configuration Managers
        languageManager = new LanguageManager<>(this, "lang.yml");
        configurationManager = new ConfigurationManager<>(this, "config.yml");

        // Register Core Utilities
        blockAccessController = fluxCore.getBlockAccessController();

        // Register Crop Harvesting
        getServer().getPluginManager().registerEvents(new CropHarvestListener(this, configurationManager.getConfig().getInt("ch-vmblocks")), this);

        // Register Ping Listener and Command
        getServer().getPluginManager().registerEvents(new PingListener(), this);
        getCommand("notify").setExecutor(new NotifyCommand(languageManager));

        // Register Crafting Additions
        getServer().getPluginManager().registerEvents(new CustomRecipeUtils(this), this);

        // Register Colored Anvil Names
        getServer().getPluginManager().registerEvents(new AnvilListener(), this);

        // Setup VeinMiner Related Tasks
        veinMinerController = new VeinMinerController(this);
        getServer().getPluginManager().registerEvents(new VeinMinerListener(this), this);

        // Setup Hammer/Excavator Related Tasks
        areaToolController = new AreaToolController(this);
        getServer().getPluginManager().registerEvents(new HammerListener(this, languageManager.getConfig().getString("mi-hammer")), this);
        getServer().getPluginManager().registerEvents(new ExcavatorListener(this, languageManager.getConfig().getString("mi-excavator")), this);


        // Setup Magnet Related Tasks
        magnetUtils = new MagnetUtils(ChatColor.translateAlternateColorCodes('&', languageManager.getConfig().getString("mi-magnet")));
        magnetInstanceController = new MagnetInstanceController(this, getServer().getScheduler());
        getServer().getPluginManager().registerEvents(new MagnetListener(this), this);
        getLogger().warning("Finished Initialization");
    }

    @Override
    public void onDisable() { }

    public MagnetInstanceController getMagnetInstanceController() { return magnetInstanceController;}

    public FluxCore getCoreInstance() { return fluxCore; }

    public LanguageManager getLanguageManager() { return this.languageManager; }

    public ConfigurationManager getConfigurationManager() { return this.configurationManager; }

    public BlockAccessController getBlockAccessController() { return this.blockAccessController; }

    public VeinMinerController getVeinMinerController() { return this.veinMinerController; }

    public MagnetUtils getMagnetUtils() { return magnetUtils; }

    public AreaToolController getAreaToolController() { return areaToolController; }
}
