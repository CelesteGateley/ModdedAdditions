package xyz.fluxinc.moddedadditions.listeners.customitem;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import xyz.fluxinc.moddedadditions.ModdedAdditions;
import xyz.fluxinc.moddedadditions.spells.Spell;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static xyz.fluxinc.fluxcore.utils.InventoryUtils.generateDistributedInventory;
import static xyz.fluxinc.fluxcore.utils.LoreUtils.addLore;
import static xyz.fluxinc.moddedadditions.controllers.customitems.SpellBookController.verifySpellBook;

public class SpellBookListener implements Listener {

    private ModdedAdditions instance;

    private static final String INVENTORY_TITLE = "Select Spell";
    private ItemStack blockedItem;

    public SpellBookListener(ModdedAdditions instance) {
        this.instance = instance;
        blockedItem = addLore(new ItemStack(Material.BARRIER), instance.getLanguageManager().getFormattedString("sb-lockedSpell"));
        ItemMeta itemMeta = blockedItem.getItemMeta();
        itemMeta.setDisplayName(ChatColor.WHITE + "Locked Spell");
        itemMeta.setCustomModelData(1);
        blockedItem.setItemMeta(itemMeta);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!event.getView().getTitle().equals(INVENTORY_TITLE)) { return; }
        if (event.getClickedInventory() == null) { return; }
        event.setCancelled(true);
        if (event.getCurrentItem() == null) { return; }
        if (event.getCurrentItem().getItemMeta() == null || !event.getCurrentItem().getItemMeta().hasCustomModelData()) { return; }


        if (event.getCurrentItem().getType() == Material.BARRIER) {
            event.getWhoClicked().sendMessage(instance.getLanguageManager().generateMessage("sb-lockedSpell"));
            event.getView().close();
        } else {
            Player player = (Player) event.getWhoClicked();
            if (verifySpellBook(player.getInventory().getItemInMainHand())) {
                instance.getSpellBookController().setSpell(event.getCurrentItem().getItemMeta().getCustomModelData(), player.getInventory().getItemInMainHand());
            } else if (verifySpellBook(player.getInventory().getItemInOffHand())) {
                instance.getSpellBookController().setSpell(event.getCurrentItem().getItemMeta().getCustomModelData(), player.getInventory().getItemInOffHand());
            }
        }

        event.getView().close();
    }

    @EventHandler
    public void onSlotChange(PlayerItemHeldEvent event) {
        ItemStack newStack = event.getPlayer().getInventory().getItem(event.getNewSlot());
        if (newStack == null) {
            instance.getManaController().hideManaBar(event.getPlayer());
        } else if (verifySpellBook(newStack)) {
            instance.getManaController().showManaBar(event.getPlayer());
        } else if (event.getPlayer().getInventory().getItemInOffHand() != null
                && verifySpellBook(event.getPlayer().getInventory().getItemInOffHand())) {
            instance.getManaController().showManaBar(event.getPlayer());
        }
        else {
            instance.getManaController().hideManaBar(event.getPlayer());
        }
    }

    @EventHandler
    public void onCastSpell(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) { return; }
        if (verifySpellBook(event.getPlayer().getInventory().getItemInOffHand())) {
            if (event.getPlayer().isSneaking()) { event.getPlayer().openInventory(generateSpellInventory(event.getPlayer())); }
            else { instance.getSpellBookController().getSpell(event.getPlayer().getInventory().getItemInOffHand()).castSpell(event.getPlayer(), event.getPlayer()); }
        } else if (verifySpellBook(event.getPlayer().getInventory().getItemInMainHand())) {
            if (event.getPlayer().isSneaking()) { event.getPlayer().openInventory(generateSpellInventory(event.getPlayer())); }
            else { instance.getSpellBookController().getSpell(event.getPlayer().getInventory().getItemInMainHand()).castSpell(event.getPlayer(), event.getPlayer()); }
        }

    }


    private Inventory generateSpellInventory(Player player) {
        Map<Integer, Spell> spells = instance.getSpellBookController().getSpellRegistry().getRegistryById();
        List<ItemStack> stacks = new ArrayList<>();
        for (Integer key : spells.keySet()) {
            if (instance.getSpellBookController().knowsSpell(player, instance.getSpellBookController().getSpellRegistry().getTechnicalName(key))) {
                stacks.add(spells.get(key).getItemStack(key));
            } else {
                stacks.add(blockedItem);
            }
        }
        return generateDistributedInventory(INVENTORY_TITLE, stacks);
    }
}
