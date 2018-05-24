package com.homechef.ict.ict_homechef;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.os.Bundle;
import android.widget.Toast;


public class SearchActivity extends AppCompatActivity {
    final int MAXINGREDIENT = 10;
    final int MAXLAYOUTSIZE = 20;
    final int MARGINSIZE = 2;
    int ingredientNum = 0;
    int[] lineSize = {0, 0, 0, 0};
    Ingredient[] ingredientSet = new Ingredient[MAXINGREDIENT];
    String temp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
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

        for(int i = 0; i < MAXINGREDIENT; i++)
        {
            ingredientSet[i] = new Ingredient(null, 0);
        }

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //RecipeListActivity에 데이터 전달
                Intent intent = new Intent(SearchActivity.this, RecipeListActivity.class);
                for (int i = 0; i < ingredientNum; i++) {
                    String[] data = new String[2];
                    data[0] = ingredientSet[i].getName();
                    data[1] = String.valueOf(ingredientSet[i].getSearchMode());
                    intent.putExtra("ingredientNum", ingredientNum);
                    intent.putExtra(String.valueOf(i), data);
                }
                startActivity(intent);
            }
        });

        btnAddIngredient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                temp = edtAddIngredient.getText().toString();
                edtAddIngredient.setText(null);
                if (temp.length() > 0) {

                    if(addIngredient(temp, ingredientNum))
                    {
                        if (lineSize[0] + temp.length() <= MAXLAYOUTSIZE) {
                            ingredientSet[ingredientNum].addIngredientView(llNowIngredient1, SearchActivity.this);
                            lineSize[0] += temp.length();
                            lineSize[0] += MARGINSIZE;
                        } else if (lineSize[1] + temp.length() <= MAXLAYOUTSIZE) {
                            ingredientSet[ingredientNum].addIngredientView(llNowIngredient2, SearchActivity.this);
                            lineSize[1] += temp.length();
                            lineSize[1] += MARGINSIZE;
                        } else if (lineSize[2] + temp.length() <= MAXLAYOUTSIZE) {
                            ingredientSet[ingredientNum].addIngredientView(llNowIngredient3, SearchActivity.this);
                            lineSize[2] += temp.length();
                            lineSize[2] += MARGINSIZE;
                        } else if (lineSize[3] + temp.length() <= MAXLAYOUTSIZE) {
                            ingredientSet[ingredientNum].addIngredientView(llNowIngredient4, SearchActivity.this);
                            lineSize[3] += temp.length();
                            lineSize[3] += MARGINSIZE;
                        }

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
                    temp = edtAddIngredient.getText().toString();
                    edtAddIngredient.setText(null);
                    if(addIngredient(temp, ingredientNum))
                    {
                        if (lineSize[0] + temp.length() <= MAXLAYOUTSIZE) {
                            ingredientSet[ingredientNum].addIngredientView(llNowIngredient1, SearchActivity.this);
                            lineSize[0] += temp.length();
                            lineSize[0] += MARGINSIZE;
                        } else if (lineSize[1] + temp.length() <= MAXLAYOUTSIZE) {
                            ingredientSet[ingredientNum].addIngredientView(llNowIngredient2, SearchActivity.this);
                            lineSize[1] += temp.length();
                            lineSize[1] += MARGINSIZE;
                        } else if (lineSize[2] + temp.length() <= MAXLAYOUTSIZE) {
                            ingredientSet[ingredientNum].addIngredientView(llNowIngredient3, SearchActivity.this);
                            lineSize[2] += temp.length();
                            lineSize[2] += MARGINSIZE;
                        } else if (lineSize[3] + temp.length() <= MAXLAYOUTSIZE) {
                            ingredientSet[ingredientNum].addIngredientView(llNowIngredient4, SearchActivity.this);
                            lineSize[3] += temp.length();
                            lineSize[3] += MARGINSIZE;
                        }

                        ingredientNum++;
                    }
                    return true;
                }
                return false;
            }
        });


    }

    private boolean addIngredient(String s, int num) {
        if (ingredientNum == MAXINGREDIENT) {
            Toast.makeText(SearchActivity.this, "더 이상 재료를 추가할 수 없습니다", Toast.LENGTH_SHORT).show();
            return false;
        }

        for (int i = 0; i < ingredientNum; i++) {
            if (ingredientSet[i].getName().equals(s)) {
                Toast.makeText(SearchActivity.this, "이미 추가된 재료입니다", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        ingredientSet[num].setName(s);
        return true;
    }
}
