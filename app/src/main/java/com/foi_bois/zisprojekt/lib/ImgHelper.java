package com.foi_bois.zisprojekt.lib;

import android.app.IntentService;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.util.Base64;
import android.graphics.Matrix;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;

public class ImgHelper {
    private static int newWidth = 250;
    private static int newHeight = 250;

    public static String getEncoded64ImageStringFromBitmap(Bitmap bitmap) {
        String enc = "";

        try{
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
            enc  = Base64.encodeToString(stream.toByteArray(), Base64.NO_WRAP);
        }catch(Exception ex){
            ex.printStackTrace();
        }

        return enc;
    }

    public static Bitmap getImgFromBase64String(String base64Img){
        byte[] decodedString = Base64.decode(base64Img.replace("\n", ""), Base64.URL_SAFE);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }

    public static Bitmap getSmallImg(Bitmap bm) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;

        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);

        Bitmap resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);
        bm.recycle();
        return resizedBitmap;
    }
}
