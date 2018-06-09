package com.homechef.ict.ict_homechef;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.homechef.ict.ict_homechef.ConnectUtil.ConnectUtil;
import com.homechef.ict.ict_homechef.ConnectUtil.HttpCallback;
import com.homechef.ict.ict_homechef.ConnectUtil.RequestBody.RecipePut;
import com.homechef.ict.ict_homechef.ConnectUtil.ResponseBody.PostRecipeGet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class PostActivity extends AppCompatActivity {

    // connectUtil
    ConnectUtil connectUtil;
    Map<String,String> header = new HashMap<>();

    // user 정보
    private JsonObject userJson;
    private String userInfo;
    private JsonParser parser = new JsonParser();
    String jwt;
    int userID;

    String savedPostedRecipeByID;
    String filePostedRecipeByID;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        connectUtil = ConnectUtil.getInstance(this).createBaseApi();

        // get userInfo
        final Intent startIntent = getIntent();
        userInfo = startIntent.getExtras().getString("user_info");
        userJson = (JsonObject) parser.parse(userInfo);
        jwt = userJson.get("jwt_token").getAsString();
        userID = userJson.get("user_id").getAsInt();

        filePostedRecipeByID = "PostedRecipeBy" + String.valueOf(userID);


        /////////////////////////////////////////////////////////뷰 선언

        LinearLayout ll_postMain = findViewById(R.id.post_ll_main);
        LinearLayout ll_recipeTitle = findViewById(R.id.post_ll_recipetitle);
        LinearLayout ll_recipeInfo = findViewById(R.id.post_ll_recipeinfo);
        final LinearLayout ll_ingredientName = findViewById(R.id.post_ll_ingredientname);
        final LinearLayout ll_ingredientQuan = findViewById(R.id.post_ll_ingredientquan);
        final LinearLayout ll_ingredientButton = findViewById(R.id.post_ll_delingredientbutton);
        final LinearLayout ll_steps = findViewById(R.id.post_ll_steps);

        final EditText edt_recipeTitle = findViewById(R.id.post_edt_recipetitle) ;
        final EditText edt_serve = findViewById(R.id.post_edt_serve) ;
        final EditText edt_timeCost = findViewById(R.id.post_edt_timecost) ;

        final List<EditText> edt_ingredientName = new ArrayList<>();
        edt_ingredientName.add((EditText)findViewById(R.id.post_edt_ingre));

        final List<EditText> edt_ingredientQuantity = new ArrayList<>();
        edt_ingredientQuantity.add((EditText)findViewById(R.id.post_edt_quan));

        final Button btn_addIngredient = findViewById(R.id.post_btn_addingredient);
        final Button btn_addsteps = findViewById(R.id.post_btn_addsteps);


        final List<EditText> edt_steps = new ArrayList<>();
        edt_steps.add((EditText)findViewById(R.id.post_edt_step));


        Button btn_register = findViewById(R.id.post_btn_register);
        ///////////////////////////////////////////////////////////////////////////////

        makeHeader(jwt);

        btn_addIngredient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int instance = edt_ingredientName.size();

                edt_ingredientName.add(new EditText(PostActivity.this));
                edt_ingredientQuantity.add(new EditText(PostActivity.this));


                edt_ingredientName.get(instance).setHint("재료 입력");
                edt_ingredientQuantity.get(instance).setHint("양 입력");
                edt_ingredientName.get(instance).setSingleLine();
                edt_ingredientQuantity.get(instance).setSingleLine();

                edt_ingredientName.get(instance).setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                edt_ingredientQuantity.get(instance).setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

                ll_ingredientName.addView(edt_ingredientName.get(instance));
                ll_ingredientQuan.addView(edt_ingredientQuantity.get(instance));

            }
        });

        btn_addsteps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                RelativeLayout stepInstance = new RelativeLayout(PostActivity.this);
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                stepInstance.setLayoutParams(params);

                ll_steps.addView(stepInstance);
                int instance = edt_steps.size();

                edt_steps.add(new EditText(PostActivity.this));

                edt_steps.get(instance).setHint("조리 방법 입력");
                RelativeLayout.LayoutParams rightButton = new RelativeLayout.LayoutParams(getDPSize(40f), ViewGroup.LayoutParams.WRAP_CONTENT);
                RelativeLayout.LayoutParams leftEdt = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                rightButton.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                leftEdt.addRule(RelativeLayout.LEFT_OF, instance);
                edt_steps.get(instance).setLayoutParams(leftEdt);

                stepInstance.addView(edt_steps.get(instance));


            }
        });



        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = edt_recipeTitle.getText().toString();
                String serve = edt_serve.getText().toString();
                String timeCost = edt_timeCost.getText().toString();
                Map<String, String> ingredientList = new HashMap<>();
                for(int i = 0; i < edt_ingredientName.size(); i++)
                {
                    if(!edt_ingredientName.get(i).getText().toString().equals(""))
                    {
                        ingredientList.put(edt_ingredientName.get(i).getText().toString(), edt_ingredientQuantity.get(i).getText().toString());
                    }
                }
                String steps = "";
                String imgUrl = "";

                int j = 1;
                for(int i = 0; i < edt_steps.size(); i++)
                {

                    if(!edt_steps.get(i).getText().toString().equals(""))
                    {
                        steps += "step" + j + '\n';
                        steps += edt_steps.get(i).getText().toString() + '\n';
                        j++;
                    }
                }
                RecipePut recipeput = new RecipePut(title, serve, timeCost, ingredientList, steps, imgUrl);
                if(title.isEmpty())
                {
                    Toast.makeText(PostActivity.this, "제목을 적어주세요", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(serve.isEmpty())
                {
                    Toast.makeText(PostActivity.this, "인원 수를 적어주세요", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(timeCost.isEmpty())
                {
                    Toast.makeText(PostActivity.this, "소요 시간을 적어주세요", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(ingredientList.size() == 0)
                {
                    Toast.makeText(PostActivity.this, "최소 한 개의 재료를 입력해주세요", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(steps.equals(""))
                {
                    Toast.makeText(PostActivity.this, "최소 한 개의 요리방법을 적어주세요", Toast.LENGTH_SHORT).show();
                    return;
                }
                recipePost(recipeput);
            }
        });

    }

    public void makeHeader(String jwt_token){

        String token = "Bearer " + jwt_token;
        header.put("Authorization",token);

    }

    public void recipePost(RecipePut parameter) {

        connectUtil.postRecipe(header, parameter, new HttpCallback() {
            @Override
            public void onError(Throwable t) {
                // 내부적 에러 발생할 경우
            }
            @Override
            public void onSuccess(int code, Object receivedData) {
                // 성공적으로 완료한 경우
                CacheUtil cacheUtil = new CacheUtil(PostActivity.this);
                try {
                    savedPostedRecipeByID = cacheUtil.Read(filePostedRecipeByID);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                String writeString = String.valueOf(((PostRecipeGet)receivedData).recipe_id) + " " + savedPostedRecipeByID;
                try {
                    cacheUtil.Write(writeString, filePostedRecipeByID);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Toast.makeText(PostActivity.this, "레시피가 성공적으로 등록되었습니다!", Toast.LENGTH_SHORT).show();

                finish();
            }

            @Override
            public void onFailure(int code) {
                // 통신에 실패한 경우
                // 결과값이 없다거나, 서버에서 오류를 리턴했거나
                // 또는 ResponseBody 안의 key 값이 이상하거나
            }
        });
    }

    public int getDPSize(float dp)
    {
        DisplayMetrics metrics = PostActivity.this.getResources().getDisplayMetrics();
        float fpixels = metrics.density * dp;
        int pixels = (int) (fpixels + 0.5f);
        return pixels;
    }
}
