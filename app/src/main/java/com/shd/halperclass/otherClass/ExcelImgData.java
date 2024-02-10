package com.shd.halperclass.otherClass;

public class ExcelImgData {
    int row ,col;
    byte[] img ;

    public ExcelImgData(int row, int col, byte[] img) {
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

    public byte[] getImg() {
        return img;
    }
}
