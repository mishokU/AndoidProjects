package com.example.polyfinderv2;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;

import java.io.ByteArrayOutputStream;
import java.io.File;


public class BitmapHelper {

    private String action;
    private String directory_to;
    private Bitmap originalBitmap;
    private float newWidth;
    private float newHeight;

    public BitmapHelper(){

    }

    public BitmapHelper(String action, String directory_to) {
        this.action = action;
        this.directory_to = directory_to;
    }

    public Intent createPath() {

        Intent photoPickerIntent = new Intent(action);

        File directoryForImage = Environment.getExternalStoragePublicDirectory(directory_to);
        String pictureDirectoryPath = directoryForImage.getPath();
        //Bitmap bitmap = BitmapHelper.decodeSampledBitmapFromResource(pictureDirectoryPath);
        Uri data = Uri.parse(pictureDirectoryPath);

        photoPickerIntent.setDataAndType(data,"image/*");

        return photoPickerIntent;
    }

    public void createBitmap(byte[] bytes){
        originalBitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    public void createResizedBitmap(int width, int height){
        float epsilonWidth = originalBitmap.getWidth() / (float)width;
        float epsilonHeight = originalBitmap.getHeight() / (float)height;

        newWidth = originalBitmap.getWidth() / epsilonWidth;
        newHeight = originalBitmap.getHeight() / epsilonHeight;
    }

    public Bitmap getOriginalBitmap(){
        return originalBitmap;
    }

    public Bitmap getResizedBitmap(){
        return Bitmap.createScaledBitmap(originalBitmap, (int)newWidth, (int)newHeight, false);
    }

    public byte[] getByteArray(Bitmap bitmap){
        ByteArrayOutputStream bStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, bStream);
        return bStream.toByteArray();
    }
}
