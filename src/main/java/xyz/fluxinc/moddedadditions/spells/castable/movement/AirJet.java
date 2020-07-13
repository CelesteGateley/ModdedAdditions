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
import org.bukkit.util.Vector;
import xyz.fluxinc.moddedadditions.spells.Spell;

import static xyz.fluxinc.fluxcore.utils.LoreUtils.addLore;

public class AirJet extends Spell {

    @Override
    public String getLocalizedName() {
        return "Air Jet";
    }

    @Override
    public String getTechnicalName() {
        return "airjet";
    }

    @Override
    public ItemStack getItemStack(World.Environment environment, int modelId, int level) {
        ItemStack airjet = addLore(new ItemStack(Material.FEATHER), "Costs: " + getCost(environment, level) + " Mana");
        airjet = addLore(airjet, "Cooldown: " + getCooldown(level) / 1000d + " Seconds");
        ItemMeta iMeta = airjet.getItemMeta();
        iMeta.setCustomModelData(modelId);
        iMeta.setDisplayName(ChatColor.WHITE + getLocalizedName());
        airjet.setItemMeta(iMeta);
        return airjet;
    }

    @Override
    public int getCost(World.Environment environment, int level) {
        if (environment == World.Environment.NORMAL) {
            return 15;
        } else {
            return 45;
        }
    }

    @Override
    public String getRiddle(int level) {
        return "Tied up tight, the chicken may not fly free";
    }

    @Override
    public long getCooldown(int level) {
        return 250;
    }

    @Override
    public boolean enactSpell(Player caster, LivingEntity target, int level) {
        if (caster.getWorld().getEnvironment() == World.Environment.NETHER && caster.getLocation().getY() > 127)
            return false;
        if (caster.getWorld().getEnvironment() != World.Environment.NORMAL) {
            caster.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("The dense atmosphere makes gathering wind harder"));
        }
        Vector newVector = caster.getEyeLocation().getDirection().multiply(new Vector(2, -2, 2));
        if (newVector.getY() < 0) {
            newVector.multiply(new Vector(1, -1, 1));
        }
        caster.setVelocity(newVector);
        return true;
    }
}
