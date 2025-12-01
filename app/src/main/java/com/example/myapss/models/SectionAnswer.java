package com.example.myapss.models;

public class SectionAnswer {

    private String sectionTitle;
    private String selectedOption;
    private String remarks;

    public SectionAnswer(String sectionTitle, String selectedOption, String remarks) {
        this.sectionTitle = sectionTitle;
        this.selectedOption = selectedOption;
        this.remarks = remarks;
    }

    public String getSectionTitle() {
        return sectionTitle;
    }

    public String getSelectedOption() {
        return selectedOption;
    }

    public String getRemarks() {
        return remarks;
    }
}