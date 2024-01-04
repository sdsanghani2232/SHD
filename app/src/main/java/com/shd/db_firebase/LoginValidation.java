package com.shd.db_firebase;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.shd.dataclass.Admin;

import java.util.Objects;

public class LoginValidation {
    private final String email,password ;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
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
            db.collection("admin").document(email).get().addOnCompleteListener(task -> {
                if(task.isSuccessful())
                {
                    DocumentSnapshot document = task.getResult();
                    if(document.exists())
                    {
                        Admin admin = document.toObject(Admin.class);
                        if (admin != null)
                        {
                            checkPassword(result);
                        }else
                        {
                            result.onResult("other");
                        }
                    }else
                    {
                        result.onResult("Invalid Email");
                    }
                }
            }).addOnFailureListener(e -> result.onResult("other"));
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
