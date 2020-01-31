package xyz.fluxinc.moddedadditions.controllers.customitems;

import org.bukkit.inventory.ItemStack;
import xyz.fluxinc.moddedadditions.ModdedAdditions;

public class SpellBookController {

    private ModdedAdditions instance;

    public SpellBookController(ModdedAdditions instance) {
        this.instance = instance;
    }

    public boolean verifySpellBook(ItemStack itemStack) {
        return true;
    }
}
