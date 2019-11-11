package xyz.fluxinc.moddedadditions.listeners;

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
import xyz.fluxinc.moddedadditions.ModdedAdditions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static xyz.fluxinc.fluxcore.utils.BlockUtils.getVMBlockList;

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
    }

    public CropHarvestListener(ModdedAdditions pluginInstance, int blockLimit) { this.instance = pluginInstance; this.blockLimit = blockLimit; }

    // When a Player right clicks on a block, run this
    @EventHandler
    public void interactEvent(PlayerInteractEvent event) {
        // If right
        if (event.getClickedBlock() == null || event.getAction() != Action.RIGHT_CLICK_BLOCK) { return; }
        // If they don't have an empty hand, exit
        if (event.getItem() != null && instance.getConfig().getBoolean("emptyhand")) { return; }
        // Is the block a valid crop?
        if (crops.contains(event.getMaterial())) { return; }
        // Should it veinmine
        boolean veinminer = instance.getConfig().getBoolean("veinmine") && event.getPlayer().isSneaking();

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

    private boolean verifyBlock(Player player, Block block) {
        return block != null && (block.getBlockData() instanceof Ageable)
                && ((Ageable) block.getBlockData()).getAge() == ((Ageable) block.getBlockData()).getMaximumAge()
                && instance.getBlockAccessController().checkBreakPlace(player, block.getLocation(), true)
                && instance.getBlockAccessController().checkBreakPlace(player, block.getLocation(), false);
    }

}
