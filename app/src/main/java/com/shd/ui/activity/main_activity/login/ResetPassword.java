package com.shd.ui.activity.main_activity.login;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.shd.R;
import com.shd.halperclass.informationclass.AdminInfo;
import com.shd.halperclass.otherClass.CheckInternet;

import java.util.Objects;

public class ResetPassword extends AppCompatActivity {

    TextInputEditText email;
    TextInputLayout emailLayout;
    Button send;
    TextView error;
    private boolean isConnection = true;
    MaterialToolbar toolbar;
    final FirebaseAuth auth = FirebaseAuth.getInstance();
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        findId();
        checkConnection();

        toolbar.setNavigationOnClickListener(v -> {
            OnBackPressedDispatcher dispatcher = getOnBackPressedDispatcher();
            dispatcher.addCallback(this, new OnBackPressedCallback(true) {
                @Override
                public void handleOnBackPressed() {
                    finish();
                }
            });
        });

        send.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    emailLayout.setErrorEnabled(false);
                    checkConnection();
                    if(isConnection)
                    {
                        send.setBackgroundColor(getColor(R.color.login_button_background_onClick_color));
                        checkData();
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    if(isConnection)
                    {
                        send.setBackgroundColor(getColor(R.color.login_button_background_color));
                    }else {
                        send.setBackgroundColor(getColor(R.color.RP_not_enable_button_color));
                    }

                    break;
            }
            return true;
        });
    }

    @SuppressLint("SetTextI18n")
    private void checkConnection() {
        CheckInternet internet = new CheckInternet(getApplicationContext());
        if(!internet.Check())
        {
            isConnection = false;
            error.setVisibility(View.VISIBLE);
            send.setBackgroundColor(getColor(R.color.RP_not_enable_button_color));
            send.setClickable(false);
            error.setText("Check Internet Connectivity");
        }else {
            isConnection = true;
            error.setVisibility(View.GONE);
            send.setBackgroundColor(getColor(R.color.login_button_background_color));
            send.setClickable(true);
        }
    }

    private void findId() {
        email = findViewById(R.id.conform_email_text);
        emailLayout = findViewById(R.id.conform_email_layout);
        send = findViewById(R.id.send_button);
        error = findViewById(R.id.error_text);
        toolbar = findViewById(R.id.appbar_material);
    }
    private void checkData() {
        send.setBackgroundColor(getColor(R.color.login_button_background_color));
        String emailId = Objects.requireNonNull(email.getText()).toString().trim();

        if(emailId.isEmpty() || emailId.trim().length() == 0 )
        {
            emailLayout.setErrorEnabled(true);
            emailLayout.setError("Enter The email id ");
        }else {
            AdminInfo adminInfo = AdminInfo.getInstance();
            if(adminInfo.isAdmin(emailId))
            {
                sendEmail(emailId);
            }else {
                emailLayout.setErrorEnabled(true);
                emailLayout.setError("Enter valid email id ");
            }
        }
    }

    private void sendEmail(String emailId) {
        auth.sendPasswordResetEmail(emailId)
                .addOnCompleteListener(task -> Toast.makeText(ResetPassword.this, "Password Rest Email send", Toast.LENGTH_SHORT).show()).
                addOnFailureListener(e -> Toast.makeText(ResetPassword.this, "Some Thing was Wrong please try again", Toast.LENGTH_SHORT).show());
    }
}