package xyz.fluxinc.moddedadditions.listeners;

import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import xyz.fluxinc.moddedadditions.ModdedAdditions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static xyz.fluxinc.moddedadditions.storage.Tools.pickaxes;

public class HammerListener implements Listener {

    private ModdedAdditions instance;
    private String lore;
    private Map<Player, BlockFace> playerBlockFaceMap;

    public HammerListener(ModdedAdditions instance, String lore) {
        this.instance = instance;
        this.lore = lore;
        playerBlockFaceMap = new HashMap<>();
    }

    @EventHandler
    public void onPlayerInteractEvent(PlayerInteractEvent event) {
        // If the player isn't breaking a block, ignore the event
        if (event.getAction() != Action.LEFT_CLICK_BLOCK || event.getItem() == null) { return; }
        if (!verifyHammer(event.getItem())) { return; }

        playerBlockFaceMap.put(event.getPlayer(), event.getBlockFace());
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (!verifyHammer(event.getPlayer().getInventory().getItemInMainHand())) { return; }
        if (!playerBlockFaceMap.containsKey(event.getPlayer())) { return; }
        int x = event.getBlock().getX();
        int y = event.getBlock().getY();
        int z = event.getBlock().getZ();
        World world = event.getBlock().getWorld();
        List<Block> extraBlocks = new ArrayList<>();
        switch (playerBlockFaceMap.get(event.getPlayer())) {
            case UP:
            case DOWN:
                for (int xMod = -1; xMod < 2; xMod++) { for (int zMod = -1; zMod < 2; zMod++) { extraBlocks.add(world.getBlockAt(x+xMod, y, z+zMod)); }}
                break;
            case EAST:
            case WEST:
                for (int yMod = -1; yMod < 2; yMod++) { for (int zMod = -1; zMod < 2; zMod++) { extraBlocks.add(world.getBlockAt(x, y+yMod, z+zMod)); }}
                break;
            case NORTH:
            case SOUTH:
                for (int yMod = -1; yMod < 2; yMod++) { for (int xMod = -1; xMod < 2; xMod++) { extraBlocks.add(world.getBlockAt(x+xMod, y+yMod, z)); }}
                break;
            default:
                break;
        }

    }

    private boolean verifyHammer(ItemStack tool) {
        return pickaxes.contains(tool.getType()) && tool.getItemMeta() != null && tool.getItemMeta().getLore().contains(lore);
    }
}
