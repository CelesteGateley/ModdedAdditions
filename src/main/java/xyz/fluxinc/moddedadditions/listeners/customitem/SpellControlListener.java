package xyz.fluxinc.moddedadditions.listeners.customitem;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import xyz.fluxinc.moddedadditions.controllers.customitems.SpellBookController;
import xyz.fluxinc.moddedadditions.listeners.customitem.spells.ResearchInventoryListener;
import xyz.fluxinc.moddedadditions.spells.Spell;
import xyz.fluxinc.moddedadditions.spells.SpellSchool;
import xyz.fluxinc.moddedadditions.storage.PlayerData;
import xyz.fluxinc.moddedadditions.utils.registries.SpellRegistry;

import java.util.ArrayList;
import java.util.List;

import static xyz.fluxinc.fluxcore.utils.InventoryUtils.generateDistributedInventory;
import static xyz.fluxinc.fluxcore.utils.LoreUtils.addLore;
import static xyz.fluxinc.moddedadditions.ModdedAdditions.instance;
import static xyz.fluxinc.moddedadditions.controllers.customitems.SpellBookController.verifySpellBook;

public class SpellControlListener implements Listener {

    public static final String SELECT_SPELL = "Select Spell";
    public static final String SELECT_SCHOOL = "Select School";

    @EventHandler
    public void onSchoolSelect(InventoryClickEvent event) {
        if (!event.getView().getTitle().equals(SpellControlListener.SELECT_SCHOOL)) return;
        if (event.getClickedInventory() == null) return;
        event.setCancelled(true);
        if (event.getCurrentItem() == null) return;
        if (event.getClickedInventory().getType() == InventoryType.PLAYER) {
            event.setCancelled(true);
            return;
        }
        if (event.getCurrentItem().getType() == Material.BARRIER) {
            event.getWhoClicked().sendMessage(instance.getLanguageManager().generateMessage("sb-lockedSpell"));
            event.getView().close();
        } else if (event.getCurrentItem().getType() == Material.ENCHANTED_BOOK) {
            event.getView().close();
            Bukkit.getScheduler().scheduleSyncDelayedTask(instance, () -> ResearchInventoryListener.openInventory((Player) event.getWhoClicked()));
        } else {
            event.getView().close();
            SpellSchool school = SpellRegistry.getSchoolById(event.getCurrentItem().getItemMeta().getCustomModelData());
            Bukkit.getScheduler().scheduleSyncDelayedTask(instance, () -> event.getWhoClicked().openInventory(generateSpellInventory(school, (Player) event.getWhoClicked())));
        }
        event.getView().close();
    }

    @EventHandler
    public void onSpellSelect(InventoryClickEvent event) {
        if (!event.getView().getTitle().equals(SELECT_SPELL)) return;
        if (event.getClickedInventory() == null) return;
        event.setCancelled(true);
        if (event.getCurrentItem() == null) return;
        if (event.getClickedInventory().getType() == InventoryType.PLAYER) {
            event.setCancelled(true);
            return;
        }
        if (event.getCurrentItem().getType() == Material.BARRIER) {
            event.getWhoClicked().sendMessage(instance.getLanguageManager().generateMessage("sb-lockedSpell"));
            event.getView().close();
        } else if (event.getCurrentItem().getType() == Material.ENCHANTED_BOOK) {
            event.getView().close();
            Bukkit.getScheduler().scheduleSyncDelayedTask(instance, () -> ResearchInventoryListener.openInventory((Player) event.getWhoClicked()));
        } else {
            Player player = (Player) event.getWhoClicked();
            if (verifySpellBook(player.getInventory().getItemInMainHand())) {
                SpellBookController.setSpell(event.getCurrentItem().getItemMeta().getCustomModelData(), player.getInventory().getItemInMainHand());
            } else if (verifySpellBook(player.getInventory().getItemInOffHand())) {
                SpellBookController.setSpell(event.getCurrentItem().getItemMeta().getCustomModelData(), player.getInventory().getItemInOffHand());
            }
        }
        event.getView().close();
    }

    public static Inventory generateSchoolInventory(Player player) {
        List<ItemStack> stacks = new ArrayList<>();
        for (SpellSchool school : SpellRegistry.getAllSchools()) {
            if (SpellBookController.hasSchool(player, school.getTechnicalName())) {
                stacks.add(school.getItemStack());
            } else {
                ItemStack iStack = addLore(new ItemStack(Material.BARRIER), ChatColor.translateAlternateColorCodes('&', school.getRiddle()));
                ItemMeta iMeta = iStack.getItemMeta();
                iMeta.setDisplayName(school.getLocalizedName());
                iStack.setItemMeta(iMeta);
                stacks.add(iStack);
            }
        }
        return addResearchButton(stacks, SELECT_SCHOOL);
    }

    private static Inventory addResearchButton(List<ItemStack> stacks, String selectSchool) {
        Inventory dummy = generateDistributedInventory(selectSchool, stacks);
        Inventory master = Bukkit.createInventory(null, dummy.getSize() + 9, selectSchool);
        for (int i = 0; i < dummy.getSize(); i++) {
            master.setItem(i, dummy.getItem(i));
        }
        ItemStack itemStack = addLore(new ItemStack(Material.ENCHANTED_BOOK), "Research new Spells!");
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(ChatColor.RESET + "Research");
        itemStack.setItemMeta(itemMeta);
        master.setItem(master.getSize() - 5, itemStack);
        return master;
    }

    public static Inventory generateSpellInventory(SpellSchool school, Player player) {
        List<Spell> spells = school.getSpells();
        PlayerData data = instance.getPlayerDataController().getPlayerData(player);
        List<ItemStack> stacks = new ArrayList<>();
        for (Spell spell : spells) {
            if (SpellBookController.knowsSpell(player, spell.getTechnicalName())) {
                stacks.add(spell.getItemStack(player.getWorld().getEnvironment(), spell.getModelId(), data.getSpellLevel(spell.getTechnicalName())));
            } else {
                ItemStack iStack = addLore(new ItemStack(Material.BARRIER), ChatColor.translateAlternateColorCodes('&', spell.getRiddle(data.getSpellLevel(spell.getTechnicalName()))));
                ItemMeta iMeta = iStack.getItemMeta();
                iMeta.setDisplayName(spell.getLocalizedName());
                iStack.setItemMeta(iMeta);
                stacks.add(iStack);
            }
        }
        return addResearchButton(stacks, SELECT_SPELL);
    }
}
