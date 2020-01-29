package xyz.fluxinc.moddedadditions.enums;

import org.bukkit.ChatColor;
import org.bukkit.Material;

public enum SaberColor {

    BLUE,
    GREEN,
    PURPLE,
    RED,
    YELLOW,
    ORANGE,
    WHITE,
    DARK;

    public static SaberColor getColor(String color) {
        switch (color.toLowerCase()) {
            case "blue":    return BLUE;
            case "green":   return GREEN;
            case "purple":  return PURPLE;
            case "red":     return RED;
            case "yellow":  return YELLOW;
            case "orange":  return ORANGE;
            case "white":   return WHITE;
            case "dark":    return DARK;
            default:        return null;
        }
    }

    public static ChatColor getChatColor(SaberColor color) {
        switch (color) {
            case BLUE:      return ChatColor.AQUA;
            case GREEN:     return ChatColor.GREEN;
            case PURPLE:    return ChatColor.DARK_PURPLE;
            case RED:       return ChatColor.DARK_RED;
            case YELLOW:    return ChatColor.YELLOW;
            case ORANGE:    return ChatColor.GOLD;
            case WHITE:     return ChatColor.WHITE;
            case DARK:      return ChatColor.DARK_GRAY;
            default:        return null;
        }
    }

    public static int getColorMod(SaberColor color) {
        switch (color) {
            case BLUE:      return 1;
            case GREEN:     return 2;
            case PURPLE:    return 3;
            case RED:       return 4;
            case YELLOW:    return 5;
            case ORANGE:    return 6;
            case WHITE:     return 7;
            case DARK:      return 8;
            default:        return 0;
        }
    }

    public static SaberColor getModColor(int keyMod) {
        switch (keyMod) {
            case 1:     return BLUE;
            case 2:     return GREEN;
            case 3:     return PURPLE;
            case 4:     return RED;
            case 5:     return YELLOW;
            case 6:     return ORANGE;
            case 7:     return WHITE;
            case 8:     return DARK;
            default:    return null;
        }
    }

    public static Material getStainedGlass(SaberColor color) {
        switch (color) {
            case BLUE:      return Material.LIGHT_BLUE_STAINED_GLASS;
            case GREEN:     return Material.GREEN_STAINED_GLASS;
            case PURPLE:    return Material.PURPLE_STAINED_GLASS;
            case RED:       return Material.RED_STAINED_GLASS;
            case YELLOW:    return Material.YELLOW_STAINED_GLASS;
            case ORANGE:    return Material.ORANGE_STAINED_GLASS;
            case WHITE:     return Material.WHITE_STAINED_GLASS;
            case DARK:      return Material.BLACK_STAINED_GLASS;
            default:        return Material.GLASS;
        }
    }
}
