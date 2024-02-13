package com.shd.viewmodes;

import android.graphics.Bitmap;

public class ExcelImgData {
    int row ,col;
//    byte[] img ;
    Bitmap img;

    public ExcelImgData(int row, int col, Bitmap img) {
        this.row = row;
        this.col = col;
        this.img = img;

    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public Bitmap getBitmap() {
        return img;
    }
}
