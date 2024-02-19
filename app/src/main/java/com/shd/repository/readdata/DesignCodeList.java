package com.shd.repository.readdata;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
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
    private boolean documentExits = false;
    final MutableLiveData<List<DesignCode>> codeList = new MutableLiveData<>();
    final FirebaseFirestore db = FirebaseFirestore.getInstance();

    List<DesignCode> designCodeList;

    public LiveData<List<DesignCode>> getDesignCode()
    {
        db.collection("jewelleryCodes").document("designCodes").addSnapshotListener((value, error) -> {
            if(error == null && value != null && value.exists())
            {
                documentExits = true;
                designCodeList = new ArrayList<>();
                DesignCode dc = value.toObject(DesignCode.class);
                designCodeList.add(dc);
                codeList.postValue(designCodeList);
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
