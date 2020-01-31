package xyz.fluxinc.moddedadditions.listeners.customitem;

import org.bukkit.*;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.Fireball;
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
import xyz.fluxinc.moddedadditions.enums.Spell;

import static xyz.fluxinc.fluxcore.utils.LoreUtils.addLore;
import static xyz.fluxinc.moddedadditions.controllers.customitems.SpellBookController.verifySpellBook;

public class SpellBookListener implements Listener {

    private ModdedAdditions instance;

    private static final String INVENTORY_TITLE = "Select Spell";
    private static Inventory selectSpellScreen = Bukkit.getServer().createInventory(null, 9, INVENTORY_TITLE);
    private static ItemStack fireball;
    private static ItemStack teleport;
    private static ItemStack arrows;
    private static ItemStack heal;
    static {
        fireball = addLore(new ItemStack(Material.FIRE_CHARGE), "Costs: 50 Mana");
        ItemMeta iMeta = fireball.getItemMeta(); iMeta.setDisplayName(ChatColor.WHITE + "Fireball"); fireball.setItemMeta(iMeta);
        selectSpellScreen.setItem(1, fireball);

        teleport = addLore(new ItemStack(Material.ENDER_PEARL), "Costs: 50 Mana");
        iMeta = teleport.getItemMeta(); iMeta.setDisplayName(ChatColor.WHITE + "Teleport"); teleport.setItemMeta(iMeta);
        selectSpellScreen.setItem(3, teleport);

        arrows = addLore(new ItemStack(Material.BOW), "Costs: 5 Mana");
        arrows = addLore(arrows, "Costs: 1 Arrow");
        iMeta = arrows.getItemMeta(); iMeta.setDisplayName(ChatColor.WHITE + "Fire Arrows"); arrows.setItemMeta(iMeta);
        selectSpellScreen.setItem(5, arrows);

        heal = addLore(new ItemStack(Material.ENCHANTED_GOLDEN_APPLE), "Costs: 25 Mana");
        iMeta = heal.getItemMeta(); iMeta.setDisplayName(ChatColor.WHITE + "Heal"); heal.setItemMeta(iMeta);
        selectSpellScreen.setItem(7, heal);
    }

    public SpellBookListener(ModdedAdditions instance) {
        this.instance = instance;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!event.getView().getTitle().equals(INVENTORY_TITLE)) { return; }
        if (event.getClickedInventory().getSize() != 9) { return; }
        if (event.getCurrentItem() == null) { return; }
        event.setCancelled(true);


        Spell newSpell;
        if (event.getCurrentItem().equals(fireball)) { newSpell = Spell.FIREBALL; }
        else if (event.getCurrentItem().equals(teleport)) { newSpell = Spell.TELEPORT; }
        else if (event.getCurrentItem().equals(arrows)) { newSpell = Spell.ARROWS; }
        else  { newSpell = Spell.HEAL; }

        Player player = (Player) event.getWhoClicked();
        if (verifySpellBook(player.getInventory().getItemInMainHand())) {
            instance.getSpellBookController().setSpell(newSpell, player.getInventory().getItemInMainHand());
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
        } else {
            instance.getManaController().hideManaBar(event.getPlayer());
        }
    }

    @EventHandler
    public void onCastSpell(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) { return; }
        if (!verifySpellBook(event.getPlayer().getInventory().getItemInMainHand())) { return; }
        System.out.println(event.getPlayer().getInventory().getItemInMainHand().getItemMeta().getCustomModelData());
        if (event.getPlayer().isSneaking()) {
            event.getPlayer().openInventory(selectSpellScreen);
        } else {
            Spell castSpell = instance.getSpellBookController().getSpell(event.getPlayer().getInventory().getItemInMainHand());
            switch (castSpell) {
                case FIREBALL:
                    if (instance.getManaController().getMana(event.getPlayer()) >= 50) {
                        event.getPlayer().launchProjectile(Fireball.class);
                        instance.getManaController().useMana(event.getPlayer(), 50);
                    } else { event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.BLOCK_FIRE_EXTINGUISH, 1, 1); }
                    return;
                case TELEPORT:
                    if (instance.getManaController().getMana(event.getPlayer()) >= 50) {
                        event.getPlayer().launchProjectile(EnderPearl.class);
                        instance.getManaController().useMana(event.getPlayer(), 50);
                    } else { event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.BLOCK_FIRE_EXTINGUISH, 1, 1); }
                    return;
                case ARROWS:
                    if (event.getPlayer().getInventory().contains(Material.ARROW) && instance.getManaController().getMana(event.getPlayer()) >= 5) {
                        event.getPlayer().launchProjectile(Arrow.class);
                        event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.ENTITY_ARROW_SHOOT, 1, 1);
                        instance.getManaController().useMana(event.getPlayer(), 5);
                        takeArrow(event.getPlayer());
                    } else { event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.BLOCK_FIRE_EXTINGUISH, 1, 1); }
                    return;
                case HEAL:
                    if (instance.getManaController().getMana(event.getPlayer()) >= 25 && event.getPlayer().getHealth() != 20) {
                        if (event.getPlayer().getHealth() == 19) { event.getPlayer().setHealth(event.getPlayer().getHealth() + 1); }
                        else { event.getPlayer().setHealth(event.getPlayer().getHealth() + 2); }
                        event.getPlayer().spawnParticle(Particle.VILLAGER_HAPPY, event.getPlayer().getLocation(), 50, 0.5, 1, 0.5);
                        event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
                        instance.getManaController().useMana(event.getPlayer(), 25);
                    } else { event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.BLOCK_FIRE_EXTINGUISH, 1, 1); }
                    return;
                case FREEZE:
                default:
                    event.getPlayer().sendMessage("That spell is not implemented yet!");
            }
        }
    }

    private void takeArrow(Player player) {
        if (player.getInventory().contains(Material.ARROW)) {
            int slot = player.getInventory().first(Material.ARROW);
            ItemStack iStack = player.getInventory().getItem(slot);
            if (iStack.getAmount() == 1) { player.getInventory().setItem(slot, null); }
            else { iStack.setAmount(iStack.getAmount() - 1); player.getInventory().setItem(slot, iStack); }
        }
    }
}
