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
import xyz.fluxinc.moddedadditions.areatool.AreaToolCommand;
import xyz.fluxinc.moddedadditions.areatool.AreaToolController;
import xyz.fluxinc.moddedadditions.areatool.ExcavatorListener;
import xyz.fluxinc.moddedadditions.areatool.HammerListener;
import xyz.fluxinc.moddedadditions.armor.listeners.CopperArmorListener;
import xyz.fluxinc.moddedadditions.armor.listeners.HoneyChestplateListener;
import xyz.fluxinc.moddedadditions.armor.listeners.LongFallBootsListener;
import xyz.fluxinc.moddedadditions.armor.listeners.SlimeChestplateListener;
import xyz.fluxinc.moddedadditions.common.CustomRecipeUtils;
import xyz.fluxinc.moddedadditions.common.commands.ModdedAdditionsCommand;
import xyz.fluxinc.moddedadditions.common.commands.VoteDayCommand;
import xyz.fluxinc.moddedadditions.common.commands.legacy.NotifyCommand;
import xyz.fluxinc.moddedadditions.common.listeners.BookSignListener;
import xyz.fluxinc.moddedadditions.common.listeners.PreventLootDestructionListener;
import xyz.fluxinc.moddedadditions.common.listeners.chat.PingListener;
import xyz.fluxinc.moddedadditions.common.listeners.chat.ResponseListener;
import xyz.fluxinc.moddedadditions.common.listeners.inventory.AnvilListener;
import xyz.fluxinc.moddedadditions.common.listeners.inventory.SortChestListener;
import xyz.fluxinc.moddedadditions.common.storage.PlayerData;
import xyz.fluxinc.moddedadditions.common.storage.PlayerDataController;
import xyz.fluxinc.moddedadditions.lightsaber.LightSaberListener;
import xyz.fluxinc.moddedadditions.magic.SpellBookCommand;
import xyz.fluxinc.moddedadditions.magic.controller.ManaController;
import xyz.fluxinc.moddedadditions.magic.controller.SpellBookController;
import xyz.fluxinc.moddedadditions.magic.listener.SpellBookListener;
import xyz.fluxinc.moddedadditions.magic.listener.SpellControlListener;
import xyz.fluxinc.moddedadditions.magic.listener.spells.*;
import xyz.fluxinc.moddedadditions.magnet.MagnetController;
import xyz.fluxinc.moddedadditions.magnet.MagnetExecutor;
import xyz.fluxinc.moddedadditions.sonic.SonicScrewdriverListener;
import xyz.fluxinc.moddedadditions.veinminer.CropHarvestListener;
import xyz.fluxinc.moddedadditions.veinminer.VeinMinerCommand;
import xyz.fluxinc.moddedadditions.veinminer.VeinMinerController;
import xyz.fluxinc.moddedadditions.veinminer.VeinMinerListener;

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
    private CustomRecipeUtils customRecipeUtils;
    private PlayerDataController playerDataController;

    private SpellBookController spellBookController;
    private ManaController manaController;
    private ForceFieldListener forceFieldListener;
    private ReflectDamageListener reflectDamageController;

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
        //languageManager.verifyKeys(); // DISABLED DUE TO SNAKEYAML CHANGES
        configurationManager = new ConfigurationManager<>(this, "config.yml");
        //configurationManager.verifyKeys(); // DISABLED DUE TO SNAKEYAML CHANGES
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

        // Setup Book Handling
        getServer().getPluginManager().registerEvents(new BookSignListener(), this);

        // Setup DayVote Command
        String worldName = configurationManager.getString("dv-dayWorld");
        if (worldName != null && getServer().getWorld(worldName) != null) {
            VoteDayCommand voteDayCommand = new VoteDayCommand(getServer().getWorld(worldName));
            getServer().getPluginManager().registerEvents(voteDayCommand, this);
        } else {
            getLogger().warning("No or invalid world defined for DayVote. It will not be enabled");
        }

        // Setup Chest Sorting
        getServer().getPluginManager().registerEvents(new SortChestListener(), this);

        // Sonic Screwdriver Related Functions
        getServer().getPluginManager().registerEvents(new SonicScrewdriverListener(), this);

        // Magic Related Functionality
        manaController = new ManaController();
        manaController.initializeAllManaBars();
        spellBookController = new SpellBookController();
        reflectDamageController = new ReflectDamageListener();
        getServer().getPluginManager().registerEvents(reflectDamageController, this);
        getServer().getPluginManager().registerEvents(manaController, this);
        getServer().getPluginManager().registerEvents(new SpellBookListener(), this);
        getServer().getPluginManager().registerEvents(new ResearchInventoryListener(), this);
        getServer().getPluginManager().registerEvents(new SlowBallListener(), this);
        getServer().getPluginManager().registerEvents(new LavaWalkListener(), this);
        getServer().getPluginManager().registerEvents(new SpellControlListener(), this);
        SpellBookCommand.registerCommands();
        forceFieldListener = new ForceFieldListener();
        getServer().getPluginManager().registerEvents(forceFieldListener, this);

        // Tell Player Damage Dealt
        //getServer().getPluginManager().registerEvents(new DamageListener(), this);

        // Register Taunt Command
        //TauntCommand.registerCommands();

        // LightSaber Related Functions
        getServer().getPluginManager().registerEvents(new ResponseListener(), this);
        getServer().getPluginManager().registerEvents(new LightSaberListener(), this);

        // Register Custom Armor Listeners
        getServer().getPluginManager().registerEvents(new LongFallBootsListener(), this);
        getServer().getPluginManager().registerEvents(new HoneyChestplateListener(), this);
        getServer().getPluginManager().registerEvents(new SlimeChestplateListener(), this);
        getServer().getPluginManager().registerEvents(new CopperArmorListener(), this);

        // Register Prevent Chests
        if (configurationManager.getBoolean("ma-preventLootBreak")) {
            getServer().getPluginManager().registerEvents(new PreventLootDestructionListener(), this);
        }

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
        if (playerDataController != null) playerDataController.saveToDisk();
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

    public ReflectDamageListener getReflectDamageController() {
        return reflectDamageController;
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
