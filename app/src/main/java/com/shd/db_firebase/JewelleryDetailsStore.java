package com.shd.db_firebase;

import android.net.Uri;
import android.util.Log;

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

    Uri img1,img2;
    String customerName,designCode,customerCode,tempCode,work_by,work_place,selectedDate,mainType,subType;
    String img1DownloadUrl,img2DownloadUrl;
    boolean status;
    float length,width,height,gold,diamond;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference reference;
    StorageMetadata metadata = new StorageMetadata.Builder()
            .setContentType("image/jpg")
            .build();

    public interface Result{
        void onResult(String result);
    }
    public JewelleryDetailsStore(Uri img1, Uri img2, String customerName, String designCode, String customerCode, String tempCode, String work_by, String work_place, String selectedDate, String mainType, String subType,boolean status, float length, float width, float height, float gold, float diamond) {
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
        this.status = status;
        this.length = length;
        this.width = width;
        this.height = height;
        this.gold = gold;
        this.diamond = diamond;
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
            }));
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
            }));
        }
    }

    public void storeJewelleryData(Result result)
    {
            JewelleryDesign design = new JewelleryDesign(img1DownloadUrl, img2DownloadUrl, customerName, designCode, customerCode, tempCode, work_by, work_place, selectedDate, mainType, subType, status, length, width, height, gold, diamond);
            db.collection("jewellery").document().set(design.jewelleryDetails()).addOnCompleteListener(task -> {
                if(task.isSuccessful())
                {
                    if(!designCode.isEmpty()) storeDesignCode();
                }
            }).addOnFailureListener(e -> result.onResult("Design Not Store"));
    }

    private void storeDesignCode() {
        Log.d("values","design store");

        db.collection("jewelleryDesignCode").get().addOnCompleteListener(task -> {
            if(task.getResult().isEmpty()) // create collection and list
            {
                List<String> design = new ArrayList<>();
                design.add(designCode);
                Map<String,Object> map = new HashMap<>();
                map.put("Codes",designCode);
                db.collection("jewelleryDesignCode").document("DesignCodes").set(map);
            }else // update collection and update collection
            {
                db.collection("jewelleryDesignCode").document("DesignCodes")
                        .update("Codes", FieldValue.arrayUnion(designCode));
            }
        });
        Log.d("values","design code store");
    }
}
