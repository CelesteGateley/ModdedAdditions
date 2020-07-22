package xyz.fluxinc.moddedadditions.storage;

import org.bukkit.inventory.ItemStack;
import xyz.fluxinc.fluxcore.enums.ToolLevel;
import xyz.fluxinc.moddedadditions.controllers.customitems.LightSaberController;
import xyz.fluxinc.moddedadditions.controllers.customitems.MagnetController;
import xyz.fluxinc.moddedadditions.controllers.customitems.SonicScrewdriverController;
import xyz.fluxinc.moddedadditions.controllers.customitems.SpellBookController;
import xyz.fluxinc.moddedadditions.enums.SaberColor;
import xyz.fluxinc.moddedadditions.utils.CustomRecipeUtils;
import xyz.fluxinc.moddedadditions.utils.SpecialArmorUtils;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static xyz.fluxinc.moddedadditions.ModdedAdditions.instance;

public class CustomItemRegistry {

    private static final Map<String, ItemStack> defaultItems;
    static {
        defaultItems = new LinkedHashMap<>();
        defaultItems.put("sonic_screwdriver", SonicScrewdriverController.generateNewSonic());
        defaultItems.put("spellbook", instance.getSpellBookController().generateNewSpellBook());
        defaultItems.put("long_fall_boots", SpecialArmorUtils.generateNewLongFallBoots());
        defaultItems.put("magnet", instance.getMagnetController().generateNewMagnet());
        defaultItems.put("elytra_repair_kit", CustomRecipeUtils.generateElytraKit());
        for (SaberColor color : SaberColor.values()) {
            defaultItems.put(color.toString().toLowerCase() + "_kyber_crystal", instance.getLightSaberController().generateNewKyberCrystal(color));
            defaultItems.put(color.toString().toLowerCase() + "_lightsaber", instance.getLightSaberController().generateNewLightSaber(color));
            defaultItems.put(color.toString().toLowerCase() + "_dark_core_saber", LightSaberController.generateNewDarkLightSaber(color));
        }

        for (ToolLevel level : ToolLevel.values()) {
            defaultItems.put(level.toString().toLowerCase() + "_hammer", instance.getAreaToolController().generateHammer(level));
            defaultItems.put(level.toString().toLowerCase() + "_excavator", instance.getAreaToolController().generateExcavator(level));
        }
    }

    public static List<String> getAllItemNames() {
        return (List<String>) defaultItems.keySet();
    }

    public static Map<String, ItemStack> getAllItems() {
        return defaultItems;
    }

    public static ItemStack getItem(String key) {
        return defaultItems.getOrDefault(key, null);
    }
}
