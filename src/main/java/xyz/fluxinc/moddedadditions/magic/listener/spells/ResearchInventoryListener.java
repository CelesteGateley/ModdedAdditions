package xyz.fluxinc.moddedadditions.magic.listener.spells;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import xyz.fluxinc.moddedadditions.common.storage.PlayerData;
import xyz.fluxinc.moddedadditions.magic.SpellRegistry;
import xyz.fluxinc.moddedadditions.magic.spells.Magic;
import xyz.fluxinc.moddedadditions.magic.spells.Spell;
import xyz.fluxinc.moddedadditions.magic.spells.SpellRecipe;
import xyz.fluxinc.moddedadditions.magic.spells.SpellSchool;

import java.util.ArrayList;
import java.util.List;

import static xyz.fluxinc.moddedadditions.ModdedAdditions.instance;

@SuppressWarnings("SuspiciousMethodCalls")
public class ResearchInventoryListener implements Listener {

    private static final List<Player> openInventories = new ArrayList<>();
    private static final int[] skipSlots = {3, 5, 10, 16, 22, 28, 34, 39, 41};

    public static Inventory generateBlankInventory() {
        Inventory blankInventory = Bukkit.createInventory(null, 54);
        for (int i = 0; i < 45; i++) {
            boolean skip = false;
            for (int x : skipSlots) {
                if (x > i) break;
                if (x == i) {
                    skip = true;
                    break;
                }
            }
            if (!skip) {
                blankInventory.setItem(i, new ItemStack(Material.BLACK_STAINED_GLASS_PANE));
            }
        }
        for (int i = 45; i < 54; i++) {
            blankInventory.setItem(i, new ItemStack(Material.YELLOW_STAINED_GLASS_PANE));
        }
        for (int i = 18; i < 27; i++) {
            if (i == 22) continue;
            blankInventory.setItem(i, new ItemStack(Material.BLACK_TERRACOTTA));
        }
        return blankInventory;
    }


    public static void openInventory(Player player) {
        openInventories.add(player);
        player.openInventory(generateBlankInventory());
    }

    @EventHandler
    public void onInventoryMoveEvent(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;
        if (!openInventories.contains(event.getWhoClicked())) return;
        if (event.getClickedInventory() == null) return;
        if (event.getClickedInventory().getType() != InventoryType.CHEST) return;
        if (event.getCurrentItem() == null) return;
        Material type = event.getCurrentItem().getType();
        if (type == Material.BLACK_STAINED_GLASS_PANE || type == Material.RED_TERRACOTTA || type == Material.LIME_TERRACOTTA || type == Material.BLACK_TERRACOTTA) {
            event.setCancelled(true);
        } else if (type == Material.RED_STAINED_GLASS_PANE || type == Material.YELLOW_STAINED_GLASS_PANE || type == Material.GREEN_STAINED_GLASS_PANE) {
            event.setCancelled(true);
            PlayerData data = instance.getPlayerDataController().getPlayerData((Player) event.getWhoClicked());
            StorageContainer result = verifyInventory((Player) event.getWhoClicked(), event.getClickedInventory());
            if (result == null || result.count < 8) {
                for (int i = 45; i < 54; i++) {
                    event.getClickedInventory().setItem(i, new ItemStack(Material.RED_STAINED_GLASS_PANE));
                }
                if (result != null) {
                    for (int i = 18; i < 27; i++) {
                        if (i == 22) continue;
                        if (result.count > 0) {
                            event.getClickedInventory().setItem(i, new ItemStack(Material.LIME_TERRACOTTA));
                            result.count--;
                        } else event.getClickedInventory().setItem(i, new ItemStack(Material.RED_TERRACOTTA));
                    }
                }
                for (int i : skipSlots) {
                    ItemStack iStack = event.getInventory().getItem(i);
                    if (iStack != null) {
                        event.getWhoClicked().getInventory().addItem(iStack);
                        event.getInventory().setItem(i, null);
                    }
                }
                //emptyInventory(event.getClickedInventory(), (Player) event.getWhoClicked());
            } else if (result.result instanceof Spell) {
                Spell spell = (Spell) result.result;
                data.setSpell(spell.getTechnicalName(), data.getSpellLevel(spell.getTechnicalName()) + 1);
                data.evaluateMana();
                instance.getPlayerDataController().setPlayerData((Player) event.getWhoClicked(), data);
                emptyInventory(event.getClickedInventory(), (Player) event.getWhoClicked());
                for (int i = 18; i < 27; i++) {
                    if (i == 22) continue;
                    event.getClickedInventory().setItem(i, new ItemStack(Material.LIME_TERRACOTTA));
                }
                for (int i = 45; i < 54; i++) {
                    event.getClickedInventory().setItem(i, new ItemStack(Material.GREEN_STAINED_GLASS_PANE));
                }
            } else if (result.result instanceof SpellSchool) {
                SpellSchool school = (SpellSchool) result.result;
                data.setSchool(school.getTechnicalName(), true);
                instance.getPlayerDataController().setPlayerData((Player) event.getWhoClicked(), data);
                emptyInventory(event.getClickedInventory(), (Player) event.getWhoClicked());
                for (int i = 45; i < 54; i++) {
                    event.getClickedInventory().setItem(i, new ItemStack(Material.GREEN_STAINED_GLASS_PANE));
                }
                for (int i = 18; i < 27; i++) {
                    if (i == 22) continue;
                    event.getClickedInventory().setItem(i, new ItemStack(Material.LIME_TERRACOTTA));
                }
            } else {
                for (int i = 45; i < 54; i++) {
                    event.getClickedInventory().setItem(i, new ItemStack(Material.YELLOW_STAINED_GLASS_PANE));
                }
            }
        }
    }

    private StorageContainer verifyInventory(Player player, Inventory inventory) {
        ItemStack catalyst = null;
        List<ItemStack> items = new ArrayList<>();
        for (int i : skipSlots) {
            ItemStack iStack = inventory.getItem(i);
            if (iStack == null) return null;
            if (i == 22) catalyst = iStack;
            else items.add(iStack);
        }

        SpellRecipe closestFit = null;
        int strength = 0;
        for (SpellRecipe recipe : SpellRegistry.getAvailableRecipes(player)) {
            int recipeStrength = recipe.verifyItems(items, catalyst);
            if (recipeStrength > strength) {
                closestFit = recipe;
                strength = recipeStrength;
            }
        }

        return new StorageContainer(strength, closestFit != null ? closestFit.getResult() : null);
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (!(event.getPlayer() instanceof Player)) return;
        if (!openInventories.contains(event.getPlayer())) return;
        for (int i : skipSlots) {
            ItemStack iStack = event.getInventory().getItem(i);
            if (iStack != null) {
                event.getPlayer().getInventory().addItem(iStack);
            }
        }
        openInventories.remove(event.getPlayer());

    }

    private void emptyInventory(Inventory inventory, Player player) {
        for (int i : skipSlots) {
            ItemStack iStack = inventory.getItem(i);
            if (iStack == null) continue;
            if (iStack.getAmount() > 1) {
                iStack.setAmount(iStack.getAmount() - 1);
                if (player != null) {
                    player.getInventory().addItem(iStack);
                }
            }
            inventory.setItem(i, null);
        }
    }

    private static class StorageContainer {
        public int count;
        public Magic result;

        public StorageContainer(int count, Magic result) {
            this.count = count;
            this.result = result;
        }
    }
}
