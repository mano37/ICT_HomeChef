package com.homechef.ict.ict_homechef;


import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
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
import com.homechef.ict.ict_homechef.ConnectUtil.ResponseBody.RecipeListGet;
import com.squareup.picasso.Picasso;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class RecipeListActivity extends AppCompatActivity {

    ConnectUtil connectUtil = ConnectUtil.getInstance(this).createBaseApi();
    Map<String,String> header = new HashMap<>();
    static int int_scrollViewPos;
    static int int_TextView_lines;

    final ArrayList<String> searchList = new ArrayList<>();

    int loadedThumnail = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipelist);

        //변수 선언
        Intent intent = getIntent();
        int ingredientNum;
        final ScrollView svRecipeList = findViewById(R.id.sv_recipelist);
        final LinearLayout llRecipeList = findViewById(R.id.ll_recipelist);


        //intent받기
        ingredientNum = intent.getIntExtra("ingredientNum", 0);
        final String contain = intent.getStringExtra("contain");
        final String except = intent.getStringExtra("except");;



        String[] searchModeList = new String[ingredientNum];

        makeHeader("eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJlbWFpbCI6ImtqaHdhbmlkQGdtYWlsLmNvbSIsImV4cCI6MTUyODczMDY3OSwianRpIjoiNSIsImlhdCI6MTUyODI5ODY3OSwiaXNzIjoiSG9tZWNoZWYtU2VydmVyIn0.okMQOfVNKtDATGX99Xo_Xt3K5V6I-dFG5FnILgMIBWoX07fQmp1nEq2yVXCfar2KrU54Yd3FHPmBWPpjHS8eFQ");


        recipeListGet(contain, "0", "10", except, llRecipeList);
        loadedThumnail = 10;



        //화면 최하단 스크롤


        //Detect Bottom ScrollView
        svRecipeList.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                int_scrollViewPos = svRecipeList.getScrollY();
                int_TextView_lines = svRecipeList.getChildAt(0).getBottom() - svRecipeList.getHeight();

                if(int_TextView_lines == int_scrollViewPos){
                    //화면 최하단 스크롤시 이벤트

                    recipeListGet(contain, String.valueOf(loadedThumnail), "5", except, llRecipeList);
                    loadedThumnail += 5;

                }

            }
        });

    }


    private void showRecipeThumnail(final ThumnailInfo ti, LinearLayout ll)
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
        Picasso.get().load(ti.getImgUrl()).resize(350, 300).centerCrop().into(image_thumnail);


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
                Intent intent = new Intent(RecipeListActivity.this, RecipeInfoActivity.class);
                intent.putExtra("id", ti.getRecipeId());
                startActivity(intent);
            }
        });
    }

    public void makeHeader(String jwt_token){

        String token = "Bearer " + jwt_token;
        header.put("Authorization",token);

    }

    public void recipeListGet(String contain, String offset, String limit, String except, final LinearLayout ll) {

        connectUtil.getRecipeList(header, contain,offset,limit,except, new HttpCallback() {
            @Override
            public void onError(Throwable t) {
                // 내부적 에러 발생할 경우
                System.out.println("RecipeListGet onError@@@@@@");
            }
            @Override
            public void onSuccess(int code, Object receivedData) {
                // 성공적으로 완료한 경우
                List<RecipeListGet> data = (List<RecipeListGet>) receivedData;
                for(int i = 0; i < data.size(); i++)
                {
                    Iterator<String> ingreVal = data.get(i).ingre_count.keySet().iterator();
                    int j = 0;
                    ArrayList<String> ingreList = new ArrayList<>();
                    while(ingreVal.hasNext())
                    {
                        String keys = (String)ingreVal.next();
                        ingreList.add(keys);
                    }
                    ThumnailInfo thumnailinfo = new ThumnailInfo(data.get(i).recipe_id, data.get(i).title, data.get(i).image_url, ingreList,data.get(i).author_name, data.get(i).created_at, data.get(i).recommend_count);
                    showRecipeThumnail(thumnailinfo, ll);
                }


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

