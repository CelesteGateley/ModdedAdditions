package xyz.fluxinc.moddedadditions.spells.castable.tank;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import xyz.fluxinc.moddedadditions.ModdedAdditions;
import xyz.fluxinc.moddedadditions.controllers.customitems.SpellBookController;
import xyz.fluxinc.moddedadditions.spells.Spell;
import xyz.fluxinc.moddedadditions.spells.SpellRecipe;
import xyz.fluxinc.moddedadditions.spells.recipe.EnchantedBookRecipeIngredient;
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
    public String getDescription(int level) {
        switch (level) {
            case 1: return "Increases the radius!";
            case 2: return "Increases the duration!";
            case 3: return "Projectiles are reflected, dealing damage to who fired!";
            default: return "Places a forcefield around the target, keeping mobs and projectiles away!";
        }
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
            case 1:
                return "&9Catalyst: &cGlowstone Dust\n\n&9Knocking back, and punching through your allies, the field will trip your opponents and blow them away";
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
            case 1:
                return new SpellRecipe(this, new MaterialRecipeIngredient(Material.GLOWSTONE_DUST),
                        new EnchantedBookRecipeIngredient(Enchantment.KNOCKBACK), new EnchantedBookRecipeIngredient(Enchantment.ARROW_KNOCKBACK),
                        new MaterialRecipeIngredient(Material.TNT), new MaterialRecipeIngredient(Material.TRIPWIRE_HOOK));
            default:
                return null;
        }
    }

    @Override
    public boolean enactSpell(Player caster, LivingEntity target, int level) {
        caster.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("An energy shield surrounds you, keeping mobs away"));
        ModdedAdditions.instance.getForceFieldListener().addForceField(caster, level >= 2 ? 8 : 4,level >= 3 ? 45 : 30);
        if (level == 4) { ModdedAdditions.instance.getReflectDamageController().addReflected(caster, 45);}
        return true;
    }
}
