package com.greenledger.app.models.embedded;

public class CropInfo {
    private String type;        // Rice|Wheat|Cotton|Sugarcane|etc
    private String variety;     // Specific variety name
    private String category;    // Kharif|Rabi|Zaid

    public CropInfo() {
        // Required empty constructor for Firebase
    }

    public CropInfo(String type, String variety, String category) {
        this.type = type;
        this.variety = variety;
        this.category = category;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getVariety() {
        return variety;
    }

    public void setVariety(String variety) {
        this.variety = variety;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
