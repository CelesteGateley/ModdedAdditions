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
import xyz.fluxinc.moddedadditions.commands.AreaToolCommand;
import xyz.fluxinc.moddedadditions.commands.ModdedAdditionsCommand;
import xyz.fluxinc.moddedadditions.commands.SpellBookCommand;
import xyz.fluxinc.moddedadditions.commands.VeinMinerCommand;
import xyz.fluxinc.moddedadditions.commands.legacy.NotifyCommand;
import xyz.fluxinc.moddedadditions.commands.legacy.VoteDayCommand;
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
import xyz.fluxinc.moddedadditions.listeners.customitem.armor.LongFallBootsListener;
import xyz.fluxinc.moddedadditions.listeners.customitem.spells.ForceFieldListener;
import xyz.fluxinc.moddedadditions.listeners.customitem.spells.ResearchInventoryListener;
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
        playerDataController = new PlayerDataController("storage.yml");
        getServer().getPluginManager().registerEvents(playerDataController, this);

        // Register Language and Configuration Managers
        languageManager = new LanguageManager<>(this, "lang.yml");
        languageManager.verifyKeys();
        configurationManager = new ConfigurationManager<>(this, "config.yml");
        configurationManager.verifyKeys();
        ModdedAdditionsCommand.registerCommands();

        // Register Core Utilities
        blockAccessController = fluxCore.getBlockAccessController();

        // Register Crop Harvesting
        getServer().getPluginManager().registerEvents(new CropHarvestListener(configurationManager.getInt("ch-vmblocks")), this);

        // Register Ping Listener and Command
        getServer().getPluginManager().registerEvents(new PingListener(), this);
        getCommand("notify").setExecutor(new NotifyCommand());

        // Register Colored Anvil Names
        getServer().getPluginManager().registerEvents(new AnvilListener(), this);

        // Setup VeinMiner Related Tasks
        veinMinerController = new VeinMinerController();
        getServer().getPluginManager().registerEvents(new VeinMinerListener(), this);
        VeinMinerCommand.registerCommands();

        // Setup Hammer/Excavator Related Tasks
        areaToolController = new AreaToolController();
        getServer().getPluginManager().registerEvents(new HammerListener(languageManager.getString("mi-hammer")), this);
        getServer().getPluginManager().registerEvents(new ExcavatorListener(languageManager.getString("mi-excavator")), this);
        AreaToolCommand.registerCommands();

        // Setup Magnet Related Tasks
        magnetController = new MagnetController();
        getServer().getPluginManager().registerEvents(new InventoryChecker(this, new MagnetExecutor()), this);
        getServer().getPluginManager().registerEvents(new InventoryChecker(this, new OldMagnetExecutor()), this);

        // Setup Book Handling
        getServer().getPluginManager().registerEvents(new BookSignListener(), this);

        // Setup DayVote Command
        String worldName = configurationManager.getString("dv-dayWorld");
        if (worldName != null && getServer().getWorld(worldName) != null) {
            VoteDayCommand voteDayCommand = new VoteDayCommand(getServer().getWorld(worldName));
            getCommand("voteday").setExecutor(voteDayCommand);
            getServer().getPluginManager().registerEvents(voteDayCommand, this);
        } else {
            getLogger().warning("No or invalid world defined for DayVote. It will not be enabled");
        }

        // Setup Chest Sorting
        getServer().getPluginManager().registerEvents(new SortChestListener(), this);

        // Sonic Screwdriver Related Functions
        sonicScrewdriverController = new SonicScrewdriverController();
        getServer().getPluginManager().registerEvents(new SonicScrewdriverListener(), this);

        // Magic Related Functionality
        manaController = new ManaController();
        manaController.initializeAllManaBars();
        spellBookController = new SpellBookController();
        getServer().getPluginManager().registerEvents(manaController, this);
        getServer().getPluginManager().registerEvents(new SpellBookListener(), this);
        getServer().getPluginManager().registerEvents(new ResearchInventoryListener(), this);
        SpellBookCommand.registerCommands();
        forceFieldListener = new ForceFieldListener();
        getServer().getPluginManager().registerEvents(forceFieldListener, this);

        // Tell Player Damage Dealt
        //getServer().getPluginManager().registerEvents(new DamageListener(), this);

        // LightSaber Related Functions
        getServer().getPluginManager().registerEvents(new ResponseListener(), this);
        lightSaberController = new LightSaberController();
        getServer().getPluginManager().registerEvents(new LightSaberListener(), this);

        // Register Custom Armor Listeners
        getServer().getPluginManager().registerEvents(new LongFallBootsListener(), this);
        //getServer().getPluginManager().registerEvents(new HoneyChestplateListener(), this);
        //getServer().getPluginManager().registerEvents(new SlimeChestplateListener(), this);

        // Register Crafting Additions (Must Be Last)
        customRecipeUtils = new CustomRecipeUtils();
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

    public ForceFieldListener getForceFieldListener() {
        return forceFieldListener;
    }

    public MagnetController getMagnetController() {
        return magnetController;
    }

    public FluxCore getCoreInstance() {
        return fluxCore;
    }

    public LanguageManager<ModdedAdditions> getLanguageManager() {
        return this.languageManager;
    }

    public ConfigurationManager<ModdedAdditions> getConfigurationManager() {
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
