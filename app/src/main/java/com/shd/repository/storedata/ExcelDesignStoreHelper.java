package com.shd.repository.storedata;

import android.Manifest;
import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import com.shd.R;
import com.shd.viewmodes.ExcelFileData;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class ExcelDesignStoreHelper {
    private static final String CHANNEL_ID = "upload_notification";
    private static final int NOTIFICATION_ID = 1;
    private static int totalData = 0;
    private final ExcelFileData excelFileData = ExcelFileData.getInstance();
    List<List<Object>> mainList = excelFileData.getExcelDataList();
    private NotificationCompat.Builder notification;
    private NotificationManagerCompat managerCompat;
    List<Object> designList;
    Context context;
    AtomicBoolean errorSet = new AtomicBoolean(false);
    public ExcelDesignStoreHelper(Context context) {
        this.context = context;
    }

    public void store() {
        createNotificationChannel(context);
        createNotification(mainList.size());

        for (int position = 0; position < mainList.size(); position++) {
            if (!errorSet.get()) {
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
                        stopNotification();
                    }
                    if (result.equals("Successfully")) {
                        updateNotification();
                    }
                });
            }
        }
    }



    private void createNotificationChannel(Context context) {
        CharSequence name = "Design Upload";
        String description = "Upload Files notification";
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
        channel.setDescription(description);
        NotificationManager manager = context.getSystemService(NotificationManager.class);
        manager.createNotificationChannel(channel);
    }

    private void createNotification(int size) {
        notification = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Upload")
                .setContentText("uploading..")
                .setProgress(size, 0, false)
                .setOngoing(true)
                .setSound(null);

        managerCompat = NotificationManagerCompat.from(context);
    }

    private void updateNotification() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.VIBRATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.VIBRATE}, 1);
        }
        totalData++;
        notification.setProgress(mainList.size(), totalData, false);
        managerCompat.notify(NOTIFICATION_ID, notification.build());

        if(totalData == mainList.size()) {
            notification.setContentTitle("Complete");
            notification.setOngoing(false);
            notification.setContentText(totalData+" Files Uploaded");
            notification.setProgress(0, 0, false);
            managerCompat.notify(NOTIFICATION_ID, notification.build());
        }
    }
    private void stopNotification() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.VIBRATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.VIBRATE}, 1);
        }
        notification.setContentTitle("InComplete");
        notification.setContentText("Design File uploading fail");
        notification.setOngoing(false);
        notification.setProgress(0, 0, false);
        managerCompat.notify(NOTIFICATION_ID, notification.build());
    }
}
