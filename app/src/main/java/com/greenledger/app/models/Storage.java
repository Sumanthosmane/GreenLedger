package com.greenledger.app.models;

import com.greenledger.app.models.enums.StorageType;
import java.util.ArrayList;
import java.util.List;

public class Storage {
    private String storageId;
    private String farmerId;
    private StorageInfo storageInfo;
    private List<StorageInventory> inventory;
    private Metadata metadata;

    public Storage() {
        // Required empty constructor for Firebase
        this.inventory = new ArrayList<>();
    }

    public static class StorageInfo {
        private String name;             // Barn A|Warehouse 1
        private StorageType type;        // Barn|Warehouse|Cold Storage|Open Storage
        private double capacity;         // Total capacity in quintals
        private double currentOccupancy; // Current occupied capacity
        private String location;         // Location description

        public StorageInfo() {
            // Required empty constructor for Firebase
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public StorageType getType() {
            return type;
        }

        public void setType(StorageType type) {
            this.type = type;
        }

        public double getCapacity() {
            return capacity;
        }

        public void setCapacity(double capacity) {
            this.capacity = capacity;
        }

        public double getCurrentOccupancy() {
            return currentOccupancy;
        }

        public void setCurrentOccupancy(double currentOccupancy) {
            if (currentOccupancy > capacity) {
                throw new IllegalArgumentException("Current occupancy cannot exceed capacity");
            }
            this.currentOccupancy = currentOccupancy;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        // Helper method to check available capacity
        public double getAvailableCapacity() {
            return capacity - currentOccupancy;
        }

        // Helper method to get occupancy percentage
        public double getOccupancyPercentage() {
            if (capacity == 0) return 0;
            return (currentOccupancy / capacity) * 100;
        }
    }

    public static class Metadata {
        private long createdAt;
        private long updatedAt;

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
    }

    // Getters and Setters
    public String getStorageId() {
        return storageId;
    }

    public void setStorageId(String storageId) {
        this.storageId = storageId;
    }

    public String getFarmerId() {
        return farmerId;
    }

    public void setFarmerId(String farmerId) {
        this.farmerId = farmerId;
    }

    public StorageInfo getStorageInfo() {
        return storageInfo;
    }

    public void setStorageInfo(StorageInfo storageInfo) {
        this.storageInfo = storageInfo;
    }

    public List<StorageInventory> getInventory() {
        return inventory;
    }

    public void setInventory(List<StorageInventory> inventory) {
        this.inventory = inventory;
    }

    public void addInventory(StorageInventory item) {
        if (this.inventory == null) {
            this.inventory = new ArrayList<>();
        }
        // Check if there's enough capacity
        if (storageInfo != null &&
            (storageInfo.getCurrentOccupancy() + item.getQuantity()) > storageInfo.getCapacity()) {
            throw new IllegalStateException("Not enough storage capacity");
        }
        this.inventory.add(item);
        // Update current occupancy
        if (storageInfo != null) {
            storageInfo.setCurrentOccupancy(storageInfo.getCurrentOccupancy() + item.getQuantity());
        }
    }

    public void removeInventory(StorageInventory item) {
        if (this.inventory != null && this.inventory.remove(item)) {
            // Update current occupancy
            if (storageInfo != null) {
                storageInfo.setCurrentOccupancy(storageInfo.getCurrentOccupancy() - item.getQuantity());
            }
        }
    }

    public Metadata getMetadata() {
        return metadata;
    }

    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }

    // Helper method to get inventory for a specific crop
    public StorageInventory getCropInventory(String cropId) {
        if (inventory == null) return null;
        return inventory.stream()
                .filter(item -> cropId.equals(item.getCropId()))
                .findFirst()
                .orElse(null);
    }

    // Helper method to get total inventory value
    public double getTotalInventoryValue() {
        if (inventory == null) return 0;
        return inventory.stream()
                .mapToDouble(StorageInventory::getTotalValue)
                .sum();
    }
}
