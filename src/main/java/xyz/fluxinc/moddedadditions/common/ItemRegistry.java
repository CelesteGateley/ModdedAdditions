package xyz.fluxinc.moddedadditions.common;

import org.bukkit.inventory.ItemStack;
import xyz.fluxinc.fluxcore.enums.ToolLevel;
import xyz.fluxinc.moddedadditions.areatool.AreaToolController;
import xyz.fluxinc.moddedadditions.armor.SpecialArmorUtils;
import xyz.fluxinc.moddedadditions.common.simple.ElytraRepairKit;
import xyz.fluxinc.moddedadditions.lightsaber.SaberColor;
import xyz.fluxinc.moddedadditions.lightsaber.items.DarkSaber;
import xyz.fluxinc.moddedadditions.lightsaber.items.KyberCrystal;
import xyz.fluxinc.moddedadditions.lightsaber.items.LightSaber;
import xyz.fluxinc.moddedadditions.magic.controller.SpellBookController;
import xyz.fluxinc.moddedadditions.magnet.MagnetController;
import xyz.fluxinc.moddedadditions.sonic.SonicScrewdriverController;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class ItemRegistry {

    private static final Map<String, ItemStack> defaultItems;

    static {
        defaultItems = new LinkedHashMap<>();
        defaultItems.put("sonic_screwdriver", SonicScrewdriverController.getSonic().getNewItem());
        defaultItems.put("spellbook", SpellBookController.generateNewSpellBook());
        defaultItems.put("long_fall_boots", SpecialArmorUtils.getLongFallBoots().getNewItem());
        defaultItems.put("magnet", MagnetController.getMagnet().getNewItem());
        defaultItems.put("elytra_repair_kit", ElytraRepairKit.getElytraRepairKit().getNewItem());
        defaultItems.put("honey_chestplate", SpecialArmorUtils.getHoneyChestplate().getNewItem());
        defaultItems.put("slime_chestplate", SpecialArmorUtils.getSlimeChestplate().getNewItem());
        for (SaberColor color : SaberColor.values()) {
            defaultItems.put(color.toString().toLowerCase() + "_kyber_crystal", new KyberCrystal(color).getNewItem());
            defaultItems.put(color.toString().toLowerCase() + "_lightsaber", new LightSaber(color).getNewItem());
            defaultItems.put(color.toString().toLowerCase() + "_dark_core_saber", new DarkSaber(color).getNewItem());
        }

        for (ToolLevel level : ToolLevel.values()) {
            defaultItems.put(level.toString().toLowerCase() + "_hammer", AreaToolController.generateHammer(level).getNewItem());
            defaultItems.put(level.toString().toLowerCase() + "_excavator", AreaToolController.generateExcavator(level).getNewItem());
        }
    }

    public static Set<String> getAllItemNames() {
        return defaultItems.keySet();
    }

    public static Map<String, ItemStack> getAllItems() {
        return defaultItems;
    }

    public static ItemStack getItem(String key) {
        return defaultItems.getOrDefault(key, null);
    }
}
