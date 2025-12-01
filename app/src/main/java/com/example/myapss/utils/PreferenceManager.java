package com.example.myapss.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceManager {

    private static final String PREF_NAME = "InspectionAPKPrefs";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    private static final String KEY_USER_EMAIL = "userEmail";
    private static final String KEY_USER_NAME = "userName";

    private static SharedPreferences getPreferences(Context context) {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public static void saveLoginState(Context context, String email) {
        SharedPreferences prefs = getPreferences(context);
        prefs.edit()
                .putBoolean(KEY_IS_LOGGED_IN, true)
                .putString(KEY_USER_EMAIL, email)
                .apply();
    }

    public static boolean isLoggedIn(Context context) {
        return getPreferences(context).getBoolean(KEY_IS_LOGGED_IN, false);
    }

    public static String getUserEmail(Context context) {
        return getPreferences(context).getString(KEY_USER_EMAIL, "");
    }

    public static void logout(Context context) {
        SharedPreferences prefs = getPreferences(context);
        prefs.edit()
                .putBoolean(KEY_IS_LOGGED_IN, false)
                .putString(KEY_USER_EMAIL, "")
                .apply();
    }

    public static void saveDraft(Context context, String inspectionData) {
        getPreferences(context).edit()
                .putString("currentDraft", inspectionData)
                .apply();
    }

    public static String getDraft(Context context) {
        return getPreferences(context).getString("currentDraft", "");
    }
}
