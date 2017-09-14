package com.tmdb;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.tmdb.databinding.ActivitySplashScreenBinding;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        @SuppressWarnings("unused")
        ActivitySplashScreenBinding activitySplashScreenBinding =
                DataBindingUtil.setContentView(this, R.layout.activity_splash_screen);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashScreen.this,MainScreenActivity.class));
                finish();
            }
        }, 3000);
    }
}
