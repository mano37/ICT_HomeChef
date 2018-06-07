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

import com.homechef.ict.ict_homechef.ConnectUtil.ConnectUtil;
import com.homechef.ict.ict_homechef.ConnectUtil.HttpCallback;
import com.homechef.ict.ict_homechef.ConnectUtil.ResponseBody.RecipeGet;
import com.homechef.ict.ict_homechef.ConnectUtil.ResponseBody.RecipeListGet;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class RecipeListActivity extends AppCompatActivity {

    ConnectUtil connectUtil;
    RecipeGet recipeData;
    static int int_scrollViewPos;
    static int int_TextView_lines;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipelist);

        //변수 선언
        Intent intent = getIntent();
        int ingredientNum;




        //레이아웃 선언
        final ScrollView svRecipeList = findViewById(R.id.sv_recipelist);
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

        final ArrayList<String> searchList = new ArrayList<>();

        String[] searchModeList = new String[ingredientNum];

        for(int i = 0; i < ingredientNum; i++)
        {
            searchList.add(intent.getStringArrayExtra(String.valueOf(i))[0]);
            final TextView tv_SearchInfo = new TextView(RecipeListActivity.this);
            tv_SearchInfo.setTextSize(15);
            tv_SearchInfo.setTextColor(Color.parseColor("#000000"));
            if(i == 0)
            {
                tv_SearchInfo.setText("'" + searchList.get(0) + "'");
            }
            else
            {
                tv_SearchInfo.setText(", '" + searchList.get(i) + "'");
            }
            ll_Status.addView(tv_SearchInfo);
        }

        final TextView tv_ListGuidance2 = new TextView(RecipeListActivity.this);
        tv_ListGuidance2.setTextSize(15);
        tv_ListGuidance2.setTextColor(Color.parseColor("#000000"));
        tv_ListGuidance2.setText(" (으)로 검색한 결과입니다.");
        ll_Status.addView(tv_ListGuidance2);

        ///////////////데이터 받아오기 테스트
        List<RecipeListGet> recipeList = new ArrayList<>();
        recipeList = null;

        String header = "eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJlbWFpbCI6ImtqaHdhbmlkQGdtYWlsLmNvbSIsImV4cCI6MTUyODczMDY3OSwianRpIjoiNSIsImlhdCI6MTUyODI5ODY3OSwiaXNzIjoiSG9tZWNoZWYtU2VydmVyIn0.okMQOfVNKtDATGX99Xo_Xt3K5V6I-dFG5FnILgMIBWoX07fQmp1nEq2yVXCfar2KrU54Yd3FHPmBWPpjHS8eFQ";
        RecipeListGet(header, "할라피뇨+장아찌&limit=5&offset=0", recipeList);

        ///////////////



        for(int i = 0; i < 20; i++)
        {
            ThumnailInfo thumnailinfo = new ThumnailInfo(i, "ex" + String.valueOf(i), searchList,"sinwindis", 20160044, 0);
            showRecipeThumnail(thumnailinfo, llRecipeList);
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

                    for(int i = 0; i < 5; i++)
                    {
                        ThumnailInfo thumnailinfo = new ThumnailInfo(i, "ex" + String.valueOf(i), searchList,"sinwindis", 20160044, 0);
                        showRecipeThumnail(thumnailinfo, llRecipeList);
                    }

                }

            }
        });

    }


    private JSONObject[] getRecipe()
    {
        return null;
    }

    private void parse(JSONObject jsonObject, ThumnailInfo thumnailInfo)
    {
        int id;
        String title;
        String img;
        ArrayList<String> ingredient = new ArrayList<>();
        String authorName;
        String createdAt;
        int recommendedCount;

        int ingreNum;



        try {
            id = jsonObject.getInt("id");
            title = jsonObject.getString("title");

            ingreNum = jsonObject.getJSONArray("Ingredients").length();
            for(int i=0; i<ingreNum; i++) {

                ingredient.add(jsonObject.getString("ingre_count").toString());
            }

            authorName = jsonObject.getString("author_name");
            createdAt = jsonObject.getString("created_at");
            /*createdAt.replace("_", "");
            createdAt = createdAt.substring(0, 7);*/
            recommendedCount = jsonObject.getInt("recommended_count");

            ThumnailInfo temp = new ThumnailInfo(id, title, ingredient, authorName, Integer.parseInt(createdAt), recommendedCount);
            thumnailInfo = temp;

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    private void showRecipeThumnail(ThumnailInfo ti, LinearLayout ll)
    {
        //Thumnail의 틀 레이아웃
        final LinearLayout ll_thumnail = new LinearLayout(RecipeListActivity.this);
        ll_thumnail.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 300));
        ll_thumnail.setBackgroundResource(R.drawable.boxline_gradientwhite);

        //Thumnail의 위아래 여백

        //Thumnail 내의 이미지 틀 레이아웃
        final LinearLayout ll_image = new LinearLayout(RecipeListActivity.this);
        ll_image.setLayoutParams(new LinearLayout.LayoutParams(350, 300));
        ll_image.setBackgroundResource(R.drawable.boxline_black);

        //Thumnail 내의 메인 이미지
        ImageView image_thumnail = new ImageView(RecipeListActivity.this);

        final LinearLayout ll_content = new LinearLayout(RecipeListActivity.this);
        ll_content.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        ll_content.setOrientation(LinearLayout.VERTICAL);
        ll_content.setPadding(10, 10, 10, 10);

        final TextView tv_recipeName = new TextView(RecipeListActivity.this);
        tv_recipeName.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        tv_recipeName.setTextColor(Color.parseColor("#000000"));
        tv_recipeName.setPadding(10, 10, 10, 10);
        tv_recipeName.setTextSize(15);
        tv_recipeName.setText(ti.getRecipeName());
        tv_recipeName.setSingleLine();

        final TextView tv_ingredientList = new TextView(RecipeListActivity.this);
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

        final TextView tv_recommendCount = new TextView(RecipeListActivity.this);
        tv_recommendCount.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        tv_recommendCount.setTextColor(Color.parseColor("#000000"));
        tv_recommendCount.setPadding(10, 10, 10, 10);
        tv_recommendCount.setTextSize(10);
        tv_recommendCount.setText("추천수 " + ti.getCount());
        tv_recommendCount.setSingleLine();

        final TextView tv_writerName = new TextView(RecipeListActivity.this);
        tv_writerName.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        tv_writerName.setTextColor(Color.parseColor("#000000"));
        tv_writerName.setPadding(10, 10, 10, 10);
        tv_writerName.setTextSize(12);
        tv_writerName.setText("by " + ti.getWriterName());
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

                Intent intent = new Intent(RecipeListActivity.this, RecipeInfoActivity.class);

                intent.putExtra("id", 0);
                startActivity(intent);
            }
        });
    }

    public void RecipeListGet(String jwt_token, String query, final List<RecipeListGet> recipelist){

        connectUtil = ConnectUtil.getInstance(this).createBaseApi();

        String token = "Bearer " + jwt_token;

        connectUtil.getRecipeList(token, query, new HttpCallback() {
            @Override
            public void onError(Throwable t) {
                // 내부적 에러 발생할 경우
                System.out.println("RecipeListGet onError@@@@@@");
            }
            @Override
            public void onSuccess(int code, Object receivedData) {
                // 성공적으로 완료한 경우
                List<RecipeListGet> RecipeData = (List<RecipeListGet>) receivedData;

                System.out.println(receivedData);

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

    public void RecipeGet(String jwt_token, String id){

        connectUtil = ConnectUtil.getInstance(this).createBaseApi();

        String token = "Bearer " + jwt_token;

        connectUtil.getRecipe(token, id, new HttpCallback() {
            @Override
            public void onError(Throwable t) {
                // 내부적 에러 발생할 경우
                System.out.println("RecipeGet onError@@@@@@");
            }
            @Override
            public void onSuccess(int code, Object receivedData) {
                // 성공적으로 완료한 경우
                RecipeGet RecipeData = (RecipeGet) receivedData;
                System.out.println("RecipeGet onSuccess@@@@@@");

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

