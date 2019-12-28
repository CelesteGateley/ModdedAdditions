package xyz.fluxinc.moddedadditions.listeners;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import xyz.fluxinc.fluxcore.security.CoreProtectLogger;
import xyz.fluxinc.moddedadditions.ModdedAdditions;
import xyz.fluxinc.moddedadditions.controllers.AreaToolController;

import java.util.*;

import static xyz.fluxinc.moddedadditions.storage.Tools.shovels;

public class ExcavatorListener implements Listener {

    private ModdedAdditions instance;
    private String lore;
    private AreaToolController areaToolController;
    private Map<Player, BlockFace> playerBlockFaceMap;

    public ExcavatorListener(ModdedAdditions instance, String lore) {
        this.instance = instance;
        this.areaToolController = instance.getAreaToolController();
        this.lore = ChatColor.translateAlternateColorCodes('&', lore);
        playerBlockFaceMap = new HashMap<>();
    }

    @EventHandler
    public void onPlayerInteractEvent(PlayerInteractEvent event) {
        // If the player isn't breaking a block, ignore the event
        if (event.getAction() != Action.LEFT_CLICK_BLOCK || event.getItem() == null) { return; }
        if (!verifyExcavator(event.getPlayer().getInventory().getItemInMainHand())) { return; }
        playerBlockFaceMap.put(event.getPlayer(), event.getBlockFace());
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        // Verify that the tool is a Excavator, and that the player has a known block face
        if (!verifyExcavator(event.getPlayer().getInventory().getItemInMainHand())) { return; }
        if (!playerBlockFaceMap.containsKey(event.getPlayer())) { return; }
        if (!areaToolController.checkExcavator(event.getBlock().getType())) { return; }
        // Check the player has access to the block and is in survival mode
        if (!instance.getBlockAccessController().checkBreakPlace(event.getPlayer(), event.getBlock().getLocation(), true)) { return; }
        if (event.getPlayer().getGameMode() != GameMode.SURVIVAL) { return; }

        // Extract the X Y Z coordinates and world for easy access
        int x = event.getBlock().getX(); int y = event.getBlock().getY(); int z = event.getBlock().getZ();
        World world = event.getBlock().getWorld();
        // Create a list for the extra blocks
        List<Block> extraBlocks = new ArrayList<>();

        // Switch to get the surrounding blocks based on the block face
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
        // Iterate through the extra blocks
        for (Block block : extraBlocks) {
            // If it is the initial block, ignore
            if (block.getLocation() == event.getBlock().getLocation()) { continue; }
            // If the block is not a Excavator material or you do not have access to it, ignore
            if (!areaToolController.checkExcavator(block.getType())) { continue; }
            if (!instance.getBlockAccessController().checkBreakPlace(event.getPlayer(), block.getLocation(), true)) { continue; }
            // Log the block as broken, then break it
            CoreProtectLogger.logBlockBreak(event.getPlayer(), block);
            block.breakNaturally(event.getPlayer().getInventory().getItemInMainHand());
        }
        // Take the durability from the tool
        takeDurability(event.getPlayer().getInventory().getItemInMainHand());

    }

    private boolean verifyExcavator(ItemStack tool) {
        return shovels.contains(tool.getType())
                && tool.getItemMeta() != null
                && tool.getItemMeta().getLore() != null
                && tool.getItemMeta().getLore().contains(lore);
    }

    private void takeDurability(ItemStack tool) {
        Random random = new Random();
        double chance = (100D / (getUnbreakingLevel(tool)+1));
        ItemMeta iMeta = tool.getItemMeta();
        if (iMeta instanceof Damageable) {
            Damageable damageable = (Damageable) iMeta;
            for (int i = 0; i < 3; i++) {
                double rand = random.nextInt(99) + 1;
                if (rand >= chance) { continue; }
                damageable.setDamage(damageable.getDamage()+1);
            }
            tool.setItemMeta((ItemMeta) damageable);
        }
    }

    private int getUnbreakingLevel(ItemStack tool) {
        ItemMeta itemMeta = tool.getItemMeta();
        if (itemMeta == null) { return 0; }
        return itemMeta.getEnchantLevel(Enchantment.DURABILITY);
    }
}
