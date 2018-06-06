package com.homechef.ict.ict_homechef;

import java.util.ArrayList;

public class ThumnailInfo {

    private int recipeId;
    private String recipeName;
    //private String mainImgAddress;
    ArrayList<String> ingredientList;
    private String writerName;
    private int createdTime;
    private int count = 0;

    ThumnailInfo(int id, String title, /*String img,*/ ArrayList<String> ingredient, String authorName, int createdAt, int recommendedCount)
    {
        recipeId = id;
        recipeName = title;
        //mainImgAddress = img;
        ingredientList = ingredient;
        writerName = authorName;
        createdTime = createdAt;
        count = recommendedCount;
    }

    public int getRecipeId() { return recipeId; }
    public String getRecipeName()
    {
        return recipeName;
    }
    /*public String getMainImgAddress()
    {
        return mainImgAddress;
    }*/
    public ArrayList<String> getIngredientList()
    {
        return ingredientList;
    }
    public String getWriterName() { return writerName; }
    public int getCreatedTime() { return createdTime; }
    public int getCount()
    {
        return count;
    }
}
