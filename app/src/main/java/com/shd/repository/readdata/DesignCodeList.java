package com.shd.repository.readdata;

import android.util.Log;
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
    MutableLiveData<List<DesignCode>> codeList = new MutableLiveData<>();
    FirebaseFirestore db = FirebaseFirestore.getInstance();

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
        Log.d("value of document ","in method"+documentExits);
        return documentExits;
    }
}
