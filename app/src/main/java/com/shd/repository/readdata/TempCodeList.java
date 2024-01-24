package com.shd.repository.readdata;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.shd.model.TempCode;

import java.util.ArrayList;
import java.util.List;

public class TempCodeList {
    private static TempCodeList instance;
    public static synchronized TempCodeList getInstance()
    {
        if(instance == null)
        {
            instance = new TempCodeList();
        }
        return instance;
    }
    MutableLiveData<List<TempCode>> codeList = new MutableLiveData<>();
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    List<TempCode> tempCodeList;

    public LiveData<List<TempCode>> getTempCode()
    {
        db.collection("jewelleryCodes").document("tempCodes").addSnapshotListener((value, error) -> {
            if(error == null && value != null)
            {
                tempCodeList = new ArrayList<>();
                TempCode tc = value.toObject(TempCode.class);
                tempCodeList.add(tc);
                codeList.postValue(tempCodeList);
            }
        });
        return codeList;
    }

    public boolean documentExits()
    {
        final boolean[] doxExits = {false};
        db.collection("jewelleryCodes").document("tempCodes").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
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
