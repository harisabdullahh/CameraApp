package com.blood.cameraapp;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;

public class ImageUtils {


    public static Uri saveImageToMediaStore(Context context, String displayName, Bitmap bitmap) {
        Uri imageContentUri = null;

        // Check Android Version
        Uri imageCollections;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            imageCollections = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY);
        } else {
            imageCollections = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        }

        // Create Content Values
        ContentValues imageDetails = new ContentValues();
        imageDetails.put(MediaStore.Images.Media.DISPLAY_NAME, displayName);
        imageDetails.put(MediaStore.Images.Media.MIME_TYPE, "image/png");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            imageDetails.put(MediaStore.Images.Media.IS_PENDING, 1);
        }

        // Insert Image Into MediaStore
        ContentResolver resolver = context.getApplicationContext().getContentResolver();
        imageContentUri = resolver.insert(imageCollections, imageDetails);
        if (imageContentUri == null) {
            return null;
        }

        try {
            // Write Bitmap to Output Stream
            try (OutputStream os = resolver.openOutputStream(imageContentUri, "w")) {
                if (os != null) {
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
                }
            }

            // Update IS_PENDING for Android Q and Above
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                imageDetails.clear();
                imageDetails.put(MediaStore.Images.Media.IS_PENDING, 0);
                resolver.update(imageContentUri, imageDetails, null, null);
            }

        } catch (IOException e) {
            // Some legacy devices won't create directory for the Uri if dir not exist, resulting in
            // a FileNotFoundException. To resolve this issue, we should use the File API to save the
            // image, which allows us to create the directory ourselves.
            return null;
        }

        // Return Content URI
        return imageContentUri;
    }
}
