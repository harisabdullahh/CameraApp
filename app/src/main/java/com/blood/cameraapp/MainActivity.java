package com.blood.cameraapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {


    // Define the pic id
    private static final int pic_id = 123;
    // Define the button and imageview type variable
    Button camera_open_id;
    ImageView click_image_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // By ID we can get each component which id is assigned in XML file get Buttons and imageview.
        camera_open_id = findViewById(R.id.camera_button);
        click_image_id = findViewById(R.id.click_image);

        // Camera_open button is for open the camera and add the setOnClickListener in this button
        camera_open_id.setOnClickListener(v -> {
            // Create the camera_intent ACTION_IMAGE_CAPTURE it will open the camera for capture the image
            Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            camera_intent.putExtra("android.intent.extras.CAMERA_FACING", 1);

            // Start the activity with camera_intent, and request pic id
            startActivityForResult(camera_intent, pic_id);
        });
    }

    private void createImageFile(Bitmap photo) {
        File externalCacheDir = getExternalCacheDir();

        String filename = "image_" + System.currentTimeMillis() + ".png";
        Log.d("Tracking: Filename:", "filename: " + filename);

        File imageFile = new File(externalCacheDir, filename);
        Log.d("Tracking: ex:", "-> " + String.valueOf(externalCacheDir));

        OutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(imageFile);
            // Choose a format and quality for the image, for example, PNG and 100
            photo.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };

//    private void uploadImage(Bitmap photo) {
//        UploadApi uploadApi = RetrofitClient.getClient().create(UploadApi.class);
//
//        // Convert Bitmap to byte array
//        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//        photo.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
//        byte[] imageBytes = byteArrayOutputStream.toByteArray();
//
//        // Create a RequestBody from the byte array
//        RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("image/jpeg"), imageBytes);
//
//        // Create a MultipartBody.Part from the RequestBody
//        MultipartBody.Part imagePart = MultipartBody.Part.createFormData("image", "image.jpg", requestBody);
//
//        // Make the API call
//        Call<Void> call = uploadApi.uploadImage(imagePart);
//        call.enqueue(new Callback<Void>() {
//            @Override
//            public void onResponse(Call<Void> call, Response<Void> response) {
//                // Handle successful response
//                if (response.isSuccessful()) {
//
//                    Log.d("Tracking: ", "Success");
//                } else {
//                    Log.d("Tracking: ", "Error");
//                }
//            }
//
//            @Override
//            public void onFailure(Call<Void> call, Throwable t) {
//                // Handle failure
//                // You may want to show an error message or perform some other action
//            }
//        });
//    }

    // This method will help to retrieve the image
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Match the request 'pic id with requestCode
        if (requestCode == pic_id) {
            // BitMap is data structure of image file which store the image in memory
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            // Set the image in imageview for display
            click_image_id.setImageBitmap(photo);
            createImageFile(photo);

//            Log.d("Tracking: ", "Uploading");
//            uploadImage(photo);


        }
    }
}