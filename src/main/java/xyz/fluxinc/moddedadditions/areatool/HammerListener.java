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
import xyz.fluxinc.fluxcore.hooks.JobsRebornHook;
import xyz.fluxinc.fluxcore.hooks.McMMOHook;
import xyz.fluxinc.fluxcore.security.CoreProtectLogger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static xyz.fluxinc.fluxcore.utils.MineabilityUtils.verifyBlockMining;
import static xyz.fluxinc.fluxcore.utils.ToolUtils.pickaxes;
import static xyz.fluxinc.fluxcore.utils.ToolUtils.takeDurability;
import static xyz.fluxinc.moddedadditions.ModdedAdditions.KEY_BASE;
import static xyz.fluxinc.moddedadditions.ModdedAdditions.instance;
import static xyz.fluxinc.moddedadditions.areatool.AreaToolController.AT_KEY_BASE;

@SuppressWarnings("BooleanMethodIsAlwaysInverted")
public class HammerListener implements Listener {

    private final String lore;
    private final Map<Player, BlockFace> playerBlockFaceMap;

    public HammerListener(String lore) {
        this.lore = ChatColor.translateAlternateColorCodes('&', lore);
        playerBlockFaceMap = new HashMap<>();
    }

    @EventHandler
    public void onPlayerInteractEvent(PlayerInteractEvent event) {
        // If the player isn't breaking a block, ignore the event
        if (event.getAction() != Action.LEFT_CLICK_BLOCK || event.getItem() == null) {
            return;
        }
        if (!verifyHammer(event.getPlayer().getInventory().getItemInMainHand())) {
            return;
        }
        playerBlockFaceMap.put(event.getPlayer(), event.getBlockFace());
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        // Update Hammer to new system
        if (verifyLore(event.getPlayer().getInventory().getItemInMainHand())
                && !verifyHammer(event.getPlayer().getInventory().getItemInMainHand())) {
            updateHammer(event.getPlayer());
        }
        // Verify that the tool is a hammer, and that the player has a known block face
        if (!verifyHammer(event.getPlayer().getInventory().getItemInMainHand())) return;
        if (!verifyBlockMining(event.getPlayer().getInventory().getItemInMainHand(), event.getBlock().getType()))
            return;
        if (!playerBlockFaceMap.containsKey(event.getPlayer())) return;
        if (!instance.getAreaToolController().checkHammer(event.getBlock().getType())) return;

        // Check the player has access to the block and is in survival mode
        if (!instance.getBlockAccessController().checkBreakPlace(event.getPlayer(), event.getBlock().getLocation(), true))
            return;
        if (event.getPlayer().getGameMode() != GameMode.SURVIVAL) return;
        // Obsidian checking
        boolean breakObsidian = event.getBlock().getType() == Material.OBSIDIAN;

        // Get Extra Blocks
        List<Block> extraBlocks = AreaToolController.getBlockList(event.getBlock(), playerBlockFaceMap.get(event.getPlayer()));
        int blocksBroken = 0;
        // Iterate through the extra blocks
        for (Block block : extraBlocks) {
            // If it is the initial block, ignore
            if (block.getLocation() == event.getBlock().getLocation()) continue;
            // If the initial block is not obsidian and the block is, ignore
            if (block.getType() == Material.OBSIDIAN && !breakObsidian) continue;
            // If the block is not a hammer material or you do not have access to it, ignore
            if (!instance.getAreaToolController().checkHammer(block.getType())) continue;
            if (!instance.getBlockAccessController().checkBreakPlace(event.getPlayer(), block.getLocation(), true))
                continue;
            // If the block is not mineable by the tool, ignore
            if (!verifyBlockMining(event.getPlayer().getInventory().getItemInMainHand(), block.getType())) continue;
            // Log the block as broken, then break it
            try {
                JobsRebornHook.addExperienceForBlockBreak(block, event.getPlayer());
            } catch (NoClassDefFoundError ignored) {
            }
            McMMOHook.addBlockExperience(block.getState(), event.getPlayer());
            CoreProtectLogger.logBlockBreak(event.getPlayer(), block);
            block.breakNaturally(event.getPlayer().getInventory().getItemInMainHand());
            blocksBroken++;
        }
        // Take the durability from the tool
        takeDurability(event.getPlayer(), event.getPlayer().getInventory().getItemInMainHand(), Math.floorDiv(blocksBroken, 3));
    }

    private void updateHammer(Player player) {
        ItemMeta iMeta = player.getInventory().getItemInMainHand().getItemMeta();
        Material type = player.getInventory().getItemInMainHand().getType();
        int id;
        if (iMeta != null) {
            switch (type) {
                case WOODEN_PICKAXE:
                    id = 11;
                    break;
                case STONE_PICKAXE:
                    id = 12;
                    break;
                case IRON_PICKAXE:
                    id = 13;
                    break;
                case GOLDEN_PICKAXE:
                    id = 14;
                    break;
                case DIAMOND_PICKAXE:
                    id = 15;
                    break;
                default:
                    return;
            }
            iMeta.setCustomModelData(KEY_BASE + AT_KEY_BASE + id);
            player.getInventory().getItemInMainHand().setItemMeta(iMeta);
        }
    }

    private boolean verifyHammer(ItemStack tool) {
        return pickaxes.contains(tool.getType())
                && tool.getItemMeta() != null
                && tool.getItemMeta().hasCustomModelData()
                && (tool.getItemMeta().getCustomModelData() == KEY_BASE + AT_KEY_BASE + 11
                || tool.getItemMeta().getCustomModelData() == KEY_BASE + AT_KEY_BASE + 12
                || tool.getItemMeta().getCustomModelData() == KEY_BASE + AT_KEY_BASE + 13
                || tool.getItemMeta().getCustomModelData() == KEY_BASE + AT_KEY_BASE + 14
                || tool.getItemMeta().getCustomModelData() == KEY_BASE + AT_KEY_BASE + 15
                || tool.getItemMeta().getCustomModelData() == KEY_BASE + AT_KEY_BASE + 16);
    }

    private boolean verifyLore(ItemStack tool) {
        return pickaxes.contains(tool.getType())
                && tool.getItemMeta() != null
                && tool.getItemMeta().getLore() != null
                && tool.getItemMeta().getLore().contains(lore);
    }
}
