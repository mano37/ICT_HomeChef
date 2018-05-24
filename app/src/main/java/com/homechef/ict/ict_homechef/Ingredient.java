package com.homechef.ict.ict_homechef;

import android.content.Context;
import android.graphics.Color;
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

    private void cycleSearchMode()
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

    public void setName(String s)
    {
        name = s;
    }

    public void addIngredientView(LinearLayout ll, Context viewClass)
    {

        //동적 textview 생성
        final TextView textview_ingredient = new TextView(viewClass);
        textview_ingredient.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        textview_ingredient.setBackgroundColor(Color.parseColor("#00FFFFFF"));

        textview_ingredient.setTextColor(Color.parseColor("#000000"));
        textview_ingredient.setTextSize(16);
        textview_ingredient.setText(Ingredient.this.getName());
        textview_ingredient.setSingleLine();
        ll.addView(textview_ingredient);

        textview_ingredient.setBackgroundResource(R.drawable.roundingbox_green);


        //버튼화
        textview_ingredient.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Ingredient.this.cycleSearchMode();
                switch (Ingredient.this.getSearchMode())
                {
                    case 0:
                        textview_ingredient.setTextColor(Color.parseColor("#000000"));
                        textview_ingredient.setBackgroundResource(R.drawable.roundingbox_green);
                        break;
                    case 1:
                        textview_ingredient.setTextColor(Color.parseColor("#000000"));
                        textview_ingredient.setBackgroundResource(R.drawable.roundingbox_blue);
                        break;
                    case 2:
                        textview_ingredient.setTextColor(Color.parseColor("#000000"));
                        textview_ingredient.setBackgroundResource(R.drawable.roundingbox_red);
                        break;
                }
            }
        });


    }
}


