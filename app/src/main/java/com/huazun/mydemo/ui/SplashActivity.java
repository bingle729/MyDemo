package com.huazun.mydemo.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.huazun.mydemo.Globals;
import com.huazun.mydemo.R;
import com.huazun.mydemo.ServerEnvironment;

public class SplashActivity extends AppCompatActivity {
    private final int SPLASH_DISPLAY_LENGHT = 3000;
    private Handler handler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        handler = new Handler();
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                initData();
                Intent intent = new Intent(SplashActivity.this,
                        LoginActivity.class);
                startActivity(intent);
                SplashActivity.this.finish();
            }
        }, SPLASH_DISPLAY_LENGHT);

    }

    private void initData(){
        SharedPreferences sharedPreferences= getSharedPreferences(Globals.SERVER_URL, Context.MODE_PRIVATE);
        String serverUrl = sharedPreferences.getString(Globals.SERVER_URL, "");
        if (serverUrl == null || serverUrl.equals("")) {
            serverUrl = ServerEnvironment.BASE_URL;
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(Globals.SERVER_URL, serverUrl);
            editor.commit();
        }
    }

}
