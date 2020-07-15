package xyz.fluxinc.moddedadditions.spells.castable.combat;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
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

public class Fireball extends Spell {

    @Override
    public String getLocalizedName() {
        return "Fireball";
    }

    @Override
    public String getTechnicalName() {
        return "fireball";
    }

    @Override
    public int getModelId() {
        return ModdedAdditions.KEY_BASE + SpellBookController.SB_KEY_BASE + 3;
    }

    @Override
    public ItemStack getItemStack(World.Environment environment, int modelId, int level) {
        ItemStack fireball = addLore(new ItemStack(Material.FIRE_CHARGE), "Costs: " + getCost(environment, level) + " Mana");
        fireball = addLore(fireball, "Cooldown: " + getCooldown(level) / 1000 + " Seconds");
        ItemMeta iMeta = fireball.getItemMeta();
        iMeta.setCustomModelData(modelId);
        iMeta.setDisplayName(ChatColor.WHITE + getLocalizedName());
        fireball.setItemMeta(iMeta);
        return fireball;
    }

    @Override
    public int getCost(World.Environment environment, int level) {
        return 50;
    }

    @Override
    public String getRiddle(int level) {
        return "The tears of your enemies will burn away as you charge forth";
    }

    @Override
    public long getCooldown(int level) {
        return 1000;
    }

    @Override
    public SpellRecipe getRecipe(int level) {
        if (level != 1) return null;
        return new SpellRecipe(new MaterialRecipeIngredient(Material.REDSTONE),
                new MaterialRecipeIngredient(Material.FIRE_CHARGE), new MaterialRecipeIngredient(Material.GHAST_TEAR));
    }

    @Override
    public boolean enactSpell(Player caster, LivingEntity target, int level) {
        org.bukkit.entity.Fireball fireball = caster.launchProjectile(org.bukkit.entity.Fireball.class);
        fireball.setYield(2);
        caster.getWorld().playSound(caster.getLocation(), Sound.ENTITY_GHAST_SHOOT, 1, 1);
        return true;
    }
}
