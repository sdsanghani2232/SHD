package com.shd.login;

import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;

import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import com.shd.R;

public class LogIn extends AppCompatActivity {
    Button login;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        login = findViewById(R.id.login_button);


        login.setOnTouchListener((v, event) -> {
             switch (event.getAction()) {
                 case MotionEvent.ACTION_DOWN:
                     login.setBackgroundColor(getColor(R.color.login_button_click));
                     login.setElevation(16f);
                     Toast.makeText(LogIn.this, ""+login.getElevation(), Toast.LENGTH_SHORT).show();
                     break;
                 case MotionEvent.ACTION_UP:
                     login.setBackgroundColor(getColor(R.color.login_button_bg));
                     break;
             }
             return true;
         });
    }
}