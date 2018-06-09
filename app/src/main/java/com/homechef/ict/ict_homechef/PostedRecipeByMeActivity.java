package com.homechef.ict.ict_homechef;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

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

public class PostedRecipeByMeActivity extends AppCompatActivity {

    ConnectUtil connectUtil = ConnectUtil.getInstance(this).createBaseApi();
    Map<String,String> header = new HashMap<>();
    static int int_scrollViewPos;
    static int int_TextView_lines;

    int loadedThumnail = 0;

    // user 정보
    private JsonObject userJson;
    private String userInfo;
    private JsonParser parser = new JsonParser();
    String jwt;
    int userID;

    final CacheUtil cacheUtil = new CacheUtil(PostedRecipeByMeActivity.this);
    String savedPostedRecipeByID;
    String filePostedRecipeByID;

    String[] searchList;
    int searchIndex;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipelist);

        //변수 선언
        Intent intent = getIntent();
        int ingredientNum;
        final ScrollView svRecipeList = findViewById(R.id.sv_recipelist);
        final LinearLayout llRecipeList = findViewById(R.id.ll_recipelist);


        // user 정보 얻기
        Intent startIntent = getIntent();
        userInfo = startIntent.getExtras().getString("user_info");
        userJson = (JsonObject) parser.parse(userInfo);
        jwt = userJson.get("jwt_token").getAsString();
        userID = userJson.get("user_id").getAsInt();

        makeHeader(jwt);
        filePostedRecipeByID = "PostedRecipeBy" + String.valueOf(userID);

        //로컬에 저장된 파일 불러오기
        try {
            savedPostedRecipeByID = cacheUtil.Read(filePostedRecipeByID);
            System.out.println("불러온 캐쉬 데이터 :" + savedPostedRecipeByID);
        } catch (IOException e) {
            System.out.println("cacheRead Fail!");
            e.printStackTrace();
        }

        searchList = savedPostedRecipeByID.split(" ");

        for(int i = 0; (i < 10 && i < searchList.length); i++){

            recipesGet(searchList[i], llRecipeList);
            loadedThumnail++;
            searchIndex++;

        }


        //화면 최하단 스크롤


        //Detect Bottom ScrollView
        svRecipeList.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                int_scrollViewPos = svRecipeList.getScrollY();
                int_TextView_lines = svRecipeList.getChildAt(0).getBottom() - svRecipeList.getHeight();

                if(int_TextView_lines == int_scrollViewPos){
                    //화면 최하단 스크롤시 이벤트

                    if(searchIndex < searchList.length){
                        int tmp = searchList.length - searchIndex;
                        for(int i = 0; (i < 10 && i < tmp); i++){

                            recipesGet(searchList[i], llRecipeList);
                            loadedThumnail++;
                            searchIndex++;

                        }
                    }
                }

            }
        });

    }


    private void showRecipeThumnail(final ThumnailInfo ti, LinearLayout ll)
    {
        //Thumnail의 틀 레이아웃
        final LinearLayout ll_thumnail = new LinearLayout(PostedRecipeByMeActivity.this);
        ll_thumnail.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 300));
        ll_thumnail.setBackgroundResource(R.drawable.boxline_gradientwhite);

        //Thumnail의 위아래 여백

        //Thumnail 내의 이미지 틀 레이아웃
        final LinearLayout ll_image = new LinearLayout(PostedRecipeByMeActivity.this);
        ll_image.setLayoutParams(new LinearLayout.LayoutParams(350, 300));
        ll_image.setBackgroundResource(R.drawable.boxline_black);

        //Thumnail 내의 메인 이미지
        ImageView image_thumnail = new ImageView(PostedRecipeByMeActivity.this);

        image_thumnail.setImageResource(R.drawable.thumnail);
        if(!ti.getImgUrl().isEmpty())
        {
            Picasso.get().load(ti.getImgUrl()).placeholder(R.drawable.thumnail).resize(350, 300).centerCrop().into(image_thumnail);
        }

        final LinearLayout ll_content = new LinearLayout(PostedRecipeByMeActivity.this);
        ll_content.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        ll_content.setOrientation(LinearLayout.VERTICAL);
        ll_content.setPadding(10, 10, 10, 10);

        final TextView tv_recipeName = new TextView(PostedRecipeByMeActivity.this);
        tv_recipeName.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        tv_recipeName.setTextColor(Color.parseColor("#000000"));
        tv_recipeName.setPadding(10, 10, 10, 10);
        tv_recipeName.setTextSize(15);
        tv_recipeName.setText(ti.getRecipeName());
        tv_recipeName.setSingleLine();

        final TextView tv_ingredientList = new TextView(PostedRecipeByMeActivity.this);
        tv_ingredientList.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        tv_ingredientList.setTextColor(Color.parseColor("#000000"));
        tv_ingredientList.setPadding(10, 10, 10, 10);
        tv_ingredientList.setTextSize(12);
        String recipeList = "재료: ";
        for(int i = 0; i < ti.getIngredientList().size(); i++)
        {
            recipeList += ti.getIngredientList().get(i) + " ";
        }
        tv_ingredientList.setText(recipeList);
        tv_ingredientList.setSingleLine();

        final TextView tv_recommendCount = new TextView(PostedRecipeByMeActivity.this);
        tv_recommendCount.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        tv_recommendCount.setTextColor(Color.parseColor("#000000"));
        tv_recommendCount.setPadding(10, 10, 10, 10);
        tv_recommendCount.setTextSize(10);
        tv_recommendCount.setText("추천수 " + ti.getCount());
        tv_recommendCount.setSingleLine();

        final TextView tv_writerName = new TextView(PostedRecipeByMeActivity.this);
        tv_writerName.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        tv_writerName.setTextColor(Color.parseColor("#000000"));
        tv_writerName.setPadding(10, 10, 10, 10);
        tv_writerName.setTextSize(12);
        tv_writerName.setText(ti.getWriterName());
        tv_writerName.setSingleLine();

        //완성된 썸네일 레이아웃을 레이아웃에 표출
        ll.addView(ll_thumnail);

        //썸네일 레이아웃에 내용 표출
        ll_thumnail.addView(ll_image);
        ll_thumnail.addView(ll_content);

        ll_image.addView(image_thumnail);

        ll_content.addView(tv_recipeName);
        ll_content.addView(tv_ingredientList);
        ll_content.addView(tv_recommendCount);
        ll_content.addView(tv_writerName);

        ll_thumnail.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(PostedRecipeByMeActivity.this, RecipeInfoActivity.class);
                intent.putExtra("id", ti.getRecipeId());
                startActivity(intent);
            }
        });
    }

    public void makeHeader(String jwt_token){

        String token = "Bearer " + jwt_token;
        header.put("Authorization",token);

    }

    public void recipesGet(String id, final LinearLayout ll) {

        connectUtil.getRecipe(header, id, new HttpCallback() {
            @Override
            public void onError(Throwable t) {
                // 내부적 에러 발생할 경우
                System.out.println("RecipeListGet onError@@@@@@");
            }
            @Override
            public void onSuccess(int code, Object receivedData) {
                // 성공적으로 완료한 경우
                RecipeGet data = (RecipeGet) receivedData;
                Iterator<String> ingreVal = data.ingre_count.keySet().iterator();
                int j = 0;
                ArrayList<String> ingreList = new ArrayList<>();
                while(ingreVal.hasNext())
                {
                    String keys = (String)ingreVal.next();
                    ingreList.add(keys);
                }
                ThumnailInfo thumnailinfo = new ThumnailInfo(
                        data.recipe_id,
                        data.title,
                        data.image_url,
                        ingreList,data.author_name,
                        data.created_at,
                        data.recommend_count);
                showRecipeThumnail(thumnailinfo, ll);


                System.out.println("RecipeListGet onSuccess@@@@@@");

            }

            @Override
            public void onFailure(int code) {
                // 통신에 실패한 경우
                // 결과값이 없다거나, 서버에서 오류를 리턴했거나
                // 또는 ResponseBody 안의 key 값이 이상하거나
                System.out.println("RecipeLIstGet onFailure@@@@@@");
            }
        });

    }
}
