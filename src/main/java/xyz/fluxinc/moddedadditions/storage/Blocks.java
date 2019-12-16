package xyz.fluxinc.moddedadditions.storage;

import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;

public class Blocks {

    public static List<Material> HAMMER_EXCEPTIONS;
    static {
        HAMMER_EXCEPTIONS = new ArrayList<>();
        HAMMER_EXCEPTIONS.add(Material.GLASS);
    }
}
