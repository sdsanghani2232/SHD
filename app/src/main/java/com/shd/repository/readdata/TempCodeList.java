package com.shd.repository.readdata;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
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
    private boolean documentExits = false;
    MutableLiveData<List<TempCode>> codeList = new MutableLiveData<>();
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    List<TempCode> tempCodeList;

    public LiveData<List<TempCode>> getTempCode()
    {
        db.collection("jewelleryCodes").document("tempCodes").addSnapshotListener((value, error) -> {
            if(error == null && value != null && value.exists())
            {
                documentExits = true;
                tempCodeList = new ArrayList<>();
                TempCode tc = value.toObject(TempCode.class);
                tempCodeList.add(tc);
                codeList.postValue(tempCodeList);
            }
            else
            {
                documentExits = false;
            }
        });
        return codeList;
    }

    public boolean documentExits()
    {
        return documentExits;
    }
}
