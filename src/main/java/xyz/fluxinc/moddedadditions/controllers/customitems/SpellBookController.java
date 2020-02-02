package xyz.fluxinc.moddedadditions.controllers.customitems;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import xyz.fluxinc.moddedadditions.ModdedAdditions;
import xyz.fluxinc.moddedadditions.spells.Spell;
import xyz.fluxinc.moddedadditions.spells.castable.Arrows;
import xyz.fluxinc.moddedadditions.spells.castable.Fireball;
import xyz.fluxinc.moddedadditions.spells.castable.Heal;
import xyz.fluxinc.moddedadditions.spells.castable.Teleport;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static xyz.fluxinc.fluxcore.utils.LoreUtils.addLore;
import static xyz.fluxinc.fluxcore.utils.StringUtils.toTitleCase;
import static xyz.fluxinc.moddedadditions.ModdedAdditions.KEY_BASE;

public class SpellBookController {

    private ModdedAdditions instance;
    private Map<Integer, Spell> spells;
    public static final int SB_KEY_BASE = 3000;

    public SpellBookController(ModdedAdditions instance) {
        this.instance = instance;

        spells = getAllSpells();
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
        return spells.getOrDefault(spellBook.getItemMeta().getCustomModelData(), null);
    }

    public ItemStack setSpell(int spellId, ItemStack spellBook) {
        ItemMeta iMeta = spellBook.getItemMeta();
        List<String> lore = iMeta.getLore();
        if (lore.size() < 2) {
            lore.add("Current Spell: " + toTitleCase(spells.get(spellId).getName()));
        } else {
            lore.set(lore.size() - 1, "Current Spell: " + toTitleCase(spells.get(spellId).getName()));
        }
        iMeta.setDisplayName("Spellbook: " + toTitleCase(spells.get(spellId).getName()));
        iMeta.setCustomModelData(spellId);
        iMeta.setLore(lore);
        spellBook.setItemMeta(iMeta);
        return spellBook;
    }

    public boolean knowsSpell(Player player, Spell spell) {
        return instance.getPlayerDataController().getPlayerData(player).knowsSpell(spell.getName());
    }

    public Map<Integer, Spell> getAllSpells() {
        Map<Integer, Spell> spells = new LinkedHashMap<>();
        spells.put(KEY_BASE + SB_KEY_BASE + 1, new Fireball(instance));
        spells.put(KEY_BASE + SB_KEY_BASE + 2, new Teleport(instance));
        spells.put(KEY_BASE + SB_KEY_BASE + 3, new Arrows(instance));
        spells.put(KEY_BASE + SB_KEY_BASE + 4, new Heal(instance));
        return spells;
    }

    public Spell getSpell(int modelId) {
        return spells.getOrDefault(modelId, null);
    }
}
