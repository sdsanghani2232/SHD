package com.shd.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.shd.R;
import com.shd.db_firebase.AdminInfo;
import com.shd.login.LogIn;

public class SplashScreen extends AppCompatActivity {

    AdminInfo adminInfo = AdminInfo.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        adminInfo.setContext(this);
        adminInfo.getAdmin();
        new Handler().postDelayed(() -> {
            startActivity(new Intent(getApplicationContext(), LogIn.class));
            finish();
        },300);
    }
}