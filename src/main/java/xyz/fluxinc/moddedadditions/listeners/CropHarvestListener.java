package xyz.fluxinc.moddedadditions.listeners;

import com.gamingmesh.jobs.actions.BlockActionInfo;
import com.gamingmesh.jobs.container.ActionType;
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
import xyz.fluxinc.fluxcore.security.CoreProtectLogger;
import xyz.fluxinc.moddedadditions.hooks.JobsRebornHook;
import xyz.fluxinc.moddedadditions.hooks.McMMOHook;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static xyz.fluxinc.fluxcore.utils.BlockUtils.getDirectionalBlockList;
import static xyz.fluxinc.fluxcore.utils.BlockUtils.getVMBlockList;
import static xyz.fluxinc.moddedadditions.ModdedAdditions.instance;

@SuppressWarnings("BooleanMethodIsAlwaysInverted")
public class CropHarvestListener implements Listener {

    private static final List<Material> crops;
    private static final List<Material> tallCrops;

    static {
        crops = new ArrayList<>();
        crops.add(Material.BEETROOTS);
        crops.add(Material.CARROTS);
        crops.add(Material.POTATOES);
        crops.add(Material.NETHER_WART);
        crops.add(Material.WHEAT);
        crops.add(Material.COCOA);
    }

    static {
        tallCrops = new ArrayList<>();
        tallCrops.add(Material.BAMBOO);
        tallCrops.add(Material.CACTUS);
        tallCrops.add(Material.SUGAR_CANE);
    }

    private final int blockLimit;

    public CropHarvestListener(int blockLimit) {
        this.blockLimit = blockLimit;
    }

    @EventHandler
    public void cropInteractEvent(PlayerInteractEvent event) {
        if (!verifyEvent(event.getClickedBlock(), event.getAction(), event.getItem())) {
            return;
        }
        if (!crops.contains(event.getClickedBlock().getType())) {
            return;
        }
        // Should it veinmine
        boolean veinminer = instance.getConfigurationManager().getBoolean("ch-veinmine") && event.getPlayer().isSneaking();

        // Get information about the block they clicked on
        BlockData data = event.getClickedBlock().getBlockData();

        // If the block can be aged (therefore is a crop), run the below code
        if (data instanceof Ageable) {
            List<Block> blocks;
            // Get VeinMine list
            if (veinminer) {
                blocks = getVMBlockList(event.getClickedBlock(), this.blockLimit);
            }
            // Get single block
            else {
                blocks = new ArrayList<>();
                blocks.add(event.getClickedBlock());
            }
            for (Block b : blocks) {
                if (!verifyBlock(event.getPlayer(), b)) {
                    continue;
                }
                McMMOHook.addBlockExperience(b.getState(), event.getPlayer());
                try { JobsRebornHook.addExperienceForBlockBreak(b, event.getPlayer()); } catch (NoClassDefFoundError ignored) {}
                CoreProtectLogger.logBlockBreak(event.getPlayer(), b);
                Ageable age = (Ageable) b.getBlockData();
                Collection<ItemStack> drops = b.getDrops();
                age.setAge(0);
                b.setBlockData(age);
                CoreProtectLogger.logBlockPlace(event.getPlayer(), b);
                McMMOHook.addBlockExperience(b.getState(), event.getPlayer());
                try { JobsRebornHook.addExperienceForBlockPlace(b, event.getPlayer()); } catch (NoClassDefFoundError ignored) {}
                for (ItemStack i : drops) {
                    event.getClickedBlock().getWorld().dropItemNaturally(event.getClickedBlock().getLocation(), i);
                }
            }
        }
    }

    //@EventHandler
    public void tallCropHandler(PlayerInteractEvent event) {
        if (!verifyEvent(event.getClickedBlock(), event.getAction(), event.getItem())) {
            return;
        }
        if (tallCrops.contains(event.getMaterial())) {
            return;
        }
        List<Block> blockList = getDirectionalBlockList(event.getClickedBlock(), Direction.UP);
        blockList.remove(0);
        for (Block block : blockList) {
            if (!instance.getBlockAccessController().checkBreakPlace(event.getPlayer(), block.getLocation(), false)) {
                continue;
            }
            CoreProtectLogger.logBlockBreak(event.getPlayer(), block);
            block.breakNaturally();
        }
    }


    private boolean verifyEvent(Block clickedBlock, Action action, ItemStack item) {
        return clickedBlock != null
                && clickedBlock.getType() != Material.AIR
                && action == Action.RIGHT_CLICK_BLOCK
                && !(item != null && instance.getConfigurationManager().getBoolean("ch-emptyhand"));
    }

    private boolean verifyBlock(Player player, Block block) {
        return block != null && (block.getBlockData() instanceof Ageable)
                && ((Ageable) block.getBlockData()).getAge() == ((Ageable) block.getBlockData()).getMaximumAge()
                && instance.getBlockAccessController().checkBreakPlace(player, block.getLocation(), true)
                && instance.getBlockAccessController().checkBreakPlace(player, block.getLocation(), false);
    }

}
