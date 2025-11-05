package com.greenledger.app.models;

import com.greenledger.app.models.embedded.Location;
import java.util.ArrayList;
import java.util.List;

public class Farm {
    private String farmId;
    private String farmerId;
    private FarmDetails farmDetails;
    private List<Land> lands;
    private Metadata metadata;

    public Farm() {
        // Required empty constructor for Firebase
        this.lands = new ArrayList<>();
    }

    public static class FarmDetails {
        private String name;
        private double totalArea; // in acres
        private Location location;
        private String soilType;
        private String irrigationType; // Drip|Sprinkler|Flood

        public FarmDetails() {
            // Required empty constructor for Firebase
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public double getTotalArea() {
            return totalArea;
        }

        public void setTotalArea(double totalArea) {
            this.totalArea = totalArea;
        }

        public Location getLocation() {
            return location;
        }

        public void setLocation(Location location) {
            this.location = location;
        }

        public String getSoilType() {
            return soilType;
        }

        public void setSoilType(String soilType) {
            this.soilType = soilType;
        }

        public String getIrrigationType() {
            return irrigationType;
        }

        public void setIrrigationType(String irrigationType) {
            this.irrigationType = irrigationType;
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

    public FarmDetails getFarmDetails() {
        return farmDetails;
    }

    public void setFarmDetails(FarmDetails farmDetails) {
        this.farmDetails = farmDetails;
    }

    public List<Land> getLands() {
        return lands;
    }

    public void setLands(List<Land> lands) {
        this.lands = lands;
    }

    public void addLand(Land land) {
        if (this.lands == null) {
            this.lands = new ArrayList<>();
        }
        this.lands.add(land);
    }

    public void removeLand(Land land) {
        if (this.lands != null) {
            this.lands.remove(land);
        }
    }

    public Metadata getMetadata() {
        return metadata;
    }

    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }

    // Helper method to calculate total land area
    public double calculateTotalLandArea() {
        if (lands == null) return 0;
        return lands.stream()
                .mapToDouble(Land::getArea)
                .sum();
    }

    // Helper method to get number of occupied lands
    public long getOccupiedLandsCount() {
        if (lands == null) return 0;
        return lands.stream()
                .filter(land -> "Occupied".equals(land.getStatus()))
                .count();
    }

    public String getName() {
        return farmDetails != null ? farmDetails.getName() : "";
    }
}
