package xyz.fluxinc.moddedadditions.lightsaber;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import xyz.fluxinc.fluxcore.utils.ToolUtils;
import xyz.fluxinc.moddedadditions.lightsaber.items.DarkSaber;
import xyz.fluxinc.moddedadditions.lightsaber.items.KyberCrystal;
import xyz.fluxinc.moddedadditions.lightsaber.items.LightSaber;

import static xyz.fluxinc.moddedadditions.ModdedAdditions.KEY_BASE;

@SuppressWarnings("ConstantConditions")
public class LightSaberListener implements Listener {


    @EventHandler
    public void onLightSaberBlockHit(BlockBreakEvent event) {
        ItemStack iStack = event.getPlayer().getInventory().getItemInMainHand();
        ItemMeta iMeta = iStack.getItemMeta();
        if (!(LightSaber.isLightSaber(iStack) || DarkSaber.isDarkSaber(iStack))) return;
        if (LightSaber.getColor(iStack) == SaberColor.DEPLETED || DarkSaber.getColor(iStack) == SaberColor.DEPLETED) {
            event.setCancelled(true);
            return;
        }
        if (iMeta instanceof Damageable) {
            if (((Damageable) iMeta).getDamage() + 2 >= iStack.getType().getMaxDurability()) {
                if (LightSaber.isLightSaber(iStack)) {
                    LightSaber.depleteSaber(iStack);
                } else if (DarkSaber.isDarkSaber(iStack)) {
                    DarkSaber.depleteSaber(iStack);
                }
            }
        }
    }


    @EventHandler
    public void onLightSaberHit(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player)) return;
        ItemStack itemStack = ((Player) event.getDamager()).getInventory().getItemInMainHand();
        if (!LightSaber.isLightSaber(itemStack) && !DarkSaber.isDarkSaber(itemStack)) return;
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta.getCustomModelData() == KEY_BASE + LightSaber.LIGHT_SABER_BASE || itemMeta.getCustomModelData() == KEY_BASE + DarkSaber.DARK_SABER_BASE) {
            event.setCancelled(true);
            return;
        }
        if (itemMeta instanceof Damageable) {
            if (((Damageable) itemMeta).getDamage() + 1 == itemStack.getType().getMaxDurability()) {
                if (LightSaber.isLightSaber(itemStack)) {
                    LightSaber.depleteSaber(itemStack);
                } else if (DarkSaber.isDarkSaber(itemStack)) {
                    DarkSaber.depleteSaber(itemStack);
                }
            }
        }


        if (itemStack.getType() != Material.DIAMOND_SWORD) {
            return;
        }

        ItemMeta iMeta = itemStack.getItemMeta();
        if (iMeta == null || iMeta.getLore() == null) return;

        if (LightSaber.isLightSaber(itemStack)) {
            if (itemStack.getType() == Material.DIAMOND_SWORD) itemStack.setType(Material.NETHERITE_SWORD);
            ((Player) event.getDamager()).playSound(event.getDamager().getLocation(), Sound.BLOCK_ENCHANTMENT_TABLE_USE, 1, 1);
        }
    }

    @EventHandler
    public void onLightSaberRepair(PrepareAnvilEvent event) {
        if (event.getInventory().getItem(0) == null || event.getInventory().getItem(1) == null) return;
        if (!LightSaber.isLightSaber(event.getInventory().getItem(0)) && !DarkSaber.isDarkSaber(event.getInventory().getItem(0)))
            return;
        if (event.getInventory().getItem(1).getType() == Material.NETHERITE_SWORD) {
            event.setResult(ToolUtils.transferEnchantments(event.getInventory().getItem(1), event.getInventory().getItem(0)));
            return;
        }
        if (event.getInventory().getItem(1).getType() == Material.ENCHANTED_BOOK) return;
        if (!KyberCrystal.isKyberCrystal(event.getInventory().getItem(1))) return;
        SaberColor saberColor = SaberColor.getModColor(event.getInventory().getItem(1).getItemMeta().getCustomModelData() - KyberCrystal.KYBER_CRYSTAL_BASE - KEY_BASE);
        ItemStack saber = event.getInventory().getItem(0).clone();
        if (LightSaber.isLightSaber(saber)) {
            saber = LightSaber.repairSaber(saber, saberColor);
        } else if (DarkSaber.isDarkSaber(saber)) {
            saber = DarkSaber.repairSaber(saber, saberColor);
        }
        event.setResult(saber);
    }

    @EventHandler
    public void onLightSaberCraft(PrepareItemCraftEvent event) {
        boolean containsKyberCrystal = false;
        SaberColor color = null;

        if (event.getRecipe() != null
                && event.getRecipe().getResult().getItemMeta() != null
                && event.getRecipe().getResult().getItemMeta().hasCustomModelData()
                && event.getRecipe().getResult().getItemMeta().getCustomModelData() == KEY_BASE + LightSaber.LIGHT_SABER_BASE) {
            for (ItemStack item : event.getInventory().getMatrix()) {
                if (item != null && item.getType().equals(Material.EMERALD)) {
                    if (item.getItemMeta() != null && item.getItemMeta().hasCustomModelData()) {
                        if (KyberCrystal.isKyberCrystal(item)) {
                            containsKyberCrystal = true;
                            color = SaberColor.getModColor(item.getItemMeta().getCustomModelData() - KyberCrystal.KYBER_CRYSTAL_BASE - KEY_BASE);
                            break;
                        }
                    }
                }
            }
            if (!containsKyberCrystal) {
                event.getInventory().setResult(null);
            } else {
                event.getInventory().setResult(new LightSaber(color).getNewItem());
            }
        }
    }


    @EventHandler
    public void onLightSaberUpgrade(PrepareItemCraftEvent event) {
        if (event.getInventory().getItem(4) == null) return;
        if (event.getRecipe() != null
                && event.getRecipe().getResult().getItemMeta() != null
                && event.getRecipe().getResult().getItemMeta().hasCustomModelData()
                && event.getRecipe().getResult().getItemMeta().getCustomModelData() == KEY_BASE + DarkSaber.DARK_SABER_BASE) {
            if (!LightSaber.isLightSaber(event.getInventory().getMatrix()[4])) {
                event.getInventory().setResult(null);
                return;
            }
            event.getInventory().setResult(DarkSaber.upgradeFromLightSaber(event.getInventory().getMatrix()[4]));
        }
    }

}