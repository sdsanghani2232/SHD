package com.shd.viewmodes;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import com.shd.repository.readdata.AdminList;
import com.shd.model.Admin;
import java.util.Map;

public class AdminModel extends ViewModel {
    private final AdminList adminList = AdminList.getInstance();
    private final LiveData<Map<String, Admin>> adminMap = adminList.getAdmin();

    public LiveData<Map<String, Admin>> getAdminMap()
    {
        return adminMap;
    }

}
