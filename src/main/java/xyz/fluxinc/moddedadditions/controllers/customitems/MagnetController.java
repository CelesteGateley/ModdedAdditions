package xyz.fluxinc.moddedadditions.controllers.customitems;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitScheduler;
import xyz.fluxinc.moddedadditions.ModdedAdditions;
import xyz.fluxinc.moddedadditions.runnables.MagnetRunnable;

import java.util.HashMap;
import java.util.Map;

import static xyz.fluxinc.fluxcore.utils.LoreUtils.addLore;
import static xyz.fluxinc.moddedadditions.ModdedAdditions.KEY_BASE;

public class MagnetController {

    private static final int MAGNET_MODEL_KEY = KEY_BASE + 9001;
    private static final Material MAGNET_MATERIAL = Material.IRON_NUGGET;
    private final BukkitScheduler taskScheduler;
    private final ModdedAdditions instance;
    private final Map<Player, Integer> vacuumInstances;

    public MagnetController(ModdedAdditions instance) {
        this.instance = instance;
        this.taskScheduler = instance.getServer().getScheduler();
        vacuumInstances = new HashMap<>();
    }

    public Material getMagnetMaterial() {
        return MAGNET_MATERIAL;
    }

    public boolean isMagnet(ItemStack item) {
        return item != null
                && item.getType() == MAGNET_MATERIAL
                && item.getItemMeta() != null
                && item.getItemMeta().hasCustomModelData()
                && item.getItemMeta().getCustomModelData() == MAGNET_MODEL_KEY;
    }

    public boolean hasVacuumInstance(Player player) {
        return vacuumInstances.containsKey(player);
    }

    public ItemStack generateNewMagnet() {
        ItemStack magnet = addLore(new ItemStack(MAGNET_MATERIAL), instance.getLanguageManager().getFormattedString("mi-magnet"));
        ItemMeta itemMeta = magnet.getItemMeta();
        itemMeta.setCustomModelData(MAGNET_MODEL_KEY);
        itemMeta.setDisplayName(ChatColor.AQUA + "Item " + ChatColor.RED + "Magnet");
        magnet.setItemMeta(itemMeta);
        return magnet;
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

    public boolean verifyOldMagnet(ItemStack item) {
        return item != null
                && item.getType() == Material.COMPASS
                && item.getItemMeta() != null
                && item.getItemMeta().getLore() != null
                && item.getItemMeta().getLore().contains(instance.getLanguageManager().getFormattedString("mi-magnet"));
    }

    public ItemStack updateOldMagnet(ItemStack itemStack) {
        ItemMeta iMeta = itemStack.getItemMeta();
        itemStack.setType(Material.IRON_NUGGET);
        iMeta.setCustomModelData(MAGNET_MODEL_KEY);
        itemStack.setItemMeta(iMeta);
        return itemStack;
    }

}
