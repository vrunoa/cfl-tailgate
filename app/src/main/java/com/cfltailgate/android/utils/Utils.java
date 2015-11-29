package com.cfltailgate.android.utils;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;



import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.cfltailgate.android.R;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.List;

/**
 * Created by vruno on 9/2/15.
 */
public abstract class Utils {

    public static void Toast(Context context, String text) {
        Toast toast = Toast.makeText(context, text, Toast.LENGTH_LONG);
        toast.show();
    }

    public static void Toast(Context context, int resourceID) {
        Toast toast = Toast.makeText(context, resourceID, Toast.LENGTH_LONG);
        toast.show();
    }

    public static void Toast(Context context, String text, int length) {
        Toast toast = Toast.makeText(context, text, length);
        toast.show();
    }

    public static void Toast(Context context, int resourceID, int length) {
        Toast toast = Toast.makeText(context, resourceID, length);
        toast.show();
    }

    public static Dialog Preloader(Context context, String title, String message) {
        Dialog dialog = ProgressDialog.show(context, title, message, true);
        dialog.setCancelable(false);
        return dialog;
    }

    public static boolean isConnected(Context context) {
        ConnectivityManager conMgr = (ConnectivityManager) context.getSystemService (Context.CONNECTIVITY_SERVICE);
        if (conMgr.getActiveNetworkInfo() != null
                && conMgr.getActiveNetworkInfo().isAvailable()
                && conMgr.getActiveNetworkInfo().isConnected()) return true;

        return false;
    }

    public static byte[] GetImageBytes(Context context, Uri imageUri) throws IOException {
        InputStream inputStream = context.getContentResolver().openInputStream(imageUri);
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }

    public static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    public static void Share(final Context context, String subject, String body) {

        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, subject);
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, body);

        Intent openInChooser = Intent.createChooser(sharingIntent, "via @cfltailgate");
        openInChooser.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(openInChooser);
    }

    public static byte[] ResizeImage(Context context, Uri imageURI, int max) {
        try {
            Bitmap scaledphoto = Utils.ScaleBitmap(context, imageURI, max);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            scaledphoto.compress(Bitmap.CompressFormat.JPEG, 80, stream);
            byte[] bitmapdata = stream.toByteArray();
            return bitmapdata;
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] ResizeImage(Context context, Bitmap bmp, int max) {
        try {
            Bitmap scaledPhoto = Utils.ScaleBitmap(context, bmp, max);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.JPEG, 80, stream);
            byte[] bitmapdata = stream.toByteArray();
            return bitmapdata;
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static Bitmap ScaleBitmap(Context context, Uri imageURI, int max) {
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), imageURI);
            int w = bitmap.getWidth(), h = bitmap.getHeight();
            int width, height;
            if (w > max || h > max) {
                if (w == h) {
                    width = height = max;
                } else if (w < h) {
                    height = max;
                    width = max * w / h;
                } else {
                    width = max;
                    height = max * h / w;
                }
            } else {
                width = w;
                height = h;
            }
            //Bitmap bitmap = ((BitmapDrawable) commentImage.getDrawable()).getBitmap();
            Bitmap scaledphoto = Bitmap.createScaledBitmap(bitmap, width, height, true);
            return scaledphoto;

        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static Bitmap ScaleBitmap(Context context, Bitmap bitmap, int max) {
        try {

            int w = bitmap.getWidth(), h = bitmap.getHeight();
            int width, height;
            if (w > max || h > max) {
                if (w == h) {
                    width = height = max;
                } else if (w < h) {
                    height = max;
                    width = max * w / h;
                } else {
                    width = max;
                    height = max * h / w;
                }
            } else {
                width = w;
                height = h;
            }
            //Bitmap bitmap = ((BitmapDrawable) commentImage.getDrawable()).getBitmap();
            Bitmap scaledphoto = Bitmap.createScaledBitmap(bitmap, width, height, true);
            return scaledphoto;

        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

}

