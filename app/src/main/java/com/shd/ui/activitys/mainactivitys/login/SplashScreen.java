package com.shd.ui.activitys.mainactivitys.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.shd.R;
import com.shd.ui.activitys.mainactivitys.home.HomeActivity;

public class SplashScreen extends AppCompatActivity {

    // TODO : that code is working. after complete Entire app remove comment
//    AdminInfo adminInfo = AdminInfo.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

//        adminInfo.setContext(this);
//        adminInfo.getAdmin();
        new Handler().postDelayed(() -> {
//            startActivity(new Intent(getApplicationContext(), LogIn.class));
//            finish();
            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
            finish();
        },300);
    }
}