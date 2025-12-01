package com.example.myapss.utils;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.OpenableColumns;

import androidx.annotation.NonNull;

import com.example.myapss.models.FileUploadModel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class FileUtils {

    // ✅ Convert Uri to temporary File
    public static String getPath(Context context, Uri uri) throws Exception {
        // Get the display name
        Cursor returnCursor = context.getContentResolver().query(uri, null, null, null, null);
        String name = "temp_file";
        if (returnCursor != null && returnCursor.moveToFirst()) {
            int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
            if (nameIndex >= 0) name = returnCursor.getString(nameIndex);
            returnCursor.close();
        }

        // Copy content to temp file
        File file = new File(context.getCacheDir(), name);
        InputStream inputStream = context.getContentResolver().openInputStream(uri);
        FileOutputStream outputStream = new FileOutputStream(file);

        byte[] buf = new byte[1024];
        int len;
        if (inputStream != null) {
            while ((len = inputStream.read(buf)) > 0) {
                outputStream.write(buf, 0, len);
            }
            inputStream.close();
        }
        outputStream.close();
        return file.getAbsolutePath();
    }
}