package xyz.fluxinc.moddedadditions.listeners.customitem;

import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import xyz.fluxinc.moddedadditions.ModdedAdditions;
import xyz.fluxinc.moddedadditions.controllers.customitems.SpellBookController;
import xyz.fluxinc.moddedadditions.spells.Spell;
import xyz.fluxinc.moddedadditions.spells.castable.Fireball;
import xyz.fluxinc.moddedadditions.spells.castable.Heal;
import xyz.fluxinc.moddedadditions.spells.castable.SlowBall;
import xyz.fluxinc.moddedadditions.storage.PlayerData;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static xyz.fluxinc.fluxcore.utils.InventoryUtils.generateDistributedInventory;
import static xyz.fluxinc.fluxcore.utils.LoreUtils.addLore;
import static xyz.fluxinc.moddedadditions.controllers.customitems.SpellBookController.verifySpellBook;

public class SpellBookListener implements Listener {

    private static final String INVENTORY_TITLE = "Select Spell";
    private final ModdedAdditions instance;
    private final ItemStack blockedItem;

    public SpellBookListener(ModdedAdditions instance) {
        this.instance = instance;
        blockedItem = addLore(new ItemStack(Material.BARRIER), instance.getLanguageManager().getFormattedString("sb-lockedSpell"));
        ItemMeta itemMeta = blockedItem.getItemMeta();
        itemMeta.setDisplayName(ChatColor.WHITE + "Locked Spell");
        itemMeta.setCustomModelData(1);
        blockedItem.setItemMeta(itemMeta);
    }

    @EventHandler
    public void onInventoryMoveItem(InventoryMoveItemEvent event) {
        /*
            Make sure the destination is a player inventory
            check if main hand or off hand is a spell book and if so show mana bar
         */
        if (!event.getDestination().getType().equals(InventoryType.PLAYER)) {
            return;
        }
        if (!(event.getDestination().getHolder() instanceof HumanEntity)) {
            return;
        }
        HumanEntity entity = (HumanEntity) event.getDestination().getHolder();
        if (!(entity instanceof Player)) {
            return;
        }
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
    public void onInventoryClick(InventoryClickEvent event) {
        if (!event.getView().getTitle().equals(INVENTORY_TITLE)) {
            return;
        }
        if (event.getClickedInventory() == null) {
            return;
        }
        event.setCancelled(true);
        if (event.getCurrentItem() == null) {
            return;
        }
        if (event.getCurrentItem().getItemMeta() == null || !event.getCurrentItem().getItemMeta().hasCustomModelData()) {
            return;
        }

        if (event.getClickedInventory().getType() == InventoryType.PLAYER) {
            event.setCancelled(true);
            return;
        }

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
        } else {
            instance.getManaController().hideManaBar(event.getPlayer());
        }
    }

    @EventHandler
    public void onCastSpell(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        if (verifySpellBook(event.getPlayer().getInventory().getItemInOffHand())) {
            if (event.getPlayer().isSneaking()) {
                event.getPlayer().openInventory(generateSpellInventory(event.getPlayer()));
            } else {
                Spell spell = instance.getSpellBookController().getSpell(event.getPlayer().getInventory().getItemInOffHand());
                if (spell == null) return;
                if (!instance.getPlayerDataController().getPlayerData(event.getPlayer()).knowsSpell(spell.getName())) return;
                if (spell instanceof Fireball && event.getAction() == Action.RIGHT_CLICK_BLOCK) return;
                spell.castSpell(event.getPlayer(), event.getPlayer());
            }
        } else if (verifySpellBook(event.getPlayer().getInventory().getItemInMainHand())) {
            if (event.getPlayer().isSneaking()) {
                event.getPlayer().openInventory(generateSpellInventory(event.getPlayer()));
            } else {
                Spell spell = instance.getSpellBookController().getSpell(event.getPlayer().getInventory().getItemInMainHand());
                if (spell == null) return;
                if (!instance.getPlayerDataController().getPlayerData(event.getPlayer()).knowsSpell(spell.getName())) return;
                spell.castSpell(event.getPlayer(), event.getPlayer());
            }
        }

    }

