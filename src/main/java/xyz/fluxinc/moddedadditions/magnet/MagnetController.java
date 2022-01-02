package xyz.fluxinc.moddedadditions.magnet;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitScheduler;
import xyz.fluxinc.moddedadditions.common.storage.CustomItem;
import xyz.fluxinc.moddedadditions.magnet.MagnetRunnable;

import java.util.HashMap;
import java.util.Map;

import static org.bukkit.Bukkit.getServer;
import static xyz.fluxinc.fluxcore.utils.LoreUtils.addLore;
import static xyz.fluxinc.moddedadditions.ModdedAdditions.KEY_BASE;
import static xyz.fluxinc.moddedadditions.ModdedAdditions.instance;

public class MagnetController {

    private static final int MAGNET_MODEL_KEY = KEY_BASE + 9001;
    private static final Material MAGNET_MATERIAL = Material.IRON_NUGGET;
    private final BukkitScheduler taskScheduler;
    private final Map<Player, Integer> vacuumInstances;

    public MagnetController() {
        this.taskScheduler = instance.getServer().getScheduler();
        vacuumInstances = new HashMap<>();
    }

    public static CustomItem getMagnet() {
        return new CustomItem(MAGNET_MODEL_KEY, MAGNET_MATERIAL, "MAGNET", ChatColor.AQUA + "Item " + ChatColor.RED + "Magnet", "mi-magnet") {
            @Override
            public ShapedRecipe getRecipe() {
                NamespacedKey magnetKey = new NamespacedKey(instance, this.getKeyName());
                ShapedRecipe magnetRecipe = new ShapedRecipe(magnetKey, this.getNewItem());
                magnetRecipe.shape("REL", "IEI", "III");
                magnetRecipe.setIngredient('R', Material.REDSTONE_BLOCK);
                magnetRecipe.setIngredient('E', Material.EMERALD_BLOCK);
                magnetRecipe.setIngredient('I', Material.IRON_BLOCK);
                magnetRecipe.setIngredient('L', Material.LAPIS_BLOCK);
                return magnetRecipe;
            }
        };
    }

    public static ItemStack generateNewMagnet() {
        ItemStack magnet = addLore(new ItemStack(MAGNET_MATERIAL), instance.getLanguageManager().getFormattedString("mi-magnet"));
        ItemMeta itemMeta = magnet.getItemMeta();
        itemMeta.setCustomModelData(MAGNET_MODEL_KEY);
        itemMeta.setDisplayName(ChatColor.AQUA + "Item " + ChatColor.RED + "Magnet");
        magnet.setItemMeta(itemMeta);
        return magnet;
    }

    public boolean hasVacuumInstance(Player player) {
        return vacuumInstances.containsKey(player);
    }

    public void registerVacuumInstance(Player player) {
        if (!hasVacuumInstance(player)) {
            int taskId = taskScheduler.scheduleSyncRepeatingTask(instance, new MagnetRunnable(player), 0L, 10L);
            vacuumInstances.put(player, taskId);
        }
    }

    public void deregisterVacuumInstance(Player player) {
        if (hasVacuumInstance(player)) {
            taskScheduler.cancelTask(vacuumInstances.get(player));
            vacuumInstances.remove(player);
        }
    }

}
