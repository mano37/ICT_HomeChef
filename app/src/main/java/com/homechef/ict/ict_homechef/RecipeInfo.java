package com.homechef.ict.ict_homechef;

public class RecipeInfo {

    private String recipeName;
    private String mainImgAddress;
    private Ingredient[] ingredientList;
    private String[] cookingOrder;

    RecipeInfo(String name, String img, Ingredient[] ingredient, String[] order)
    {
        recipeName = name;
        mainImgAddress = img;
        ingredientList = ingredient;
        cookingOrder = order;
    }

    public String getRecipeName()
    {
        return recipeName;
    }

    public String getMainImgAddress()
    {
        return mainImgAddress;
    }

    public Ingredient[] getIngredientList()
    {
        return ingredientList;
    }
    public String[] getCookingOrder()
    {
        return cookingOrder;
    }
}
