package xyz.fluxinc.moddedadditions.common.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.loot.Lootable;

import static xyz.fluxinc.moddedadditions.ModdedAdditions.instance;

public class PreventLootDestructionListener implements Listener {

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (!(event.getBlock().getBlockData() instanceof Lootable)) return;
        if (((Lootable)event.getBlock().getBlockData()).getLootTable() == null) return;
        event.getPlayer().sendMessage(instance.getLanguageManager().generateMessage("ma-breakLoot"));
        if (event.getPlayer().hasPermission("moddedadditions.lootchest.bypass")){
            event.getPlayer().sendMessage(instance.getLanguageManager().generateMessage("ma-breakBypass"));
            return;
        }
        event.setCancelled(true);
    }

    @EventHandler
    public void onBlockExplosion(BlockExplodeEvent event) {
        if (!(event.getBlock().getBlockData() instanceof Lootable)) return;
        if (((Lootable)event.getBlock().getBlockData()).getLootTable() == null) return;
        event.setCancelled(true);
    }

    @EventHandler
    public void onEnderManMoveEvent(EntityChangeBlockEvent event) {
        if (!(event.getBlock().getBlockData() instanceof Lootable)) return;
        if (((Lootable)event.getBlock().getBlockData()).getLootTable() == null) return;
        if (event.getEntity() instanceof Player) {
            if (event.getEntity().hasPermission("moddedadditions.lootchest.bypass")){
                event.getEntity().sendMessage(instance.getLanguageManager().generateMessage("ma-breakBypass"));
                return;
            }
            event.getEntity().sendMessage(instance.getLanguageManager().generateMessage("ma-breakLoot"));
        }
        event.setCancelled(true);
    }
}
