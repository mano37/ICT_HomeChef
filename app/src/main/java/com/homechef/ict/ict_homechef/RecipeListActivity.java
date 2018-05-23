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

        Intent intent = getIntent();
        int ingredientNum;

        String[] searchlist = new String[2];
        searchlist[0] = null;
        searchlist[1] = null;

        final LinearLayout llRecipeList = findViewById(R.id.ll_recipelist);

        final LinearLayout ll_Status = new LinearLayout(RecipeListActivity.this);
        ll_Status.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

        final TextView tv_ListGuidance1 = new TextView(RecipeListActivity.this);
        tv_ListGuidance1.setTextSize(15);
        tv_ListGuidance1.setTextColor(Color.parseColor("#000000"));
        tv_ListGuidance1.setText("재료명 ");
        ll_Status.addView(tv_ListGuidance1);


        llRecipeList.addView(ll_Status);
        ingredientNum = intent.getIntExtra("ingredientNum", 0);
        for(int i = 0; i < ingredientNum; i++)
        {
            searchlist = intent.getStringArrayExtra(String.valueOf(i));
            final TextView tv_SearchInfo = new TextView(RecipeListActivity.this);
            tv_SearchInfo.setTextSize(15);
            tv_SearchInfo.setTextColor(Color.parseColor("#000000"));
            if(i == 0)
            {
                tv_SearchInfo.setText("'" + searchlist[0] + "'");
            }
            else
            {
                tv_SearchInfo.setText(", '" + searchlist[0] + "'");
            }
            ll_Status.addView(tv_SearchInfo);
        }

        final TextView tv_ListGuidance2 = new TextView(RecipeListActivity.this);
        tv_ListGuidance2.setTextSize(15);
        tv_ListGuidance2.setTextColor(Color.parseColor("#000000"));
        tv_ListGuidance2.setText(" (으)로 검색한 결과입니다.");
        ll_Status.addView(tv_ListGuidance2);



        for(int i = 0; i < 20; i++)
        {
            showRecipeThumnail("Test " + String.valueOf(i), "sinwindis", llRecipeList);
        }

    }

    private String getRecipe()
    {
        return null;
    }

    private void showRecipeThumnail(String recipeName, String writerName, LinearLayout ll)
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

        //Thumnail 내의 메인 이미지
        ImageView image_thumnail = new ImageView(RecipeListActivity.this);

        final LinearLayout ll_content = new LinearLayout(RecipeListActivity.this);
        ll_content.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        ll_content.setOrientation(LinearLayout.VERTICAL);
        ll_content.setPadding(20, 20, 20, 20);

        final TextView tv_recipeName = new TextView(RecipeListActivity.this);
        tv_recipeName.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        tv_recipeName.setTextColor(Color.parseColor("#000000"));
        tv_recipeName.setPadding(20, 20, 20, 20);
        tv_recipeName.setTextSize(15);
        tv_recipeName.setText(recipeName);
        tv_recipeName.setSingleLine();

        final TextView tv_writerName = new TextView(RecipeListActivity.this);
        tv_writerName.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        tv_writerName.setTextColor(Color.parseColor("#000000"));
        tv_writerName.setPadding(20, 20, 20, 20);
        tv_writerName.setTextSize(12);
        tv_writerName.setText(writerName);
        tv_writerName.setSingleLine();

        //완성된 썸네일 레이아웃을 레이아웃에 표출
        ll.addView(ll_thumnail);

        //썸네일 레이아웃에 내용 표출
        ll_thumnail.addView(ll_image);
        ll_thumnail.addView(ll_content);
        ll_image.addView(image_thumnail);
        ll_content.addView(tv_recipeName);
        ll_content.addView(tv_writerName);

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

