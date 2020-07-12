package xyz.fluxinc.moddedadditions.spells.castable.tank;

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

public class HardenedForm extends Spell {

    public HardenedForm() {
        super();
    }

    @Override
    public String getName() {
        return "Hardened Form";
    }

    @Override
    public ItemStack getItemStack(World.Environment environment, int modelId) {
        ItemStack hardenedForm = addLore(new ItemStack(Material.BEDROCK), "Costs: " + getCost(environment) + " Mana");
        hardenedForm = addLore(hardenedForm, "Cooldown: " + getCooldown() / 1000d + " Seconds");
        ItemMeta iMeta = hardenedForm.getItemMeta();
        iMeta.setCustomModelData(modelId);
        iMeta.setDisplayName(ChatColor.WHITE + getName());
        hardenedForm.setItemMeta(iMeta);
        return hardenedForm;
    }

    @Override
    public int getCost(World.Environment environment) {
        return 75;
    }

    @Override
    public String getRiddle() {
        return "Hard as the rocks closest to the core, you stand strong but slow, as if held by a sticky thread";
    }

    @Override
    public long getCooldown() {
        return 20 * 1000;
    }

    @Override
    public boolean enactSpell(Player caster, LivingEntity target) {
        if (caster != target) {
            caster.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("The body of your target becomes hard as a rock"));
            if (target instanceof Player) {
                ((Player) target).spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("Your body becomes hard as a rock"));
            }
        } else {
            caster.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("Your body becomes hard as a rock"));
        }
        new PotionEffect(PotionEffectType.ABSORPTION, 20 * 20, 2).apply(target);
        new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20 * 20, 1).apply(target);
        new PotionEffect(PotionEffectType.SLOW, 20 * 20, 0).apply(target);
        return true;
    }
}