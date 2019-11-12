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

import java.util.ArrayList;
import java.util.List;

public class VeinMinerListener implements Listener {

    private BlockAccessController accessController;
    private VeinMinerController vmController;

    private static List<Material> pickaxes;
    private static List<Material> axes;
    private static List<Material> shovels;
    private static List<Material> hoes;
    static {
        pickaxes = new ArrayList<>();
        pickaxes.add(Material.WOODEN_PICKAXE);
        pickaxes.add(Material.STONE_PICKAXE);
        pickaxes.add(Material.IRON_PICKAXE);
        pickaxes.add(Material.GOLDEN_PICKAXE);
        pickaxes.add(Material.DIAMOND_PICKAXE);

        axes = new ArrayList<>();
        axes.add(Material.WOODEN_AXE);
        axes.add(Material.STONE_AXE);
        axes.add(Material.IRON_AXE);
        axes.add(Material.GOLDEN_AXE);
        axes.add(Material.DIAMOND_AXE);

        shovels = new ArrayList<>();
        shovels.add(Material.WOODEN_SHOVEL);
        shovels.add(Material.STONE_SHOVEL);
        shovels.add(Material.IRON_SHOVEL);
        shovels.add(Material.GOLDEN_SHOVEL);
        shovels.add(Material.DIAMOND_SHOVEL);

        hoes = new ArrayList<>();
        hoes.add(Material.WOODEN_HOE);
        hoes.add(Material.STONE_HOE);
        hoes.add(Material.IRON_HOE);
        hoes.add(Material.GOLDEN_HOE);
        hoes.add(Material.DIAMOND_HOE);
    }

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
