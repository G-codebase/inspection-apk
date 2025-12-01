package com.example.myapss.models;

public class Page1Request {

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

    public Page1Request(InspectionData data) {
        this.engineerName = data.engineerName;
        this.vesselName = data.vesselName;
        this.location = data.location;
        this.vesselFlag = data.vesselFlag;
        this.mainSerialNumber = data.mainSerialNumber;
        this.inspectionDate = data.dateTime;
        this.imoNumber = data.imoNumber;
        this.mmsiNumber = data.mmsiNumber;
        this.vesselClass = data.vesselClass;
        this.vdrMake = data.vdrMake;
    }
}