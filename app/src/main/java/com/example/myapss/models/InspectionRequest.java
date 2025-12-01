package com.example.myapss.models;

import java.util.List;

public class InspectionRequest {
    public String vesselName;
    public String inspectorName;
    public String vdrMake;
    public String date;

    public String xBandChannel;
    public String xBandType;
    public String xBandInfo;

    public String sBandChannel;
    public String sBandType;
    public String sBandInfo;

    public String masterChannel;
    public String masterType;
    public String masterInfo;

    public String backupChannel;
    public String backupType;
    public String backupInfo;

    public List<SectionAnswer> sectionAnswers;
}