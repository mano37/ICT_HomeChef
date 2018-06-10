package com.homechef.ict.ict_homechef;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.view.View;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.homechef.ict.ict_homechef.ConnectUtil.ConnectUtil;
import com.homechef.ict.ict_homechef.ConnectUtil.HttpCallback;

import java.util.HashMap;

public class SplashActivity extends Activity{

    private static final String TAG = "SplashActivity";

    // for UserInfo
    private JsonObject userJson;
    private String userInfo;
    private JsonParser parser = new JsonParser();

    // SharedPreferences
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    String tokenKeyName = "jwt_token";

    // for Login
    private GoogleSignInClient mGoogleSignInClient;
    private static final int RC_GET_AUTH_CODE = 9003;

    ConnectUtil connectUtil;



    // onCreate 시작

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        // 구글 로그인 버튼 설정
        SignInButton signInButton = (SignInButton) findViewById(R.id.splash_sign_in_button);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onStart();
            }
        });
        // 구글 로그인 버튼 설정 끝


        // 대기화면 시작
        try {
            Thread.sleep(2500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // 대기화면 끝


        // 로그인 셋업
        String serverClientId = getString(R.string.server_client_id);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestScopes(new Scope(Scopes.DRIVE_APPFOLDER))
                .requestServerAuthCode(serverClientId)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        // 로그인 셋업 끝
    }

    // onCreate 끝


    // onStart 시작
    @Override
    public void onStart() {

        super.onStart();

        final Intent mainActivity = new Intent(this, MainActivity.class);

        // 로컬 저장소에서 유저 token 얻기
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        editor = pref.edit();
        String tokenCalled = pref.getString(tokenKeyName, "0");

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        // 로컬 저장소에서 유저 token 얻기 끝


        // 서버에 접속하여 유저 정보 얻기
        // 접속 기록이 있다면 서버에 토큰 갱신을 시도함
        if(account != null){
            connectUtil = ConnectUtil.getInstance(this).createBaseApi();
            connectUtil.postSession(tokenCalled, new HttpCallback() {
                @Override
                public void onError(Throwable t) {

                 //   System.out.println("postSession Error Splash@@@@@");

                }

                @Override
                public void onSuccess(int code, Object receivedData) {

                    // temp 변수 셋업
                    String data = (String) receivedData;
                    userJson = (JsonObject) parser.parse(data);

                    // 토큰을 로컬에 저장
                    editor.putString(tokenKeyName, userJson.get(tokenKeyName).getAsString());
                    editor.commit();
                    System.out.println(userJson.get(tokenKeyName).toString());
                    // 토큰을 로컬에 저장 끝

                    // mainActivity 시작, 유저 정보 전달
                    mainActivity.putExtra("user_info", data);
                    startActivity(mainActivity);

                    finish();
                    // 끝
                }

                @Override
                public void onFailure(int code) {
                    // 토큰 갱신에 실패했을 경우
                    signOut();
                    signIn();
                }
            });
        }
        // 접속 기록이 없을 경우
        else{
            signIn();
        }



    }
    // [START signIn]
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_GET_AUTH_CODE);
    }
    // [END signIn]

    // [START signOut]
    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // [START_EXCLUDE]
                        // [END_EXCLUDE]
                    }
                });
    }


    // [START onActivityResult]
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_GET_AUTH_CODE) {
            // [START get_auth_code]
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                String authCode = account.getServerAuthCode();

                HashMap<String , String> loginPara = new HashMap<String , String>();

                loginPara.put("auth_code", authCode);
                loginPara.put("auth_type", "google");

                connectUtil = ConnectUtil.getInstance(this).createBaseApi();
                connectUtil.postLogin(loginPara, new HttpCallback() {
                    @Override
                    public void onError(Throwable t) {

                  //      System.out.println("On login ,,, postLogin Error@@@@@");

                    }

                    @Override
                    public void onSuccess(int code, Object receivedData) {

                        String data = (String) receivedData;
                    //    System.out.println("On login,, postSession Response Get Splash @@@@@ : " + data );
                        userJson = (JsonObject) parser.parse(data);

                        editor.putString(tokenKeyName, userJson.get(tokenKeyName).getAsString());
                        editor.commit();
                   //     System.out.println("On login,,,, @@@@@@@@@@@@@@@@@@@@@ token is");
                        System.out.println(userJson.get(tokenKeyName).toString());

                        Intent mainActivity = new Intent(getApplicationContext(), MainActivity.class);
                        mainActivity.putExtra("user_info", data);
                        startActivity(mainActivity);

                        finish();

                    }

                    @Override
                    public void onFailure(int code) {

                   //     System.out.println("On login,,, postLogin Fail@@@@@@@@@@@@");

                    }
                });

            } catch (ApiException e) {
           //     Log.w(TAG, "Sign-in failed", e);
            }

            // [END get_auth_code]
        }
    }

}
