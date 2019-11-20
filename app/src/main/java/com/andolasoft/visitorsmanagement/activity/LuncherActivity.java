package com.andolasoft.visitorsmanagement.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import com.andolasoft.visitorsmanagement.R;

public class LuncherActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    boolean is_login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_luncher);

        sharedPreferences = getSharedPreferences("VM_db",MODE_PRIVATE);
        if(sharedPreferences.contains("is_login")){
            is_login = sharedPreferences.getBoolean("is_login",false);
        }

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                if(is_login){
                    Intent intent = new Intent(LuncherActivity.this, BaseActivity.class);
                    startActivity(intent);
                    finish();
                }else {
                    Intent intent = new Intent(LuncherActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        },5000);
    }
}
