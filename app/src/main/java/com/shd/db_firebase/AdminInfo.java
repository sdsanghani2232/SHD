package com.shd.db_firebase;

import android.content.Context;
import android.widget.Toast;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.shd.model.Admin;
import java.util.HashMap;
import java.util.Map;

public class AdminInfo {

    Context context;
    private static AdminInfo instance;

    public void setContext(Context context)
    {
        this.context = context;
    }
    public static synchronized AdminInfo getInstance()
    {
        if(instance == null)
        {
            instance = new AdminInfo();
        }
        return instance;
    }
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    public Map<String,Admin> adminInfo = new HashMap<>();
    public void getAdmin()
    {
        db.collection("admin")
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful())
                    {
                        for (DocumentSnapshot document : task.getResult())
                        {
                            Admin admin= document.toObject(Admin.class);
                            if(admin != null)
                            {
                                adminInfo.put(document.getId(), admin); // get all admin on firebase storage
                            }
                        }
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(context, "Some Thing was Wrong please try again letter!", Toast.LENGTH_SHORT).show());
    }

    public boolean isAdmin(String email)
    {
        return adminInfo.containsKey(email); // check that document id her is email
    }

    public Admin getAdminInfo(String email)
    {
        if(isAdmin(email))
        {
            return adminInfo.get(email); // return only 1 admin detail
        }
        return null; // not any admin
    }
}
