package com.greenledger.app.database.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

@Entity(tableName = "business_partners")
public class BusinessPartnerEntity {
    @PrimaryKey
    @NonNull
    private String partnerId;
    private String farmerId;
    private String partnerInfo;
    private String preferences;
    private String transactionHistory;
    private String metadata;
    private long lastSync;
    private int syncStatus; // 0: Synced, 1: Pending Upload, 2: Pending Update, 3: Pending Delete

    @NonNull
    public String getPartnerId() { return partnerId; }
    public void setPartnerId(@NonNull String partnerId) { this.partnerId = partnerId; }

    public String getFarmerId() { return farmerId; }
    public void setFarmerId(String farmerId) { this.farmerId = farmerId; }

    public String getPartnerInfo() { return partnerInfo; }
    public void setPartnerInfo(String partnerInfo) { this.partnerInfo = partnerInfo; }

    public String getPreferences() { return preferences; }
    public void setPreferences(String preferences) { this.preferences = preferences; }

    public String getTransactionHistory() { return transactionHistory; }
    public void setTransactionHistory(String transactionHistory) { this.transactionHistory = transactionHistory; }

    public String getMetadata() { return metadata; }
    public void setMetadata(String metadata) { this.metadata = metadata; }

    public long getLastSync() { return lastSync; }
    public void setLastSync(long lastSync) { this.lastSync = lastSync; }

    public int getSyncStatus() { return syncStatus; }
    public void setSyncStatus(int syncStatus) { this.syncStatus = syncStatus; }
}
