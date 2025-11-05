package com.greenledger.app.database.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

@Entity(tableName = "farms")
public class FarmEntity {
    @PrimaryKey
    @NonNull
    private String farmId;
    private String farmerId;
    private String farmDetails;
    private String lands;
    private String metadata;
    private long lastSync;
    private int syncStatus; // 0: Synced, 1: Pending Upload, 2: Pending Update, 3: Pending Delete

    @NonNull
    public String getFarmId() { return farmId; }
    public void setFarmId(@NonNull String farmId) { this.farmId = farmId; }

    public String getFarmerId() { return farmerId; }
    public void setFarmerId(String farmerId) { this.farmerId = farmerId; }

    public String getFarmDetails() { return farmDetails; }
    public void setFarmDetails(String farmDetails) { this.farmDetails = farmDetails; }

    public String getLands() { return lands; }
    public void setLands(String lands) { this.lands = lands; }

    public String getMetadata() { return metadata; }
    public void setMetadata(String metadata) { this.metadata = metadata; }

    public long getLastSync() { return lastSync; }
    public void setLastSync(long lastSync) { this.lastSync = lastSync; }

    public int getSyncStatus() { return syncStatus; }
    public void setSyncStatus(int syncStatus) { this.syncStatus = syncStatus; }
}
