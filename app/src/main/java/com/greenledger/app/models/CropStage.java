package com.greenledger.app.models;

import com.greenledger.app.models.enums.CropStatus;

import java.util.ArrayList;
import java.util.List;

public class CropStage {
    private String stage;              // Sowing|Plowing|Irrigation|Fertilizing|Harvesting|Reaping
    private long startDate;            // Start date timestamp
    private long endDate;              // End date timestamp
    private String completedBy;        // userId of labourer who completed the stage
    private String notes;              // Additional notes or instructions
    private List<String> photos;       // URLs of photos documenting the stage
    private List<String> labourers;    // List of labourer IDs assigned to this stage
    private double cost;               // Cost incurred in this stage
    private CropStatus status;         // Status of this stage

    public CropStage() {
        // Required empty constructor for Firebase
        this.photos = new ArrayList<>();
        this.labourers = new ArrayList<>();
        this.status = CropStatus.PLANNING;
    }

    public CropStage(String stage, long startDate) {
        this();
        this.stage = stage;
        this.startDate = startDate;
    }

    public String getStage() {
        return stage;
    }

    public void setStage(String stage) {
        this.stage = stage;
    }

    public long getStartDate() {
        return startDate;
    }

    public void setStartDate(long startDate) {
        this.startDate = startDate;
    }

    public long getEndDate() {
        return endDate;
    }

    public void setEndDate(long endDate) {
        this.endDate = endDate;
    }

    public String getCompletedBy() {
        return completedBy;
    }

    public void setCompletedBy(String completedBy) {
        this.completedBy = completedBy;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public List<String> getPhotos() {
        return photos;
    }

    public void setPhotos(List<String> photos) {
        this.photos = photos;
    }

    public void addPhoto(String photoUrl) {
        if (this.photos == null) {
            this.photos = new ArrayList<>();
        }
        this.photos.add(photoUrl);
    }

    public List<String> getLabourers() {
        return labourers;
    }

    public void setLabourers(List<String> labourers) {
        this.labourers = labourers;
    }

    public void addLabourer(String labourerId) {
        if (this.labourers == null) {
            this.labourers = new ArrayList<>();
        }
        this.labourers.add(labourerId);
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public CropStatus getStatus() {
        return status;
    }

    public void setStatus(CropStatus status) {
        this.status = status;
    }

    // Helper method to check if stage is complete
    public boolean isComplete() {
        return endDate > 0 && completedBy != null;
    }

    // Helper method to calculate duration in days
    public int getDurationDays() {
        if (endDate == 0) return 0;
        return (int) ((endDate - startDate) / (1000 * 60 * 60 * 24));
    }
}
