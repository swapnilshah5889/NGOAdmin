package com.example.ngoadmin.Models;

import android.graphics.Bitmap;

public class ImageGallery {

    int id;
    Bitmap imgBitmap;
    String uploadPath;

    public ImageGallery(int id, Bitmap imgBitmap, String uploadPath) {
        this.id = id;
        this.imgBitmap = imgBitmap;
        this.uploadPath = uploadPath;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Bitmap getImgBitmap() {
        return imgBitmap;
    }

    public void setImgBitmap(Bitmap imgBitmap) {
        this.imgBitmap = imgBitmap;
    }

    public String getUploadPath() {
        return uploadPath;
    }

    public void setUploadPath(String uploadPath) {
        this.uploadPath = uploadPath;
    }
}
