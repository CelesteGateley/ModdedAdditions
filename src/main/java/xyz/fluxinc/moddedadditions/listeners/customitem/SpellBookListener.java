package xyz.fluxinc.moddedadditions.listeners.customitem;

import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import xyz.fluxinc.moddedadditions.ModdedAdditions;
import xyz.fluxinc.moddedadditions.controllers.customitems.SpellBookController;
import xyz.fluxinc.moddedadditions.spells.Spell;
import xyz.fluxinc.moddedadditions.spells.castable.combat.Fireball;
import xyz.fluxinc.moddedadditions.storage.PlayerData;

import static xyz.fluxinc.moddedadditions.ModdedAdditions.instance;
import static xyz.fluxinc.moddedadditions.controllers.customitems.SpellBookController.verifySpellBook;
import static xyz.fluxinc.moddedadditions.listeners.customitem.SpellControlListener.generateSchoolInventory;

@SuppressWarnings("ConstantConditions")
public class SpellBookListener implements Listener {

    @EventHandler
    public void onInventoryMoveItem(InventoryMoveItemEvent event) {
        /*
            Make sure the destination is a player inventory
            check if main hand or off hand is a spell book and if so show mana bar
         */
        if (!event.getDestination().getType().equals(InventoryType.PLAYER)) return;

        if (!(event.getDestination().getHolder() instanceof HumanEntity)) return;

        HumanEntity entity = (HumanEntity) event.getDestination().getHolder();
        if (!(entity instanceof Player)) return;
        Player player = (Player) entity;

        if (verifySpellBook(player.getInventory().getItemInMainHand())) {
            instance.getManaController().showManaBar(player);
        } else if (verifySpellBook(player.getInventory().getItemInOffHand())) {
            instance.getManaController().showManaBar(player);
        } else {
            instance.getManaController().hideManaBar(player);
        }
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
        } else {
            instance.getManaController().hideManaBar(event.getPlayer());
        }
    }

    @EventHandler
    public void onCastSpell(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }
        if (event.getItem() == null) return;
        PlayerData data = instance.getPlayerDataController().getPlayerData(event.getPlayer());
        if (verifySpellBook(event.getItem())) {
            if (event.getPlayer().isSneaking()) {
                event.getPlayer().openInventory(generateSchoolInventory(event.getPlayer()));
            } else {
                Spell spell = SpellBookController.getSpell(event.getItem());
                if (spell instanceof Fireball && event.getAction() == Action.RIGHT_CLICK_BLOCK) return;
                if (spell != null) {
                    if (!data.knowsSpell(spell.getTechnicalName())) return;
                    spell.castSpell(event.getPlayer(), event.getPlayer(), data.getSpellLevel(spell.getTechnicalName()));
                }
            }
        }
    }

    @EventHandler
    public void onCastAtEntity(PlayerInteractAtEntityEvent event) {
        ItemStack item = event.getPlayer().getInventory().getItem(event.getHand());
        PlayerData data = instance.getPlayerDataController().getPlayerData(event.getPlayer());
        if (verifySpellBook(item)) {
            if (event.getPlayer().isSneaking()) {
                event.getPlayer().openInventory(generateSchoolInventory(event.getPlayer()));
            } else {
                Spell spell = SpellBookController.getSpell(item);
                if (spell != null) {
                    if (!data.knowsSpell(spell.getTechnicalName())
                            && item.getItemMeta().getCustomModelData() < ModdedAdditions.KEY_BASE + SpellBookController.SB_KEY_BASE + 100) return;
                    if (event.getRightClicked() instanceof LivingEntity) {
                        spell.castSpell(event.getPlayer(), (LivingEntity) event.getRightClicked(), data.getSpellLevel(spell.getTechnicalName()));
                    } else {
                        spell.castSpell(event.getPlayer(), event.getPlayer(), data.getSpellLevel(spell.getTechnicalName()));
                    }
                }
            }
        }
    }

    @EventHandler
    public void onMakeSpellbook(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (event.getClickedBlock() == null || event.getClickedBlock().getType() != Material.BOOKSHELF) return;
        if (event.getItem() == null || event.getItem().getType() != Material.BOOK) return;
        if (SpellBookController.verifySpellBook(event.getItem())) return;
        if (event.getPlayer().getLevel() < 8) {
            event.getClickedBlock().getWorld().playSound(event.getPlayer().getLocation(), Sound.BLOCK_FIRE_EXTINGUISH, 1, 1);
            return;
        }
        World world = event.getClickedBlock().getWorld();
        world.spawnParticle(Particle.VILLAGER_HAPPY, event.getClickedBlock().getLocation(), 5, 3, 3, 3);
        event.getPlayer().setLevel(event.getPlayer().getLevel() - 8);
        if (event.getPlayer().getInventory().getItemInMainHand().getAmount() == 1) {
            event.getPlayer().getInventory().setItemInMainHand(SpellBookController.generateNewSpellBook());
        } else {
            ItemStack bookStack = event.getPlayer().getInventory().getItemInMainHand();
            bookStack.setAmount(bookStack.getAmount() - 1);
            event.getPlayer().getInventory().setItemInMainHand(bookStack);
            event.getPlayer().getInventory().addItem(SpellBookController.generateNewSpellBook());
        }
    }
}
