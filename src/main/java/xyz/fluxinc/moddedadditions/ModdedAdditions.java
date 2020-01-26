package xyz.fluxinc.moddedadditions;

import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.fluxinc.fluxcore.FluxCore;
import xyz.fluxinc.fluxcore.configuration.ConfigurationManager;
import xyz.fluxinc.fluxcore.configuration.LanguageManager;
import xyz.fluxinc.fluxcore.inventory.InventoryChecker;
import xyz.fluxinc.fluxcore.security.BlockAccessController;
import xyz.fluxinc.moddedadditions.commands.*;
import xyz.fluxinc.moddedadditions.controllers.AreaToolController;
import xyz.fluxinc.moddedadditions.controllers.LightSaberController;
import xyz.fluxinc.moddedadditions.controllers.MagnetController;
import xyz.fluxinc.moddedadditions.controllers.VeinMinerController;
import xyz.fluxinc.moddedadditions.executors.MagnetExecutor;
import xyz.fluxinc.moddedadditions.listeners.BookSignListener;
import xyz.fluxinc.moddedadditions.listeners.CropHarvestListener;
import xyz.fluxinc.moddedadditions.listeners.VeinMinerListener;
import xyz.fluxinc.moddedadditions.listeners.chat.PingListener;
import xyz.fluxinc.moddedadditions.listeners.chat.ResponseListener;
import xyz.fluxinc.moddedadditions.listeners.customitem.LightSaberListener;
import xyz.fluxinc.moddedadditions.listeners.customitem.SonicListener;
import xyz.fluxinc.moddedadditions.listeners.customitem.areatool.ExcavatorListener;
import xyz.fluxinc.moddedadditions.listeners.customitem.areatool.HammerListener;
import xyz.fluxinc.moddedadditions.listeners.inventory.AnvilListener;
import xyz.fluxinc.moddedadditions.utils.CustomRecipeUtils;

public final class ModdedAdditions extends JavaPlugin {

    public static final int KEY_BASE = 5120000;

    private MagnetController magnetController;
    private LanguageManager<ModdedAdditions> languageManager;
    private ConfigurationManager<ModdedAdditions> configurationManager;
    private FluxCore fluxCore;
    private BlockAccessController blockAccessController;
    private VeinMinerController veinMinerController;
    private AreaToolController areaToolController;
    private LightSaberController lightSaberController;
    private CustomRecipeUtils customRecipeUtils;

    @Override
    public void onEnable() {
        // Register Core Plugin
        fluxCore = (FluxCore) getServer().getPluginManager().getPlugin("FluxCore");
        if (fluxCore == null) {
            getLogger().severe("FluxCore not initialised, disabling!");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        // Register Language and Configuration Managers
        languageManager = new LanguageManager<>(this, "lang.yml");
        configurationManager = new ConfigurationManager<>(this, "config.yml");
        getCommand("moddedadditions").setExecutor(new ModdedAdditionsCommand(this));

        // Register Core Utilities
        blockAccessController = fluxCore.getBlockAccessController();

        // Register Crop Harvesting
        getServer().getPluginManager().registerEvents(new CropHarvestListener(this, configurationManager.getInt("ch-vmblocks")), this);

        // Register Ping Listener and Command
        getServer().getPluginManager().registerEvents(new PingListener(), this);
        getCommand("notify").setExecutor(new NotifyCommand(this));

        // Register Colored Anvil Names
        getServer().getPluginManager().registerEvents(new AnvilListener(), this);

        // Setup VeinMiner Related Tasks
        veinMinerController = new VeinMinerController(this);
        getServer().getPluginManager().registerEvents(new VeinMinerListener(this), this);
        getCommand("veinminer").setExecutor(new VeinMinerCommand(this));

        // Setup Hammer/Excavator Related Tasks
        areaToolController = new AreaToolController(this);
        getServer().getPluginManager().registerEvents(new HammerListener(this, languageManager.getString("mi-hammer")), this);
        getServer().getPluginManager().registerEvents(new ExcavatorListener(this, languageManager.getString("mi-excavator")), this);
        getCommand("areatool").setExecutor(new AreaToolCommand(this));

        // Setup Magnet Related Tasks
        magnetController = new MagnetController(this);
        getServer().getPluginManager().registerEvents(new InventoryChecker(this, new MagnetExecutor(this)), this);

        // Setup Book Handling
        getServer().getPluginManager().registerEvents(new BookSignListener(), this);

        // Setup DayVote Command
        String worldName = configurationManager.getString("dv-dayWorld");
        if (worldName != null && getServer().getWorld(worldName) != null) {
            VoteDayCommand voteDayCommand = new VoteDayCommand(this, getServer().getWorld(worldName));
            getCommand("voteday").setExecutor(voteDayCommand);
            getServer().getPluginManager().registerEvents(voteDayCommand, this);
        } else {
            getLogger().warning("No or invalid world defined for DayVote. It will not be enabled");
        }

        // Tell Player Damage Dealt
        //getServer().getPluginManager().registerEvents(new DamageListener(), this);

        // LightSaber Related Functions
        getServer().getPluginManager().registerEvents(new ResponseListener(), this);
        lightSaberController = new LightSaberController(this);
        getServer().getPluginManager().registerEvents(new LightSaberListener(this), this);


        getServer().getPluginManager().registerEvents(new SonicListener(), this);

        // Register Crafting Additions (Must Be Last)
        customRecipeUtils = new CustomRecipeUtils(this);
        getServer().getPluginManager().registerEvents(customRecipeUtils, this);
    }

    @Override
    public void onDisable() {
        magnetController = null;
        fluxCore = null;
        languageManager = null;
        configurationManager = null;
        veinMinerController = null;
        areaToolController = null;
        blockAccessController = null;
        customRecipeUtils = null;

        HandlerList.unregisterAll(this);
    }

    public void reloadConfiguration() {
        languageManager = new LanguageManager<>(this, "lang.yml");
        configurationManager = new ConfigurationManager<>(this, "config.yml");
        veinMinerController.loadFromConfiguration();
        areaToolController.loadFromConfiguration();
    }

    public MagnetController getMagnetController() { return magnetController; }

    public FluxCore getCoreInstance() { return fluxCore; }

    public LanguageManager getLanguageManager() { return this.languageManager; }

    public ConfigurationManager getConfigurationManager() { return this.configurationManager; }

    public BlockAccessController getBlockAccessController() { return this.blockAccessController; }

    public VeinMinerController getVeinMinerController() { return this.veinMinerController; }

    public AreaToolController getAreaToolController() { return areaToolController; }

    public CustomRecipeUtils getCustomRecipeUtils() { return customRecipeUtils; }

    public LightSaberController getLightSaberController() { return lightSaberController; }
}
