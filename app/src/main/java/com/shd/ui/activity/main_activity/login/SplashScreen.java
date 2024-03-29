package com.shd.ui.activity.main_activity.login;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import com.shd.R;
import com.shd.halperclass.informationclass.AdminInfo;
import com.shd.viewmodes.AdminModel;
import com.shd.ui.activity.main_activity.home.HomeActivity;

@SuppressLint("CustomSplashScreen")
public class SplashScreen extends AppCompatActivity {


    AdminModel model;
    final AdminInfo adminInfo = AdminInfo.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
// Todo : that code is working. after complete Entire app remove comment
//        model = new ViewModelProvider(this).get(AdminModel.class);
//        model.getAdminMap().observe(this, adminInfo::updateAdminList);

        new Handler().postDelayed(() -> {
//            startActivity(new Intent(getApplicationContext(), LogIn.class));
//            finish();
            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
            finish();
        },500);
    }
}