package com.homechef.ict.ict_homechef;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


public class RecipeListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipelist);

        final LinearLayout llRecipeList = (LinearLayout)findViewById(R.id.ll_recipelist);

        showRecipeThumnail("Test", llRecipeList);

    }

    private String getRecipe()
    {
        return null;
    }

    private void showRecipeThumnail(String s, LinearLayout ll)
    {
        //Thumnail의 틀 레이아웃
        final LinearLayout ll_thumnail = new LinearLayout(RecipeListActivity.this);
        ll_thumnail.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 350));
        ll_thumnail.setBackgroundColor(Color.parseColor("#80FF00"));


        final TextView textview_ingredient = new TextView(RecipeListActivity.this);
        textview_ingredient.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        textview_ingredient.setBackgroundColor(Color.parseColor("#00FFFFFF"));
        textview_ingredient.setPadding(20, 10, 10, 10);
        textview_ingredient.setTextColor(Color.parseColor("#000000"));
        textview_ingredient.setTextSize(13);
        textview_ingredient.setText(s);
        textview_ingredient.setSingleLine();

        //썸네일 레이아웃에 내용 표출
        ll.addView(ll_thumnail);

        //완성된 썸네일 레이아웃을 레이아웃에 표출
        ll_thumnail.addView(textview_ingredient);
    }
}

