package xyz.fluxinc.moddedadditions.common.events;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;

public class SpecialBlockBreakEvent extends BlockBreakEvent {

    public SpecialBlockBreakEvent(Block theBlock, Player player) {
        super(theBlock, player);
    }
}
