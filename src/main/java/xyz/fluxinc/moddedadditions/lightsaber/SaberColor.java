package xyz.fluxinc.moddedadditions.lightsaber;

import org.bukkit.ChatColor;
import org.bukkit.Material;

public enum SaberColor {

    DEPLETED(ChatColor.RESET, 0, Material.BEDROCK), // Default value used instead of null
    BLUE(ChatColor.AQUA, 1, Material.LIGHT_BLUE_STAINED_GLASS),
    GREEN(ChatColor.GREEN, 2, Material.LIGHT_BLUE_STAINED_GLASS),
    PURPLE(ChatColor.DARK_PURPLE, 3, Material.LIGHT_BLUE_STAINED_GLASS),
    RED(ChatColor.DARK_RED, 4, Material.LIGHT_BLUE_STAINED_GLASS),
    YELLOW(ChatColor.YELLOW, 5, Material.LIGHT_BLUE_STAINED_GLASS),
    ORANGE(ChatColor.GOLD, 6, Material.LIGHT_BLUE_STAINED_GLASS),
    WHITE(ChatColor.WHITE, 7, Material.LIGHT_BLUE_STAINED_GLASS),
    DARK(ChatColor.DARK_GRAY, 8, Material.LIGHT_BLUE_STAINED_GLASS);

    public final ChatColor chatColor;
    public final int model;
    public final Material component;

    SaberColor(ChatColor chatColor, int model, Material component) {
        this.chatColor = chatColor;
        this.model = model;
        this.component = component;
    }

    public static SaberColor getModColor(int keyMod) {
        for (SaberColor color : values()) {
            if (color.model == keyMod) return color;
        }
        return DEPLETED;
    }
}
