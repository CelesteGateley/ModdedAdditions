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
import xyz.fluxinc.moddedadditions.ModdedAdditions;
import xyz.fluxinc.moddedadditions.spells.Spell;

import static xyz.fluxinc.fluxcore.utils.LoreUtils.addLore;

public class ForceField extends Spell {

    @Override
    public String getLocalizedName() {
        return "ForceField";
    }

    @Override
    public String getTechnicalName() {
        return "forcefield";
    }

    @Override
    public ItemStack getItemStack(World.Environment environment, int modelId, int level) {
        ItemStack forcefield = addLore(new ItemStack(Material.GLASS), "Costs: " + getCost(environment, level) + " Mana");
        forcefield = addLore(forcefield, "Cooldown: " + getCooldown(level) / 1000d + " Seconds");
        ItemMeta iMeta = forcefield.getItemMeta();
        iMeta.setCustomModelData(modelId);
        iMeta.setDisplayName(ChatColor.WHITE + getLocalizedName());
        forcefield.setItemMeta(iMeta);
        return forcefield;
    }

    @Override
    public int getCost(World.Environment environment, int level) {
        return 100;
    }

    @Override
    public String getRiddle(int level) {
        return "An invisible shield protects you, and pushes away all those who come close";
    }

    @Override
    public long getCooldown(int level) {
        return 30 * 1000;
    }

    @Override
    public boolean enactSpell(Player caster, LivingEntity target, int level) {
        caster.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("An energy shield surrounds you, keeping mobs away"));
        ModdedAdditions.instance.getForceFieldListener().addForceField(caster, 30);
        return true;
    }
}
