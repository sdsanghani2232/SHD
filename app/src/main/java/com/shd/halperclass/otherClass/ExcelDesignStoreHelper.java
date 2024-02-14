package com.shd.halperclass.otherClass;

import android.graphics.Bitmap;
import com.shd.repository.storedata.JewelleryDetailsStore;
import com.shd.viewmodes.ExcelFileData;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class ExcelDesignStoreHelper {
    private static int totalData = 0;
    private final ExcelFileData excelFileData = ExcelFileData.getInstance();
    List<List<Object>> mainList = excelFileData.getExcelDataList();
    List<Object> designList;

    public void store(JewelleryDetailsStore.Result storeResult)
    {
        AtomicBoolean errorSet = new AtomicBoolean(false);
        for (int position = 0 ; position<mainList.size() ; position++)
        {
            if(!errorSet.get())
            {
                designList = mainList.get(position);
                Bitmap img1Bitmap = designList.get(0).equals("null") ? null : (Bitmap) designList.get(0);
                Bitmap img2Bitmap = designList.get(1).equals("null") ? null : (Bitmap) designList.get(1);
                String customerName = designList.get(2).equals("null") ? "" : (String) designList.get(2);
                String designCode = designList.get(3).equals("null") ? "" : (String) designList.get(3);
                String customerCode = designList.get(4).equals("null") ? "" : (String) designList.get(4);
                String tempCode = designList.get(5).equals("null") ? "" : (String) designList.get(5);
                String work_by = designList.get(6).equals("null") ? "" : (String) designList.get(6);
                String work_place = designList.get(7).equals("null") ? "" : (String) designList.get(7);
                String selectedDate = designList.get(8).equals("null") ? "" : (String) designList.get(8);
                String mainType = designList.get(9).equals("null") ? "" : (String) designList.get(9);
                String subType = designList.get(10).equals("null") ? "" : (String) designList.get(10);
                boolean status = designList.get(11).equals("true");
                String length = designList.get(12).equals("null") ? "" : (String) designList.get(12);
                String width = designList.get(13).equals("null") ? "" : (String) designList.get(13);
                String height = designList.get(14).equals("null") ? "" : (String) designList.get(14);
                String gold = designList.get(15).equals("null") ? "" : (String) designList.get(15);
                String diamond = designList.get(16).equals("null") ? "" : (String) designList.get(16);

                JewelleryDetailsStore store = new JewelleryDetailsStore(img1Bitmap, img2Bitmap, customerName, designCode, customerCode, tempCode, work_by, work_place, selectedDate, mainType, subType, status, length, width, height, gold, diamond);
                store.storeExcelJewelleryImg(result -> {
                    if (result.equals("error")) {
                        errorSet.set(true);
                        storeResult.onResult("Error");
                    }
                    if(result.equals("Successfully"))
                    {
                        notification(storeResult);
                    }
                });
            }
        }
    }

    private void notification(JewelleryDetailsStore.Result storeResult) {
        totalData++;

        if(totalData == mainList.size()) storeResult.onResult("Successfully");
    }
}
