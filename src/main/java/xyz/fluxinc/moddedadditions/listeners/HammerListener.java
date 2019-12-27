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

import static xyz.fluxinc.moddedadditions.storage.Tools.pickaxes;

public class HammerListener implements Listener {

    private CoreProtectLogger coreProtectLogger;
    private ModdedAdditions instance;
    private String lore;
    private AreaToolController areaToolController;
    private Map<Player, BlockFace> playerBlockFaceMap;

    public HammerListener(ModdedAdditions instance, String lore) {
        this.instance = instance;
        this.areaToolController = instance.getAreaToolController();
        this.lore = ChatColor.translateAlternateColorCodes('&', lore);
        playerBlockFaceMap = new HashMap<>();
        this.coreProtectLogger = instance.getCoreInstance().getCoreProtectLogger();
    }

    @EventHandler
    public void onPlayerInteractEvent(PlayerInteractEvent event) {
        // If the player isn't breaking a block, ignore the event
        if (event.getAction() != Action.LEFT_CLICK_BLOCK || event.getItem() == null) { return; }
        if (!verifyHammer(event.getPlayer().getInventory().getItemInMainHand())) { return; }
        playerBlockFaceMap.put(event.getPlayer(), event.getBlockFace());
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (!verifyHammer(event.getPlayer().getInventory().getItemInMainHand())) { return; }
        if (!playerBlockFaceMap.containsKey(event.getPlayer())) { return; }
        if (!areaToolController.checkHammer(event.getBlock().getType())) { return; }
        if (!instance.getBlockAccessController().checkBreakPlace(event.getPlayer(), event.getBlock().getLocation(), true)) { return; }
        if (event.getPlayer().getGameMode() != GameMode.SURVIVAL) { return; }
        boolean breakObsidian = event.getBlock().getType() == Material.OBSIDIAN;
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
        for (Block block : extraBlocks) {
            if (block.getLocation() == event.getBlock().getLocation()) { continue; }
            if (block.getType() == Material.OBSIDIAN && !breakObsidian) { continue; }
            if (!areaToolController.checkHammer(block.getType())) { continue; }
            if (!instance.getBlockAccessController().checkBreakPlace(event.getPlayer(), block.getLocation(), true)) { continue; }
            block.breakNaturally(event.getPlayer().getInventory().getItemInMainHand());
            coreProtectLogger.logBlockBreak(event.getPlayer(), block);
        }
        takeDurability(event.getPlayer().getInventory().getItemInMainHand());

    }

    private boolean verifyHammer(ItemStack tool) {
        return pickaxes.contains(tool.getType())
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
