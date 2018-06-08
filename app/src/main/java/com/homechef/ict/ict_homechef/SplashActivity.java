package com.homechef.ict.ict_homechef;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import static java.lang.Boolean.FALSE;

public class SplashActivity extends Activity{

    boolean login = FALSE;
    String userInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Intent signInActivityIntent = new Intent(this, SignInActivity.class);
        startActivityForResult(signInActivityIntent, 4444);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 4444){
            if(resultCode == 1){
                System.out.println("ready @@@@@ for Start MainActivity");
                userInfo = data.getStringExtra("user_info");

                Intent mainActivityIntent = new Intent(this, MainActivity.class);
                mainActivityIntent.putExtra("user_info", userInfo);
                startActivity(mainActivityIntent);

                finish();

            }
        }

    }

}
