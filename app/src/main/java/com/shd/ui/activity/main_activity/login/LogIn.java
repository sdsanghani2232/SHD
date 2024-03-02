package com.shd.ui.activity.main_activity.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.shd.R;
import com.shd.halperclass.informationclass.AdminInfo;
import com.shd.halperclass.otherClass.CheckInternet;
import com.shd.viewmodes.AdminModel;
import com.shd.ui.activity.main_activity.home.HomeActivity;
import com.shd.halperclass.validationclass.LoginValidation;
import java.util.Objects;

public class LogIn extends AppCompatActivity {
    Button login,forgotPassword;
    TextInputEditText email,password;
    TextInputLayout emailLayout, passwordLayout;
    TextView errorTextView;
    AdminModel model;
    boolean isConnection = false;
    final AdminInfo adminInfo = AdminInfo.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        model = new ViewModelProvider(this).get(AdminModel.class);
        model.getAdminMap().observe(this, adminInfo::updateAdminList);
        findId();
        checkConnection();
        errorTextView.setVisibility(View.GONE);

        login.setOnClickListener(v -> {
            emailLayout.setErrorEnabled(false);
            passwordLayout.setErrorEnabled(false);
            checkConnection();
            if(isConnection) checkData();
            else {
                Snackbar.make(findViewById(R.id.login_page),R.string.internet_error,Snackbar.LENGTH_LONG)
                        .setTextColor(getColor(R.color.colorPrimaryText))
                        .setBackgroundTint(getColor(R.color.colorSecondaryBackground)).show();
            }
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
        errorTextView = findViewById(R.id.error_text);
    }

    private void checkConnection() {
        CheckInternet internet = new CheckInternet(getApplicationContext());
        isConnection = internet.Check();
    }

    private void checkData() {

        String emailId = Objects.requireNonNull(email.getText()).toString().trim();
        String pass = Objects.requireNonNull(password.getText()).toString().trim();

        LoginValidation lv = new LoginValidation(emailId,pass);

        lv.signIn(msg -> {
            switch (msg)
            {
                case "Login Successfully" :
                {
                    errorTextView.setVisibility(View.GONE);
                    startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                    finish();
                    break;
                }

                case "Empty Email" :
                {
                    emailLayout.setErrorEnabled(true);
                    emailLayout.setError(" ");
                    errorTextView.setVisibility(View.VISIBLE);
                    errorTextView.setText(getResources().getString(R.string.empty_email_error));
                    break;
                }
                case "Empty Password" :
                {
                    passwordLayout.setErrorEnabled(true);
                    passwordLayout.setError(" ");
                    errorTextView.setVisibility(View.VISIBLE);
                    errorTextView.setText(getResources().getString(R.string.empty_password_error));
                    break;
                }
                case "Invalid Email" :
                {
                    emailLayout.setErrorEnabled(true);
                    emailLayout.setError(" ");
                    errorTextView.setVisibility(View.VISIBLE);
                    errorTextView.setText(getResources().getString(R.string.invalid_email_error));
                    break;
                }
                case "Invalid Password" :
                {
                    passwordLayout.setErrorEnabled(true);
                    passwordLayout.setError(" ");
                    errorTextView.setVisibility(View.VISIBLE);
                    errorTextView.setText(getResources().getString(R.string.invalid_password_error));
                    break;
                }
                case "other" :
                {
                    errorTextView.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.other_error), Toast.LENGTH_SHORT).show();
                    emailLayout.setErrorEnabled(true);
                    emailLayout.setError(" ");
                    passwordLayout.setErrorEnabled(true);
                    passwordLayout.setError(" ");
                    break;
                }
            }
        });
    }
}
