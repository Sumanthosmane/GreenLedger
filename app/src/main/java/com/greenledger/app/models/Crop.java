package com.greenledger.app.models;

import com.greenledger.app.models.embedded.CropInfo;
import com.greenledger.app.models.embedded.Quality;
import com.greenledger.app.models.embedded.Inventory;
import com.greenledger.app.models.enums.CropStatus;

import java.util.ArrayList;
import java.util.List;

public class Crop {
    private String cropId;           // Unique ID
    private String farmId;          // Reference to Farm
    private String farmerId;        // Reference to Farmer (User)
    private String landId;          // Reference to Land plot

    private CropInfo cropInfo;      // Type, variety, category

    private CropLifecycle lifecycle;
    private Quantity quantity;
    private Quality quality;
    private Inventory inventory;
    private Storage storage;
    private Financial financial;
    private Metadata metadata;

    public Crop() {
        // Required empty constructor for Firebase
    }

    // Nested Classes
    public static class CropLifecycle {
        private CropStatus status;
        private List<CropStage> stages;
        private long sowingDate;
        private long expectedHarvestDate;
        private long actualHarvestDate;

        public CropLifecycle() {
            this.stages = new ArrayList<>();
            this.status = CropStatus.PLANNING;
        }

        public CropStatus getStatus() {
            return status;
        }

        public void setStatus(CropStatus status) {
            this.status = status;
        }

        public List<CropStage> getStages() {
            return stages;
        }

        public void setStages(List<CropStage> stages) {
            this.stages = stages;
        }

        public void addStage(CropStage stage) {
            if (this.stages == null) {
                this.stages = new ArrayList<>();
            }
            this.stages.add(stage);
        }

        public long getSowingDate() {
            return sowingDate;
        }

        public void setSowingDate(long sowingDate) {
            this.sowingDate = sowingDate;
        }

        public long getExpectedHarvestDate() {
            return expectedHarvestDate;
        }

        public void setExpectedHarvestDate(long expectedHarvestDate) {
            this.expectedHarvestDate = expectedHarvestDate;
        }

        public long getActualHarvestDate() {
            return actualHarvestDate;
        }

        public void setActualHarvestDate(long actualHarvestDate) {
            this.actualHarvestDate = actualHarvestDate;
        }

        // Helper method to get current stage
        public CropStage getCurrentStage() {
            if (stages == null || stages.isEmpty()) return null;
            return stages.get(stages.size() - 1);
        }

        // Helper method to calculate growing days
        public int getGrowingDays() {
            if (actualHarvestDate > 0) {
                return (int) ((actualHarvestDate - sowingDate) / (1000 * 60 * 60 * 24));
            }
            return 0;
        }
    }

    public static class Quantity {
        private double expected;     // Expected yield
        private double actual;       // Actual yield
        private String unit;         // kg|quintal|ton

        public Quantity() {
            // Required empty constructor for Firebase
        }

        public double getExpected() {
            return expected;
        }

        public void setExpected(double expected) {
            this.expected = expected;
        }

        public double getActual() {
            return actual;
        }

        public void setActual(double actual) {
            this.actual = actual;
        }

        public String getUnit() {
            return unit;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }

        // Helper method to calculate yield difference
        public double getYieldDifference() {
            return actual - expected;
        }

        // Helper method to calculate yield percentage
        public double getYieldPercentage() {
            if (expected == 0) return 0;
            return (actual / expected) * 100;
        }
    }

    public static class Storage {
        private String storageId;
        private String location;
        private long dateStored;
        private String condition;    // Good|Fair|Poor

        public Storage() {
            // Required empty constructor for Firebase
        }

        public String getStorageId() {
            return storageId;
        }

        public void setStorageId(String storageId) {
            this.storageId = storageId;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public long getDateStored() {
            return dateStored;
        }

        public void setDateStored(long dateStored) {
            this.dateStored = dateStored;
        }

        public String getCondition() {
            return condition;
        }

        public void setCondition(String condition) {
            this.condition = condition;
        }
    }

    public static class Financial {
        private double costOfProduction;
        private double sellingPrice;     // per unit
        private double totalRevenue;
        private double profitLoss;

        public Financial() {
            // Required empty constructor for Firebase
        }

        public double getCostOfProduction() {
            return costOfProduction;
        }

        public void setCostOfProduction(double costOfProduction) {
            this.costOfProduction = costOfProduction;
            calculateProfitLoss();
        }

        public double getSellingPrice() {
            return sellingPrice;
        }

        public void setSellingPrice(double sellingPrice) {
            this.sellingPrice = sellingPrice;
        }

        public double getTotalRevenue() {
            return totalRevenue;
        }

        public void setTotalRevenue(double totalRevenue) {
            this.totalRevenue = totalRevenue;
            calculateProfitLoss();
        }

        public double getProfitLoss() {
            return profitLoss;
        }

        private void calculateProfitLoss() {
            this.profitLoss = totalRevenue - costOfProduction;
        }

        // Helper method to calculate profit margin percentage
        public double getProfitMarginPercentage() {
            if (totalRevenue == 0) return 0;
            return (profitLoss / totalRevenue) * 100;
        }
    }

    public static class Metadata {
        private long createdAt;
        private long updatedAt;
        private String createdBy;    // userId

        public Metadata() {
            // Required empty constructor for Firebase
            this.createdAt = System.currentTimeMillis();
            this.updatedAt = System.currentTimeMillis();
        }

        public long getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(long createdAt) {
            this.createdAt = createdAt;
        }

        public long getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(long updatedAt) {
            this.updatedAt = updatedAt;
        }

        public String getCreatedBy() {
            return createdBy;
        }

        public void setCreatedBy(String createdBy) {
            this.createdBy = createdBy;
        }
    }

    // Getters and Setters for main class
    public String getCropId() {
        return cropId;
    }

    public void setCropId(String cropId) {
        this.cropId = cropId;
    }

    public String getFarmId() {
        return farmId;
    }

    public void setFarmId(String farmId) {
        this.farmId = farmId;
    }

    public String getFarmerId() {
        return farmerId;
    }

    public void setFarmerId(String farmerId) {
        this.farmerId = farmerId;
    }

    public String getLandId() {
        return landId;
    }

    public void setLandId(String landId) {
        this.landId = landId;
    }

    public CropInfo getCropInfo() {
        return cropInfo;
    }

    public void setCropInfo(CropInfo cropInfo) {
        this.cropInfo = cropInfo;
    }

    public CropLifecycle getLifecycle() {
        return lifecycle;
    }

    public void setLifecycle(CropLifecycle lifecycle) {
        this.lifecycle = lifecycle;
    }

    public Quantity getQuantity() {
        return quantity;
    }

    public void setQuantity(Quantity quantity) {
        this.quantity = quantity;
    }

    public Quality getQuality() {
        return quality;
    }

    public void setQuality(Quality quality) {
        this.quality = quality;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    public Storage getStorage() {
        return storage;
    }

    public void setStorage(Storage storage) {
        this.storage = storage;
    }

    public Financial getFinancial() {
        return financial;
    }

    public void setFinancial(Financial financial) {
        this.financial = financial;
    }

    public Metadata getMetadata() {
        return metadata;
    }

    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }
}