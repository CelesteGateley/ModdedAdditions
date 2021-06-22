package xyz.fluxinc.moddedadditions.magic.spells.castable.support;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import xyz.fluxinc.moddedadditions.ModdedAdditions;
import xyz.fluxinc.moddedadditions.magic.controller.SpellBookController;
import xyz.fluxinc.moddedadditions.magic.spells.Spell;
import xyz.fluxinc.moddedadditions.magic.spells.SpellRecipe;
import xyz.fluxinc.moddedadditions.magic.spells.recipe.MaterialRecipeIngredient;
import xyz.fluxinc.moddedadditions.magic.spells.recipe.PotionRecipeIngredient;

public class Vanish extends Spell {

    @Override
    public String getLocalizedName() {
        return "Cloak of the Night";
    }

    @Override
    public String getTechnicalName() {
        return "vanish";
    }

    @Override
    public String getDescription(int level) {
        switch (level) {
            case 1:
                return "Increases the duration!";
            case 2:
                return "Increases the duration further!";
            case 3:
                return "Increases the duration even further!";
            default:
                return "Makes your target invisible!";
        }
    }

    @Override
    public int getModelId() {
        return ModdedAdditions.KEY_BASE + SpellBookController.SB_KEY_BASE + 41;
    }

    @Override
    public ItemStack getDefaultItem(World.Environment environment, int level) {
        ItemStack iStack = new ItemStack(Material.POTION);
        PotionMeta meta = (PotionMeta) iStack.getItemMeta();
        meta.setBasePotionData(new PotionData(PotionType.INVISIBILITY));
        iStack.setItemMeta(meta);
        return iStack;
    }

    @Override
    public int getCost(World.Environment environment, int level) {
        return 75;
    }

    @Override
    public String getRiddle(int level) {
        switch (level) {
            case 0:
                return "&9Catalyst: &4Redstone\n\n&9With a root of gold, you can become as dark as the blackest stone";
            case 1:
                return "&9Catalyst: &cGlowstone Dust\n\n&9An elixir, a hellish root, and a glistening piece, create a transparency to yourself, clear as crystal";
            case 2:
                return "&9Catalyst: &bDiamond\n\n&9Eyes the pierce the night, the root of potions, a glimmering root, and the cure eye of the fallen";
            default:
                return null;
        }

    }

    @Override
    public long getCooldown(int level) {
        return 250;
    }

    @Override
    public SpellRecipe getRecipe(int level) {
        switch (level) {
            case 0:
                return new SpellRecipe(this, new MaterialRecipeIngredient(Material.REDSTONE),
                        new MaterialRecipeIngredient(Material.GOLDEN_CARROT), new MaterialRecipeIngredient(Material.BLACKSTONE));
            case 1:
                return new SpellRecipe(this, new MaterialRecipeIngredient(Material.GLOWSTONE_DUST),
                        new MaterialRecipeIngredient(Material.NETHER_WART), new MaterialRecipeIngredient(Material.GOLD_NUGGET),
                        new PotionRecipeIngredient(PotionType.INVISIBILITY), new MaterialRecipeIngredient(Material.DIAMOND));
            case 2:
                return new SpellRecipe(this, new MaterialRecipeIngredient(Material.DIAMOND),
                        new MaterialRecipeIngredient(Material.NETHER_WART), new MaterialRecipeIngredient(Material.GOLDEN_CARROT),
                        new PotionRecipeIngredient(PotionType.NIGHT_VISION), new MaterialRecipeIngredient(Material.FERMENTED_SPIDER_EYE));
            default:
                return null;
        }
    }

    @Override
    public boolean enactSpell(Player caster, LivingEntity target, int level) {
        if (caster != target) {
            caster.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("The body of your target becomes shrouded in darkness"));
            if (target instanceof Player) {
                ((Player) target).spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("You feel the darkness surround you"));
            }
        } else {
            caster.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("You feel the darkness surround you"));
        }
        new PotionEffect(PotionEffectType.INVISIBILITY, 20 * 20 * level, 0).apply(target);
        return true;
    }
}
