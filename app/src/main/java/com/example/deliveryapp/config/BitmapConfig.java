package com.example.deliveryapp.config;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import io.paperdb.Paper;
import java.io.ByteArrayOutputStream;

/**
 *  A class providing help to convert a bitmap to byte array and vice-versa
 *  To store the photo snap of the orders, in the local database, which is PaperDb here in this application.
 */

public class BitmapConfig {

    // Convert Bitmap to Byte Array
    public byte[] bitmapToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }

    // Save Bitmap to PaperDb
    public void saveBitmapToPaperDb(Bitmap bitmap, String key) {
        byte[] byteArray = bitmapToByteArray(bitmap);
        Paper.book().write(key, byteArray);
    }

    // Retrieve Bitmap from PaperDb
    public Bitmap getBitmapFromPaperDb(String key) {
        byte[] byteArray = Paper.book().read(key);
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
    }
}
