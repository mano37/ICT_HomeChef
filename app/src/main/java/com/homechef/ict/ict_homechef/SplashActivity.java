package com.homechef.ict.ict_homechef;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import static java.lang.Boolean.FALSE;

public class SplashActivity extends Activity{

    boolean login = FALSE;
    String json;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        try {
            Thread.sleep(4000);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("user_info", json);
        startActivity(intent);

        finish();
    }

}
