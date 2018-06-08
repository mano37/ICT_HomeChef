package com.homechef.ict.ict_homechef;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.homechef.ict.ict_homechef.ConnectUtil.ConnectUtil;
import com.homechef.ict.ict_homechef.ConnectUtil.HttpCallback;
import com.homechef.ict.ict_homechef.ConnectUtil.RequestBody.RecipePut;
import com.homechef.ict.ict_homechef.ConnectUtil.ResponseBody.PostRecipeGet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class PostActivity extends AppCompatActivity {

    ConnectUtil connectUtil;
    Map<String,String> header = new HashMap<>();

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        connectUtil = ConnectUtil.getInstance(this).createBaseApi();


        /////////////////////////////////////////////////////////뷰 선언

        LinearLayout ll_postMain = findViewById(R.id.post_ll_main);
        LinearLayout ll_recipeTitle = findViewById(R.id.post_ll_recipetitle);
        LinearLayout ll_recipeInfo = findViewById(R.id.post_ll_recipeinfo);
        LinearLayout ll_ingredientList = findViewById(R.id.post_ll_ingredientlist);
        LinearLayout ll_steps = findViewById(R.id.post_ll_steps);

        final EditText edt_recipeTitle = findViewById(R.id.post_edt_recipetitle) ;
        final EditText edt_serve = findViewById(R.id.post_edt_serve) ;
        final EditText edt_timeCost = findViewById(R.id.post_edt_timecost) ;

        final List<EditText> edt_ingredientName = new ArrayList<>();
        edt_ingredientName.add((EditText)findViewById(R.id.post_edt_ingre1));
        edt_ingredientName.add((EditText)findViewById(R.id.post_edt_ingre2));
        edt_ingredientName.add((EditText)findViewById(R.id.post_edt_ingre3));

        final List<EditText> edt_ingredientQuantity = new ArrayList<>();
        edt_ingredientQuantity.add((EditText)findViewById(R.id.post_edt_quan1));
        edt_ingredientQuantity.add((EditText)findViewById(R.id.post_edt_quan2));
        edt_ingredientQuantity.add((EditText)findViewById(R.id.post_edt_quan3));

        List<Button> btn_delIngredient = new ArrayList<>();
        btn_delIngredient.add((Button)findViewById(R.id.post_btn_delingredient1));
        btn_delIngredient.add((Button)findViewById(R.id.post_btn_delingredient2));
        btn_delIngredient.add((Button)findViewById(R.id.post_btn_delingredient3));

        final List<EditText> edt_steps = new ArrayList<>();
        edt_steps.add((EditText)findViewById(R.id.post_edt_step1));
        edt_steps.add((EditText)findViewById(R.id.post_edt_step2));
        edt_steps.add((EditText)findViewById(R.id.post_edt_step3));

        List<Button> btn_delSteps = new ArrayList<>();
        btn_delSteps.add((Button)findViewById(R.id.post_btn_delstep1));
        btn_delSteps.add((Button)findViewById(R.id.post_btn_delstep2));
        btn_delSteps.add((Button)findViewById(R.id.post_btn_delstep3));

        Button btn_register = findViewById(R.id.post_btn_register);
        ///////////////////////////////////////////////////////////////////////////////

        String jwt = "eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJlbWFpbCI6ImtqaHdhbmlkQGdtYWlsLmNvbSIsImV4cCI6MTUyODczMDY3OSwianRpIjoiNSIsImlhdCI6MTUyODI5ODY3OSwiaXNzIjoiSG9tZWNoZWYtU2VydmVyIn0.okMQOfVNKtDATGX99Xo_Xt3K5V6I-dFG5FnILgMIBWoX07fQmp1nEq2yVXCfar2KrU54Yd3FHPmBWPpjHS8eFQ";
        makeHeader(jwt);



        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = edt_recipeTitle.getText().toString();
                String serve = edt_serve.getText().toString();
                String timeCost = edt_timeCost.getText().toString();
                Map<String, String> ingredientList = new HashMap<>();
                for(int i = 0; i < edt_ingredientName.size(); i++)
                {
                    ingredientList.put(edt_ingredientName.get(i).getText().toString(), edt_ingredientQuantity.get(i).getText().toString());
                }
                String steps = "";
                String imgUrl = "";
                for(int i = 0; i < edt_steps.size(); i++)
                {
                    steps += "step" + i + '\n';
                    steps += edt_steps.get(i).getText().toString() + '\n';
                }
                RecipePut recipeput = new RecipePut(title, serve, timeCost, ingredientList, steps, imgUrl);
                if(title.isEmpty())
                {
                    Toast.makeText(PostActivity.this, "제목을 적어주세요", Toast.LENGTH_SHORT);
                    return;
                }
                if(serve.isEmpty())
                {
                    Toast.makeText(PostActivity.this, "인원 수를 적어주세요", Toast.LENGTH_SHORT);
                    return;
                }
                if(timeCost.isEmpty())
                {
                    Toast.makeText(PostActivity.this, "소요 시간을 적어주세요", Toast.LENGTH_SHORT);
                    return;
                }
                if(steps.isEmpty())
                {
                    Toast.makeText(PostActivity.this, "최소 한 개의 요리방법을 적어주세요", Toast.LENGTH_SHORT);
                    return;
                }
                if(ingredientList.size() == 0)
                {
                    Toast.makeText(PostActivity.this, "최소 한 개의 재료를 입력해주세요", Toast.LENGTH_SHORT);
                    return;
                }
                recipePost(recipeput);
            }
        });

    }

    public void makeHeader(String jwt_token){

        String token = "Bearer " + jwt_token;
        header.put("Authorization",token);

    }

    public void recipePost(RecipePut parameter) {

        connectUtil.postRecipe(header, parameter, new HttpCallback() {
            @Override
            public void onError(Throwable t) {
                // 내부적 에러 발생할 경우
            }
            @Override
            public void onSuccess(int code, Object receivedData) {
                // 성공적으로 완료한 경우
                CacheUtil cacheUtil = new CacheUtil(PostActivity.this);
                try {
                    cacheUtil.Write(String.valueOf(((PostRecipeGet)receivedData).recipe_id), "uploadedID.txt");
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onFailure(int code) {
                // 통신에 실패한 경우
                // 결과값이 없다거나, 서버에서 오류를 리턴했거나
                // 또는 ResponseBody 안의 key 값이 이상하거나
            }
        });
    }
}
