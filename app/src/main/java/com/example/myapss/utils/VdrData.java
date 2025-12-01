package com.example.myapss.utils;

import java.util.HashMap;

public class VdrData {

    public static HashMap<String, HashMap<String, String>> getVdrTable() {

        HashMap<String, HashMap<String, String>> vdr = new HashMap<>();

        // ============================ DANELEC ============================
        HashMap<String, String> danelec = new HashMap<>();
        danelec.put("BCP", "BRIDGE CONTROL PANEL");
        danelec.put("DAU", "DATA ACQUISITION UNIT");
        danelec.put("RVI", "REMOTE VIDEO INTERFACE");
        danelec.put("FIXED CAPSULE", "FIXED CAPSULE");
        danelec.put("FLOAT FREE CAPSULE", "FLOAT FREE CAPSULE");
        danelec.put("AUDIO", "AUDIO");
        danelec.put("RDI", "REMOTE DIGITAL INTERFACE");
        vdr.put("DANELEC", danelec);

        // ============================ NETWAVE ============================
        HashMap<String, String> netwave = new HashMap<>();
        netwave.put("BCP", "BRIDGE CONTROL UNIT");
        netwave.put("DAU", "(CORE MODULE) DATA ACQUISITION UNIT");
        netwave.put("RVI", "VIDEO INTERFACE CHANNEL 4CH.");
        netwave.put("FIXED CAPSULE", "FIXED PRODUCTIVE CAPSULE");
        netwave.put("FLOAT FREE CAPSULE", "FLOAT FREE CAPSULE");
        netwave.put("AUDIO", "AUDIO HUB, VHF AUDIO INTERFACE M1,2,3,4");
        netwave.put("RDI", "DATA EXTENTION UNIT");
        vdr.put("NETWAVE", netwave);

        // ============================ HEADWAY ============================
        HashMap<String, String> headway = new HashMap<>();
        headway.put("BCP", "REMOTE ALARM UNIT");
        headway.put("DAU", "MAIN CABINET");
        headway.put("RVI", "VIDEO INTRACE UNIT");
        headway.put("FIXED CAPSULE", "FINAL STRONG MEDIUM");
        headway.put("FLOAT FREE CAPSULE", "FLOAT FREE CAPSULE");
        headway.put("AUDIO", "INBUILD AUDIO TO (DAU)");
        headway.put("RDI", "REMOTE ACQUISITION UNIT");
        vdr.put("HEADWAY", headway);

        // ============================ SAL NAVIGATION ============================
        HashMap<String, String> salNav = new HashMap<>();
        salNav.put("BCP", "REMOTE CONTROL UNIT");
        salNav.put("DAU", "MAIN UNIT");
        salNav.put("RVI", "VIDEO GRABBER UNIT");
        salNav.put("FIXED CAPSULE", "FIXED CAPSULE PROCAPE");
        salNav.put("FLOAT FREE CAPSULE", "FLOAT FREE CAPSULE");
        salNav.put("AUDIO", "MIC, AUDIO MIC");
        salNav.put("RDI", "VIDEO GRABBER, ANA/DIGI INTERFACE");
        vdr.put("SAL NAVIGATION", salNav);

        // ============================ NSR ============================
        HashMap<String, String> nsr = new HashMap<>();
        nsr.put("BCP", "REMOTE ALARM UNIT");
        nsr.put("DAU", "MAIN CABIN UNIT");
        nsr.put("RVI", "INBUILD");
        nsr.put("FIXED CAPSULE", "FIXED PROTECTION DATA RECORDING CAPSULE");
        nsr.put("FLOAT FREE CAPSULE", "FLOAT FREE CAPSULE");
        nsr.put("AUDIO", "INBUILD");
        nsr.put("RDI", "DATA ACQUISITION MODULAR SYSTEM");
        vdr.put("NSR", nsr);

        // ============================ JRC ============================
        HashMap<String, String> jrc = new HashMap<>();
        jrc.put("BCP", "OPERATION PANEL UNIT");
        jrc.put("DAU", "RECORDING CONTROL UNIT");
        jrc.put("RVI", "FRAME GRAPHER UNIT");
        jrc.put("FIXED CAPSULE", "FIXED PROTECTIVE AND CAPSULE UNIT");
        jrc.put("FLOAT FREE CAPSULE", "FLOAT FREE CAPSULE UNIT");
        jrc.put("AUDIO", "INBUILD TO DAU");
        jrc.put("RDI", "DRY CONTENT, ANALOG UNIT");
        vdr.put("JRC", jrc);

        // ============================ FURUNO ============================
        HashMap<String, String> furuno = new HashMap<>();
        furuno.put("BCP", "REMOTE ALARM PANEL");
        furuno.put("DAU", "DATA COLLECTING UNIT");
        furuno.put("RVI", "REMOTE VIDEO INTERFACE");
        furuno.put("FIXED CAPSULE", "FIXED DATA RECORDING UNIT");
        furuno.put("FLOAT FREE CAPSULE", "FLOAT FREE DATA RECORDING UNIT");
        furuno.put("AUDIO", "INBUILD");
        furuno.put("RDI", "DATA COLLECTING UNIT");
        vdr.put("FURUNO", furuno);

        return vdr;
    }
}