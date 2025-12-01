package com.example.myapss.models;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InspectionData implements Serializable {
    private static final long serialVersionUID = 1L;

    // ---------- PAGE 1 ----------
    public String engineerName;
    public String vesselName;
    public String location;
    public String vesselFlag;
    public String mainSerialNumber;
    public String inspectionDate;
    public String imoNumber;
    public String mmsiNumber;
    public String vesselClass;
    public String vdrMake;
    public String dateTime;

    // ---------- PAGE 2 : BCP ----------
    public boolean clearReadability;
    public boolean dimFunction;
    public boolean buttonsWorking;

    // ---------- PAGE 2 : DAU ----------
    public boolean batteryReplacement;
    public String batteryExpiryYear;
    public String batteryExpiryMonth;
    public String dauCondition;

    // ---------- PAGE 2 : FIXED CAPSULE ----------
    public String capsuleDescription;
    public String beaconModel;
    public String beaconSerial;
    public String beaconVoltage;
    public String beaconBatteryYear;
    public String beaconBatteryMonth;

    // ---------- PAGE 2 : FLOAT FREE ----------
    public String floatFreeModel;
    public String epribSerial;
    public String hruModel;
    public boolean hruReplace;
    public String hruBatteryYear;
    public String hruBatteryMonth;

    // ---------- PAGE 2 : RVI ----------
    public boolean rviCables;
    public String rviPower;
    public String rviModel;

    // ---------- ✅ PAGE 3 : VIDEO ----------
    public String xBandSourceChannel;
    public String xBandSourceType;
    public String xBandSourceInfo;

    public String sBandSourceChannel;
    public String sBandSourceType;
    public String sBandSourceInfo;

    public String masterEcdisSourceChannel;
    public String masterEcdisSourceType;
    public String masterEcdisSourceInfo;

    public String backupEcdisSourceChannel;
    public String backupEcdisSourceType;
    public String backupEcdisSourceInfo;

    // ---------- PAGE 2 & 3 : FILE UPLOADS ----------
    public Map<String, List<String>> uploadedFiles= new HashMap<>();
    public long inspectionId;

    // ---------- SAFE GETTERS ----------
    public String vesselName() { return vesselName; }
    public String engineerName() { return engineerName; }
    public String vdrMake() { return vdrMake; }
    public String dateTime() { return dateTime; }
}