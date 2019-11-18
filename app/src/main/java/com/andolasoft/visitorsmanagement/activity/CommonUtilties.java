package com.andolasoft.visitorsmanagement.activity;

import android.graphics.Bitmap;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;

public class CommonUtilties {
    public static String InProgress = "0";
    public static String Completed = "1";
    public static String Pending = "2";
    public static String Employeee = "1";
    public static String Security = "2";

    public String photo_store_in_local(Bitmap imageToSave, String fileName) {

        File direct = new File(Environment.getExternalStorageDirectory() + "/VisitorImage");

        if (!direct.exists()) {
            File wallpaperDirectory = new File("/sdcard/VisitorImage/");
            wallpaperDirectory.mkdirs();
        }

        File file = new File("/sdcard/VisitorImage/", fileName+".jpg");
        if (file.exists()) {
            file.delete();
        }
        String path = file.getAbsolutePath();
        try {
            FileOutputStream out = new FileOutputStream(file);
            imageToSave.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return path;
    }
}
