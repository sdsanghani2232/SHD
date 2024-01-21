package com.shd.model;

import android.net.Uri;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class JewelleryDesign {
    String img1,img2,customerName,designCode,customerCode,tempCode,work_by,work_place,selectedDate,mainType,subType;
    boolean status;
    float length,width,height,gold,diamond;

    public JewelleryDesign(String img1, String img2, String customerName, String designCode, String customerCode, String tempCode, String work_by, String work_place, String selectedDate, String mainType, String subType, boolean status, float length, float width, float height, float gold, float diamond) {
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

    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    public String getTempCode() {
        return tempCode;
    }

    public void setTempCode(String tempCode) {
        this.tempCode = tempCode;
    }

    public String getImg1() {
        return img1;
    }

    public void setImg1(String img1) {
        this.img1 = img1;
    }

    public String getImg2() {
        return img2;
    }

    public void setImg2(String img2) {
        this.img2 = img2;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getDesignCode() {
        return designCode;
    }

    public void setDesignCode(String designCode) {
        this.designCode = designCode;
    }

    public String getWork_by() {
        return work_by;
    }

    public void setWork_by(String work_by) {
        this.work_by = work_by;
    }

    public String getWork_place() {
        return work_place;
    }

    public void setWork_place(String work_place) {
        this.work_place = work_place;
    }

    public String getSelectedDate() {
        return selectedDate;
    }

    public void setSelectedDate(String selectedDate) {
        this.selectedDate = selectedDate;
    }

    public String getMainType() {
        return mainType;
    }

    public void setMainType(String mainType) {
        this.mainType = mainType;
    }

    public String getSubType() {
        return subType;
    }

    public void setSubType(String subType) {
        this.subType = subType;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public float getLength() {
        return length;
    }

    public void setLength(float length) {
        this.length = length;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public float getGold() {
        return gold;
    }

    public void setGold(float gold) {
        this.gold = gold;
    }

    public float getDiamond() {
        return diamond;
    }

    public void setDiamond(float diamond) {
        this.diamond = diamond;
    }

    public Map<String, Object> jewelleryDetails()
    {
        Map<String,Object> jewellery = new HashMap<>();
        jewellery.put("img1",img1);
        jewellery.put("img2",img2);
        jewellery.put("designCode",designCode);
        jewellery.put("status",status);
        jewellery.put("customerName",customerName);
        jewellery.put("workBy",work_by);
        jewellery.put("workPlace",work_place);
        jewellery.put("date",selectedDate);
        jewellery.put("mainType",mainType);
        jewellery.put("subType",subType);
        jewellery.put("goldWeight",gold);
        jewellery.put("diamondWeight",diamond);
        jewellery.put("length",length);
        jewellery.put("width",width);
        jewellery.put("height",height);
        return jewellery;
    }
}
