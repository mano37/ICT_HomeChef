package com.homechef.ict.ict_homechef;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.Scopes;
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



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String serverClientId = getString(R.string.server_client_id);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestScopes(new Scope(Scopes.DRIVE_APPFOLDER))
                .requestServerAuthCode(serverClientId)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


    }

    @Override
    public void onStart() {

        super.onStart();

        final Intent mainActivity = new Intent(this, MainActivity.class);

        pref = PreferenceManager.getDefaultSharedPreferences(this);
        editor = pref.edit();
        String tokenCalled = pref.getString(tokenKeyName, "0");

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);

        if(account != null){
            connectUtil = ConnectUtil.getInstance(this).createBaseApi();
            connectUtil.postSession(tokenCalled, new HttpCallback() {
                @Override
                public void onError(Throwable t) {

                 //   System.out.println("postSession Error Splash@@@@@");

                }

                @Override
                public void onSuccess(int code, Object receivedData) {

                    String data = (String) receivedData;
                //    System.out.println("postSession Response Get Splash @@@@@ : " + data );
                    userJson = (JsonObject) parser.parse(data);

                    editor.putString(tokenKeyName, userJson.get(tokenKeyName).getAsString());
                    editor.commit();
                 //   System.out.println("@@@@@@@@@@@@@@@@@@@@@ token is");
                    System.out.println(userJson.get(tokenKeyName).toString());

                    mainActivity.putExtra("user_info", data);
                    startActivity(mainActivity);

                    finish();
                }

                @Override
                public void onFailure(int code) {
                //    System.out.println("postSession onFailure Splash @@@@@");
                    signOut();
                    signIn();
                }
            });
        }
        else{
       //     System.out.println("This is running!!!!");
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

                /*
                String message = "RC_GET_AUTH_CODE SUCCESS";

                Log.w(TAG, message);
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
                */

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
    // [END onActivityResult]

}
