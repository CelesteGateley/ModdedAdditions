package xyz.fluxinc.moddedadditions.listeners.customitem;

import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import xyz.fluxinc.moddedadditions.controllers.customitems.SpellBookController;
import xyz.fluxinc.moddedadditions.listeners.customitem.spells.ResearchInventoryListener;
import xyz.fluxinc.moddedadditions.spells.Spell;
import xyz.fluxinc.moddedadditions.spells.SpellSchool;
import xyz.fluxinc.moddedadditions.spells.castable.combat.Fireball;
import xyz.fluxinc.moddedadditions.storage.PlayerData;
import xyz.fluxinc.moddedadditions.utils.registries.SpellRegistry;

import java.util.ArrayList;
import java.util.List;

import static xyz.fluxinc.fluxcore.utils.InventoryUtils.generateDistributedInventory;
import static xyz.fluxinc.fluxcore.utils.LoreUtils.addLore;
import static xyz.fluxinc.moddedadditions.ModdedAdditions.instance;
import static xyz.fluxinc.moddedadditions.controllers.customitems.SpellBookController.generateNewSpellBook;
import static xyz.fluxinc.moddedadditions.controllers.customitems.SpellBookController.verifySpellBook;
import static xyz.fluxinc.moddedadditions.spells.castable.combat.Slowball.*;

@SuppressWarnings("ConstantConditions")
public class SpellBookListener implements Listener {

    private static final String SELECT_SPELL = "Select Spell";
    private static final String SELECT_SCHOOL = "Select School";


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
    public void onSpellSelect(InventoryClickEvent event) {
        if (!event.getView().getTitle().equals(SELECT_SPELL)) {
            return;
        }
        if (event.getClickedInventory() == null) {
            return;
        }
        event.setCancelled(true);
        if (event.getCurrentItem() == null) {
            return;
        }
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

    @EventHandler
    public void onSchoolSelect(InventoryClickEvent event) {
        if (!event.getView().getTitle().equals(SELECT_SCHOOL)) {
            return;
        }
        if (event.getClickedInventory() == null) {
            return;
        }
        event.setCancelled(true);
        if (event.getCurrentItem() == null) {
            return;
        }
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
                    if (!data.knowsSpell(spell.getTechnicalName())) return;
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
        if (event.getPlayer().getInventory().getItemInMainHand().getAmount() == 1) {
            event.getPlayer().getInventory().setItemInMainHand(SpellBookController.generateNewSpellBook());
        } else {
            ItemStack bookStack = event.getPlayer().getInventory().getItemInMainHand();
            bookStack.setAmount(bookStack.getAmount()-1);
            event.getPlayer().getInventory().setItemInMainHand(bookStack);
            event.getPlayer().getInventory().addItem(SpellBookController.generateNewSpellBook());
        }
    }

    @EventHandler
    public void onSpellBookCraft(CraftItemEvent event) {
        if (!verifySpellBook(event.getRecipe().getResult())) {
            return;
        }
        String spell = SpellRegistry.getTechnicalName(event.getRecipe().getResult().getItemMeta().getCustomModelData());
        PlayerData data = instance.getPlayerDataController().getPlayerData((Player) event.getWhoClicked());
        if (!data.knowsSpell(spell)) data.addMaximumMana(50);
        instance.getPlayerDataController().setPlayerData((Player) event.getWhoClicked(), data.setSpell(spell, 1));
    }

    @EventHandler
    public void onSnowballHit(ProjectileHitEvent event) {
        if (event.getEntity().getType() != EntityType.SNOWBALL) {
            return;
        }
        if (event.getEntity().getCustomName() == null) {
            return;
        }
        Entity target = event.getHitEntity();
        if (target instanceof LivingEntity) {
            switch (event.getEntity().getCustomName()) {
                case SLOWBALL_NAME:
                    new PotionEffect(PotionEffectType.SLOW, 10 * 20, 2).apply((LivingEntity) target);
                    new PotionEffect(PotionEffectType.SLOW_DIGGING, 10 * 20, 2).apply((LivingEntity) target);
                    return;
                case LONGER_SLOWBALL_NAME:
                    new PotionEffect(PotionEffectType.SLOW, 10 * 30, 2).apply((LivingEntity) target);
                    new PotionEffect(PotionEffectType.SLOW_DIGGING, 10 * 30, 2).apply((LivingEntity) target);
                    return;
                case POTENT_SLOWBALL_NAME:
                    new PotionEffect(PotionEffectType.SLOW, 10 * 30, 4).apply((LivingEntity) target);
                    new PotionEffect(PotionEffectType.SLOW_DIGGING, 10 * 30, 4).apply((LivingEntity) target);
                    return;
                default:
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerLavaWalk(PlayerMoveEvent event) {
        if (instance.getSpellBookController().canLavaWalk(event.getPlayer())) {
            Location feet = event.getTo().clone();
            feet.add(0, -1, 0);
            if (feet.getBlock().getType() == Material.LAVA) {
                feet.getBlock().setType(Material.COBBLESTONE);
            }
        }
    }

    private Inventory generateSchoolInventory(Player player) {
        PlayerData data = instance.getPlayerDataController().getPlayerData(player);
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

    private Inventory addResearchButton(List<ItemStack> stacks, String selectSchool) {
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

    private Inventory generateSpellInventory(SpellSchool school, Player player) {
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
