package com.homechef.ict.ict_homechef;


import android.content.Intent;
import android.graphics.Color;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


public class SearchActivity extends AppCompatActivity {
    final int MAXINGREDIENT = 10;
    final int MAXLAYOUTSIZE = 20;
    final int MARGINSIZE = 2;
    int ingredientNum = 0;
    int[] lineSize = {0, 0, 0, 0};
    Ingredient[] nowIngredientSet = new Ingredient[MAXINGREDIENT];
    Ingredient[] recentIngredientSet = new Ingredient[MAXINGREDIENT];

    final LinearLayout[] llNowIngredient = new LinearLayout[4];
    final LinearLayout[] llRecentIngredient = new LinearLayout[4];

    String temp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        llNowIngredient[0] = findViewById(R.id.ll_nowingredient1);
        llNowIngredient[1] = findViewById(R.id.ll_nowingredient2);
        llNowIngredient[2] = findViewById(R.id.ll_nowingredient3);
        llNowIngredient[3] = findViewById(R.id.ll_nowingredient4);
        llRecentIngredient[0] = findViewById(R.id.ll_recentingredient1);
        llRecentIngredient[1] = findViewById(R.id.ll_recentingredient2);
        llRecentIngredient[2] = findViewById(R.id.ll_recentingredient3);
        llRecentIngredient[3] = findViewById(R.id.ll_recentingredient4);

        final DynamicLayout dlNowIngredient = new DynamicLayout(llNowIngredient, MAXLAYOUTSIZE);
        final DynamicLayout dlRecentIngredient = new DynamicLayout(llRecentIngredient, MAXLAYOUTSIZE);

        final EditText edtAddIngredient = findViewById(R.id.edt_ingredientadd);
        Button btnAddIngredient = findViewById(R.id.btn_ingredientadd);
        Button btnSearch = findViewById(R.id.btn_search);


        for(int i = 0; i < MAXINGREDIENT; i++)
        {
            nowIngredientSet[i] = new Ingredient(null, 0);
            recentIngredientSet[i] = new Ingredient(null, 0);
        }

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //RecipeListActivity에 데이터 전달
                Intent intent = new Intent(SearchActivity.this, RecipeListActivity.class);
                for (int i = 0; i < ingredientNum; i++) {
                    String[] data = new String[2];
                    data[0] = nowIngredientSet[i].getName();
                    data[1] = String.valueOf(nowIngredientSet[i].getSearchMode());
                    intent.putExtra("ingredientNum", ingredientNum);
                    intent.putExtra(String.valueOf(i), data);
                }
                startActivity(intent);
            }
        });

        btnAddIngredient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                temp = edtAddIngredient.getText().toString().replace(" ", "");
                edtAddIngredient.setText(null);
                if (temp.length() > 0) {

                    if(ingredientCheck(temp, ingredientNum))
                    {
                        addIngredient(temp, ingredientNum, dlNowIngredient);
                        ingredientNum++;
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

                        if(ingredientCheck(temp, ingredientNum))
                        {
                            addIngredient(temp, ingredientNum, dlNowIngredient);
                            ingredientNum++;
                        }
                    }
                    return true;
                }
                return false;
            }
        });
    }

    private boolean ingredientCheck(String s, final int num)
    {
        if (ingredientNum == MAXINGREDIENT) {
            Toast.makeText(SearchActivity.this, "더 이상 재료를 추가할 수 없습니다", Toast.LENGTH_SHORT).show();
            return false;
        }

        for (int i = 0; i < ingredientNum; i++) {
            if (nowIngredientSet[i].getName().equals(s)) {
                Toast.makeText(SearchActivity.this, "이미 추가된 재료입니다", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
    }


    private void dataSave(String name)
    {
        String dirPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/DATA/";
        File file = new File(dirPath);

        if(!file.exists())
        {
            file.mkdirs();
        }

        File savefile = new File(dirPath + "DATA.txt");
        try {
            FileWriter fw = new FileWriter(dirPath + "DATA.txt", true);
            BufferedWriter bfw = new BufferedWriter(fw);
            bfw.write(name);
            bfw.write("\n");
            bfw.close();
            fw.close();
        } catch(IOException e)
        {
            e.printStackTrace();
        }
    }
    private void dataRead()
    {

        int data ;
        int i = 0;
        char ch ;

        try {
            // open file.
            FileReader fr = new FileReader("DATA.txt") ;
            BufferedReader bufrd = new BufferedReader(fr) ;

            // read file.
            while ((data = fr.read()) != -1 || i < MAXINGREDIENT) {
                recentIngredientSet[i].setName(bufrd.readLine());
                i++;
            }
            bufrd.close();
            fr.close() ;
        } catch (Exception e) {
            e.printStackTrace() ;
        }
    }


    private void addIngredient(String s, final int ingredientNum, final DynamicLayout dl) {

        nowIngredientSet[ingredientNum].setName(s);
        final int nameSize = s.length();
        final int addedLayoutNum = dl.selectLayout(nameSize + MARGINSIZE);
        final LinearLayout[] addedLayout = dl.getLayout();

        final LinearLayout llIngredientOutline = new LinearLayout(SearchActivity.this);
        llIngredientOutline.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        llIngredientOutline.setGravity(Gravity.CENTER);
        llIngredientOutline.setBackgroundResource(R.drawable.roundingbox_green);
        addedLayout[addedLayoutNum].addView(llIngredientOutline);

        //동적 textview 생성
        final TextView textview_ingredient = new TextView(SearchActivity.this);
        textview_ingredient.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        textview_ingredient.setBackgroundColor(Color.parseColor("#00FFFFFF"));

        textview_ingredient.setTextColor(Color.parseColor("#000000"));
        textview_ingredient.setTextSize(16);
        textview_ingredient.setText(nowIngredientSet[ingredientNum].getName() + ' ');
        textview_ingredient.setSingleLine();
        llIngredientOutline.addView(textview_ingredient);

        final TextView textview_delete = new TextView(SearchActivity.this);
        textview_delete.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        textview_delete.setBackgroundColor(Color.parseColor("#00FFFFFF"));

        textview_delete.setTextColor(Color.parseColor("#A0A0A0"));
        textview_delete.setTextSize(10);
        textview_delete.setText(" X");
        textview_delete.setSingleLine();
        llIngredientOutline.addView(textview_delete);




        //버튼화
        textview_ingredient.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //재료의 검색 모드 변환
                nowIngredientSet[ingredientNum].cycleSearchMode();
                switch (nowIngredientSet[ingredientNum].getSearchMode())
                {
                    case 0:
                        llIngredientOutline.setBackgroundResource(R.drawable.roundingbox_green);
                        break;
                    case 1:
                        llIngredientOutline.setBackgroundResource(R.drawable.roundingbox_blue);
                        break;
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
                if(SearchActivity.this.ingredientNum - 1 == ingredientNum)
                {
                    nowIngredientSet[ingredientNum].setName(null);
                }
                else
                {
                    nowIngredientSet[ingredientNum].setName(nowIngredientSet[SearchActivity.this.ingredientNum -1].getName());
                    nowIngredientSet[ingredientNum].setSearchMode(nowIngredientSet[SearchActivity.this.ingredientNum -1].getSearchMode());
                }
                SearchActivity.this.ingredientNum--;
                dl.calLayoutSize(-nameSize, addedLayoutNum);
                addedLayout[addedLayoutNum].removeView(llIngredientOutline);
            }
        });
    }
}
