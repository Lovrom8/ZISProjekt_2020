package com.foi_bois.zisprojekt.lib;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Patterns;

import androidx.core.util.Pair;

import com.foi_bois.zisprojekt.R;

import java.util.regex.Pattern;

public class AuthHelper {
    public static boolean isEmailValid(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }

    public static boolean isRememberMeChecked(Context context){
        SharedPreferences prefs = context.getSharedPreferences(
                context.getResources().getString(R.string.shared_perf_key), 0);

        return prefs.getBoolean("rememberMeChecked", false);
    }

    public static void saveLoginCredsToPerfs(Context context, String email, String pass){
        SharedPreferences prefs = context.getSharedPreferences(
                context.getResources().getString(R.string.shared_perf_key), 0);

        SharedPreferences.Editor prefsEditor = prefs.edit();
        prefsEditor.putBoolean("rememberMeChecked", true);
        prefsEditor.putString("email", email);
        prefsEditor.putString("pass", pass);
        prefsEditor.apply();
    }

    public static void removeSavedLoginCreds(Context context){
        SharedPreferences prefs = context.getSharedPreferences(
                context.getResources().getString(R.string.shared_perf_key), 0);

        SharedPreferences.Editor prefsEditor = prefs.edit();
        prefsEditor.putBoolean("rememberMeChecked", false);
        prefsEditor.remove("email");
        prefsEditor.remove("pass");
        prefsEditor.apply();
    }

    public static Pair<String, String> getSavedLoginCreds(Context context){
        SharedPreferences prefs = context.getSharedPreferences(
                context.getResources().getString(R.string.shared_perf_key), 0);

        return new Pair<>(prefs.getString("email", null), prefs.getString("pass", null));
    }
}
