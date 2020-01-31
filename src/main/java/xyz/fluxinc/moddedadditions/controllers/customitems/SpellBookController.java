package xyz.fluxinc.moddedadditions.controllers.customitems;

import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import xyz.fluxinc.moddedadditions.ModdedAdditions;
import xyz.fluxinc.moddedadditions.enums.Spell;

import java.util.List;

import static xyz.fluxinc.fluxcore.utils.LoreUtils.addLore;
import static xyz.fluxinc.fluxcore.utils.StringUtils.toTitleCase;
import static xyz.fluxinc.moddedadditions.ModdedAdditions.KEY_BASE;

public class SpellBookController {

    private ModdedAdditions instance;
    public static final int SB_KEY_BASE = 3000;

    public SpellBookController(ModdedAdditions instance) {
        this.instance = instance;
    }

    public static boolean verifySpellBook(ItemStack itemStack) {
        return itemStack.getItemMeta() != null
            && itemStack.getItemMeta().hasCustomModelData()
            && itemStack.getItemMeta().getCustomModelData() > KEY_BASE + SB_KEY_BASE
            && itemStack.getItemMeta().getCustomModelData() < KEY_BASE + SB_KEY_BASE + 10;
    }

    public ItemStack generateNewSpellBook() {
        ItemStack itemStack = addLore(new ItemStack(Material.BOOK), instance.getLanguageManager().getFormattedString("mi-spellbook"));
        return setSpell(Spell.FIREBALL, itemStack);
    }

    public Spell getSpell(ItemStack spellBook) {
        if (!verifySpellBook(spellBook)) { return null; }
        return Spell.getSpell(spellBook.getItemMeta().getCustomModelData() - KEY_BASE - SB_KEY_BASE);
    }

    public ItemStack setSpell(Spell spell, ItemStack spellBook) {
        ItemMeta iMeta = spellBook.getItemMeta();
        iMeta.setCustomModelData(KEY_BASE + SB_KEY_BASE + Spell.getModelId(spell));
        List<String> lore = iMeta.getLore();
        if (lore.size() < 2) {
            lore.add("Current Spell: " + toTitleCase(spell.toString()));
        } else {
            lore.set(lore.size() - 1, "Current Spell: " + toTitleCase(spell.toString()));
        }
        iMeta.setLore(lore);
        spellBook.setItemMeta(iMeta);
        return spellBook;
    }
}
