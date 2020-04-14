package com.foi_bois.zisprojekt.lib;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;

import com.foi_bois.zisprojekt.R;
import com.google.android.gms.maps.model.Marker;

import java.util.UUID;

public class Helper {
    public static float izracunajUdaljenost(Marker A, Marker B){
        if(A == null || B == null) //JIC
            return -1.0f;

        float[] udaljenost = new float[1];
        Location.distanceBetween(
                A.getPosition().latitude, A.getPosition().longitude,
                B.getPosition().latitude, B.getPosition().longitude, udaljenost);
        return udaljenost[0] / 1000; //pretvori u km
    }


    private static String sID = null;
    private final static String ID_KEY = "ID_KEY";

    public synchronized static String id(Context context) {
        if (sID == null) {
            SharedPreferences pref = context.getSharedPreferences(
                    context.getResources().getString(R.string.shared_perf_key), 0);

            sID = pref.getString(ID_KEY, "");

            if (sID == "")
                sID = generirajISpremiID(pref);

        }

        return sID;
    }


    private synchronized static String generirajISpremiID(SharedPreferences pref) {
        String id = UUID.randomUUID().toString();
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(ID_KEY, id);
        editor.apply();

        return id;
    }
}
