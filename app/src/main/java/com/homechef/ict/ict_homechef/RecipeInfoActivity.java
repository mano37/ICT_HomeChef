package com.homechef.ict.ict_homechef;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.homechef.ict.ict_homechef.ConnectUtil.ConnectUtil;
import com.homechef.ict.ict_homechef.ConnectUtil.HttpCallback;
import com.homechef.ict.ict_homechef.ConnectUtil.ResponseBody.RecipeGet;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public class RecipeInfoActivity extends Activity {

    int recipeId;
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

    ConnectUtil connectUtil;
    Map<String,String> header = new HashMap<>();

    int ingreNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipeinfo);

        Intent intent = getIntent();
        recipeId = intent.getIntExtra("id", 0);
        //id 이용해서 서버에서 데이터 받아오기




        String token = "eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJlbWFpbCI6ImtqaHdhbmlkQGdtYWlsLmNvbSIsImV4cCI6MTUyODczMDY3OSwianRpIjoiNSIsImlhdCI6MTUyODI5ODY3OSwiaXNzIjoiSG9tZWNoZWYtU2VydmVyIn0.okMQOfVNKtDATGX99Xo_Xt3K5V6I-dFG5FnILgMIBWoX07fQmp1nEq2yVXCfar2KrU54Yd3FHPmBWPpjHS8eFQ";
        makeHeader(token);
        RecipeGet(String.valueOf(recipeId));




    }
    public void makeHeader(String jwt_token){

        String token = "Bearer " + jwt_token;
        header.put("Authorization",token);

    }

    public void RecipeGet(String id) {

        connectUtil = ConnectUtil.getInstance(this).createBaseApi();


        connectUtil.getRecipe(header, id, new HttpCallback() {
            @Override
            public void onError(Throwable t) {
                // 내부적 에러 발생할 경우
                System.out.println("RecipeGet onError@@@@@@");
            }

            @Override
            public void onSuccess(int code, Object receivedData) {
                // 성공적으로 완료한 경우
                RecipeGet recipeSpec = ((RecipeGet) receivedData);
                recipeId = recipeSpec.recipe_id;
                title = recipeSpec.title;
                img = recipeSpec.image_url;
                authorName = recipeSpec.author_name;
                createdAt = recipeSpec.created_at;
                updatedAt = recipeSpec.updated_at;
                timeCost = recipeSpec.time_cost;
                steps = recipeSpec.steps;
                recommendedCount = recipeSpec.recommend_count;

                ArrayList<String> ingreList = new ArrayList<>();
                ArrayList<String> ingreQuan = new ArrayList<>();
                Iterator<String> ingreVal = recipeSpec.ingre_count.keySet().iterator();
                int j = 0;
                while(ingreVal.hasNext())
                {
                    String keys = (String)ingreVal.next();
                    ingreList.add(keys);
                    ingreQuan.add(recipeSpec.ingre_count.get(keys));
                }

                System.out.println("RecipeGet onSuccess@@@@@@");

                LinearLayout llIngredientList = findViewById(R.id.ll_ingredientlist);
                LinearLayout llIngredientQuantity = findViewById(R.id.ll_ingredientquantity);

                TextView tvAuthorName = findViewById(R.id.tv_authorname);
                TextView tvTitle = findViewById(R.id.tv_title);
                TextView tvCreatedAt = findViewById(R.id.tv_createdat);
                TextView tvUpdatedAt = findViewById(R.id.tv_updatedat);
                TextView tvRecommendCount = findViewById(R.id.tv_recommendcount);
                TextView tvServe = findViewById(R.id.tv_serve);
                TextView tvTimeCost = findViewById(R.id.tv_timecost);
                TextView tvSteps = findViewById(R.id.tv_steps);


                tvAuthorName.setText(authorName);
                tvTitle.setText(title);
                tvCreatedAt.setText(createdAt);
                tvUpdatedAt.setText(updatedAt);
                tvRecommendCount.setText("추천 " + String.valueOf(recommendedCount));
                tvServe.setText(serve);
                tvTimeCost.setText(timeCost);
                tvSteps.setText(steps);

                for(int i = 0; i < recipeSpec.ingre_count.size(); i++)
                {
                    TextView tv_IngredientName = new TextView(RecipeInfoActivity.this);

                    tv_IngredientName.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                    tv_IngredientName.setTextColor(Color.parseColor("#000000"));
                    tv_IngredientName.setPadding(10, 10, 10, 10);
                    tv_IngredientName.setTextSize(10);
                    tv_IngredientName.setText(ingreList.get(i));
                    tv_IngredientName.setSingleLine();

                    TextView tv_IngredientQuantity = new TextView(RecipeInfoActivity.this);

                    tv_IngredientQuantity.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                    tv_IngredientQuantity.setTextColor(Color.parseColor("#000000"));
                    tv_IngredientQuantity.setPadding(10, 10, 10, 10);
                    tv_IngredientQuantity.setTextSize(10);
                    tv_IngredientQuantity.setText(ingreQuan.get(i));
                    tv_IngredientQuantity.setSingleLine();

                    llIngredientList.addView(tv_IngredientName);
                    llIngredientQuantity.addView(tv_IngredientQuantity);
                }

            }

            @Override
            public void onFailure(int code) {
                // 통신에 실패한 경우
                // 결과값이 없다거나, 서버에서 오류를 리턴했거나
                // 또는 ResponseBody 안의 key 값이 이상하거나
                System.out.println("RecipeGet onFailure@@@@@@");
            }
        });

    }
}
