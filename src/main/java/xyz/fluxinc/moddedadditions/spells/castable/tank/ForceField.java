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
import xyz.fluxinc.moddedadditions.controllers.customitems.SpellBookController;
import xyz.fluxinc.moddedadditions.spells.Spell;
import xyz.fluxinc.moddedadditions.spells.SpellRecipe;
import xyz.fluxinc.moddedadditions.spells.recipe.MaterialRecipeIngredient;

import static xyz.fluxinc.fluxcore.utils.LoreUtils.addLore;

public class ForceField extends Spell {

    @Override
    public ItemStack getDefaultItem(World.Environment environment, int level) {
        return new ItemStack(Material.GLASS);
    }

    @Override
    public String getLocalizedName() {
        return "ForceField";
    }

    @Override
    public String getTechnicalName() {
        return "forcefield";
    }

    @Override
    public int getModelId() {
        return ModdedAdditions.KEY_BASE + SpellBookController.SB_KEY_BASE + 61;
    }

    @Override
    public int getCost(World.Environment environment, int level) {
        return 100;
    }

    @Override
    public String getRiddle(int level) {
        switch (level) {
            case 0:
                return "&9Catalyst: &4Redstone\n\n&9An invisible shield protects you, and pushes away all those who come close";
            default:
                return null;
        }

    }

    @Override
    public long getCooldown(int level) {
        return 30 * 1000;
    }

    @Override
    public SpellRecipe getRecipe(int level) {
        switch (level) {
            case 0:
                return new SpellRecipe(this, new MaterialRecipeIngredient(Material.REDSTONE),
                        new MaterialRecipeIngredient(Material.SHIELD), new MaterialRecipeIngredient(Material.PISTON));
            default:
                return null;
        }
    }

    @Override
    public boolean enactSpell(Player caster, LivingEntity target, int level) {
        caster.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("An energy shield surrounds you, keeping mobs away"));
        ModdedAdditions.instance.getForceFieldListener().addForceField(caster, 30);
        return true;
    }
}
