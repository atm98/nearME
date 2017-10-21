package com.agnt45.nearme;


import android.content.Intent;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


public class LoadingActivity extends AppCompatActivity {
    private static int SPLASH_TIME=4000;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent Login = new Intent(LoadingActivity.this,LoginActivity.class);
                Login.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(Login);
                finish();
            }

        },SPLASH_TIME);
    }

}
