package com.example.myapss.models;

public class InspectionModel {
    public int id;
    public String vesselName;
    public String imo;
    public String callSign;
    public String vdrMake;
    public String engineerName;
    public String engineerCompany;
    public String inspectionDate;
    public String status; // DRAFT, COMPLETED, SUBMITTED
    public long createdAt;
    public long updatedAt;

    public InspectionModel() {}

    public InspectionModel(String vesselName, String imo, String callSign,
                           String vdrMake, String engineerName, String engineerCompany,
                           String inspectionDate, String status) {
        this.vesselName = vesselName;
        this.imo = imo;
        this.callSign = callSign;
        this.vdrMake = vdrMake;
        this.engineerName = engineerName;
        this.engineerCompany = engineerCompany;
        this.inspectionDate = inspectionDate;
        this.status = status;
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = System.currentTimeMillis();
    }
}