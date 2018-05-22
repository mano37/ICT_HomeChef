package com.homechef.ict.ict_homechef;

public class Ingredient {

    private String name;
    private int search_mode = 0;

    public Ingredient(String s)
    {
        name = s;
    }
    public Ingredient(String s, int mode)
    {
        name = s;
        search_mode = mode;
    }

    public String getName()
    {
        return name;
    }
    public int getSearchMode()
    {
        return search_mode;
    }

    public void cycleSearchMode()
    {
        if(search_mode == 2)
        {
            search_mode = 0;
        }
        else
        {
            search_mode++;
        }
    }
}
