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

    public void addIngredientView(final LinearLayout ll, Context viewClass)
    {

        final LinearLayout ll_outline = new LinearLayout(viewClass);
        ll_outline.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        ll_outline.setGravity(Gravity.CENTER);
        ll_outline.setBackgroundResource(R.drawable.roundingbox_green);
        ll.addView(ll_outline);

        //동적 textview 생성
        final TextView textview_ingredient = new TextView(viewClass);
        textview_ingredient.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        textview_ingredient.setBackgroundColor(Color.parseColor("#00FFFFFF"));

        textview_ingredient.setTextColor(Color.parseColor("#000000"));
        textview_ingredient.setTextSize(16);
        textview_ingredient.setText(Ingredient.this.getName() + ' ');
        textview_ingredient.setSingleLine();
        ll_outline.addView(textview_ingredient);

        final TextView textview_delete = new TextView(viewClass);
        textview_delete.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        textview_delete.setBackgroundColor(Color.parseColor("#00FFFFFF"));

        textview_delete.setTextColor(Color.parseColor("#A0A0A0"));
        textview_delete.setTextSize(10);
        textview_delete.setText(" X");
        textview_delete.setSingleLine();
        ll_outline.addView(textview_delete);




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
                        ll_outline.setBackgroundResource(R.drawable.roundingbox_green);
                        break;
                    case 1:
                        ll_outline.setBackgroundResource(R.drawable.roundingbox_blue);
                        break;
                    case 2:
                        ll_outline.setBackgroundResource(R.drawable.roundingbox_red);
                        break;
                }
            }
        });

        textview_delete.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ll.removeView(ll_outline);
            }
        });


    }
}


