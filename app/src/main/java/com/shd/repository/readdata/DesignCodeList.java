package com.shd.repository.readdata;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.shd.model.DesignCode;
import java.util.ArrayList;
import java.util.List;

public class DesignCodeList {
    private static DesignCodeList instance;
    public static synchronized DesignCodeList getInstance()
    {
        if(instance == null)
        {
            instance = new DesignCodeList();
        }
        return instance;
    }
    MutableLiveData<List<DesignCode>> codeList = new MutableLiveData<>();
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    List<DesignCode> designCodeList;

    public LiveData<List<DesignCode>> getDesignCode()
    {
        db.collection("jewelleryCodes").document("designCodes").addSnapshotListener((value, error) -> {
            if(error == null && value != null)
            {
                designCodeList = new ArrayList<>();
                DesignCode dc = value.toObject(DesignCode.class);
                designCodeList.add(dc);
                codeList.postValue(designCodeList);
            }
        });
        return codeList;
    }

    public boolean documentExits()
    {
        final boolean[] doxExits = {false};
        db.collection("jewelleryCodes").document("designCodes").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.getResult().exists()) {
                    doxExits[0] = true;
                }
            }
        });
        return doxExits[0];
    }
}
