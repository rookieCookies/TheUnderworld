package com.daymare.theunderworld.systems.crafting;

import com.daymare.theunderworld.systems.items.ItemUtils;
import org.bukkit.inventory.ItemStack;

public class RecipeItem {
    final String item;
    int amount;

    public RecipeItem(String item, int amount) {
        this.item = item;
        this.amount = amount;
    }

    public static RecipeItem[] itemStackToRecipeItem(ItemStack[] items) {
        RecipeItem[] recipeItems = new RecipeItem[items.length];
        for (int i = 0; i < items.length; i++) {
            recipeItems[i] = itemStackToRecipeItem(items[i]);
        }
        return recipeItems;
    }

    public static RecipeItem itemStackToRecipeItem(ItemStack item) {
        return new RecipeItem(ItemUtils.getItemID(item), item.getAmount());
    }
}
