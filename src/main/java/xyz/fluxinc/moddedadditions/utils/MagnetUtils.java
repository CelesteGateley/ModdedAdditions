package xyz.fluxinc.moddedadditions.utils;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class MagnetUtils {

    private static ItemStack templateMagnet;
    private static final Material MAGNET_MATERIAL = Material.COMPASS;
    private static final String LORE_LINE = ChatColor.RED + "This magnet will draw items towards the holder";
    private static final String ITEM_NAME = ChatColor.RED + "Item " + ChatColor.BLUE + "Magnet";
    static {
        templateMagnet = new ItemStack(MAGNET_MATERIAL);
        ItemMeta iMeta = templateMagnet.getItemMeta();
        List<String> lore = iMeta.getLore() != null ? iMeta.getLore() : new ArrayList<>();
        lore.add(LORE_LINE);
        iMeta.setDisplayName(ITEM_NAME);
        iMeta.setLore(lore);
        templateMagnet.setItemMeta(iMeta);
    }

    public static boolean isMagnet(ItemStack item) {
        return item.getType() == MAGNET_MATERIAL
                && item.getItemMeta().getLore() != null
                && item.getItemMeta().getLore().contains(LORE_LINE);
    }

    public static ItemStack getNewMagnet() { return templateMagnet; }
}
