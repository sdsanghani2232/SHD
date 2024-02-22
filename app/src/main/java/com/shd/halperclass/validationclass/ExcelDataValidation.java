package com.shd.halperclass.validationclass;

import android.content.Context;
import com.shd.R;
import com.shd.halperclass.informationclass.Codes;
import com.shd.viewmodes.ExcelFileData;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ExcelDataValidation {
    Context context;
    ExcelFileData data = ExcelFileData.getInstance();
    private final Codes codes = Codes.getInstance();
    List<List<Object>> sheetData = data.getExcelDataList();
    List<String> mainJw = new ArrayList<>();
    private static int errorCount;

    public ExcelDataValidation(Context context) {
        this.context = context;
        errorCount = 0;
        mainJw.addAll(Arrays.asList(context.getResources().getStringArray(R.array.jewellery_main_type)));
        mainJw.addAll(mainJw.size(), Arrays.asList(context.getResources().getStringArray(R.array.jewellery_ring_sub_type)));
        mainJw.addAll(mainJw.size(), Arrays.asList(context.getResources().getStringArray(R.array.jewellery_nkc_sub_type)));
        mainJw.addAll(mainJw.size(), Arrays.asList(context.getResources().getStringArray(R.array.jewellery_er_sub_type)));
        mainJw.addAll(mainJw.size(), Arrays.asList(context.getResources().getStringArray(R.array.jewellery_br_sub_type)));
    }

    public int validateData() {
        for (int position = 0; position < sheetData.size(); position++) {
            boolean setError = false;
            List<Object> row = sheetData.get(position);

            if (row.get(3).equals("null")) {
                if (row.get(5).equals("null")) {

                    setErrorCount();
                    setError = true;
                } else {
                    if (row.get(5).toString().length() < 6) {

                        setErrorCount();
                        setError = true;
                    } else {
                        if (codes.isTempCodeExits(row.get(5).toString())) {

                            setErrorCount();
                            setError = true;
                        }
                    }
                }
            } else {
                if (row.get(3).toString().length() < 6) {

                    setErrorCount();
                    setError = true;
                } else {
                    if (codes.isDesignCodeExists(row.get(3).toString())) {

                        setErrorCount();
                        setError = true;
                    }
                }
            }

            if (row.get(10).equals("null")) // no sub type
            {
                if (mainJw.contains(row.get(9).toString())) // main type check
                {
                    if (mainJw.indexOf(row.get(9).toString()) < 4) // main type have sub type?
                    {
                        if (!setError) setErrorCount();
                    }
                } else {
                    if (!setError) setErrorCount();
                }
            } else {
                if (!mainJw.contains(row.get(10).toString())) {
                    if (!setError) setErrorCount();
                }
            }
            if ((!row.get(12).equals("null") && Float.parseFloat((String) row.get(12)) > 100) ||
                    (!row.get(13).equals("null") && Float.parseFloat((String) row.get(13)) > 100) ||
                    (!row.get(14).equals("null") && Float.parseFloat((String) row.get(14)) > 100) ||
                    (!row.get(15).equals("null") && Float.parseFloat((String) row.get(15)) > 100) ||
                    (!row.get(16).equals("null") && Float.parseFloat((String) row.get(16)) > 100)) {

                if (!setError) setErrorCount();
            }

        }
        return errorCount;
    }

    private static void setErrorCount() {
        errorCount++;
    }
}
