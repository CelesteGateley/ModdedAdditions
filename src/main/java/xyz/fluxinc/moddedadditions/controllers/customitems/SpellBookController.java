package xyz.fluxinc.moddedadditions.controllers.customitems;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import xyz.fluxinc.moddedadditions.ModdedAdditions;
import xyz.fluxinc.moddedadditions.spells.Spell;
import xyz.fluxinc.moddedadditions.spells.SpellRegistry;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static xyz.fluxinc.fluxcore.utils.LoreUtils.addLore;
import static xyz.fluxinc.fluxcore.utils.StringUtils.toTitleCase;
import static xyz.fluxinc.moddedadditions.ModdedAdditions.KEY_BASE;

public class SpellBookController {

    public static final int SB_KEY_BASE = 3000;
    private final SpellRegistry spellRegistry;
    private final Map<Player, Long> lavaWalkerPlayers;

    public SpellBookController() {
        spellRegistry = new SpellRegistry();
        lavaWalkerPlayers = new HashMap<>();
    }

    public boolean canLavaWalk(Player player) {
        if (lavaWalkerPlayers.containsKey(player)) {
            if (lavaWalkerPlayers.get(player) + 30*1000 > System.currentTimeMillis()) return true;
            lavaWalkerPlayers.remove(player);
            player.sendTitle("Lava walk has worn off", "", 10, 70, 20);
        }
        return false;
    }

    public void addLavaWalk(Player player) {
        lavaWalkerPlayers.put(player, System.currentTimeMillis());
    }

    public static boolean verifySpellBook(ItemStack itemStack) {
        return itemStack.getItemMeta() != null
                && itemStack.getItemMeta().hasCustomModelData()
                && itemStack.getItemMeta().getCustomModelData() >= KEY_BASE + SB_KEY_BASE
                && itemStack.getItemMeta().getCustomModelData() < KEY_BASE + SB_KEY_BASE + 1000;
    }

    public ItemStack generateNewSpellBook() {
        ItemStack itemStack = addLore(new ItemStack(Material.BOOK), ModdedAdditions.instance.getLanguageManager().getFormattedString("mi-spellbook"));
        return setSpell(KEY_BASE + SB_KEY_BASE, itemStack);
    }

    public Spell getSpell(ItemStack spellBook) {
        if (!verifySpellBook(spellBook)) {
            return null;
        }
        if (spellBook.getItemMeta().getCustomModelData() == KEY_BASE + SB_KEY_BASE) {
            return null;
        }
        return spellRegistry.getSpellById(spellBook.getItemMeta().getCustomModelData());
    }

    public ItemStack setSpell(int spellId, ItemStack spellBook) {
        ItemMeta iMeta = spellBook.getItemMeta();
        List<String> lore = iMeta.getLore();

        if (spellId == KEY_BASE + SB_KEY_BASE) {
            iMeta.setDisplayName("Spellbook");
            iMeta.setCustomModelData(spellId);
            spellBook.setItemMeta(iMeta);
            return spellBook;
        }

        Spell spell = spellRegistry.getSpellById(spellId);
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

    public boolean knowsSpell(Player player, String spell) {
        return ModdedAdditions.instance.getPlayerDataController().getPlayerData(player).knowsSpell(spell);
    }

    public Spell getSpell(int modelId) {
        return spellRegistry.getSpellById(modelId);
    }

    public SpellRegistry getSpellRegistry() {
        return spellRegistry;
    }
}
