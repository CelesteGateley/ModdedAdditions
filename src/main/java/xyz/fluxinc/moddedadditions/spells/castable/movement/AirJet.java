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
import xyz.fluxinc.moddedadditions.ModdedAdditions;
import xyz.fluxinc.moddedadditions.controllers.customitems.SpellBookController;
import xyz.fluxinc.moddedadditions.spells.Spell;
import xyz.fluxinc.moddedadditions.spells.SpellRecipe;
import xyz.fluxinc.moddedadditions.spells.recipe.MaterialRecipeIngredient;

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
    public int getModelId() {
        return ModdedAdditions.KEY_BASE + SpellBookController.SB_KEY_BASE + 20;
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
            if (level < 3) {
                return 45;
            } else {
                return 30;
            }
        }
    }

    @Override
    public String getRiddle(int level) {
        switch (level) {
            case 0:
                return "&9Catalyst: &4Redstone\n\n&9Tied up tight, the chicken may not fly free";
            default:
                return null;
        }
    }

    @Override
    public long getCooldown(int level) {
        if (level == 1) {
            return 250;
        } else {
            return 150;
        }
    }

    @Override
    public SpellRecipe getRecipe(int level) {
        switch (level) {
            case 0:
                return new SpellRecipe(this, new MaterialRecipeIngredient(Material.REDSTONE),
                        new MaterialRecipeIngredient(Material.FEATHER), new MaterialRecipeIngredient(Material.STRING));
            default:
                return null;
        }

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
        System.out.println(newVector);
        if (level == 4) {
            newVector.multiply(2);
        }
        caster.setVelocity(newVector);
        return true;
    }
}
