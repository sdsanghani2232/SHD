package com.shd.ui.activity.main_activity.login;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.snackbar.Snackbar;
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
    TextView errorTextView;
    Button send;
    private boolean isConnection = true;
    MaterialToolbar toolbar;
    final FirebaseAuth auth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        findId();
        checkConnection();
        errorTextView.setVisibility(View.GONE);

        toolbar.setNavigationOnClickListener(v -> {
            OnBackPressedDispatcher dispatcher = getOnBackPressedDispatcher();
            dispatcher.addCallback(this, new OnBackPressedCallback(true) {
                @Override
                public void handleOnBackPressed() {
                    finish();
                }
            });
        });

        send.setOnClickListener(v ->{
            emailLayout.setErrorEnabled(false);
            checkConnection();
            if(isConnection) checkData();
            else {
                Snackbar.make(findViewById(R.id.reset_password_page),R.string.internet_error,Snackbar.LENGTH_LONG)
                        .setTextColor(getColor(R.color.colorPrimaryText))
                        .setBackgroundTint(getColor(R.color.colorSecondaryBackground)).show();
            }
        });
    }

    private void checkConnection() {
        CheckInternet internet = new CheckInternet(getApplicationContext());
        isConnection = internet.Check();
    }

    private void findId() {
        email = findViewById(R.id.conform_email_text);
        emailLayout = findViewById(R.id.conform_email_layout);
        send = findViewById(R.id.send_button);
        toolbar = findViewById(R.id.appbar_material);
        errorTextView = findViewById(R.id.error_text);
    }
    private void checkData() {
        String emailId = Objects.requireNonNull(email.getText()).toString().trim();

        if(emailId.isEmpty() || emailId.trim().length() == 0 )
        {
            emailLayout.setErrorEnabled(true);
            emailLayout.setError(" ");
            errorTextView.setVisibility(View.VISIBLE);
            errorTextView.setText(getResources().getString(R.string.empty_email_error));

        }else {
            AdminInfo adminInfo = AdminInfo.getInstance();
            if(adminInfo.isAdmin(emailId))
            {
                errorTextView.setVisibility(View.GONE);
                sendEmail(emailId);
            }else {
                emailLayout.setErrorEnabled(true);
                emailLayout.setError(" ");
                errorTextView.setVisibility(View.VISIBLE);
                errorTextView.setText(getResources().getString(R.string.invalid_email_error));
            }
        }
    }

    private void sendEmail(String emailId) {
        auth.sendPasswordResetEmail(emailId)
                .addOnCompleteListener(task -> Toast.makeText(ResetPassword.this, getResources().getString(R.string.reset_password_email), Toast.LENGTH_SHORT).show()).
                addOnFailureListener(e -> Toast.makeText(ResetPassword.this, getResources().getString(R.string.other_error), Toast.LENGTH_SHORT).show());
    }
}
