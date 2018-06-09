package com.homechef.ict.ict_homechef;

import android.content.Intent;
import android.content.SharedPreferences;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.squareup.picasso.Picasso;

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
        String startStr = userNameStr +"님 " + "환영합니다.";
        Toast.makeText(getApplicationContext(), startStr, Toast.LENGTH_LONG).show();
        // 환영 메세지 끝


        // 화면 생성
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


        // user info 표시

        View nav_header_view = navigationView.getHeaderView(0);
        textView = (TextView) nav_header_view.findViewById(R.id.Navigation_profile_name);
        textView.setText(userNameStr);
        textView = (TextView) nav_header_view.findViewById(R.id.Navigation_profile_email);
        textView.setText(userEmailStr);
        imageView = (ImageView) nav_header_view.findViewById(R.id.Navigation_profile_image);
        Picasso.get().load(userPictureStr)
                .into(imageView);


        textView = (TextView)findViewById(R.id.text1);
        textView.setText(userNameStr);


        // Make SharedPreference
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        editor = pref.edit();
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

        // 레시피 검색으로 이동
        if (id == R.id.nav_searchRecipe) {
            Intent intent = new Intent(this, SearchActivity.class);
            intent.putExtra("user_info",userInfo);
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
                textView = (TextView)findViewById(R.id.text1);
                textView.setText(str);

                editor.putString(tokenKeyName, userJson.get(tokenKeyName).toString());
                editor.commit();
            }
            if(resultCode == 2){
                System.out.println("ResultCode 2 by Session");
                userInfo = data.getStringExtra("user_info");
                userJson = (JsonObject) parser.parse(userInfo);
                String str = userJson.get("name").toString();
                textView = (TextView)findViewById(R.id.text1);
                textView.setText(str);

                editor.putString(tokenKeyName, userJson.get(tokenKeyName).toString());
                editor.commit();
            }
        }

    }
}
