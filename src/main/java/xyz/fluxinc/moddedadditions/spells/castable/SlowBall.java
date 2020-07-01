package xyz.fluxinc.moddedadditions.spells.castable;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import xyz.fluxinc.moddedadditions.ModdedAdditions;
import xyz.fluxinc.moddedadditions.spells.Spell;

import static xyz.fluxinc.fluxcore.utils.LoreUtils.addLore;

public class SlowBall extends Spell {

    public static final String SLOWBALL_NAME = "abXYZoasDGAdgiQXVSAadsgasnNEFA";

    public SlowBall(ModdedAdditions instance) {
        super(instance);
    }

    @Override
    public String getName() {
        return "Slow Ball";
    }

    @Override
    public ItemStack getItemStack(int modelId) {
        ItemStack slowball = addLore(new ItemStack(Material.SNOWBALL), "Costs: " + getCost() + " Mana");
        ItemMeta iMeta = slowball.getItemMeta();
        iMeta.setCustomModelData(modelId);
        iMeta.setDisplayName(ChatColor.WHITE + getName());
        slowball.setItemMeta(iMeta);
        return slowball;
    }

    @Override
    public int getCost() {
        return 15;
    }

    @Override
    public String getRiddle() {
        return "Winters chill slows the body, packing your speed and slowing you to a crawl";
    }

    @Override
    public boolean enactSpell(Player caster, LivingEntity target) {
        Entity entity = caster.launchProjectile(Snowball.class);
        entity.setCustomName(SLOWBALL_NAME);
        return true;
    }
}
