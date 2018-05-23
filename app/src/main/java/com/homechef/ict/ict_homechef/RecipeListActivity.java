package com.homechef.ict.ict_homechef;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


public class RecipeListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipelist);

        final LinearLayout llRecipeList = findViewById(R.id.ll_recipelist);

        for(int i = 0; i < 20; i++)
        {
            showRecipeThumnail("Test " + String.valueOf(i), llRecipeList);
        }

    }

    private String getRecipe()
    {
        return null;
    }

    private void showRecipeThumnail(String s, LinearLayout ll)
    {
        //Thumnail의 틀 레이아웃
        final LinearLayout ll_thumnail = new LinearLayout(RecipeListActivity.this);
        ll_thumnail.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 300));
        ll_thumnail.setBackgroundResource(R.drawable.marginbox_white);
        ll_thumnail.setPadding(20, 20, 20, 20);

        //Thumnail 내의 이미지 틀 레이아웃
        final LinearLayout ll_image = new LinearLayout(RecipeListActivity.this);
        ll_image.setLayoutParams(new LinearLayout.LayoutParams(260, 260));
        ll_image.setPadding(20, 20, 20, 20);
        ll_image.setBackgroundResource(R.drawable.boxline_black);

        ImageView image_thumnail = new ImageView(RecipeListActivity.this);

        final TextView textview_ingredient = new TextView(RecipeListActivity.this);
        textview_ingredient.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        textview_ingredient.setBackgroundColor(Color.parseColor("#00FFFFFF"));
        textview_ingredient.setTextColor(Color.parseColor("#000000"));
        textview_ingredient.setPadding(20, 20, 20, 20);
        textview_ingredient.setTextSize(15);
        textview_ingredient.setText(s);
        textview_ingredient.setSingleLine();

        //썸네일 레이아웃에 내용 표출
        ll.addView(ll_thumnail);

        //완성된 썸네일 레이아웃을 레이아웃에 표출
        ll_thumnail.addView(ll_image);
        ll_thumnail.addView(textview_ingredient);

        ll_thumnail.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(RecipeListActivity.this, RecipeInfoActivity.class);
                startActivity(intent);
            }
        });
    }
}

