package xyz.fluxinc.moddedadditions.common;

import org.bukkit.inventory.ItemStack;
import xyz.fluxinc.fluxcore.enums.ToolLevel;
import xyz.fluxinc.moddedadditions.areatool.AreaToolController;
import xyz.fluxinc.moddedadditions.lightsaber.SaberColor;
import xyz.fluxinc.moddedadditions.lightsaber.LightSaberController;
import xyz.fluxinc.moddedadditions.armor.SpecialArmorUtils;
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
        defaultItems.put("sonic_screwdriver", SonicScrewdriverController.generateNewSonic());
        defaultItems.put("spellbook", SpellBookController.generateNewSpellBook());
        defaultItems.put("long_fall_boots", SpecialArmorUtils.getLongFallBoots().getNewItem());
        defaultItems.put("magnet", MagnetController.generateNewMagnet());
        defaultItems.put("elytra_repair_kit", CustomRecipeUtils.generateElytraKit());
        defaultItems.put("honey_chestplate", SpecialArmorUtils.getHoneyChestplate().getNewItem());
        defaultItems.put("slime_chestplate", SpecialArmorUtils.getSlimeChestplate().getNewItem());
        for (SaberColor color : SaberColor.values()) {
            defaultItems.put(color.toString().toLowerCase() + "_kyber_crystal", LightSaberController.generateNewKyberCrystal(color));
            defaultItems.put(color.toString().toLowerCase() + "_lightsaber", LightSaberController.generateNewLightSaber(color));
            defaultItems.put(color.toString().toLowerCase() + "_dark_core_saber", LightSaberController.generateNewDarkLightSaber(color));
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
