package com.foi_bois.zisprojekt.lib;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class PermsHelper {
    public static boolean checkPerms(Activity activity, String perm) {
        if (ContextCompat.checkSelfPermission(activity, perm) ==
                PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            requestPerms(activity, perm);
            return false;
        }
    }

    public static void requestPerms(Activity activity, String perm){
        ActivityCompat.requestPermissions(activity, new String[]{ perm }, 1);
    }
}
