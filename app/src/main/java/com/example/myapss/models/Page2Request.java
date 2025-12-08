package com.example.myapss.models;

import java.util.HashMap;

public class Page2Request {

    public Long inspectionId;
    public boolean clearReadability;
    public boolean dimFunction;
    public boolean buttonsWorking;

    public boolean batteryReplacement;
    public String batteryExpiryYear;
    public String batteryExpiryMonth;
    public String dauCondition;

    public String capsuleDescription;
    public String beaconModel;
    public String beaconSerial;
    public String beaconVoltage;
    public String beaconBatteryYear;
    public String beaconBatteryMonth;

    public String floatFreeModel;
    public String epribSerial;
    public String hruModel;
    public boolean hruReplace;
    public String hruBatteryYear;
    public String hruBatteryMonth;

    public boolean rviCables;
    public String rviPower;
    public String rviModel;
    public HashMap<Object, Object> uploadedFiles;
}