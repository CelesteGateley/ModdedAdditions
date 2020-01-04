package xyz.fluxinc.moddedadditions.listeners;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import xyz.fluxinc.fluxcore.security.CoreProtectLogger;
import xyz.fluxinc.fluxcore.utils.BlockUtils;
import xyz.fluxinc.moddedadditions.ModdedAdditions;

import java.util.List;
import java.util.Random;

import static xyz.fluxinc.fluxcore.utils.MineabilityUtils.verifyBlockMining;
import static xyz.fluxinc.moddedadditions.storage.Tools.*;

public class VeinMinerListener implements Listener {

    private ModdedAdditions instance;

    public VeinMinerListener(ModdedAdditions instance) {
        this.instance = instance;
    }

    @EventHandler
    public void blockBreakListener(BlockBreakEvent event) {
        if (!event.getPlayer().isSneaking()) return;
        if (event.getPlayer().getGameMode() == GameMode.CREATIVE && !instance.getVeinMinerController().getAllowInCreative()) return;
        if (!verifyBlockMining(event.getPlayer().getInventory().getItemInMainHand(), event.getBlock().getType())) return;
        if (instance.getVeinMinerController().isToggled(event.getPlayer())) { return; }

        ItemStack mainHandItem = event.getPlayer().getInventory().getItemInMainHand();
        Material toolMat = mainHandItem.getType();
        if (toolMat == Material.SHEARS && instance.getVeinMinerController().checkShears(event.getBlock().getType())) {
            processBlocks(event.getBlock(), event.getPlayer().getInventory().getItemInMainHand(), event.getPlayer());
        } else if (pickaxes.contains(toolMat) && instance.getVeinMinerController().checkPickaxe(event.getBlock().getType())) {
            processBlocks(event.getBlock(), event.getPlayer().getInventory().getItemInMainHand(), event.getPlayer());
        } else if (axes.contains(toolMat) && instance.getVeinMinerController().checkAxe(event.getBlock().getType())) {
            processBlocks(event.getBlock(), event.getPlayer().getInventory().getItemInMainHand(), event.getPlayer());
        } else if (shovels.contains(toolMat) && instance.getVeinMinerController().checkShovel(event.getBlock().getType())) {
            processBlocks(event.getBlock(), event.getPlayer().getInventory().getItemInMainHand(), event.getPlayer());
        } else if (hoes.contains(toolMat) && instance.getVeinMinerController().checkHoe(event.getBlock().getType())) {
            processBlocks(event.getBlock(), event.getPlayer().getInventory().getItemInMainHand(), event.getPlayer());
        } else if (instance.getVeinMinerController().checkHand(event.getBlock().getType())) {
            processBlocks(event.getBlock(), event.getPlayer());
        }
    }

    private void processBlocks(Block block, ItemStack toolUsed, Player player) {
        ItemStack tool = toolUsed.clone();
        ItemMeta iMeta = tool.getItemMeta();
        if (iMeta != null) {
            iMeta.setLore(null);
            tool.setItemMeta(iMeta);
        }
        List<Block> blockList = BlockUtils.getVMBlockList(block, instance.getVeinMinerController().getMaxBlocks(), true);
        blockList.remove(0);
        int blocksBroken = 0;
        for (Block b : blockList) {
            if (instance.getBlockAccessController().checkBreakPlace(player, block.getLocation(), false)) {
                iMeta = toolUsed.getItemMeta();
                if (iMeta != null) {
                    Damageable damageable = (Damageable) iMeta;
                    if (damageable.getDamage() + 1 > toolUsed.getType().getMaxDurability()) {
                        break;
                    }
                    damageable.setDamage(damageable.getDamage() + 1);
                    toolUsed.setItemMeta((ItemMeta) damageable);
                    blocksBroken += 1;
                }
                CoreProtectLogger.logBlockBreak(player, b);
                b.breakNaturally(tool);
            }
        }
        ItemMeta toolMeta = toolUsed.getItemMeta();
        if (toolMeta instanceof Damageable) {
            Damageable damageable = (Damageable) toolUsed.getItemMeta();
            damageable.setDamage(damageable.getDamage() + getToolDamage(toolUsed, blocksBroken));
            toolUsed.setItemMeta((ItemMeta) damageable);
        }
    }

    private void processBlocks(Block block, Player player) {
        List<Block> blockList = BlockUtils.getVMBlockList(block, instance.getVeinMinerController().getMaxBlocks());
        blockList.remove(0);
        for (Block b : blockList) {
            if (instance.getBlockAccessController().checkBreakPlace(player, block.getLocation(), false)) {
                CoreProtectLogger.logBlockBreak(player, b);
                b.breakNaturally();
            }
        }
    }

    private int getToolDamage(ItemStack tool, int blocksBroken) {
        Random random = new Random();
        double chance = (100D / (getUnbreakingLevel(tool) + 1));
        int counter = 0;
        for (int i = 0; i < blocksBroken; i++) {
            double rand = random.nextInt(99) + 1;
            if (rand >= chance) {
                continue;
            }
            counter++;
        }
        return counter;
    }

    private int getUnbreakingLevel(ItemStack tool) {
        ItemMeta itemMeta = tool.getItemMeta();
        if (itemMeta == null) {
            return 0;
        }
        return itemMeta.getEnchantLevel(Enchantment.DURABILITY);
    }

}
