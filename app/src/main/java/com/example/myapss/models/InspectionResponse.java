package com.example.myapss.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class InspectionResponse {

    @SerializedName("success")
    private boolean success;

    @SerializedName("message")
    private String message;

    @SerializedName("inspectionId")
    private int inspectionId;

    @SerializedName("status")
    private String status;

    @SerializedName("sections")
    private List<SectionResult> sections;

    // ------------------------------
    // GETTERS
    // ------------------------------

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public int getInspectionId() {
        return inspectionId;
    }

    public String getStatus() {
        return status;
    }

    public List<SectionResult> getSections() {
        return sections;
    }

    // ------------------------------
    // INNER CLASS FOR SECTION RESULTS
    // ------------------------------
    public static class SectionResult {

        @SerializedName("name")
        private String name;

        @SerializedName("answer")
        private String answer;

        @SerializedName("remarks")
        private String remarks;

        @SerializedName("expiryDate")
        private String expiryDate;

        // GETTERS
        public String getName() {
            return name;
        }

        public String getAnswer() {
            return answer;
        }

        public String getRemarks() {
            return remarks;
        }

        public String getExpiryDate() {
            return expiryDate;
        }
    }
}