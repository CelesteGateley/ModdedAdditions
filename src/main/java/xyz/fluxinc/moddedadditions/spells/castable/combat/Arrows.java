package xyz.fluxinc.moddedadditions.spells.castable.combat;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
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

public class Arrows extends Spell {

    @Override
    public String getLocalizedName() {
        return "Shoot Arrows";
    }

    @Override
    public String getTechnicalName() {
        return "arrows";
    }

    @Override
    public int getModelId() {
        return ModdedAdditions.KEY_BASE + SpellBookController.SB_KEY_BASE + 1;
    }

    @Override
    public ItemStack getItemStack(World.Environment environment, int modelId, int level) {
        ItemStack arrows = addLore(new ItemStack(Material.BOW), "Costs: " + getCost(environment, level) + " Mana");
        arrows = addLore(arrows, "Cooldown: " + getCooldown(level) / 1000d + " Seconds");
        ItemMeta iMeta = arrows.getItemMeta();
        iMeta.setCustomModelData(modelId);
        iMeta.setDisplayName(ChatColor.WHITE + getLocalizedName());
        arrows.setItemMeta(iMeta);
        return arrows;
    }

    @Override
    public int getCost(World.Environment environment, int level) {
        return 5;
    }

    @Override
    public String getRiddle(int level) {
        return "The eyes of a spectre cause those shot with your firearm to glow to the heavens";
    }

    @Override
    public long getCooldown(int level) {
        return 250;
    }

    @Override
    public SpellRecipe getRecipe(int level) {
        if (level == 1)
            return new SpellRecipe(this, new MaterialRecipeIngredient(Material.REDSTONE),
                    new MaterialRecipeIngredient(Material.SPECTRAL_ARROW), new MaterialRecipeIngredient(Material.CROSSBOW));
        return null;
    }

    @Override
    public boolean enactSpell(Player caster, LivingEntity target, int level) {
        caster.getWorld().playSound(caster.getLocation(), Sound.ENTITY_ARROW_SHOOT, 1, 1);
        Arrow arrow = caster.launchProjectile(Arrow.class);
        arrow.setPickupStatus(AbstractArrow.PickupStatus.CREATIVE_ONLY);
        switch (level) {
            case 1:
                return true;
            case 4:
                arrow.setBasePotionData(new PotionData(PotionType.POISON));
            case 3:
                arrow.addCustomEffect(new PotionEffect(PotionEffectType.GLOWING,100,1),true);
            case 2:
                arrow.setDamage(arrow.getDamage()*2);
                return true;
        }
        return true;
    }

}
