package xyz.fluxinc.moddedadditions.spells.castable.movement;

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

public class Speed extends Spell {

    public Speed() {
        super();
    }

    @Override
    public String getName() {
        return "Speed";
    }

    @Override
    public ItemStack getItemStack(World.Environment environment, int modelId) {
        ItemStack speed = addLore(new ItemStack(Material.GLOWSTONE_DUST), "Costs: " + getCost(environment) + " Mana");
        speed = addLore(speed, "Cooldown: " + getCooldown()/1000d + " Seconds");
        ItemMeta iMeta = speed.getItemMeta();
        iMeta.setCustomModelData(modelId);
        iMeta.setDisplayName(ChatColor.WHITE + getName());
        speed.setItemMeta(iMeta);
        return speed;
    }

    @Override
    public int getCost(World.Environment environment) {
        return 50;
    }

    @Override
    public String getRiddle() {
        return "Sweetening your life, this will make you faster than light";
    }

    @Override
    public long getCooldown() {
        return 250;
    }

    @Override
    public boolean enactSpell(Player caster, LivingEntity target) {
        if (caster != target) {
            caster.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("The body of your target becomes faster"));
            if (target instanceof Player) {
                ((Player) target).spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("Your feel your body become faster as mana courses through you"));
            }
        } else {
            caster.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("Your feel your body become faster as mana courses through you"));
        }
        new PotionEffect(PotionEffectType.SPEED, 20 * 20, 4).apply(target);
        return true;
    }
}
