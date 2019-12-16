package xyz.fluxinc.moddedadditions.listeners;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import xyz.fluxinc.fluxcore.security.BlockAccessController;
import xyz.fluxinc.fluxcore.utils.BlockUtils;
import xyz.fluxinc.moddedadditions.ModdedAdditions;
import xyz.fluxinc.moddedadditions.controllers.VeinMinerController;

import java.util.List;

import static xyz.fluxinc.moddedadditions.storage.Tools.*;

public class VeinMinerListener implements Listener {

    private BlockAccessController accessController;
    private VeinMinerController vmController;

    public VeinMinerListener(ModdedAdditions instance) {
        this.accessController = instance.getBlockAccessController();
        this.vmController = instance.getVeinMinerController();
    }

    @EventHandler
    public void blockBreakListener(BlockBreakEvent event) {
        if (!event.getPlayer().isSneaking()) { return; }
        ItemStack mainHandItem = event.getPlayer().getInventory().getItemInMainHand();
        Material toolMat = mainHandItem.getType();
        if (toolMat == Material.SHEARS && vmController.checkShears(event.getBlock().getType())) {
            processBlocks(event.getBlock(), mainHandItem, event.getPlayer());
        } else if (pickaxes.contains(toolMat) && vmController.checkPickaxe(event.getBlock().getType())) {
            processBlocks(event.getBlock(), mainHandItem, event.getPlayer());
        } else if (axes.contains(toolMat) && vmController.checkAxe(event.getBlock().getType())) {
            processBlocks(event.getBlock(), mainHandItem, event.getPlayer());
        } else if (shovels.contains(toolMat) && vmController.checkShovel(event.getBlock().getType())) {
            processBlocks(event.getBlock(), mainHandItem, event.getPlayer());
        } else if (hoes.contains(toolMat) && vmController.checkHoe(event.getBlock().getType())) {
            processBlocks(event.getBlock(), mainHandItem, event.getPlayer());
        } else if (vmController.checkHand(event.getBlock().getType())) {
            processBlocks(event.getBlock(), event.getPlayer());
        }
    }

    private void processBlocks(Block block, ItemStack tool, Player player) {
        ItemMeta iMeta = tool.getItemMeta();
        iMeta.setLore(null);
        tool.setItemMeta(iMeta);
        List<Block> blockList = BlockUtils.getVMBlockList(block, vmController.getMaxBlocks());
        blockList.remove(1);
        for (Block b : blockList) {
            if (BlockUtils.canBreak(block, tool) && accessController.checkBreakPlace(player, block.getLocation(), false)) {
                b.breakNaturally(tool);
            }
        }

    }

    private void processBlocks(Block block, Player player) {
        List<Block> blockList = BlockUtils.getVMBlockList(block, vmController.getMaxBlocks());
        blockList.remove(1);
        for (Block b : blockList) {
            if (BlockUtils.canBreak(block, null) && accessController.checkBreakPlace(player, block.getLocation(), false)) {
                b.breakNaturally();
            }
        }
    }

}
