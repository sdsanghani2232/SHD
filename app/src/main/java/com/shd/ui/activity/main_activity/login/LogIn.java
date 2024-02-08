package com.shd.ui.activity.main_activity.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.Toast;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.shd.R;
import com.shd.halperclass.informationclass.AdminInfo;
import com.shd.viewmodes.AdminModel;
import com.shd.ui.activity.main_activity.home.HomeActivity;
import com.shd.halperclass.validationclass.LoginValidation;
import java.util.Objects;

public class LogIn extends AppCompatActivity {
    Button login,forgotPassword;
    TextInputEditText email,password;
    TextInputLayout emailLayout, passwordLayout;
    AdminModel model;
    final AdminInfo adminInfo = AdminInfo.getInstance();
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        model = new ViewModelProvider(this).get(AdminModel.class);
        model.getAdminMap().observe(this, adminInfo::updateAdminList);
        findId();

        login.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    emailLayout.setErrorEnabled(false);
                    passwordLayout.setErrorEnabled(false);
                    checkData();
                    break;
                case MotionEvent.ACTION_UP:
                    login.setBackgroundColor(getColor(R.color.login_button_background_color));
                    break;
            }
            return true;
        });

        forgotPassword.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(),ResetPassword.class)));

    }

    private void findId() {
        login = findViewById(R.id.login_button);
        email = findViewById(R.id.email_text);
        emailLayout = findViewById(R.id.email_layout);
        password = findViewById(R.id.pass_text);
        passwordLayout = findViewById(R.id.pass_layout);
        emailLayout.setErrorEnabled(false);
        passwordLayout.setErrorEnabled(false);
        forgotPassword = findViewById(R.id.forgot_pass);
    }
    private void checkData() {

        login.setBackgroundColor(getColor(R.color.login_button_background_onClick_color));
        String emailId = Objects.requireNonNull(email.getText()).toString().trim();
        String pass = Objects.requireNonNull(password.getText()).toString().trim();

        LoginValidation lv = new LoginValidation(emailId,pass);

        lv.signIn(msg -> {
            switch (msg)
            {
                case "Login Successfully" :
                {
                    startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                    finish();
                    break;
                }

                case "Empty Email" :
                {
                    emailLayout.setErrorEnabled(true);
                    emailLayout.setError(getResources().getString(R.string.empty_email_error));
                    break;
                }
                case "Empty Password" :
                {
                    passwordLayout.setErrorEnabled(true);
                    passwordLayout.setError(getResources().getString(R.string.empty_email_error));
                    break;
                }
                case "Invalid Email" :
                {
                    emailLayout.setErrorEnabled(true);
                    emailLayout.setError(getResources().getString(R.string.invalid_email_error));
                    break;
                }
                case "Invalid Password" :
                {
                    passwordLayout.setErrorEnabled(true);
                    passwordLayout.setError(getResources().getString(R.string.invalid_password_error));
                    break;
                }
                case "other" :
                {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.other_error), Toast.LENGTH_SHORT).show();
                    emailLayout.setErrorEnabled(true);
                    emailLayout.setError(getResources().getString(R.string.invalid_email_error));
                    passwordLayout.setErrorEnabled(true);
                    passwordLayout.setError(getResources().getString(R.string.invalid_password_error));
                    break;
                }
            }
        });
    }
}