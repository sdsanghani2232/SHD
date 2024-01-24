package com.shd.viewmodes;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import com.shd.model.DesignCode;
import com.shd.repository.readdata.DesignCodeList;
import java.util.List;

public class DesignCodeModel extends ViewModel {
    private final DesignCodeList designCodeList = DesignCodeList.getInstance();
    private final LiveData<List<DesignCode>> codeList = designCodeList.getDesignCode();

    public LiveData<List<DesignCode>> getDesignCodeList()
    {
        return codeList;
    }
}
