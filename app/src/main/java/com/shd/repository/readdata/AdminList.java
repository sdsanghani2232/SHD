package com.shd.repository.readdata;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.shd.model.Admin;
import java.util.HashMap;
import java.util.Map;

public class AdminList {
    private static AdminList instance;
    public static synchronized AdminList getInstance()
    {
        if(instance == null)
        {
            instance = new AdminList();
        }
        return instance;
    }
    MutableLiveData<Map<String, Admin>> adminDetails = new MutableLiveData<>();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Map<String,Admin> map;
    public LiveData<Map<String,Admin>> getAdmin()
    {
        db.collection("admin").addSnapshotListener((value, error) -> {
            if (error == null && value != null)
            {
                map = new HashMap<>();
               for (DocumentSnapshot document : value)
               {
                   Admin admin = document.toObject(Admin.class);
                   map.put(document.getId(),admin);
               }
               adminDetails.postValue(map);
            }
        });
        return adminDetails;
    }
}
