package com.shd.halperclass.informationclass;

import android.annotation.SuppressLint;
import com.shd.model.Admin;
import java.util.HashMap;
import java.util.Map;

public class AdminInfo {

    @SuppressLint("StaticFieldLeak")
    private static AdminInfo instance;
    private Map<String,Admin> admin = new HashMap<>();
    public static synchronized AdminInfo getInstance()
    {
        if(instance == null)
        {
            instance = new AdminInfo();
        }
        return instance;
    }
    public void updateAdminList(Map<String,Admin> adminInfo)
    {
        admin = adminInfo;
    }

    public boolean isAdmin(String email)
    {
        return admin != null && admin.containsKey(email); // check that document id her is email
    }

    public Admin getAdminInfo(String email)
    {
        if(isAdmin(email) && admin != null)
        {
            return admin.get(email); // return only 1 admin detail
        }
        return null; // not any admin
    }
}
