package com.homechef.ict.ict_homechef;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.homechef.ict.ict_homechef.ConnectUtil.ConnectUtil;
import com.homechef.ict.ict_homechef.ConnectUtil.HttpCallback;
import com.homechef.ict.ict_homechef.ConnectUtil.ResponseBody.RecipeGet;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private String userInfo;
    private JsonParser parser = new JsonParser();

    // user 정보
    private JsonObject userJson;

    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ConnectUtil connectUtil = ConnectUtil.getInstance(this).createBaseApi();

        String jwt_token = "eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJlbWFpbCI6ImtqaHdhbmlkQGdtYWlsLmNvbSIsImV4cCI6MTUyODczMDY3OSwianRpIjoiNSIsImlhdCI6MTUyODI5ODY3OSwiaXNzIjoiSG9tZWNoZWYtU2VydmVyIn0.okMQOfVNKtDATGX99Xo_Xt3K5V6I-dFG5FnILgMIBWoX07fQmp1nEq2yVXCfar2KrU54Yd3FHPmBWPpjHS8eFQ";
        String token = "Bearer " + jwt_token;
        System.out.println("Token :" + token);

        Map<String,String> header = new HashMap<>();
        header.put("Authorization", token);

        connectUtil.getRecipe(header,"1000", new HttpCallback() {
            @Override
            public void onError(Throwable t) {
                // 내부적 에러 발생할 경우
                System.out.println("RecipeGet onError@@@@@@");
            }
            @Override
            public void onSuccess(int code, Object receivedData) {
                // 성공적으로 완료한 경우
                RecipeGet data = (RecipeGet) receivedData;
                System.out.println("RecipeGet onSuccess@@@@@@");
                System.out.println("@@@@Title : " + data.title);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        //카메라 버튼 누를 시 검색 창으로 이동
        if (id == R.id.nav_camera) {
            Intent intent = new Intent(this, SearchActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {
            Intent intent = new Intent(this, SignInActivity.class);
            startActivityForResult(intent, 4444);

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

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
                userInfo = data.getStringExtra("user_info");
                userJson = (JsonObject) parser.parse(userInfo);
                String str = userJson.get("name").toString();
                textView = (TextView)findViewById(R.id.text1);
                textView.setText(str);
            }
        }

    }
}
