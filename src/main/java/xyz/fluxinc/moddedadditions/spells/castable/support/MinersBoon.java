package xyz.fluxinc.moddedadditions.spells.castable.support;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import xyz.fluxinc.moddedadditions.spells.Spell;

import static xyz.fluxinc.fluxcore.utils.LoreUtils.addLore;

public class MinersBoon extends Spell {

    public MinersBoon() {
        super();
    }

    @Override
    public String getName() {
        return "Miners Boon";
    }

    @Override
    public ItemStack getItemStack(World.Environment environment, int modelId) {
        ItemStack minersBoon = addLore(new ItemStack(Material.DIAMOND_PICKAXE), "Costs: " + getCost(environment) + " Mana");
        minersBoon = addLore(minersBoon, "Cooldown: " + getCooldown() / 1000d + " Seconds");
        ItemMeta iMeta = minersBoon.getItemMeta();
        iMeta.setCustomModelData(modelId);
        iMeta.setDisplayName(ChatColor.WHITE + getName());
        minersBoon.setItemMeta(iMeta);
        return minersBoon;
    }

    @Override
    public int getCost(World.Environment environment) {
        return 100;
    }

    @Override
    public String getRiddle() {
        return "An ancient metal, and a buried heart, grants one the strength of the earth";
    }

    @Override
    public long getCooldown() {
        return 30000;
    }

    @Override
    public boolean enactSpell(Player caster, LivingEntity target) {
        if (caster != target) {
            caster.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("You grant your target the power of the earth"));
            if (target instanceof Player) {
                ((Player) target).spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("You feel the earth's power flow through you"));
            }
        } else {
            caster.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("You feel the earth's power flow through you"));
        }
        new PotionEffect(PotionEffectType.NIGHT_VISION, 20 * 20, 0).apply(target);
        new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 20 * 20, 0).apply(target);
        new PotionEffect(PotionEffectType.FAST_DIGGING, 20 * 20, 1).apply(target);
        return true;
    }
}
