package xyz.fluxinc.moddedadditions.spells.castable.tank;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import xyz.fluxinc.moddedadditions.spells.Spell;

import static xyz.fluxinc.fluxcore.utils.LoreUtils.addLore;

public class Taunt extends Spell {

    @Override
    public String getLocalizedName() {
        return "Taunt";
    }

    @Override
    public String getTechnicalName() {
        return "taunt";
    }

    @Override
    public ItemStack getItemStack(World.Environment environment, int modelId, int level) {
        ItemStack taunt = addLore(new ItemStack(Material.MUSIC_DISC_CAT), "Costs: " + getCost(environment, level) + " Mana");
        taunt = addLore(taunt, "Cooldown: " + getCooldown(level) / 1000d + " Seconds");
        ItemMeta iMeta = taunt.getItemMeta();
        iMeta.setCustomModelData(modelId);
        iMeta.setDisplayName(ChatColor.WHITE + getLocalizedName());
        taunt.setItemMeta(iMeta);
        return taunt;
    }

    @Override
    public int getCost(World.Environment environment, int level) {
        return 75;
    }

    @Override
    public String getRiddle(int level) {
        return "Wearing the darkened skull of your enemies, and holding the child of the sea, all enemies will want you dead";
    }

    @Override
    public long getCooldown(int level) {
        return 15;
    }

    @Override
    public boolean enactSpell(Player caster, LivingEntity target, int level) {
        caster.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("You taunt all enemies within 8 blocks!"));
        for (Entity entity : caster.getNearbyEntities(8, 8, 8)) {
            if (entity instanceof Mob) {
                ((Mob) entity).setTarget(caster);
            }
        }
        return true;
    }
}
