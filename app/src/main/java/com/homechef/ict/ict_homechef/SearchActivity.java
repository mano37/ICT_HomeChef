package com.homechef.ict.ict_homechef;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public class SearchActivity extends AppCompatActivity {

    final int MAXINGREDIENT = 10;
    final int MAXLAYOUTSIZE = 20;
    final int MARGINSIZE = 2;
    int[] lineSize = {0, 0, 0, 0};
    final LinearLayout[] llNowIngredient = new LinearLayout[4];
    final LinearLayout[] llRecentIngredient = new LinearLayout[4];

    final String fileName = "cacheIngredients.txt";


    Map<String , Integer> nowIngredientSet = new HashMap<String , Integer>();
    Map<String , Integer> recentIngredientSet = new HashMap<String , Integer>();
    //final ArrayList<String> nowIngredientSet = new ArrayList<>();
    //final ArrayList<Integer> nowSearchSet = new ArrayList<>();


    final DynamicLayout dlNowIngredient = new DynamicLayout(llNowIngredient, MAXLAYOUTSIZE, MAXINGREDIENT);
    final DynamicLayout dlRecentIngredient = new DynamicLayout(llRecentIngredient, MAXLAYOUTSIZE, MAXINGREDIENT);
    String savedIngredients = "";
    String[] loadIngredients;
    String temp;

    // user 정보
    private JsonObject userJson;
    private String userInfo;
    private JsonParser parser = new JsonParser();
    String jwt;
    int userID;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        // get userInfo
        Intent startIntent = getIntent();
        userInfo = startIntent.getExtras().getString("user_info");
        userJson = (JsonObject) parser.parse(userInfo);
        jwt = userJson.get("jwt_token").getAsString();
        userID = userJson.get("user_id").getAsInt();

        //캐쉬유틸 선언
        final CacheUtil cacheUtil = new CacheUtil(SearchActivity.this);

        //로컬에 저장된 최근 검색어 불러오기
        try {
            savedIngredients = cacheUtil.Read(fileName);
            System.out.println("불러온 캐쉬 데이터 :" + savedIngredients);
        } catch (IOException e) {
            System.out.println("cacheRead Fail!");
            e.printStackTrace();
        }

        //불러온 최근 검색어로 최근 검색어 버튼 생성하기
        loadIngredients = savedIngredients.split(" ", 10);


        //레이아웃 초기화
        llNowIngredient[0] = findViewById(R.id.ll_nowingredient1);
        llNowIngredient[1] = findViewById(R.id.ll_nowingredient2);
        llNowIngredient[2] = findViewById(R.id.ll_nowingredient3);
        llNowIngredient[3] = findViewById(R.id.ll_nowingredient4);
        llRecentIngredient[0] = findViewById(R.id.ll_recentingredient1);
        llRecentIngredient[1] = findViewById(R.id.ll_recentingredient2);
        llRecentIngredient[2] = findViewById(R.id.ll_recentingredient3);
        llRecentIngredient[3] = findViewById(R.id.ll_recentingredient4);



        final EditText edtAddIngredient = findViewById(R.id.edt_ingredientadd);
        Button btnAddIngredient = findViewById(R.id.btn_ingredientadd);
        Button btnSearch = findViewById(R.id.btn_search);

        for(int i = 0; i < loadIngredients.length; i++)
        {
            if(loadIngredients[i].length() != 0)
            {
                addIngredientButton(loadIngredients[i], recentIngredientSet, dlRecentIngredient);
            }
        }
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //RecipeListActivity에 데이터 전달
                String[] data = new String[2];
                data[0] = "";
                data[1] = "";
                Intent intent = new Intent(SearchActivity.this, RecipeListActivity.class);
                // if(nowIngredientSet.size() >= 1) 시작
                if(nowIngredientSet.size() >= 1) {
                    Iterator<String> ingreVal = nowIngredientSet.keySet().iterator();
                    int j = 0;
                    while (ingreVal.hasNext()) {
                        int searchType;
                        String keys = (String) ingreVal.next();
                        searchType = nowIngredientSet.get(keys);

                        //////// 최근 사용한 검색어에 추가
                        recentIngredientSet.put(keys, 0);
                        ////////

                        if (data[searchType].length() == 0) {
                            data[searchType] = keys;
                        } else {
                            data[searchType] += " " + keys;
                        }

                    }

                    // 기본옵션값의 데이터가 없다면
                    if (data[0].equals("")) {
                        Toast.makeText(SearchActivity.this, "최소 1개 이상의 재료를 포함해야 합니다.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    //검색한 목록 캐쉬에 저장
//////////////////
                    Iterator<String> saveIngre = recentIngredientSet.keySet().iterator();
                    int k = 0;

                    savedIngredients = "";
                    while (saveIngre.hasNext()) {
                        int searchType;
                        String keys = (String) saveIngre.next();
                        if (savedIngredients.length() == 0) {
                            savedIngredients = keys;
                        } else {
                            savedIngredients += " " + keys;
                        }
                    }
                    /////////////////////////
                    try {
                        cacheUtil.Write(savedIngredients, fileName);
                    } catch (IOException e) {
                        System.out.println("cacheWrite Fail!");
                        e.printStackTrace();
                    }

                    intent.putExtra("contain", data[0]);
                    intent.putExtra("except", data[1]);
                    intent.putExtra("user_info", userInfo);

                    startActivity(intent);
                }
                // if(nowIngredientSet.size() >= 1) 끝
            }
        });

        btnAddIngredient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                temp = edtAddIngredient.getText().toString().replace(" ", "");
                edtAddIngredient.setText(null);
                if (temp.length() > 0) {

                    if(ingredientCheck(temp, nowIngredientSet, dlNowIngredient))
                    {
                        addIngredientButton(temp, nowIngredientSet, dlNowIngredient);
                        if(ingredientCheck(temp, recentIngredientSet, dlRecentIngredient))
                        {
                            addIngredientButton(temp, recentIngredientSet, dlRecentIngredient);
                        }
                    }
                }
            }
        });

        edtAddIngredient.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                //Enter key Action
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    temp = edtAddIngredient.getText().toString().replace(" ", "");

                    edtAddIngredient.setText(null);
                    if (temp.length() > 0) {

                        if(ingredientCheck(temp, nowIngredientSet, dlNowIngredient))
                        {
                            addIngredientButton(temp, nowIngredientSet, dlNowIngredient);
                            if(ingredientCheck(temp, recentIngredientSet, dlRecentIngredient))
                            {
                                addIngredientButton(temp, recentIngredientSet, dlRecentIngredient);
                            }
                        }
                    }
                    return true;
                }
                return false;
            }
        });
    }

    private boolean ingredientCheck(String s, Map<String, Integer> ingredientSet, DynamicLayout dl)
    {
        if (dl.getMaxNum() == ingredientSet.size()) {
            if(dl == dlNowIngredient)
            {
                Toast.makeText(SearchActivity.this, "더 이상 재료를 추가할 수 없습니다", Toast.LENGTH_SHORT).show();
            }
            return false;
        }
        if (ingredientSet.keySet().contains(s)) {
            if(dl == dlNowIngredient)
            {
                Toast.makeText(SearchActivity.this, "이미 추가된 재료입니다", Toast.LENGTH_SHORT).show();
            }
            return false;
        }
        return true;
    }

    private void addIngredientButton(final String s, final Map<String, Integer> ingredientSet, final DynamicLayout dl) {

        ingredientSet.put(s, 0);
        final int nameSize = s.length();
        final int addedLayoutNum = dl.selectLayout(nameSize + MARGINSIZE);
        final LinearLayout[] addedLayout = dl.getLayout();

        final LinearLayout llIngredientOutline = new LinearLayout(SearchActivity.this);
        llIngredientOutline.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        llIngredientOutline.setGravity(Gravity.CENTER);
        if(dl == dlNowIngredient)
        {
            llIngredientOutline.setBackgroundResource(R.drawable.roundingbox_green);
        }
        else
        {
            llIngredientOutline.setBackgroundResource(R.drawable.roundingbox_gray);
        }

        addedLayout[addedLayoutNum].addView(llIngredientOutline);

        //동적 textview 생성
        final TextView textview_ingredient = new TextView(SearchActivity.this);
        textview_ingredient.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        textview_ingredient.setBackgroundColor(Color.parseColor("#00FFFFFF"));

        textview_ingredient.setTextColor(Color.parseColor("#000000"));
        textview_ingredient.setTextSize(16);
        textview_ingredient.setText(s + ' ');
        textview_ingredient.setSingleLine();
        llIngredientOutline.addView(textview_ingredient);

        final TextView textview_delete = new TextView(SearchActivity.this);
        textview_delete.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        textview_delete.setBackgroundColor(Color.parseColor("#00FFFFFF"));

        textview_delete.setTextColor(Color.parseColor("#A0A0A0"));
        textview_delete.setTextSize(10);
        textview_delete.setText(" X ");
        textview_delete.setSingleLine();
        llIngredientOutline.addView(textview_delete);




        //버튼화
        textview_ingredient.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {


                //재료의 검색 모드 변환
                if(dl == dlNowIngredient)
                {
                    if(ingredientSet.get(s) == 0)
                    {
                        ingredientSet.put(s, 1);
                    }
                    else
                    {
                        ingredientSet.put(s, 0);
                    }
                    switch (ingredientSet.get(s))
                    {
                        case 0:
                            llIngredientOutline.setBackgroundResource(R.drawable.roundingbox_green);
                            break;
                        case 1:
                            llIngredientOutline.setBackgroundResource(R.drawable.roundingbox_red);
                            break;
                    }
                }
                //클릭시 검색에 추가
                else
                {
                    if(ingredientCheck(s, nowIngredientSet,dlNowIngredient))
                    {
                        addIngredientButton(s, nowIngredientSet, dlNowIngredient);
                    }
                }
            }
        });

        //재료 삭제 버튼
        textview_delete.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //추가된 재료 갯수 확인, 재료 리스트에서 정보 지우기
                ingredientSet.remove(s);
                dl.calLayoutSize(-(nameSize+MARGINSIZE), addedLayoutNum);
                addedLayout[addedLayoutNum].removeView(llIngredientOutline);
            }
        });

    }
}
