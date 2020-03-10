package xyz.fluxinc.moddedadditions.controllers.customitems;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import xyz.fluxinc.moddedadditions.ModdedAdditions;

import static xyz.fluxinc.fluxcore.utils.LoreUtils.addLore;
import static xyz.fluxinc.moddedadditions.ModdedAdditions.KEY_BASE;

public class SonicScrewdriverController {

    private static final int SONIC_MODEL_KEY = KEY_BASE + 9002;
    private ModdedAdditions instance;

    public SonicScrewdriverController(ModdedAdditions instance) {
        this.instance = instance;
    }

    public ItemStack generateNewSonic() {
        ItemStack sonic = addLore(new ItemStack(Material.STICK), instance.getLanguageManager().getFormattedString("mi-sonic"));
        ItemMeta iMeta = sonic.getItemMeta();
        iMeta.setCustomModelData(SONIC_MODEL_KEY);
        iMeta.setDisplayName(ChatColor.WHITE + "Sonic");
        sonic.setItemMeta(iMeta);
        return sonic;
    }

    public boolean isSonic(ItemStack item) {
        return item != null
                && item.getItemMeta() != null
                && item.getItemMeta().hasCustomModelData()
                && item.getItemMeta().getCustomModelData() == SONIC_MODEL_KEY;
    }

}
