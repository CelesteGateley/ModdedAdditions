package xyz.fluxinc.moddedadditions.listeners;

import org.bukkit.Location;
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

import static java.lang.Math.abs;

public class CropHarvestListener implements Listener {

    private ModdedAdditions instance;

    public CropHarvestListener(ModdedAdditions pluginInstance) {
        this.instance = pluginInstance;
    }

    // When a Player right clicks on a block, run this
    @EventHandler
    public void interactEvent(PlayerInteractEvent event) {
        // If right
        if (event.getClickedBlock() == null || event.getAction() != Action.RIGHT_CLICK_BLOCK) { return; }
        // If they don't have an empty hand, exit
        if (event.getItem() != null && instance.getConfig().getBoolean("emptyhand")) { return; }
        // Should it veinmine
        boolean veinminer = instance.getConfig().getBoolean("veinmine") && event.getPlayer().isSneaking();
        // Get information about the block they clicked on

        BlockData data = event.getClickedBlock().getBlockData();

        // If the block can be aged (therefore is a crop), run the below code
        if (data instanceof Ageable) {

            ArrayList<Block> blocks;
            // Get VeinMine list
            if (veinminer) { blocks = getBlockList(event.getPlayer(), event.getClickedBlock(), instance.getConfig().getInt("vmradius")); }
            // Get single block
            else { blocks = getBlockList(event.getPlayer(), event.getClickedBlock(), 0); }

            for (Block b : blocks) {
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

    private boolean verifyBlock(Player player, Block block, Material expectedBlock) {
        return block != null && (block.getBlockData() instanceof Ageable)
                && ((Ageable) block.getBlockData()).getAge() == ((Ageable) block.getBlockData()).getMaximumAge()
                && block.getType() == expectedBlock
                && instance.getBlockAccessController().checkBreakPlace(player, block.getLocation(), true)
                && instance.getBlockAccessController().checkBreakPlace(player, block.getLocation(), false);
    }

    private ArrayList<Block> getBlockList(Player player, Block startingBlock, int distanceModifier) {
        ArrayList<Block> blockList = new ArrayList<>();
        distanceModifier = abs(distanceModifier);
        for (int x = distanceModifier * -1; x <= distanceModifier; x++) {
            for (int z = distanceModifier * -1; z <= distanceModifier; z++) {
                Location newLoc = new Location(startingBlock.getWorld(), startingBlock.getX() + x, startingBlock.getY(), startingBlock.getZ() + z);
                Block b = newLoc.getBlock();
                if (verifyBlock(player, b, startingBlock.getType())) { blockList.add(b); }
            }
        }
        return blockList;
    }

}
