package xyz.fluxinc.moddedadditions.spells;

import org.bukkit.inventory.ItemStack;
import xyz.fluxinc.moddedadditions.spells.recipe.RecipeIngredient;
import java.util.ArrayList;
import java.util.List;

public class SpellRecipe {

    private List<RecipeIngredient> ingredients = new ArrayList<>();
    private final RecipeIngredient catalyst;

    public SpellRecipe(RecipeIngredient catalyst, RecipeIngredient ingredient1, RecipeIngredient ingredient2) {
        this.catalyst = catalyst;
        ingredients.add(ingredient1);
        ingredients.add(ingredient1);
        ingredients.add(ingredient1);
        ingredients.add(ingredient1);
        ingredients.add(ingredient2);
        ingredients.add(ingredient2);
        ingredients.add(ingredient2);
        ingredients.add(ingredient2);
    }

    public SpellRecipe(RecipeIngredient catalyst, RecipeIngredient ingredient1, RecipeIngredient ingredient2,
                       RecipeIngredient ingredient3, RecipeIngredient ingredient4) {
        this.catalyst = catalyst;
        ingredients.add(ingredient1);
        ingredients.add(ingredient1);
        ingredients.add(ingredient2);
        ingredients.add(ingredient2);
        ingredients.add(ingredient3);
        ingredients.add(ingredient3);
        ingredients.add(ingredient4);
        ingredients.add(ingredient4);
    }
    public SpellRecipe(RecipeIngredient catalyst, RecipeIngredient ingredient1, RecipeIngredient ingredient2,
                       RecipeIngredient ingredient3, RecipeIngredient ingredient4, RecipeIngredient ingredient5,
                       RecipeIngredient ingredient6, RecipeIngredient ingredient7, RecipeIngredient ingredient8) {
        this.catalyst = catalyst;
        ingredients.add(ingredient1);
        ingredients.add(ingredient2);
        ingredients.add(ingredient3);
        ingredients.add(ingredient4);
        ingredients.add(ingredient5);
        ingredients.add(ingredient6);
        ingredients.add(ingredient7);
        ingredients.add(ingredient8);
    }

    public boolean verifyItems(ItemStack catalyst, List<ItemStack> items) {
        if (!this.catalyst.verifyItem(catalyst)) return false;
        if (items.size() != 8) return false;
        for (RecipeIngredient ingredient : ingredients) {
            boolean found = false;
            for (int i = 0; i < items.size(); i++) {
                if (ingredient.verifyItem(items.get(i))) {
                    found = true;
                    items.remove(i);
                    break;
                }
            }
            if (!found) {
                return false;
            }
        }
        return true;
    }
}
