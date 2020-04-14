package com.foi_bois.zisprojekt.lib;

import android.graphics.Bitmap;
import android.util.Base64;
import android.graphics.Matrix;

import java.io.ByteArrayOutputStream;

public class ImgHelper {
    private static int newWidth = 64;
    private static int newHeight = 64;

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
