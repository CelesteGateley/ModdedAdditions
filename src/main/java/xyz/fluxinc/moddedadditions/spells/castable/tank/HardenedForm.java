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
    public String getLocalizedName() {
        return "Hardened Form";
    }

    @Override
    public String getTechnicalName() {
        return "hardenedform";
    }

    @Override
    public int getModelId() {
        return ModdedAdditions.KEY_BASE + SpellBookController.SB_KEY_BASE + 60;
    }

    @Override
    public ItemStack getItemStack(World.Environment environment, int modelId, int level) {
        ItemStack hardenedForm = addLore(new ItemStack(Material.BEDROCK), "Costs: " + getCost(environment, level) + " Mana");
        hardenedForm = addLore(hardenedForm, "Cooldown: " + getCooldown(level) / 1000d + " Seconds");
        ItemMeta iMeta = hardenedForm.getItemMeta();
        iMeta.setCustomModelData(modelId);
        iMeta.setDisplayName(ChatColor.WHITE + getLocalizedName());
        hardenedForm.setItemMeta(iMeta);
        return hardenedForm;
    }

    @Override
    public int getCost(World.Environment environment, int level) {
        return 75;
    }

    @Override
    public String getRiddle(int level) {
        return "Hard as the rocks closest to the core, you stand strong but slow, as if held by a sticky thread";
    }

    @Override
    public long getCooldown(int level) {
        return 20 * 1000;
    }

    @Override
    public SpellRecipe getRecipe(int level) {
        if (level != 1) return null;
        return new SpellRecipe(new MaterialRecipeIngredient(Material.REDSTONE),
                new MaterialRecipeIngredient(Material.OBSIDIAN), new MaterialRecipeIngredient(Material.COBWEB));
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
        new PotionEffect(PotionEffectType.ABSORPTION, 20 * 20, 2).apply(target);
        new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20 * 20, 1).apply(target);
        new PotionEffect(PotionEffectType.SLOW, 20 * 20, 0).apply(target);
        return true;
    }
}