    @EventHandler
    public void onMakeSpellbook(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }
        if (event.getClickedBlock() == null || event.getClickedBlock().getType() != Material.BOOKSHELF) {
            return;
        }
        if (event.getItem() == null || event.getItem().getType() != Material.BOOK) {
            return;
        }
        if (SpellBookController.verifySpellBook(event.getItem())) {
            return;
        }
        if (event.getPlayer().getLevel() < 8) {
            event.getClickedBlock().getWorld().playSound(event.getPlayer().getLocation(), Sound.BLOCK_FIRE_EXTINGUISH, 1, 1);
            return;
        }
        World world = event.getClickedBlock().getWorld();
        world.spawnParticle(Particle.VILLAGER_HAPPY, event.getClickedBlock().getLocation(), 5, 3, 3, 3);
        event.getPlayer().setLevel(event.getPlayer().getLevel() - 8);
        event.getPlayer().getInventory().setItemInMainHand(instance.getSpellBookController().generateNewSpellBook());
    }

    @EventHandler
    public void onSpecialCraft(PrepareItemCraftEvent event) {
        if (event.getRecipe() == null) {
            return;
        }
        ItemStack result = event.getRecipe().getResult();
        if (!verifySpellBook(result)) {
            return;
        }
        Spell spell = instance.getSpellBookController().getSpell(event.getRecipe().getResult());
        if (spell instanceof Heal) {
            ItemStack pot1 = event.getInventory().getItem(2);
            ItemStack pot2 = event.getInventory().getItem(4);
            ItemStack pot3 = event.getInventory().getItem(6);
            ItemStack pot4 = event.getInventory().getItem(8);
            if (pot1 == null
                    || pot2 == null
                    || pot3 == null
                    || pot4 == null) {
                event.getInventory().setResult(null);
                return;
            }
            if (!(pot1.getItemMeta() instanceof PotionMeta)
                    || !(pot2.getItemMeta() instanceof PotionMeta)
                    || !(pot3.getItemMeta() instanceof PotionMeta)
                    || !(pot4.getItemMeta() instanceof PotionMeta)) {
                event.getInventory().setResult(null);
                return;
            }
            PotionMeta pM1 = (PotionMeta) pot1.getItemMeta();
            PotionMeta pM2 = (PotionMeta) pot2.getItemMeta();
            PotionMeta pM3 = (PotionMeta) pot3.getItemMeta();
            PotionMeta pM4 = (PotionMeta) pot4.getItemMeta();
            if (pM1.getBasePotionData().getType() != PotionType.INSTANT_HEAL
                    || pM2.getBasePotionData().getType() != PotionType.INSTANT_HEAL
                    || pM3.getBasePotionData().getType() != PotionType.INSTANT_HEAL
                    || pM4.getBasePotionData().getType() != PotionType.INSTANT_HEAL) {
                event.getInventory().setResult(null);
                return;
            }
        }
    }

    @EventHandler
    public void onSpellBookCraft(CraftItemEvent event) {
        if (!verifySpellBook(event.getRecipe().getResult())) {
            return;
        }
        String spell = instance.getSpellBookController().getSpellRegistry().getTechnicalName(event.getRecipe().getResult().getItemMeta().getCustomModelData());
        PlayerData data = instance.getPlayerDataController().getPlayerData((Player) event.getWhoClicked());
        if (!data.knowsSpell(spell)) data.addMaximumMana(50);
        instance.getPlayerDataController().setPlayerData((Player) event.getWhoClicked(), data.setSpell(spell, true));
    }

    @EventHandler
    public void onSnowballHit(ProjectileHitEvent event) {
        if (event.getEntity().getType() != EntityType.SNOWBALL) {
            return;
        }
        if (event.getEntity().getCustomName() == null
                || !event.getEntity().getCustomName().equals(SlowBall.SLOWBALL_NAME)) {
            return;
        }
        Entity target = event.getHitEntity();
        if (target instanceof LivingEntity) {
            new PotionEffect(PotionEffectType.SLOW, 10 * 20, 2).apply((LivingEntity) target);
            new PotionEffect(PotionEffectType.SLOW_DIGGING, 10 * 20, 2).apply((LivingEntity) target);
        }
    }

    private Inventory generateSpellInventory(Player player) {
        Map<Integer, Spell> spells = instance.getSpellBookController().getSpellRegistry().getRegistryById();
        List<ItemStack> stacks = new ArrayList<>();
        for (Integer key : spells.keySet()) {
            String spellName = instance.getSpellBookController().getSpellRegistry().getSpellById(key).getName();
            if (instance.getSpellBookController().knowsSpell(player, instance.getSpellBookController().getSpellRegistry().getTechnicalName(key))) {
                stacks.add(spells.get(key).getItemStack(key));
            } else {
                ItemStack iStack = blockedItem.clone();
                ItemMeta iMeta = blockedItem.getItemMeta();
                iMeta.setDisplayName(spellName);
                iStack.setItemMeta(iMeta);
                stacks.add(iStack);
            }
        }
        return generateDistributedInventory(INVENTORY_TITLE, stacks);
    }
}
