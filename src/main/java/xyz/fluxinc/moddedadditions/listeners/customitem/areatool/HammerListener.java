package xyz.fluxinc.moddedadditions.listeners.customitem.areatool;

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
import xyz.fluxinc.fluxcore.security.CoreProtectLogger;
import xyz.fluxinc.moddedadditions.ModdedAdditions;
import xyz.fluxinc.moddedadditions.controllers.AreaToolController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static xyz.fluxinc.moddedadditions.controllers.AreaToolController.takeDurability;
import static xyz.fluxinc.moddedadditions.storage.Tools.pickaxes;

public class HammerListener implements Listener {

    private ModdedAdditions instance;
    private String lore;
    private AreaToolController areaToolController;
    private Map<Player, BlockFace> playerBlockFaceMap;

    public HammerListener(ModdedAdditions instance, String lore) {
        this.instance = instance;
        this.areaToolController = instance.getAreaToolController();
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
        // Verify that the tool is a hammer, and that the player has a known block face
        if (!verifyHammer(event.getPlayer().getInventory().getItemInMainHand())) {
            return;
        }
        if (!playerBlockFaceMap.containsKey(event.getPlayer())) {
            return;
        }
        if (!areaToolController.checkHammer(event.getBlock().getType())) {
            return;
        }
        // Check the player has access to the block and is in survival mode
        if (!instance.getBlockAccessController().checkBreakPlace(event.getPlayer(), event.getBlock().getLocation(), true)) {
            return;
        }
        if (event.getPlayer().getGameMode() != GameMode.SURVIVAL) {
            return;
        }
        // Obsidian checking
        boolean breakObsidian = event.getBlock().getType() == Material.OBSIDIAN;

        // Get Extra Blocks
        List<Block> extraBlocks = AreaToolController.getBlockList(event.getBlock(), playerBlockFaceMap.get(event.getPlayer()));

        // Iterate through the extra blocks
        for (Block block : extraBlocks) {
            // If it is the initial block, ignore
            if (block.getLocation() == event.getBlock().getLocation()) {
                continue;
            }
            // If the initial block is not obsidian and the block is, ignore
            if (block.getType() == Material.OBSIDIAN && !breakObsidian) {
                continue;
            }
            // If the block is not a hammer material or you do not have access to it, ignore
            if (!areaToolController.checkHammer(block.getType())) {
                continue;
            }
            if (!instance.getBlockAccessController().checkBreakPlace(event.getPlayer(), block.getLocation(), true)) {
                continue;
            }
            // Log the block as broken, then break it
            CoreProtectLogger.logBlockBreak(event.getPlayer(), block);
            block.breakNaturally(event.getPlayer().getInventory().getItemInMainHand());
        }
        // Take the durability from the tool
        takeDurability(event.getPlayer(), event.getPlayer().getInventory().getItemInMainHand());
    }

    private boolean verifyHammer(ItemStack tool) {
        return pickaxes.contains(tool.getType())
                && tool.getItemMeta() != null
                && tool.getItemMeta().getLore() != null
                && tool.getItemMeta().getLore().contains(lore);
    }


}
