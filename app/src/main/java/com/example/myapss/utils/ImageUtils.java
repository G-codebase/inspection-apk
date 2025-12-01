package com.example.myapss.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;

import java.io.File;
import java.io.FileOutputStream;

public class ImageUtils {

    public static Uri saveTempBitmap(Context context, Bitmap bitmap) {
        try {
            File file = new File(context.getCacheDir(),
                    "cam_" + System.currentTimeMillis() + ".jpg");

            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fos);
            fos.close();

            return Uri.fromFile(file);

        } catch (Exception e) {
            return null;
        }
    }
}