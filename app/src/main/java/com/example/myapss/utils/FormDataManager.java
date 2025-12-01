package com.example.myapss.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.myapss.models.InspectionData;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Centralized Form Data Manager (Singleton)
 * Stores ALL Page2 data including images & inputs
 */
public class FormDataManager {

    private static FormDataManager instance;
    private final SharedPreferences preferences;
    private final Gson gson;

    private static final String PREF_NAME = "form_data_prefs";
    private static final String PAGE1_DATA_KEY = "page1_data";
    private static final String PAGE2_DATA_KEY = "page2_data";

    private FormDataManager(Context context) {
        this.preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        this.gson = new Gson();
    }

    public static synchronized FormDataManager getInstance(Context context) {
        if (instance == null) {
            instance = new FormDataManager(context.getApplicationContext());
        }
        return instance;
    }

    // ===============================
    // ✅ SAVE PAGE 1 DATA
    // ===============================
    public void savePage1Data(InspectionData data) {
        String json = gson.toJson(data);
        preferences.edit().putString(PAGE1_DATA_KEY, json).apply();
    }

    // ===============================
    // ✅ GET PAGE 1 DATA
    // ===============================
    public InspectionData getPage1Data() {
        String json = preferences.getString(PAGE1_DATA_KEY, null);
        if (json == null) return null;
        return gson.fromJson(json, InspectionData.class);
    }

    // ===============================
    // ✅ SAVE PAGE 2 DATA
    // ===============================
    public void savePage2Data(Page2Data data) {
        String json = gson.toJson(data);
        preferences.edit().putString(PAGE2_DATA_KEY, json).apply();
    }

    // ===============================
    // ✅ GET PAGE 2 DATA
    // ===============================
    public Page2Data getPage2Data() {
        String json = preferences.getString(PAGE2_DATA_KEY, null);
        if (json == null) return new Page2Data();
        return gson.fromJson(json, Page2Data.class);
    }

    // ===============================
    // ✅ CLEAR ALL DATA
    // ===============================
    public void clearAllData() {
        preferences.edit().clear().apply();
    }

    // ===============================
    // ✅ PAGE 2 DATA MODEL
    // ===============================
    public static class Page2Data {

        // Switches
        public boolean clearReadability;
        public boolean dimFunction;
        public boolean buttonsWorking;

        public boolean batteryReplacement;
        public String batteryExpiryMonth;
        public String batteryExpiryYear;
        public String dauCondition;

        // Capsule + Beacon
        public String capsuleDescription;
        public String beaconModel;
        public String beaconBatteryMonth;
        public String beaconBatteryYear;
        public String beaconSerial;
        public String beaconVoltage;

        // Float Free / HRU
        public String floatFreeModel;
        public String epribSerial;
        public String hruModel;
        public boolean hruReplace;
        public String hruBatteryMonth;
        public String hruBatteryYear;

        // RVI
        public boolean rviCables;
        public String rviPower;
        public String rviModel;

        // ✅ ✅ ✅ FILE UPLOADS (CORRECT TYPE)
        public Map<String, List<String>> uploadedFiles = new HashMap<>();

        public Page2Data() {}
    }
}