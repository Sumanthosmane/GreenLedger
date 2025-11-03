package com.greenledger.app.models;

public class StorageInventory {
    private String cropId;
    private double quantity;
    private long dateStored;
    private Long expiryDate;     // Optional, for perishable items
    private String condition;     // Current condition of stored crop
    private double valuePerUnit; // Value per unit for inventory valuation

    public StorageInventory() {
        // Required empty constructor for Firebase
    }

    public StorageInventory(String cropId, double quantity, long dateStored) {
        this.cropId = cropId;
        this.quantity = quantity;
        this.dateStored = dateStored;
        this.condition = "Good"; // Default condition
    }

    public String getCropId() {
        return cropId;
    }

    public void setCropId(String cropId) {
        this.cropId = cropId;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public long getDateStored() {
        return dateStored;
    }

    public void setDateStored(long dateStored) {
        this.dateStored = dateStored;
    }

    public Long getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Long expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public double getValuePerUnit() {
        return valuePerUnit;
    }

    public void setValuePerUnit(double valuePerUnit) {
        this.valuePerUnit = valuePerUnit;
    }

    // Helper method to get total value
    public double getTotalValue() {
        return quantity * valuePerUnit;
    }

    // Helper method to check if item is expired
    public boolean isExpired() {
        if (expiryDate == null) return false;
        return System.currentTimeMillis() > expiryDate;
    }

    // Helper method to get storage duration in days
    public int getStorageDuration() {
        return (int) ((System.currentTimeMillis() - dateStored) / (1000 * 60 * 60 * 24));
    }
}
