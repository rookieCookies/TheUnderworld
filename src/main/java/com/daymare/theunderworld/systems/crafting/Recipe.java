package com.daymare.theunderworld.systems.crafting;

public class Recipe {
    private RecipeItem recipeItem;
    private RecipeItem[] recipe;

    public RecipeItem validate(RecipeItem[] items) {
        int multiplyAmount = getRecipeResultAmount(items);
        return multiplyAmount == 0 ? null : new RecipeItem(recipeItem.item, recipeItem.amount * multiplyAmount);
    }

    public void removeItems(RecipeItem[] items) {
        int resultAmount = getRecipeResultAmount(items);
        for (int i = 0; i < items.length; i++) {
            items[i].amount -= resultAmount * recipe[i].amount;
        }
    }

    public int getRecipeResultAmount(RecipeItem[] items) {
        int resultAmount = 0;
        for (int i = 0; i < items.length; i++) {
            resultAmount = items[i].amount/recipe[i].amount;
        }
        return resultAmount;
    }
}
