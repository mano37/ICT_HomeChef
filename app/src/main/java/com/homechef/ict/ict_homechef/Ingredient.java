package com.homechef.ict.ict_homechef;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Ingredient {

    private String name;
    private int search_mode = 0;

    Ingredient(String s)
    {
        name = s;
    }
    Ingredient(String s, int mode)
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

    public void setSearchMode(int x)
    {
        search_mode = x;

    }

    public void setName(String s)
    {
        name = s;
    }
}


