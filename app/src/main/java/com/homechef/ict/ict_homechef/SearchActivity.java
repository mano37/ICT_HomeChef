package com.homechef.ict.ict_homechef;


import android.app.Application;
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

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
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


    Map<String , Integer> nowIngredientSet = new HashMap<String , Integer>();
    Map<String , Integer> recentIngredientSet = new HashMap<String , Integer>();
    //final ArrayList<String> nowIngredientSet = new ArrayList<>();
    //final ArrayList<Integer> nowSearchSet = new ArrayList<>();


    final DynamicLayout dlNowIngredient = new DynamicLayout(llNowIngredient, MAXLAYOUTSIZE, MAXINGREDIENT);
    final DynamicLayout dlRecentIngredient = new DynamicLayout(llRecentIngredient, MAXLAYOUTSIZE, MAXINGREDIENT);
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



        final EditText edtAddIngredient = findViewById(R.id.edt_ingredientadd);
        Button btnAddIngredient = findViewById(R.id.btn_ingredientadd);
        Button btnSearch = findViewById(R.id.btn_search);


        dataRead(dlRecentIngredient);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //RecipeListActivity에 데이터 전달
                String[] data = new String[2];
                data[0] = "";
                data[1] = "";
                Intent intent = new Intent(SearchActivity.this, RecipeListActivity.class);
                if(nowIngredientSet.size() >= 1)
                {
                    Iterator<String> ingreVal = nowIngredientSet.keySet().iterator();
                    int j = 0;
                    while(ingreVal.hasNext())
                    {
                        int searchType;
                        String keys = (String)ingreVal.next();
                        searchType = nowIngredientSet.get(keys);
                        if(data[searchType].length() == 0)
                        {
                            data[searchType] = keys;
                        }
                        else
                        {
                            data[searchType] += " " + keys;
                        }


                    }

                    intent.putExtra("contain", data[0]);
                    intent.putExtra("except", data[1]);
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

                    if(ingredientCheck(temp, nowIngredientSet, dlNowIngredient))
                    {
                        dataSave(temp);
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
                            dataSave(temp);
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


    private void dataSave(String name)
    {
                /*String fileName = "DATA.dat";
        File savefile = new File(getFilesDir() + fileName);
        if(!savefile.exists())
        {
            if(!savefile.mkdir())
            {
                return;
            }
        }
        try {
            FileWriter fw = new FileWriter(getFilesDir() + fileName, true);
            BufferedWriter bfw = new BufferedWriter(fw);
            bfw.write(name);
            bfw.write("\n");
            bfw.close();
            fw.close();
        } catch(IOException e)
        {
            e.printStackTrace();
        }*/
    }

    private void dataRead(DynamicLayout dl)
    {

        /*String fileName = "DATA.dat";
        int data ;
        int i = 0;

        try {
            // open file.
            FileReader fr = new FileReader(getFilesDir() + fileName) ;
            BufferedReader bufrd = new BufferedReader(fr) ;

            // read file.
            while ((data = fr.read()) != -1 && dl.getNowNum() < MAXINGREDIENT) {
                dl.getIngredientSet()[dl.getNowNum()].setName(bufrd.readLine());
                addIngredientButton(dl.getIngredientSet()[dl.getNowNum()].getName(), dl, 1);
                dl.setNowNum(dl.getNowNum()+1);
            }
            bufrd.close();
            fr.close();
        } catch (Exception e) {
            e.printStackTrace() ;
        }*/
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
                dl.calLayoutSize(-nameSize, addedLayoutNum);
                addedLayout[addedLayoutNum].removeView(llIngredientOutline);
            }
        });

    }
}
