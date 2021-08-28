package xyz.fluxinc.moddedadditions.areatool;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import xyz.fluxinc.fluxcore.security.CoreProtectLogger;
import xyz.fluxinc.moddedadditions.common.hooks.JobsRebornHook;
import xyz.fluxinc.moddedadditions.common.hooks.McMMOHook;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static xyz.fluxinc.fluxcore.utils.ToolUtils.shovels;
import static xyz.fluxinc.fluxcore.utils.ToolUtils.takeDurability;
import static xyz.fluxinc.moddedadditions.ModdedAdditions.KEY_BASE;
import static xyz.fluxinc.moddedadditions.ModdedAdditions.instance;
import static xyz.fluxinc.moddedadditions.areatool.AreaToolController.AT_KEY_BASE;

@SuppressWarnings("BooleanMethodIsAlwaysInverted")
public class ExcavatorListener implements Listener {

    private final String lore;
    private final Map<Player, BlockFace> playerBlockFaceMap;

    public ExcavatorListener(String lore) {
        this.lore = ChatColor.translateAlternateColorCodes('&', lore);
        playerBlockFaceMap = new HashMap<>();
    }

    @EventHandler
    public void onPlayerInteractEvent(PlayerInteractEvent event) {
        // If the player isn't breaking a block, ignore the event
        if (event.getAction() != Action.LEFT_CLICK_BLOCK || event.getItem() == null) {
            return;
        }
        if (!verifyExcavator(event.getPlayer().getInventory().getItemInMainHand())) {
            return;
        }
        playerBlockFaceMap.put(event.getPlayer(), event.getBlockFace());
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (event.isCancelled()) return;
        // Update Excavator to new system
        if (verifyLore(event.getPlayer().getInventory().getItemInMainHand())
                && !verifyExcavator(event.getPlayer().getInventory().getItemInMainHand())) {
            updateExcavator(event.getPlayer());
        }
        // Verify that the tool is a Excavator, and that the player has a known block face
        if (!verifyExcavator(event.getPlayer().getInventory().getItemInMainHand())) {
            return;
        }
        if (!playerBlockFaceMap.containsKey(event.getPlayer())) {
            return;
        }
        if (!instance.getAreaToolController().checkExcavator(event.getBlock().getType())) {
            return;
        }
        // Check the player has access to the block and is in survival mode
        if (!instance.getBlockAccessController().checkBreakPlace(event.getPlayer(), event.getBlock().getLocation(), false)) {
            return;
        }
        if (event.getPlayer().getGameMode() != GameMode.SURVIVAL) {
            return;
        }

        // Get Extra Blocks
        List<Block> extraBlocks = AreaToolController.getBlockList(event.getBlock(), playerBlockFaceMap.get(event.getPlayer()));
        int blocksBroken = 0;
        // Iterate through the extra blocks
        for (Block block : extraBlocks) {
            // If it is the initial block, ignore
            if (block.getLocation() == event.getBlock().getLocation()) continue;
            // If the block is not a Excavator material or you do not have access to it, ignore
            if (!instance.getAreaToolController().checkExcavator(block.getType())) continue;
            if (!instance.getBlockAccessController().checkBreakPlace(event.getPlayer(), block.getLocation(), true))
                continue;
            // Log the block as broken, then break it
            try { JobsRebornHook.addExperienceForBlockBreak(block, event.getPlayer()); } catch (NoClassDefFoundError ignored) {}
            McMMOHook.addBlockExperience(block.getState(), event.getPlayer());
            CoreProtectLogger.logBlockBreak(event.getPlayer(), block);
            block.breakNaturally(event.getPlayer().getInventory().getItemInMainHand());
            blocksBroken++;
        }
        // Take the durability from the tool
        takeDurability(event.getPlayer(), event.getPlayer().getInventory().getItemInMainHand(), Math.floorDiv(blocksBroken, 3));

    }

    private void updateExcavator(Player player) {
        ItemMeta iMeta = player.getInventory().getItemInMainHand().getItemMeta();
        Material type = player.getInventory().getItemInMainHand().getType();
        int id;
        if (iMeta != null) {
            switch (type) {
                case WOODEN_SHOVEL:
                    id = 21;
                    break;
                case STONE_SHOVEL:
                    id = 22;
                    break;
                case IRON_SHOVEL:
                    id = 23;
                    break;
                case GOLDEN_SHOVEL:
                    id = 24;
                    break;
                case DIAMOND_SHOVEL:
                    id = 25;
                    break;
                default:
                    return;
            }
            iMeta.setCustomModelData(KEY_BASE + AT_KEY_BASE + id);
            player.getInventory().getItemInMainHand().setItemMeta(iMeta);
        }
    }

    private boolean verifyExcavator(ItemStack tool) {
        return shovels.contains(tool.getType())
                && tool.getItemMeta() != null
                && tool.getItemMeta().hasCustomModelData()
                && (tool.getItemMeta().getCustomModelData() == KEY_BASE + AT_KEY_BASE + 21
                || tool.getItemMeta().getCustomModelData() == KEY_BASE + AT_KEY_BASE + 22
                || tool.getItemMeta().getCustomModelData() == KEY_BASE + AT_KEY_BASE + 23
                || tool.getItemMeta().getCustomModelData() == KEY_BASE + AT_KEY_BASE + 24
                || tool.getItemMeta().getCustomModelData() == KEY_BASE + AT_KEY_BASE + 25
                || tool.getItemMeta().getCustomModelData() == KEY_BASE + AT_KEY_BASE + 26);
    }

    private boolean verifyLore(ItemStack tool) {
        return shovels.contains(tool.getType())
                && tool.getItemMeta() != null
                && tool.getItemMeta().getLore() != null
                && tool.getItemMeta().getLore().contains(lore);
    }
}
