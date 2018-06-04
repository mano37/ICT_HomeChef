package com.homechef.ict.ict_homechef;

public class ThumnailInfo {

    private int recipeId;
    private String recipeName;
    //private String mainImgAddress;
    private String[] ingredientList;
    private String writerName;
    private int createdTime;
    private int recommendCount = 0;

    ThumnailInfo(int id, String name, /*String img,*/ String[] ingredient, String writer, int time, int count)
    {
        recipeId = id;
        recipeName = name;
        //mainImgAddress = img;
        ingredientList = ingredient;
        writerName = writer;
        createdTime = time;
        recommendCount = count;
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
    public String[] getIngredientList()
    {
        return ingredientList;
    }
    public String getWriterName() { return writerName; }
    public int getCreatedTime() { return createdTime; }
    public int getRecommendCount()
    {
        return recommendCount;
    }
}
