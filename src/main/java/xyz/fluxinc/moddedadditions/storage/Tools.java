package xyz.fluxinc.moddedadditions.storage;

import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;

public class Tools {

    public static List<Material> pickaxes;
    static {
        pickaxes = new ArrayList<>();
        pickaxes.add(Material.WOODEN_PICKAXE);
        pickaxes.add(Material.STONE_PICKAXE);
        pickaxes.add(Material.IRON_PICKAXE);
        pickaxes.add(Material.GOLDEN_PICKAXE);
        pickaxes.add(Material.DIAMOND_PICKAXE);
    }
}
