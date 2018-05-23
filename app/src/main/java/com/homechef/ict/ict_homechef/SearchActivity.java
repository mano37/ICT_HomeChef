package com.homechef.ict.ict_homechef;


import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.os.Bundle;
import android.widget.Toast;


public class SearchActivity extends AppCompatActivity
{
    final int MAXINGREDIENT = 10;
    final int MAXLAYOUTSIZE = 20;
    final int MARGINSIZE = 2;
    int ingredientNum = 0;
    int [] lineSize = { 0, 0, 0, 0 };
    Ingredient[] ingredientSet = new Ingredient[MAXINGREDIENT];

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);



        final LinearLayout llNowIngredient1 = findViewById(R.id.ll_nowingredient1);
        final LinearLayout llNowIngredient2 = findViewById(R.id.ll_nowingredient2);
        final LinearLayout llNowIngredient3 = findViewById(R.id.ll_nowingredient3);
        final LinearLayout llNowIngredient4 = findViewById(R.id.ll_nowingredient4);
        final LinearLayout llRecentIngredient = findViewById(R.id.ll_recentingredient1);
        final EditText edtAddIngredient = findViewById(R.id.edt_ingredientadd);
        Button btnAddIngredient = findViewById(R.id.btn_ingredientadd);
        Button btnSearch = findViewById(R.id.btn_search);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(SearchActivity.this, RecipeListActivity.class);
                startActivity(intent);
            }
        });

        btnAddIngredient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( edtAddIngredient.getText().toString().length() > 0 ) {

                    if(lineSize[0] <= MAXLAYOUTSIZE)
                    {
                        addIngredient(edtAddIngredient.getText().toString(), llNowIngredient1);
                        lineSize[0] += edtAddIngredient.getText().toString().length();
                        lineSize[0] += MARGINSIZE;
                    }
                    else if(lineSize[1] <= MAXLAYOUTSIZE)
                    {
                        addIngredient(edtAddIngredient.getText().toString(), llNowIngredient2);
                        lineSize[1] += edtAddIngredient.getText().toString().length();
                        lineSize[1] += MARGINSIZE;
                    }
                    else if(lineSize[2] <= MAXLAYOUTSIZE)
                    {
                        addIngredient(edtAddIngredient.getText().toString(), llNowIngredient3);
                        lineSize[2] += edtAddIngredient.getText().toString().length();
                        lineSize[2] += MARGINSIZE;
                    }
                    else if(lineSize[3] <= MAXLAYOUTSIZE)
                    {
                        addIngredient(edtAddIngredient.getText().toString(), llNowIngredient4);
                        lineSize[3] += edtAddIngredient.getText().toString().length();
                        lineSize[3] += MARGINSIZE;
                    }

                    edtAddIngredient.setText(null);

                }
            }
        });

        edtAddIngredient.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                //Enter key Action
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    if ( edtAddIngredient.getText().toString().length() > 0 ) {

                        if(lineSize[0] <= MAXLAYOUTSIZE)
                        {
                            addIngredient(edtAddIngredient.getText().toString(), llNowIngredient1);
                            lineSize[0] += edtAddIngredient.getText().toString().length();
                            lineSize[0] += MARGINSIZE;
                        }
                        else if(lineSize[1] <= MAXLAYOUTSIZE)
                        {
                            addIngredient(edtAddIngredient.getText().toString(), llNowIngredient2);
                            lineSize[1] += edtAddIngredient.getText().toString().length();
                            lineSize[1] += MARGINSIZE;
                        }
                        else if(lineSize[2] <= MAXLAYOUTSIZE)
                        {
                            addIngredient(edtAddIngredient.getText().toString(), llNowIngredient3);
                            lineSize[2] += edtAddIngredient.getText().toString().length();
                            lineSize[2] += MARGINSIZE;
                        }
                        else if(lineSize[3] <= MAXLAYOUTSIZE)
                        {
                            addIngredient(edtAddIngredient.getText().toString(), llNowIngredient4);
                            lineSize[3] += edtAddIngredient.getText().toString().length();
                            lineSize[3] += MARGINSIZE;
                        }
                        edtAddIngredient.setText(null);

                    }
                    return true;
                }
                return false;
            }
        });

    }




    private void addIngredient(String s, LinearLayout ll)
    {

        //재료 최대 갯수 제한
        if(ingredientNum == MAXINGREDIENT)
        {
            Toast.makeText(SearchActivity.this, "더 이상 재료를 추가할 수 없습니다", Toast.LENGTH_SHORT).show();
            return;
        }

        //중복 재료 검사
        for(int i = 0; i < ingredientNum; i++)
        {
            if(ingredientSet[i].getName().equals(s))
            {
                Toast.makeText(SearchActivity.this, "이미 추가된 재료입니다", Toast.LENGTH_SHORT).show();
                return;
            }
        }


        //더미 Ingredient 생성
        final Ingredient dummy = new Ingredient(s, 0);

        //동적 textview 생성
        final TextView textview_ingredient = new TextView(SearchActivity.this);
        textview_ingredient.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        textview_ingredient.setBackgroundColor(Color.parseColor("#00FFFFFF"));

        textview_ingredient.setTextColor(Color.parseColor("#000000"));
        textview_ingredient.setTextSize(16);
        textview_ingredient.setText(s);
        textview_ingredient.setSingleLine();
        ll.addView(textview_ingredient);

        textview_ingredient.setBackgroundResource(R.drawable.roundingbox_green);
        /*android:id="@+id/tbResultItemCount"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_gravity="right"
        android:textColor="#ffffff"
        android:textSize="32px"
        android:layout_marginBottom="35px"
        android:layout_marginLeft="60px"
        android:layout_marginRight="120px"*/

        //재료 목록 배열에 저장
        ingredientSet[ingredientNum] = dummy;
        ingredientNum++;

        //버튼화
        textview_ingredient.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                dummy.cycleSearchMode();
                switch (dummy.getSearchMode())
                {
                    case 0:
                        textview_ingredient.setTextColor(Color.parseColor("#000000"));
                        textview_ingredient.setBackgroundResource(R.drawable.roundingbox_green);
                        break;
                    case 1:
                        textview_ingredient.setTextColor(Color.parseColor("#FFFFFF"));
                        textview_ingredient.setBackgroundResource(R.drawable.roundingbox_blue);
                        break;
                    case 2:
                        textview_ingredient.setTextColor(Color.parseColor("#FFFFFF"));
                        textview_ingredient.setBackgroundResource(R.drawable.roundingbox_red);
                        break;
                }
            }
        });


    }
}

