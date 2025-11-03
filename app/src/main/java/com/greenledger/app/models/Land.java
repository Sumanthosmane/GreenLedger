package com.greenledger.app.models;

public class Land {
    private String landId;
    private String name;
    private double area; // in acres
    private String currentCropId; // Reference to current Crop, if any
    private String status; // Idle|Occupied|Fallow

    public Land() {
        // Required empty constructor for Firebase
    }

    public Land(String landId, String name, double area) {
        this.landId = landId;
        this.name = name;
        this.area = area;
        this.status = "Idle"; // Default status
    }

    public String getLandId() {
        return landId;
    }

    public void setLandId(String landId) {
        this.landId = landId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getArea() {
        return area;
    }

    public void setArea(double area) {
        this.area = area;
    }

    public String getCurrentCropId() {
        return currentCropId;
    }

    public void setCurrentCropId(String currentCropId) {
        this.currentCropId = currentCropId;
        this.status = currentCropId != null ? "Occupied" : "Idle";
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        if (status != null && (status.equals("Idle") || status.equals("Occupied") || status.equals("Fallow"))) {
            this.status = status;
        } else {
            throw new IllegalArgumentException("Invalid status. Must be Idle, Occupied, or Fallow");
        }
    }

    // Helper method to check if land is available for new crop
    public boolean isAvailableForCrop() {
        return "Idle".equals(status) || "Fallow".equals(status);
    }
}
