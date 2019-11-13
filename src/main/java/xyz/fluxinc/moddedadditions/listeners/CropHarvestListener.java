package xyz.fluxinc.moddedadditions.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import xyz.fluxinc.fluxcore.enums.Direction;
import xyz.fluxinc.moddedadditions.ModdedAdditions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static xyz.fluxinc.fluxcore.utils.BlockUtils.getDirectionalBlockList;
import static xyz.fluxinc.fluxcore.utils.BlockUtils.getVMBlockList;

@SuppressWarnings("BooleanMethodIsAlwaysInverted")
public class CropHarvestListener implements Listener {

    private ModdedAdditions instance;
    private int blockLimit;

    private static List<Material> crops;
    static {
        crops = new ArrayList<>();
        crops.add(Material.BEETROOTS);
        crops.add(Material.CARROTS);
        crops.add(Material.POTATOES);
        crops.add(Material.NETHER_WART);
        crops.add(Material.WHEAT);
        crops.add(Material.COCOA);
    }

    private static List<Material> tallCrops;
    static {
        tallCrops = new ArrayList<>();
        tallCrops.add(Material.BAMBOO);
        tallCrops.add(Material.CACTUS);
        tallCrops.add(Material.SUGAR_CANE);
    }


    public CropHarvestListener(ModdedAdditions pluginInstance, int blockLimit) { this.instance = pluginInstance; this.blockLimit = blockLimit; }

    @EventHandler
    public void cropInteractEvent(PlayerInteractEvent event) {
        if (!verifyEvent(event.getClickedBlock(), event.getAction(), event.getItem())) { return; }
        if (!crops.contains(event.getClickedBlock().getType())) { return; }
        // Should it veinmine
        boolean veinminer = instance.getConfig().getBoolean("ch-veinmine") && event.getPlayer().isSneaking();

        // Get information about the block they clicked on
        BlockData data = event.getClickedBlock().getBlockData();

        // If the block can be aged (therefore is a crop), run the below code
        if (data instanceof Ageable) {
            List<Block> blocks;
            // Get VeinMine list
            if (veinminer) { blocks = getVMBlockList(event.getClickedBlock(), this.blockLimit); }
            // Get single block
            else { blocks = new ArrayList<>(); blocks.add(event.getClickedBlock()); }
            for (Block b : blocks) {
                if (!verifyBlock(event.getPlayer(), b)) { continue; }
                Ageable age = (Ageable) b.getBlockData();
                Collection<ItemStack> drops = b.getDrops();
                age.setAge(0);
                b.setBlockData(age);
                for (ItemStack i : drops) {
                    event.getClickedBlock().getWorld().dropItemNaturally(event.getClickedBlock().getLocation(), i);
                }
            }
        }
    }

    @EventHandler
    public void tallCropHandler(PlayerInteractEvent event) {
        if (!verifyEvent(event.getClickedBlock(), event.getAction(), event.getItem())) { return; }
        if (tallCrops.contains(event.getMaterial())) { return; }
        List<Block> blockList = getDirectionalBlockList(event.getClickedBlock(), Direction.UP);
        blockList.remove(0);
        for (Block block : blockList) {
            if (!instance.getBlockAccessController().checkBreakPlace(event.getPlayer(), block.getLocation(), false)) { continue; }
            block.breakNaturally();
        }
    }


    private boolean verifyEvent(Block clickedBlock, Action action, ItemStack item) {
        return clickedBlock != null
                && clickedBlock.getType() != Material.AIR
                && action == Action.RIGHT_CLICK_BLOCK
                && !(item != null && instance.getConfig().getBoolean("ch-emptyhand"));
    }

    private boolean verifyBlock(Player player, Block block) {
        return block != null && (block.getBlockData() instanceof Ageable)
                && ((Ageable) block.getBlockData()).getAge() == ((Ageable) block.getBlockData()).getMaximumAge();
                //&& instance.getBlockAccessController().checkBreakPlace(player, block.getLocation(), true)
                //&& instance.getBlockAccessController().checkBreakPlace(player, block.getLocation(), false);
    }

}
