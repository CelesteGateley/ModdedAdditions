package xyz.fluxinc.moddedadditions.spells.castable.tank;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import xyz.fluxinc.moddedadditions.ModdedAdditions;
import xyz.fluxinc.moddedadditions.controllers.customitems.SpellBookController;
import xyz.fluxinc.moddedadditions.spells.Spell;
import xyz.fluxinc.moddedadditions.spells.SpellRecipe;
import xyz.fluxinc.moddedadditions.spells.recipe.EnchantedBookRecipeIngredient;
import xyz.fluxinc.moddedadditions.spells.recipe.MaterialRecipeIngredient;

public class Taunt extends Spell {

    @Override
    public ItemStack getDefaultItem(World.Environment environment, int level) {
        return new ItemStack(Material.MUSIC_DISC_CAT);
    }

    @Override
    public String getLocalizedName() {
        return "Taunt";
    }

    @Override
    public String getTechnicalName() {
        return "taunt";
    }

    @Override
    public String getDescription(int level) {
        switch (level) {
            case 1:
                return "Increases the radius!";
            case 2:
                return "Further increases the radius!";
            case 3:
                return "Provides thorns damage on all hits for 15 seconds";
            default:
                return "Draws the attention of all nearby mobs!";
        }
    }

    @Override
    public int getModelId() {
        return ModdedAdditions.KEY_BASE + SpellBookController.SB_KEY_BASE + 62;
    }

    @Override
    public int getCost(World.Environment environment, int level) {
        return 75;
    }

    @Override
    public String getRiddle(int level) {
        switch (level) {
            case 0:
                return "&9Catalyst: &4Redstone\n\n&9Wearing the darkened skull of your enemies, and holding the child of the sea, all enemies will want you dead";
            case 1:
                return "&9Catalyst: &cGlowstone Dust\n\n&9The dead flesh to face the transparent wall, and with luck, the enemy will not see a way past";
            case 2:
                return "&9Catalyst: &bDiamond\n\n&9Glowing brightly, the dust and block, you become a beacon and draw the damned flesh towards you";
            default:
                return null;
        }
    }

    @Override
    public long getCooldown(int level) {
        return 15;
    }

    @Override
    public SpellRecipe getRecipe(int level) {
        switch (level) {
            case 0:
                return new SpellRecipe(this, new MaterialRecipeIngredient(Material.REDSTONE),
                        new MaterialRecipeIngredient(Material.WITHER_SKELETON_SKULL), new MaterialRecipeIngredient(Material.TURTLE_EGG));
            case 1:
                return new SpellRecipe(this, new MaterialRecipeIngredient(Material.GLOWSTONE_DUST),
                        new MaterialRecipeIngredient(Material.ROTTEN_FLESH), new MaterialRecipeIngredient(Material.GLASS_PANE),
                        new EnchantedBookRecipeIngredient(Enchantment.LUCK), new MaterialRecipeIngredient(Material.SPIDER_EYE));
            case 2:
                return new SpellRecipe(this, new MaterialRecipeIngredient(Material.DIAMOND),
                        new MaterialRecipeIngredient(Material.BEACON), new MaterialRecipeIngredient(Material.GLOWSTONE),
                        new MaterialRecipeIngredient(Material.GLOWSTONE_DUST), new MaterialRecipeIngredient(Material.ROTTEN_FLESH));
            default:
                return null;
        }
    }

    @Override
    public boolean enactSpell(Player caster, LivingEntity target, int level) {
        int radius = 16;
        switch (level) {
            case 3:
                radius += 8;
            case 2:
                radius += 8;
        }
        caster.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("You taunt all enemies within 8 blocks!"));
        for (Entity entity : caster.getNearbyEntities(radius, 8, radius)) {
            if (entity instanceof Mob) {
                ((Mob) entity).setTarget(caster);
            }
        }
        if (level == 4) {
            ModdedAdditions.instance.getReflectDamageController().addReflected(caster, 15);
        }
        return true;
    }
}
