package com.homechef.ict.ict_homechef;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.homechef.ict.ict_homechef.ConnectUtil.ConnectUtil;
import com.homechef.ict.ict_homechef.ConnectUtil.HttpCallback;
import com.homechef.ict.ict_homechef.ConnectUtil.ResponseBody.RecipeGet;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Map;
import java.util.Queue;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    // For UserInfo
    private JsonObject userJson;
    private String userInfo;
    private JsonParser parser = new JsonParser();

    String userNameStr;
    String userEmailStr;
    String userPictureStr;


    // SharedPreferences
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    String tokenKeyName = "jwt_token";


    TextView textView;
    ImageView imageView;

    Queue<ThumnailInfo> thumnailInfoList = new LinkedList<>();
    int connect_idx = 0;
    int thumnail_idx = 0;

    //////////////////////////동하가 추가한 변수들
    ConnectUtil connectUtil = ConnectUtil.getInstance(this).createBaseApi();
    Map<String,String> header = new HashMap<>();
    static int int_scrollViewPos;
    static int int_TextView_lines;

    Locale en;
    //////////////////////////////////////////////


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // userinfo 세팅
        Intent startIntent = getIntent();
        userInfo = startIntent.getExtras().getString("user_info");
        userJson = (JsonObject) parser.parse(userInfo);
        userNameStr = userJson.get("name").getAsString();
        userEmailStr = userJson.get("email").getAsString();
        userPictureStr = userJson.get("picture").getAsString();
        // userinfo 세팅 끝



        // 환영 메세지
        String startStr = userNameStr + getString(R.string.greeting);
        Toast.makeText(getApplicationContext(), startStr, Toast.LENGTH_LONG).show();
        // 환영 메세지 끝


        /* @@@@@@@@주의 , 저장된 레시피 캐시 유틸 초기화용 주석@@@@@@@@@@@@
        CacheUtil cacheUtil = new CacheUtil(MainActivity.this);
        String filePostedRecipeByID = "PostedRecipeBy" + String.valueOf(userJson.get("user_id").getAsString());
        try {
            cacheUtil.Write("", filePostedRecipeByID);
        } catch (IOException e) {
            e.printStackTrace();
        }
        */


        // 화면 생성
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, getString(R.string.beta_alert), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        // user info 표시

        View nav_header_view = navigationView.getHeaderView(0);
        textView = (TextView) nav_header_view.findViewById(R.id.Navigation_profile_name);
        textView.setText(userNameStr);
        textView = (TextView) nav_header_view.findViewById(R.id.Navigation_profile_email);
        textView.setText(userEmailStr);
        imageView = (ImageView) nav_header_view.findViewById(R.id.Navigation_profile_image);
        Picasso.get().load(userPictureStr)
                .into(imageView);



        // Make SharedPreference
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        editor = pref.edit();

        ////////////////////////////////////////////랜덤 레시피////////////////////////////////////////////


        System.out.println("this is Main Page Random Recipe");
        //변수 선언
        Intent intent = getIntent();
        int ingredientNum;
        final ScrollView svRecipeList = findViewById(R.id.main_sv_main);
        final LinearLayout llRecipeList = findViewById(R.id.main_ll_recipelist);




        makeHeader(userJson.get("jwt_token").getAsString());

        for(int i = 0; i < 20; i++)
        {
            int id = (int) (Math.random() * (120000)) + 1;
            recipesGet(String.valueOf(id), llRecipeList);
        }


        //화면 최하단 스크롤


        //Detect Bottom ScrollView
        svRecipeList.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                // 스크롤 하는동안 drawer 사용불가
               // drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

                int_scrollViewPos = svRecipeList.getScrollY();
                int_TextView_lines = svRecipeList.getChildAt(0).getBottom() - svRecipeList.getHeight();

                if(int_TextView_lines == int_scrollViewPos && connect_idx == 0){
                    //화면 최하단 스크롤시 이벤트
                    for(int i = 0; i < 20; i++)
                    {
                        int id = (int) (Math.random() * (120000)) + 1;
                        recipesGet(String.valueOf(id), llRecipeList);
                    }

                }

                // drawer 사용가능
               // drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            }
        });
        //////////////////////////////////////////////////////////////////////////////////////////////////

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);

        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            if(getResources().getConfiguration().locale.equals(Locale.KOREA))
            {
                en = Locale.US;
                Configuration config = new Configuration();
                config.locale = en;
                getResources().updateConfiguration(config, getResources().getDisplayMetrics());
                Intent intent = new Intent(this, SplashActivity.class);
                startActivity(intent);
                finish();
            }
            else
            {
                en = Locale.KOREA;
                Configuration config = new Configuration();
                config.locale = en;
                getResources().updateConfiguration(config, getResources().getDisplayMetrics());
                Intent intent = new Intent(this, SplashActivity.class);
                startActivity(intent);
                finish();
            }

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        // 레시피 검색으로 이동
        if (id == R.id.nav_searchRecipe) {
            Intent intent = new Intent(this, SearchActivity.class);
            intent.putExtra("user_info", userInfo);
            startActivity(intent);

        }
        // 내가 봤던 레시피로 이동
        else if (id == R.id.nav_recipeShown) {
            Intent intent = new Intent(this, RecipeShownByMe.class);
            intent.putExtra("user_info", userInfo);
            startActivity(intent);
        }
        // 레시피 추가로 이동
        else if (id == R.id.nav_postRecipe) {
            Intent intent = new Intent(this, PostActivity.class);
            intent.putExtra("user_info", userInfo);
            startActivity(intent);

        }
        // 내가 추가한 레시피로 이동
        else if (id == R.id.nav_recipePosted) {
            Intent intent = new Intent(this, PostedRecipeByMeActivity.class);
            intent.putExtra("user_info", userInfo);
            startActivity(intent);

        }
        // 로그아웃으로 이동
        else if (id == R.id.nav_logout) {
            Intent intent = new Intent(this, SignInActivity.class);
            startActivityForResult(intent, 4444);

        }  else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 4444){
            if(resultCode == 1){
                System.out.println("ResultCode 1 by login");
                userInfo = data.getStringExtra("user_info");
                userJson = (JsonObject) parser.parse(userInfo);
                String str = userJson.get("name").toString();

                editor.putString(tokenKeyName, userJson.get(tokenKeyName).getAsString());
                editor.commit();
            }
            if(resultCode == 2){
                System.out.println("ResultCode 2 by Session");
                userInfo = data.getStringExtra("user_info");
                userJson = (JsonObject) parser.parse(userInfo);
                String str = userJson.get("name").toString();
                editor.putString(tokenKeyName, userJson.get(tokenKeyName).getAsString());
                editor.commit();
            }
        }

    }

    ////////////////////////////////////동하가 추가한 메소드들
    private void showRecipeThumnail(final ThumnailInfo ti, LinearLayout ll)
    {
        //Thumnail의 틀 레이아웃
        final LinearLayout ll_thumnail = new LinearLayout(MainActivity.this);
        ll_thumnail.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        ll_thumnail.setBackgroundResource(R.drawable.boxline_gradientwhite);
        ll_thumnail.setOrientation(LinearLayout.VERTICAL);

        //Thumnail 내의 이미지 틀 레이아웃
        final LinearLayout ll_image = new LinearLayout(MainActivity.this);
        ll_image.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

        //Thumnail 내의 메인 이미지
        ImageView image_thumnail = new ImageView(MainActivity.this);
        image_thumnail.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        image_thumnail.setImageResource(R.drawable.thumnail);

        if(!ti.getImgUrl().isEmpty())
        {

            Picasso.get().load(ti.getImgUrl()).resize(width, 0).centerCrop().into(image_thumnail);
        }


        final LinearLayout ll_content = new LinearLayout(MainActivity.this);
        ll_content.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        ll_content.setOrientation(LinearLayout.VERTICAL);
        ll_content.setPadding(30, 20, 30, 15);

        final TextView tv_recipeName = new TextView(MainActivity.this);
        tv_recipeName.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        tv_recipeName.setTextColor(Color.parseColor("#000000"));
        tv_recipeName.setPadding(10, 10, 10, 10);
        tv_recipeName.setTextSize(18);
        tv_recipeName.setText(ti.getRecipeName());
        tv_recipeName.setSingleLine();

        final TextView tv_ingredientList = new TextView(MainActivity.this);
        tv_ingredientList.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        tv_ingredientList.setTextColor(Color.parseColor("#000000"));
        tv_ingredientList.setPadding(10, 10, 10, 10);
        tv_ingredientList.setTextSize(12);
        String recipeList = getString(R.string.ingredientis);
        for(int i = 0; i < ti.getIngredientList().size(); i++)
        {
            recipeList += ti.getIngredientList().get(i) + " ";
        }
        tv_ingredientList.setText(recipeList);
        tv_ingredientList.setSingleLine();

        final TextView tv_recommendCount = new TextView(MainActivity.this);
        tv_recommendCount.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        tv_recommendCount.setTextColor(Color.parseColor("#000000"));
        tv_recommendCount.setPadding(10, 10, 10, 10);
        tv_recommendCount.setTextSize(10);
        tv_recommendCount.setText(getString(R.string.recommendcount) + ti.getCount());
        tv_recommendCount.setSingleLine();

        final TextView tv_writerName = new TextView(MainActivity.this);
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
                Intent intent = new Intent(MainActivity.this, RecipeInfoActivity.class);
                intent.putExtra("id", ti.getRecipeId());
                intent.putExtra("user_info", userInfo);
                System.out.println("startActivitiy - recipeInfoActivity @@@@@@");
                System.out.println("userInfo is : " + userInfo);
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
                connect_idx++;

                if(connect_idx == 20){
                    for(int i = 0; i < thumnail_idx; i++){
                        showRecipeThumnail(thumnailInfoList.remove(), ll);
                    }
                    thumnail_idx = 0;
                    connect_idx = 0;
                }
                // 내부적 에러 발생할 경우
            //    System.out.println("RecipeShownByMe getRecipe onError@@@@@@");
            }
            @Override
            public void onSuccess(int code, Object receivedData) {
                connect_idx++;

                // 성공적으로 완료한 경우
            //    System.out.println("RecipeShownByMe getRecipe onSuccess1 @@@@@@");
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

                if(getResources().getConfiguration().locale.equals(Locale.KOREA))
                {
                    try {
                        if (ingreList.get(0).matches("^.*[ㄱ-ㅎㅏ-ㅣ가-힣]+.*$")) {
                            thumnailInfoList.add(thumnailinfo);
                            thumnail_idx++;
                        }
                    }
                    catch (Exception e) {
                    e.printStackTrace();
                    }
                }
                else
                {
                    try {
                        if (ingreList.get(0).matches("^.*[ㄱ-ㅎㅏ-ㅣ가-힣]+.*$")) {

                        } else {
                            thumnailInfoList.add(thumnailinfo);
                            thumnail_idx++;
                        }
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if(connect_idx == 20){
                    for(int i = 0; i < thumnail_idx; i++){
                        showRecipeThumnail(thumnailInfoList.remove(), ll);
                    }
                    thumnail_idx = 0;
                    connect_idx = 0;
                }
            }

            @Override
            public void onFailure(int code) {
                connect_idx++;

                if(connect_idx == 20){
                    for(int i = 0; i < thumnail_idx; i++){
                        showRecipeThumnail(thumnailInfoList.remove(), ll);
                    }
                    thumnail_idx = 0;
                    connect_idx = 0;
                }
                // 통신에 실패한 경우
                // 결과값이 없다거나, 서버에서 오류를 리턴했거나
                // 또는 ResponseBody 안의 key 값이 이상하거나
              //  System.out.println("RecipeShownByMe getRecipe onFailure@@@@@@");
            }
        });

    }
}
