package xyz.fluxinc.moddedadditions.controllers.customitems;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import xyz.fluxinc.moddedadditions.ModdedAdditions;
import xyz.fluxinc.moddedadditions.spells.Spell;
import xyz.fluxinc.moddedadditions.utils.registries.SpellRegistry;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static xyz.fluxinc.fluxcore.utils.LoreUtils.addLore;
import static xyz.fluxinc.fluxcore.utils.StringUtils.toTitleCase;
import static xyz.fluxinc.moddedadditions.ModdedAdditions.KEY_BASE;

public class SpellBookController {

    public static final int SB_KEY_BASE = 3000;
    private final Map<Player, Long> lavaWalkerPlayers;

    public SpellBookController() {
        lavaWalkerPlayers = new HashMap<>();
    }

    public static boolean verifySpellBook(ItemStack itemStack) {
        return itemStack.getItemMeta() != null
                && itemStack.getItemMeta().hasCustomModelData()
                && itemStack.getItemMeta().getCustomModelData() >= KEY_BASE + SB_KEY_BASE
                && itemStack.getItemMeta().getCustomModelData() < KEY_BASE + SB_KEY_BASE + 1000;
    }

    public static ItemStack generateNewSpellBook() {
        ItemStack itemStack = addLore(new ItemStack(Material.BOOK), ModdedAdditions.instance.getLanguageManager().getFormattedString("mi-spellbook"));
        return setSpell(KEY_BASE + SB_KEY_BASE, itemStack);
    }

    public static Spell getSpell(ItemStack spellBook) {
        if (!verifySpellBook(spellBook)) {
            return null;
        }
        if (spellBook.getItemMeta().getCustomModelData() == KEY_BASE + SB_KEY_BASE) {
            return null;
        }
        return SpellRegistry.getSpellById(spellBook.getItemMeta().getCustomModelData());
    }

    public static ItemStack setSpell(int spellId, ItemStack spellBook) {
        ItemMeta iMeta = spellBook.getItemMeta();
        List<String> lore = iMeta.getLore();

        if (spellId == KEY_BASE + SB_KEY_BASE) {
            iMeta.setDisplayName("Spellbook");
            iMeta.setCustomModelData(spellId);
            spellBook.setItemMeta(iMeta);
            return spellBook;
        }

        Spell spell = SpellRegistry.getSpellById(spellId);
        if (lore.size() < 2) {
            lore.add("Current Spell: " + toTitleCase(spell.getLocalizedName()));
        } else {
            lore.set(lore.size() - 1, "Current Spell: " + toTitleCase(spell.getLocalizedName()));
        }
        iMeta.setDisplayName("Spellbook: " + toTitleCase(spell.getLocalizedName()));
        iMeta.setCustomModelData(spellId);
        iMeta.setLore(lore);
        spellBook.setItemMeta(iMeta);
        return spellBook;
    }

    public static boolean knowsSpell(Player player, String spell) {
        return ModdedAdditions.instance.getPlayerDataController().getPlayerData(player).knowsSpell(spell);
    }

    public static boolean hasSchool(Player player, String school) {
        return ModdedAdditions.instance.getPlayerDataController().getPlayerData(player).checkSchool(school);
    }

    public boolean canLavaWalk(Player player) {
        if (lavaWalkerPlayers.containsKey(player)) {
            if (lavaWalkerPlayers.get(player) > System.currentTimeMillis()) return true;
            lavaWalkerPlayers.remove(player);
            player.sendTitle("Lava walk has worn off", "", 10, 70, 20);
        }
        return false;
    }

    public void addLavaWalk(Player player, int duration) {
        lavaWalkerPlayers.put(player, System.currentTimeMillis() + (duration * 1000));
    }

}
