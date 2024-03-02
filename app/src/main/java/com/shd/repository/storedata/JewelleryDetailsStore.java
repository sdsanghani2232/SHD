package com.shd.repository.storedata;

import android.graphics.Bitmap;
import android.net.Uri;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.shd.model.JewelleryDesign;
import com.shd.repository.readdata.DesignCodeList;
import com.shd.repository.readdata.TempCodeList;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JewelleryDetailsStore {

    private Uri img1 = null,img2 = null;
    private Bitmap img1Bitmap  = null ,img2Bitmap = null;
    private final String customerName,designCode,customerCode,tempCode,work_by,work_place,selectedDate,mainType,subType,length,width,height,gold,diamond;
    String img1DownloadUrl,img2DownloadUrl;
    final boolean status;
    final FirebaseFirestore db = FirebaseFirestore.getInstance();
    final FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference reference;
    final StorageMetadata metadata = new StorageMetadata.Builder()
            .setContentType("image/jpg")
            .build();

    final DesignCodeList dcList = DesignCodeList.getInstance();
    final TempCodeList tcList = TempCodeList.getInstance();
    public interface Result{
        void onResult(String result);
    }


    public JewelleryDetailsStore(Uri img1, Uri img2, String customerName, String designCode, boolean status, String customerCode, String tempCode, String work_by, String work_place, String selectedDate, String mainType, String subType, String length, String width, String height, String gold, String diamond) {
        this.img1 = img1;
        this.img2 = img2;
        this.customerName = customerName;
        this.designCode = designCode;
        this.status = status;
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
    }

    public JewelleryDetailsStore(Bitmap img1Bitmap, Bitmap img2Bitmap, String customerName, String designCode, String customerCode, String tempCode, String work_by, String work_place, String selectedDate, String mainType, String subType, boolean status, String length, String width, String height, String gold, String diamond)
    {
        this.img1Bitmap = img1Bitmap;
        this.img2Bitmap = img2Bitmap;
        this.customerName = customerName;
        this.designCode = designCode;
        this.status = status;
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
    }

    public void storeFormJewelleryImg(Result result)
    {
        storeFormImg1(result);
    }

    public void storeExcelJewelleryImg(Result result){ storeExcelImg1(result);}

    private void storeFormImg1(Result result) {
        if(img1.toString().isEmpty())
        {
            img1DownloadUrl = "";
            storeFromImg2(result);
        }else {
            String imgName = designCode+customerCode+tempCode+"Img1";
            reference = storage.getReference().child("jewellery/" + selectedDate+"/"+imgName);
            reference.putFile(img1,metadata).addOnSuccessListener(taskSnapshot -> reference.getDownloadUrl().addOnSuccessListener(uri -> {
                img1DownloadUrl = uri.toString();
                storeFromImg2(result);
            })).addOnFailureListener(e -> result.onResult("Img Not Stored"));
        }
    }

    private void storeFromImg2(Result result) {
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

    private void storeExcelImg1(Result result) {
        if(img1Bitmap == null)
        {
            img1DownloadUrl = "";
            storeExcelImg2(result);
        }else {
            Bitmap bitmap = img1Bitmap;
            ByteArrayOutputStream imgBytes = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, imgBytes);
            byte[] data = imgBytes.toByteArray();

            String imgName = designCode+customerCode+tempCode+"Img1";
            reference = storage.getReference().child("jewellery/" + selectedDate+"/"+imgName);
            reference.putBytes(data,metadata).addOnSuccessListener(taskSnapshot -> reference.getDownloadUrl().addOnSuccessListener(uri -> {
                img1DownloadUrl = uri.toString();
                storeExcelImg2(result);
            })).addOnFailureListener(e -> result.onResult("error"));
        }
    }

    private void storeExcelImg2(Result result) {
        if(img2Bitmap == null)
        {
            img2DownloadUrl = "";
            storeJewelleryData(result);
        }else {
            Bitmap bitmap = img2Bitmap;
            ByteArrayOutputStream imgBytes = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, imgBytes);
            byte[] data = imgBytes.toByteArray();

            String imgName = designCode+customerCode+tempCode+"Img2";
            reference = storage.getReference().child("jewellery/" + selectedDate+"/"+imgName);
            reference.putBytes(data,metadata).addOnSuccessListener(taskSnapshot -> reference.getDownloadUrl().addOnSuccessListener(uri -> {
                img2DownloadUrl = uri.toString();
                storeJewelleryData(result);
            })).addOnFailureListener(e -> result.onResult("error"));
        }
    }

    public void storeJewelleryData(Result result)
    {
            JewelleryDesign design = new JewelleryDesign(img1DownloadUrl, img2DownloadUrl, customerName, designCode, customerCode, tempCode, work_by, work_place, selectedDate, mainType, subType, status, length, width, height, gold, diamond);
            db.collection("jewellery").document().set(design.jewelleryDetails()).addOnCompleteListener(task -> {
                if(task.isSuccessful())
                {
                    if (!designCode.isEmpty()) storeDesignCode(result);
                    if (!tempCode.isEmpty()) storeTempCode(result);
                    result.onResult("Successfully");
                }
            }).addOnFailureListener(e -> result.onResult("Design Not Store"));
    }



    private void storeDesignCode(Result result) {

        if(!dcList.documentExits())
        {
            List<String> design = new ArrayList<>();
            design.add(designCode);
            Map<String,Object> map = new HashMap<>();
            map.put("codes",design);
            db.collection("jewelleryCodes").document("designCodes").set(map)
                    .addOnFailureListener(e -> result.onResult("DesignCode Not Stored"));
        }
        else {
            db.collection("jewelleryCodes").document("designCodes")
                    .update("codes", FieldValue.arrayUnion(designCode))
                    .addOnFailureListener(e -> result.onResult("DesignCode Not Stored"));
        }
    }
    private void storeTempCode(Result result) {
        if(!tcList.documentExits())
        {
            List<String> design = new ArrayList<>();
            design.add(tempCode);
            Map<String,Object> map = new HashMap<>();
            map.put("codes",design);
            db.collection("jewelleryCodes").document("tempCodes").set(map)
                    .addOnFailureListener(e -> result.onResult("TempCode Not Stored"));
        }else {
            db.collection("jewelleryCodes").document("tempCodes")
                    .update("codes", FieldValue.arrayUnion(tempCode))
                    .addOnFailureListener(e -> result.onResult("TempCode Not Stored"));
        }
    }
}
