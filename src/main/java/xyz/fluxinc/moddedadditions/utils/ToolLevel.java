package xyz.fluxinc.moddedadditions.utils;

public enum ToolLevel {

    WOODEN,
    STONE,
    IRON,
    GOLD,
    DIAMOND;

    public static String getName(ToolLevel level) {
        switch (level) {
            case WOODEN:
                return "Wooden";
            case STONE:
                return "Stone";
            case IRON:
                return "Iron";
            case GOLD:
                return "Gold";
            case DIAMOND:
                return "Diamond";
            default:
                return "";
        }
    }
}
