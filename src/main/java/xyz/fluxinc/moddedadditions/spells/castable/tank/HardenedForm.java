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
import xyz.fluxinc.moddedadditions.ModdedAdditions;
import xyz.fluxinc.moddedadditions.controllers.customitems.SpellBookController;
import xyz.fluxinc.moddedadditions.spells.Spell;
import xyz.fluxinc.moddedadditions.spells.SpellRecipe;
import xyz.fluxinc.moddedadditions.spells.recipe.MaterialRecipeIngredient;

import static xyz.fluxinc.fluxcore.utils.LoreUtils.addLore;

public class HardenedForm extends Spell {

    @Override
    public ItemStack getDefaultItem(World.Environment environment, int level) {
        return new ItemStack(Material.BEDROCK);
    }

    @Override
    public String getLocalizedName() {
        return "Hardened Form";
    }

    @Override
    public String getTechnicalName() {
        return "hardenedform";
    }

    @Override
    public String getDescription(int level) {
        switch (level) {
            case 1: return "Increases the potency!";
            case 2: return "Increased duration!";
            case 3: return "Removes the slowdown!";
            default: return "Increases your resistance, while decreasing your speed!";
        }
    }

    @Override
    public int getModelId() {
        return ModdedAdditions.KEY_BASE + SpellBookController.SB_KEY_BASE + 60;
    }

    @Override
    public int getCost(World.Environment environment, int level) {
        return 75;
    }

    @Override
    public String getRiddle(int level) {
        switch (level) {
            case 0:
                return "&9Catalyst: &4Redstone\n\n&9Hard as the rocks closest to the core, you stand strong but slow, as if held by a sticky thread";
            default:
                return null;
        }
    }

    @Override
    public long getCooldown(int level) {
        return 20 * 1000;
    }

    @Override
    public SpellRecipe getRecipe(int level) {
        switch (level) {
            case 0:
                return new SpellRecipe(this, new MaterialRecipeIngredient(Material.REDSTONE),
                        new MaterialRecipeIngredient(Material.OBSIDIAN), new MaterialRecipeIngredient(Material.COBWEB));
            default:
                return null;
        }
    }

    @Override
    public boolean enactSpell(Player caster, LivingEntity target, int level) {
        if (caster != target) {
            caster.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("The body of your target becomes hard as a rock"));
            if (target instanceof Player) {
                ((Player) target).spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("Your body becomes hard as a rock"));
            }
        } else {
            caster.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("Your body becomes hard as a rock"));
        }
        new PotionEffect(PotionEffectType.ABSORPTION, (level >= 3 ? 40 : 20) * 20, level >= 2 ? 4 : 2).apply(target);
        new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, (level >= 3 ? 40 : 20) * 20, level >= 2 ? 2 : 1).apply(target);
        if (level > 4) { new PotionEffect(PotionEffectType.SLOW, 20 * 20, 0).apply(target); }
        return true;
    }
}
