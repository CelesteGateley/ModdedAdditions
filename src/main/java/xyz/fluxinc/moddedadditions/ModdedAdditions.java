package xyz.fluxinc.moddedadditions;

import org.bukkit.boss.KeyedBossBar;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.fluxinc.fluxcore.FluxCore;
import xyz.fluxinc.fluxcore.configuration.ConfigurationManager;
import xyz.fluxinc.fluxcore.configuration.LanguageManager;
import xyz.fluxinc.fluxcore.inventory.InventoryChecker;
import xyz.fluxinc.fluxcore.security.BlockAccessController;
import xyz.fluxinc.moddedadditions.commands.*;
import xyz.fluxinc.moddedadditions.controllers.ManaController;
import xyz.fluxinc.moddedadditions.controllers.PlayerDataController;
import xyz.fluxinc.moddedadditions.controllers.VeinMinerController;
import xyz.fluxinc.moddedadditions.controllers.customitems.*;
import xyz.fluxinc.moddedadditions.executors.MagnetExecutor;
import xyz.fluxinc.moddedadditions.executors.OldMagnetExecutor;
import xyz.fluxinc.moddedadditions.listeners.BookSignListener;
import xyz.fluxinc.moddedadditions.listeners.CropHarvestListener;
import xyz.fluxinc.moddedadditions.listeners.VeinMinerListener;
import xyz.fluxinc.moddedadditions.listeners.chat.PingListener;
import xyz.fluxinc.moddedadditions.listeners.chat.ResponseListener;
import xyz.fluxinc.moddedadditions.listeners.customitem.LightSaberListener;
import xyz.fluxinc.moddedadditions.listeners.customitem.SonicScrewdriverListener;
import xyz.fluxinc.moddedadditions.listeners.customitem.SpellBookListener;
import xyz.fluxinc.moddedadditions.listeners.customitem.areatool.ExcavatorListener;
import xyz.fluxinc.moddedadditions.listeners.customitem.areatool.HammerListener;
import xyz.fluxinc.moddedadditions.listeners.customitem.armor.HoneyChestplateListener;
import xyz.fluxinc.moddedadditions.listeners.customitem.armor.LongFallBootsListener;
import xyz.fluxinc.moddedadditions.listeners.customitem.armor.SlimeChestplateListener;
import xyz.fluxinc.moddedadditions.listeners.customitem.spells.ForceFieldListener;
import xyz.fluxinc.moddedadditions.listeners.inventory.AnvilListener;
import xyz.fluxinc.moddedadditions.listeners.inventory.SortChestListener;
import xyz.fluxinc.moddedadditions.storage.PlayerData;
import xyz.fluxinc.moddedadditions.utils.CustomRecipeUtils;

import java.util.ArrayList;
import java.util.List;

public final class ModdedAdditions extends JavaPlugin {

    public static final int KEY_BASE = 5120000;
    public static ModdedAdditions instance;

    private MagnetController magnetController;
    private LanguageManager<ModdedAdditions> languageManager;
    private ConfigurationManager<ModdedAdditions> configurationManager;
    private FluxCore fluxCore;
    private BlockAccessController blockAccessController;
    private VeinMinerController veinMinerController;
    private AreaToolController areaToolController;
    private LightSaberController lightSaberController;
    private CustomRecipeUtils customRecipeUtils;
    private SonicScrewdriverController sonicScrewdriverController;
    private PlayerDataController playerDataController;

    private SpellBookController spellBookController;
    private ManaController manaController;
    private ForceFieldListener forceFieldListener;

