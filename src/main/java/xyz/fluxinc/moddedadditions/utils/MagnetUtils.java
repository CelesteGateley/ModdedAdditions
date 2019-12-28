package xyz.fluxinc.moddedadditions.utils;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import static xyz.fluxinc.fluxcore.utils.LoreUtils.addLore;

public class MagnetUtils {

    private ItemStack templateMagnet;
    private Material MAGNET_MATERIAL = Material.COMPASS;
    private String lore;
    private static final String ITEM_NAME = ChatColor.RED + "Item " + ChatColor.BLUE + "Magnet";


    public MagnetUtils(String lore) {
        templateMagnet = addLore(new ItemStack(MAGNET_MATERIAL), lore);
        this.lore = lore;
        ItemMeta iMeta = templateMagnet.getItemMeta();
        if (iMeta != null) {
            iMeta.setDisplayName(ITEM_NAME);
            templateMagnet.setItemMeta(iMeta);
        }
    }

    public boolean isMagnet(ItemStack item) {
        return item != null
                && item.getType() == MAGNET_MATERIAL
                && item.getItemMeta() != null
                && item.getItemMeta().getLore() != null
                && item.getItemMeta().getLore().contains(lore);
    }

    public ItemStack getNewMagnet() {
        return templateMagnet;
    }
}
