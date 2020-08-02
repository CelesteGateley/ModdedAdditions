package xyz.fluxinc.moddedadditions.spells.castable.support;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import xyz.fluxinc.moddedadditions.ModdedAdditions;
import xyz.fluxinc.moddedadditions.controllers.customitems.SpellBookController;
import xyz.fluxinc.moddedadditions.spells.Spell;
import xyz.fluxinc.moddedadditions.spells.SpellRecipe;
import xyz.fluxinc.moddedadditions.spells.recipe.MaterialRecipeIngredient;

import static xyz.fluxinc.fluxcore.utils.LoreUtils.addLore;

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
    public int getModelId() {
        return ModdedAdditions.KEY_BASE + SpellBookController.SB_KEY_BASE + 41;
    }

    private ItemStack getInvisPotion() {
        ItemStack iStack = new ItemStack(Material.POTION);
        PotionMeta meta = (PotionMeta) iStack.getItemMeta();
        meta.setBasePotionData(new PotionData(PotionType.INVISIBILITY));
        iStack.setItemMeta(meta);
        return iStack;
    }

    @Override
    public ItemStack getItemStack(World.Environment environment, int modelId, int level) {
        ItemStack speed = addLore(getInvisPotion(), "Costs: " + getCost(environment, level) + " Mana");
        speed = addLore(speed, "Cooldown: " + getCooldown(level) / 1000d + " Seconds");
        ItemMeta iMeta = speed.getItemMeta();
        iMeta.setCustomModelData(modelId);
        iMeta.setDisplayName(ChatColor.WHITE + getLocalizedName());
        speed.setItemMeta(iMeta);
        return speed;
    }

    @Override
    public int getCost(World.Environment environment, int level) {
        return 75;
    }

    @Override
    public String getRiddle(int level) {
        return "With a root of gold, you can become as dark as the blackest stone";
    }

    @Override
    public long getCooldown(int level) {
        return 250;
    }

    @Override
    public SpellRecipe getRecipe(int level) {
        if (level != 1) return null;
        return new SpellRecipe(this, new MaterialRecipeIngredient(Material.REDSTONE),
                new MaterialRecipeIngredient(Material.GOLDEN_CARROT), new MaterialRecipeIngredient(Material.BLACKSTONE));
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
        new PotionEffect(PotionEffectType.INVISIBILITY, 20 * 20, 0).apply(target);
        return true;
    }
}
