package com.greenledger.app.database.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

@Entity(tableName = "schedules")
public class ScheduleEntity {
    @PrimaryKey
    @NonNull
    private String scheduleId;
    private String farmerId;
    private String farmId;
    private String scheduleInfo;
    private String assignedLabourers;
    private String cropId;
    private String landId;
    private String instructions;
    private String metadata;
    private long lastSync;
    private int syncStatus; // 0: Synced, 1: Pending Upload, 2: Pending Update, 3: Pending Delete

    @NonNull
    public String getScheduleId() { return scheduleId; }
    public void setScheduleId(@NonNull String scheduleId) { this.scheduleId = scheduleId; }

    public String getFarmerId() { return farmerId; }
    public void setFarmerId(String farmerId) { this.farmerId = farmerId; }

    public String getFarmId() { return farmId; }
    public void setFarmId(String farmId) { this.farmId = farmId; }

    public String getScheduleInfo() { return scheduleInfo; }
    public void setScheduleInfo(String scheduleInfo) { this.scheduleInfo = scheduleInfo; }

    public String getAssignedLabourers() { return assignedLabourers; }
    public void setAssignedLabourers(String assignedLabourers) { this.assignedLabourers = assignedLabourers; }

    public String getCropId() { return cropId; }
    public void setCropId(String cropId) { this.cropId = cropId; }

    public String getLandId() { return landId; }
    public void setLandId(String landId) { this.landId = landId; }

    public String getInstructions() { return instructions; }
    public void setInstructions(String instructions) { this.instructions = instructions; }

    public String getMetadata() { return metadata; }
    public void setMetadata(String metadata) { this.metadata = metadata; }

    public long getLastSync() { return lastSync; }
    public void setLastSync(long lastSync) { this.lastSync = lastSync; }

    public int getSyncStatus() { return syncStatus; }
    public void setSyncStatus(int syncStatus) { this.syncStatus = syncStatus; }
}
