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
    int ingredientNum = 0;
    Ingredient[] ingredientSet = new Ingredient[MAXINGREDIENT];

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);



        final LinearLayout llNowIngredient1 = (LinearLayout)findViewById(R.id.ll_nowingredient1);
        final LinearLayout llRecentIngredient = (LinearLayout)findViewById(R.id.ll_recentingredient1);
        final EditText edtAddIngredient = (EditText)findViewById(R.id.edt_ingredientadd);
        Button btnAddIngredient = (Button)findViewById(R.id.btn_ingredientadd);
        Button btnSearch = (Button)findViewById(R.id.btn_search);

        btnAddIngredient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( edtAddIngredient.getText().toString().length() > 0 ) {

                    addIngredient(edtAddIngredient.getText().toString(), llNowIngredient1);
                    edtAddIngredient.setText(null);
                }
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(SearchActivity.this, RecipeListActivity.class);
                startActivity(intent);
            }
        });

        edtAddIngredient.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                //Enter key Action
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    if ( edtAddIngredient.getText().toString().length() > 0 ) {

                        addIngredient(edtAddIngredient.getText().toString(), llNowIngredient1);
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
            Toast.makeText(this, "더 이상 재료를 추가할 수 없습니다", Toast.LENGTH_SHORT).show();
            return;
        }

        //중복 재료 검사
        for(int i = 0; i < ingredientNum; i++)
        {
            if(ingredientSet[i].getName().equals(s))
            {
                Toast.makeText(this, "이미 추가된 재료입니다", Toast.LENGTH_SHORT).show();
                return;
            }
        }


        //더미 Ingredient 생성
        final Ingredient dummy = new Ingredient(s, 0);

        //동적 textview 생성
        final TextView textview_ingredient = new TextView(SearchActivity.this);
        textview_ingredient.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        textview_ingredient.setBackgroundColor(Color.parseColor("#00FFFFFF"));
        textview_ingredient.setPadding(20, 10, 10, 10);
        textview_ingredient.setTextColor(Color.parseColor("#000000"));
        textview_ingredient.setTextSize(13);
        textview_ingredient.setText(s);
        textview_ingredient.setSingleLine();
        ll.addView(textview_ingredient);

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
                        break;
                    case 1:
                        textview_ingredient.setTextColor(Color.parseColor("#0000FF"));
                        break;
                    case 2:
                        textview_ingredient.setTextColor(Color.parseColor("#FF0000"));
                        break;
                }
            }
        });


    }
}

