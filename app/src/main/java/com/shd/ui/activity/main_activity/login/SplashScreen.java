package com.shd.ui.activity.main_activity.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
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

    // TODO : that code is working. after complete Entire app remove comment
    AdminModel model;
    AdminInfo adminInfo = AdminInfo.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        model = new ViewModelProvider(this).get(AdminModel.class);
        model.getAdminMap().observe(this, adminMap -> adminInfo.updateAdminList(adminMap));

        new Handler().postDelayed(() -> {
//            startActivity(new Intent(getApplicationContext(), LogIn.class));
//            finish();
            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
            finish();
        },300);
    }
}