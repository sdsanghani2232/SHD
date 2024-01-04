package com.shd.validation;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.shd.dataclass.Admin;
import com.shd.db_firebase.AdminInfo;

import java.util.Objects;

public class LoginValidation {
    private final String email,password ;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    AdminInfo adminInfo = AdminInfo.getInstance();

    public interface LoginResult{
        void onResult(String msg);
    }
    public LoginValidation(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public void signIn(LoginResult result)
    {
        if(email.isEmpty() || email.trim().length() ==0)
        {
            result.onResult("Empty Email");
        }else if (password.isEmpty() || password.trim().length() == 0)
        {
            result.onResult("Empty Password");
        }else {
            Admin admin = adminInfo.getAdminInfo(email);
            if(admin != null)
            {
                checkPassword(result);
            }else {
                result.onResult("Invalid Email");
            }
        }
    }

    private void checkPassword(LoginResult result) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                result.onResult("Login Successfully");
            } else {
                try {
                    throw Objects.requireNonNull(task.getException());
                } catch (FirebaseAuthInvalidUserException e) {
                    result.onResult("Invalid Email");
                } catch (FirebaseAuthInvalidCredentialsException e) {
                    result.onResult("Invalid Password");
                } catch (Exception e) {
                    result.onResult("other");
                }
            }
        });
    }
}