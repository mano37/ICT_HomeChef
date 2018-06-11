package com.homechef.ict.ict_homechef;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.homechef.ict.ict_homechef.ConnectUtil.ConnectUtil;
import com.homechef.ict.ict_homechef.ConnectUtil.HttpCallback;
import com.homechef.ict.ict_homechef.ConnectUtil.ResponseBody.RecipeGet;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public class RecipeInfoActivity extends Activity {

    int recipeId;
    String title;
    String img;
    String serve;
    String authorName;
    String createdAt;
    String updatedAt;
    String timeCost;
    String steps;
    int authorID;
    int recommendedCount;

    ConnectUtil connectUtil = ConnectUtil.getInstance(this).createBaseApi();
    Map<String,String> header = new HashMap<>();

    // user 정보
    private JsonObject userJson;
    private String userInfo;
    private JsonParser parser = new JsonParser();
    String jwt;
    int userID;

    // 캐시 관련 스트링
    String savedRecipeShownByID;
    String fileRecipeShownByID;
    String savedPostedRecipeByID;
    String filePostedRecipeByID;

    // startActivityForResult 반환용
    Intent sendResult = new Intent();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipeinfo);

        Intent intent = getIntent();
        recipeId = intent.getIntExtra("id", 0);

        // get userInfo
        Intent startIntent = getIntent();
        userInfo = startIntent.getExtras().getString("user_info");
        userJson = (JsonObject) parser.parse(userInfo);
        jwt = userJson.get("jwt_token").getAsString();
        userID = userJson.get("user_id").getAsInt();

        //id 이용해서 서버에서 데이터 받아오기


        fileRecipeShownByID = "RecipeShownBy" + userID;
        filePostedRecipeByID = "PostedRecipeBy" + userID;




        makeHeader(jwt);
        RecipeGet(String.valueOf(recipeId));


        Button deleteButton = (Button)findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AlertDialog.Builder(RecipeInfoActivity.this)
                        .setTitle(getString(R.string.recipedelete))
                        .setMessage(getString(R.string.recipedeletecheck))
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                if(userID == authorID){
                                    deleteRecipe();
                                }
                            }})
                        .setNegativeButton(android.R.string.no, null).show();


            }
        });



    }
    public void makeHeader(String jwt_token){

        String token = "Bearer " + jwt_token;
        header.put("Authorization",token);

    }

    // 레시피 삭제 함수
    public void deleteRecipe(){

        connectUtil.deleteRecipe(header, recipeId, new HttpCallback() {
            @Override
            public void onError(Throwable t) {
                Toast.makeText(RecipeInfoActivity.this, getString(R.string.deletefail), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(int code, Object receivedData) {
                Toast.makeText(RecipeInfoActivity.this, getString(R.string.deletesuccessfully), Toast.LENGTH_SHORT).show();

                CacheUtil cacheUtil = new CacheUtil(RecipeInfoActivity.this);
                try {
                    savedPostedRecipeByID = cacheUtil.Read(filePostedRecipeByID);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                String[] splitStr = savedPostedRecipeByID.split(" ");
                String tmpString = "";
                int tmpindex = 0;
                for(int i=0; i < splitStr.length; i++){
                    if(splitStr[i].equals(Integer.toString(recipeId))){
                        splitStr[i] = null;
                    }
                    else{
                        tmpString = tmpString + splitStr[i] + " ";
                    }
                }
                String writeString = tmpString;
                try {
                    cacheUtil.Write(writeString, filePostedRecipeByID);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Intent sendResult = new Intent();
                sendResult.putExtra("delete", 1);
                setResult(112, sendResult);

                finish();
            }

            @Override
            public void onFailure(int code) {
                Toast.makeText(RecipeInfoActivity.this, getString(R.string.deletefail), Toast.LENGTH_SHORT).show();
            }
        });

    }


    // 레시피 get 함수
    public void RecipeGet(final String id) {

        connectUtil.getRecipe(header, id, new HttpCallback() {
            @Override
            public void onError(Throwable t) {
                // 내부적 에러 발생할 경우
                System.out.println("RecipeGet onError@@@@@@");
            }

            @Override
            public void onSuccess(int code, Object receivedData) {
                // 성공적으로 완료한 경우
                CacheUtil cacheUtil = new CacheUtil(RecipeInfoActivity.this);
                try {
                    savedRecipeShownByID = cacheUtil.Read(fileRecipeShownByID);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                String[] splitStr = savedRecipeShownByID.split(" ", 10);
                String tmpString = "";
                int tmpindex = 0;
                for(int i=0; i < splitStr.length; i++){
                    if(splitStr[i].equals(id)){
                        splitStr[i] = null;
                    }
                    else{
                        tmpindex++;
                        if(tmpindex < 10) tmpString = tmpString + splitStr[i] + " ";

                    }
                }

                String writeString = id + " " + tmpString;

                try {
                    cacheUtil.Write(writeString, fileRecipeShownByID);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                RecipeGet recipeSpec = ((RecipeGet) receivedData);
                recipeId = recipeSpec.recipe_id;
                title = recipeSpec.title;
                img = recipeSpec.image_url;
                authorName = recipeSpec.author_name;
                createdAt = recipeSpec.created_at;
                updatedAt = recipeSpec.updated_at;
                timeCost = recipeSpec.time_cost;
                steps = recipeSpec.steps;
                serve = recipeSpec.serve;
                recommendedCount = recipeSpec.recommend_count;
                authorID = recipeSpec.author_id;

                if(authorID == userID){
                    Button button = (Button) findViewById(R.id.deleteButton);
                    button.setVisibility(View.VISIBLE);
                }

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

                ImageView imgMain = findViewById(R.id.image);

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
                tvRecommendCount.setText(String.valueOf(recommendedCount));
                tvServe.setText(serve);
                tvTimeCost.setText(timeCost);
                tvSteps.setText(steps);


                Display display = getWindowManager().getDefaultDisplay();
                Point size = new Point();
                display.getSize(size);
                int width = size.x;
                if(!img.isEmpty())
                {

                    Picasso.get().load(img).resize(width, 0).centerCrop().into(imgMain);
                }


                for(int i = 0; i < recipeSpec.ingre_count.size(); i++)
                {
                    TextView tv_IngredientName = new TextView(RecipeInfoActivity.this);

                    tv_IngredientName.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                    tv_IngredientName.setTextColor(Color.parseColor("#000000"));
                    tv_IngredientName.setPadding(10, 10, 10, 10);
                    tv_IngredientName.setTextSize(13);
                    tv_IngredientName.setText(ingreList.get(i));
                    tv_IngredientName.setSingleLine();


                    TextView tv_IngredientQuantity = new TextView(RecipeInfoActivity.this);

                    tv_IngredientQuantity.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                    tv_IngredientQuantity.setTextColor(Color.parseColor("#000000"));
                    tv_IngredientQuantity.setPadding(10, 10, 10, 10);
                    tv_IngredientQuantity.setTextSize(13);
                    if(ingreQuan.get(i).equals(" "))
                    {
                        ingreQuan.set(i, "-");
                    }
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
