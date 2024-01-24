package com.shd.viewmodes;

import android.view.View;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.shd.model.DesignCode;
import com.shd.model.TempCode;
import com.shd.repository.readdata.TempCodeList;

import java.util.List;

public class TempCodeModel extends ViewModel {
    private final TempCodeList designCodeList = TempCodeList.getInstance();
    private final LiveData<List<TempCode>> codeList = designCodeList.getTempCode();

    public LiveData<List<TempCode>> getTempCodeList()
    {
        return codeList;
    }
}
