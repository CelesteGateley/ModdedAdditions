package xyz.fluxinc.moddedadditions.controllers.customitems;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import xyz.fluxinc.moddedadditions.ModdedAdditions;
import xyz.fluxinc.moddedadditions.spells.Spell;
import xyz.fluxinc.moddedadditions.spells.SpellRegistry;

import java.util.List;

import static xyz.fluxinc.fluxcore.utils.LoreUtils.addLore;
import static xyz.fluxinc.fluxcore.utils.StringUtils.toTitleCase;
import static xyz.fluxinc.moddedadditions.ModdedAdditions.KEY_BASE;

public class SpellBookController {

    private ModdedAdditions instance;
    private SpellRegistry spellRegistry;
    public static final int SB_KEY_BASE = 3000;

    public SpellBookController(ModdedAdditions instance) {
        this.instance = instance;

        spellRegistry = new SpellRegistry(instance);
    }

    public static boolean verifySpellBook(ItemStack itemStack) {
        return itemStack.getItemMeta() != null
            && itemStack.getItemMeta().hasCustomModelData()
            && itemStack.getItemMeta().getCustomModelData() > KEY_BASE + SB_KEY_BASE
            && itemStack.getItemMeta().getCustomModelData() < KEY_BASE + SB_KEY_BASE + 10;
    }

    public ItemStack generateNewSpellBook() {
        ItemStack itemStack = addLore(new ItemStack(Material.BOOK), instance.getLanguageManager().getFormattedString("mi-spellbook"));
        return setSpell(KEY_BASE + SB_KEY_BASE + 1, itemStack);
    }

    public Spell getSpell(ItemStack spellBook) {
        if (!verifySpellBook(spellBook)) { return null; }
        return spellRegistry.getSpellById(spellBook.getItemMeta().getCustomModelData());
    }

    public ItemStack setSpell(int spellId, ItemStack spellBook) {
        ItemMeta iMeta = spellBook.getItemMeta();
        List<String> lore = iMeta.getLore();
        Spell spell = spellRegistry.getSpellById(spellBook.getItemMeta().getCustomModelData());
        if (lore.size() < 2) {
            lore.add("Current Spell: " + toTitleCase(spell.getName()));
        } else {
            lore.set(lore.size() - 1, "Current Spell: " + toTitleCase(spell.getName()));
        }
        iMeta.setDisplayName("Spellbook: " + toTitleCase(spell.getName()));
        iMeta.setCustomModelData(spellId);
        iMeta.setLore(lore);
        spellBook.setItemMeta(iMeta);
        return spellBook;
    }

    public boolean knowsSpell(Player player, Spell spell) {
        return instance.getPlayerDataController().getPlayerData(player).knowsSpell(spell.getName());
    }

    public Spell getSpell(int modelId) {
        return spellRegistry.getSpellById(modelId);
    }

    public SpellRegistry getSpellRegistry() {
        return spellRegistry;
    }
}
