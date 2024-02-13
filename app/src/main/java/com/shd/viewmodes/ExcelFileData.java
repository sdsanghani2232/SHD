package com.shd.viewmodes;

import java.util.ArrayList;
import java.util.List;

public class ExcelFileData {
    private static ExcelFileData instance;
    private List<List<Object>> excelDataList = new ArrayList<>();
    public static synchronized ExcelFileData getInstance()
    {
        if(instance == null)
        {
            instance = new ExcelFileData();
        }
        return instance;
    }

    public List<List<Object>> getExcelDataList() {
        return excelDataList;
    }

    public void setExcelDataList(List<List<Object>> excelDataList) {
        this.excelDataList = excelDataList;
    }
}
