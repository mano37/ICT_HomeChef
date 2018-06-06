package com.homechef.ict.ict_homechef;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;



public class RecipeInfoActivity extends Activity {

    int id;
    String title;
    String img;
    String serve;
    ArrayList<String> ingredient = new ArrayList<>();
    String authorName;
    String createdAt;
    String updatedAt;
    String timeCost;
    String steps;
    int recommendedCount;

    int ingreNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipeinfo);

        Intent intent = getIntent();
        id = intent.getIntExtra("id", 0);
        //id 이용해서 서버에서 데이터 받아오기
        JSONObject recipeData = new JSONObject();




        LinearLayout llingredientList = findViewById(R.id.ll_ingredientlist);
        LinearLayout llIngredientQuantity = findViewById(R.id.ll_ingredientquantity);

        TextView tvAuthorName = findViewById(R.id.tv_authorname);
        TextView tvTitle = findViewById(R.id.tv_title);
        TextView tvCreatedAt = findViewById(R.id.tv_createdat);
        TextView tvRecommendCount = findViewById(R.id.tv_recommendcount);
        TextView tvServe = findViewById(R.id.tv_serve);
        TextView tvTimeCost = findViewById(R.id.tv_timecost);
        TextView tvSteps = findViewById(R.id.tv_steps);

        if(!parse(recipeData))
        {
           return;
        }

        tvAuthorName.setText(authorName);
        tvTitle.setText(title);
        tvCreatedAt.setText(createdAt);
        tvRecommendCount.setText(recommendedCount);
        tvServe.setText(serve);
        tvTimeCost.setText(timeCost);
        tvSteps.setText(steps);

    }

    private boolean parse(JSONObject jsonObject)
    {




        try {
            if(id != Integer.parseInt(jsonObject.getString("recipe_id")))
            {
                Toast.makeText(RecipeInfoActivity.this, "통신 에러가 발생했습니다.", Toast.LENGTH_SHORT).show();
                return false;
            }
            authorName = jsonObject.getString("author_name");
            id = jsonObject.getInt("recipe_id");
            title = jsonObject.getString("title");
            createdAt = jsonObject.getString("created_at");
            updatedAt = jsonObject.getString("updated_at");
            recommendedCount = jsonObject.getInt("recommended_count");
            serve = jsonObject.getString("serve");
            timeCost = jsonObject.getString("time_cost");
            img = jsonObject.getString("image_url");
            steps = jsonObject.getString("steps");

            ingreNum = jsonObject.getJSONArray("Ingredients").length();
            for(int i=0; i<ingreNum; i++) {

                ingredient.add(jsonObject.getString("ingre_count").toString());
            }


            /*createdAt = createdAt.replace("_", "");
            createdAt = createdAt.substring(0, 7);
            updatedAt.replace("_", "");
            updatedAt = updatedAt.substring(0, 7);*/



        } catch (JSONException e) {
            Toast.makeText(RecipeInfoActivity.this, "통신 에러가 발생했습니다.", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
            return false;
        }


        return true;
    }
}
