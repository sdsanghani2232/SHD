package com.shd.repository.storedata;

import android.net.Uri;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.shd.model.JewelleryDesign;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JewelleryDetailsStore {

    private final Uri img1,img2;
    private final String customerName,designCode,customerCode,tempCode,work_by,work_place,selectedDate,mainType,subType,length,width,height,gold,diamond;
    String img1DownloadUrl,img2DownloadUrl;
    final boolean status;

    final FirebaseFirestore db = FirebaseFirestore.getInstance();
    final FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference reference;
    final StorageMetadata metadata = new StorageMetadata.Builder()
            .setContentType("image/jpg")
            .build();

    public interface Result{
        void onResult(String result);
    }


    public JewelleryDetailsStore(Uri img1, Uri img2, String customerName, String designCode, boolean status, String customerCode, String tempCode, String work_by, String work_place, String selectedDate, String mainType, String subType, String length, String width, String height, String gold, String diamond) {
        this.img1 = img1;
        this.img2 = img2;
        this.customerName = customerName;
        this.designCode = designCode;
        this.customerCode = customerCode;
        this.tempCode = tempCode;
        this.work_by = work_by;
        this.work_place = work_place;
        this.selectedDate = selectedDate;
        this.mainType = mainType;
        this.subType = subType;
        this.length = length;
        this.width = width;
        this.height = height;
        this.gold = gold;
        this.diamond = diamond;
        this.status = status;
    }

    public void StoreJewelleryImg(Result result)
    {
        StoreImg1(result);
    }

    private void StoreImg1(Result result) {
        if(img1.toString().isEmpty())
        {
            img1DownloadUrl = "";
            StoreImg2(result);
        }else {
            String imgName = designCode+customerCode+tempCode+"Img1";
            reference = storage.getReference().child("jewellery/" + selectedDate+"/"+imgName);
            reference.putFile(img1,metadata).addOnSuccessListener(taskSnapshot -> reference.getDownloadUrl().addOnSuccessListener(uri -> {
                img1DownloadUrl = uri.toString();
                StoreImg2(result);
            })).addOnFailureListener(e -> result.onResult("Img Not Stored"));
        }
    }

    private void StoreImg2(Result result) {
        if(img2.toString().isEmpty())
        {
            img2DownloadUrl = "";
            storeJewelleryData(result);
        }else {
            String imgName = designCode+customerCode+tempCode+"Img2";
            reference = storage.getReference().child("jewellery/" + selectedDate+"/"+imgName);
            reference.putFile(img2,metadata).addOnSuccessListener(taskSnapshot -> reference.getDownloadUrl().addOnSuccessListener(uri -> {
                img2DownloadUrl = uri.toString();
                storeJewelleryData(result);
            })).addOnFailureListener(e -> result.onResult("Img Not Stored"));
        }
    }

    public void storeJewelleryData(Result result)
    {
            JewelleryDesign design = new JewelleryDesign(img1DownloadUrl, img2DownloadUrl, customerName, designCode, customerCode, tempCode, work_by, work_place, selectedDate, mainType, subType, status, length, width, height, gold, diamond);
            db.collection("jewellery").document().set(design.jewelleryDetails()).addOnCompleteListener(task -> {
                if(task.isSuccessful())
                {
                    if(!designCode.isEmpty()) storeDesignCode(result);
                    if(!tempCode.isEmpty()) storeTempCode(result);
                }
            }).addOnFailureListener(e -> result.onResult("Design Not Store"));
    }

    private void storeDesignCode(Result result) {
        db.collection("jewelleryCodes").document("designCodes").get().addOnCompleteListener(task -> {
            if(!task.getResult().exists()) // create document and list
            {
                List<String> design = new ArrayList<>();
                design.add(designCode);
                Map<String,Object> map = new HashMap<>();
                map.put("codes",design);
                db.collection("jewelleryCodes").document("designCodes").set(map)
                        .addOnSuccessListener(command -> result.onResult("Successfully"))
                        .addOnFailureListener(e -> result.onResult("DesignCode Not Stored"));
            }else // update list
            {
                db.collection("jewelleryCodes").document("designCodes")
                        .update("codes", FieldValue.arrayUnion(designCode))
                        .addOnSuccessListener(command -> result.onResult("Successfully"))
                        .addOnFailureListener(e -> result.onResult("DesignCode Not Stored"));
            }
        });
    }
    private void storeTempCode(Result result) {
        db.collection("jewelleryCodes").document("tempCodes").get().addOnCompleteListener(task -> {
            if(!task.getResult().exists()) // create document and list
            {
                List<String> design = new ArrayList<>();
                design.add(tempCode);
                Map<String,Object> map = new HashMap<>();
                map.put("codes",design);
                db.collection("jewelleryCodes").document("tempCodes").set(map)
                        .addOnSuccessListener(command -> result.onResult("Successfully"))
                        .addOnFailureListener(e -> result.onResult("TempCode Not Stored"));
            }else // update list
            {
                db.collection("jewelleryCodes").document("tempCodes")
                        .update("codes", FieldValue.arrayUnion(tempCode))
                        .addOnSuccessListener(command -> result.onResult("Successfully"))
                        .addOnFailureListener(e -> result.onResult("TempCode Not Stored"));
            }
        });
    }



}