    @Override
    public void onEnable() {
        instance = this;
        // Register Core Plugin
        fluxCore = (FluxCore) getServer().getPluginManager().getPlugin("FluxCore");
        if (fluxCore == null) {
            getLogger().severe("FluxCore not initialised, disabling!");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        List<KeyedBossBar> bossBars = new ArrayList<>();
        getServer().getBossBars().forEachRemaining(bossBars::add);

        for (KeyedBossBar bossBar : bossBars) {
            if (bossBar.getKey().getNamespace().equals("moddedadditions")) {
                getServer().removeBossBar(bossBar.getKey());
            }
        }

        // Initialize PlayerData
        ConfigurationSerialization.registerClass(PlayerData.class);
        playerDataController = new PlayerDataController(this, "storage.yml");
        getServer().getPluginManager().registerEvents(playerDataController, this);

        // Register Language and Configuration Managers
        languageManager = new LanguageManager<>(this, "lang.yml");
        languageManager.verifyKeys();
        configurationManager = new ConfigurationManager<>(this, "config.yml");
        configurationManager.verifyKeys();
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
        getServer().getPluginManager().registerEvents(new InventoryChecker(this, new OldMagnetExecutor(this)), this);

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

        // Setup Chest Sorting
        getServer().getPluginManager().registerEvents(new SortChestListener(this), this);

        // Sonic Screwdriver Related Functions
        sonicScrewdriverController = new SonicScrewdriverController(this);
        getServer().getPluginManager().registerEvents(new SonicScrewdriverListener(this), this);

        // Magic Related Functionality
        manaController = new ManaController(this);
        manaController.initializeAllManaBars();
        spellBookController = new SpellBookController(this);
        getServer().getPluginManager().registerEvents(manaController, this);
        getServer().getPluginManager().registerEvents(new SpellBookListener(this), this);
        getCommand("spellbook").setExecutor(new SpellBookCommand(this));
        forceFieldListener = new ForceFieldListener(this);
        getServer().getPluginManager().registerEvents(forceFieldListener, this);

        // Tell Player Damage Dealt
        //getServer().getPluginManager().registerEvents(new DamageListener(), this);

        // LightSaber Related Functions
        getServer().getPluginManager().registerEvents(new ResponseListener(), this);
        lightSaberController = new LightSaberController(this);
        getServer().getPluginManager().registerEvents(new LightSaberListener(this), this);

        // Register Custom Armor Listeners
        getServer().getPluginManager().registerEvents(new LongFallBootsListener(), this);
        //getServer().getPluginManager().registerEvents(new HoneyChestplateListener(), this);
        //getServer().getPluginManager().registerEvents(new SlimeChestplateListener(), this);

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
        if (playerDataController != null) {
            playerDataController.saveToDisk();
        }
        playerDataController = null;
        spellBookController = null;
        manaController = null;

        HandlerList.unregisterAll(this);
    }

    public void reloadConfiguration() {
        languageManager = new LanguageManager<>(this, "lang.yml");
        configurationManager = new ConfigurationManager<>(this, "config.yml");
        playerDataController.loadConfiguration();
        veinMinerController.loadFromConfiguration();
        areaToolController.loadFromConfiguration();
    }

    public ForceFieldListener getForceFieldListener() { return forceFieldListener; }

    public MagnetController getMagnetController() {
        return magnetController;
    }

    public FluxCore getCoreInstance() {
        return fluxCore;
    }

    public LanguageManager getLanguageManager() {
        return this.languageManager;
    }

    public ConfigurationManager getConfigurationManager() {
        return this.configurationManager;
    }

    public BlockAccessController getBlockAccessController() {
        return this.blockAccessController;
    }

    public VeinMinerController getVeinMinerController() {
        return this.veinMinerController;
    }

    public AreaToolController getAreaToolController() {
        return areaToolController;
    }

    public CustomRecipeUtils getCustomRecipeUtils() {
        return customRecipeUtils;
    }

    public LightSaberController getLightSaberController() {
        return lightSaberController;
    }

    public SonicScrewdriverController getSonicScrewdriverController() {
        return sonicScrewdriverController;
    }

    public PlayerDataController getPlayerDataController() {
        return playerDataController;
    }

    public SpellBookController getSpellBookController() {
        return spellBookController;
    }

    public ManaController getManaController() {
        return manaController;
    }
}
