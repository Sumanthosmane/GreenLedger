package com.greenledger.app.models;

import com.google.firebase.database.PropertyName;
import java.util.List;
import java.util.ArrayList;

public class BusinessPartner {
    private String partnerId;
    private String farmerId;
    private PartnerInfo partnerInfo;
    private Preferences preferences;
    private TransactionHistory transactionHistory;
    private Metadata metadata;

    public BusinessPartner() {
        // Required empty constructor for Firebase
    }

    public static class PartnerInfo {
        private String businessName;
        private String type; // Buyer/Supplier/Both
        private String gstNumber;
        private String panNumber;

        public String getBusinessName() { return businessName; }
        public void setBusinessName(String businessName) { this.businessName = businessName; }

        @PropertyName("type")
        public String getType() { return type; }
        @PropertyName("type")
        public void setType(String type) { this.type = type; }

        public String getGstNumber() { return gstNumber; }
        public void setGstNumber(String gstNumber) { this.gstNumber = gstNumber; }

        public String getPanNumber() { return panNumber; }
        public void setPanNumber(String panNumber) { this.panNumber = panNumber; }
    }

    public static class Preferences {
        private List<String> preferredCrops;
        private String paymentTerms;
        private double minimumOrder;

        public Preferences() {
            preferredCrops = new ArrayList<>();
        }

        public List<String> getPreferredCrops() { return preferredCrops; }
        public void setPreferredCrops(List<String> preferredCrops) { this.preferredCrops = preferredCrops; }

        public String getPaymentTerms() { return paymentTerms; }
        public void setPaymentTerms(String paymentTerms) { this.paymentTerms = paymentTerms; }

        public double getMinimumOrder() { return minimumOrder; }
        public void setMinimumOrder(double minimumOrder) { this.minimumOrder = minimumOrder; }
    }

    public static class TransactionHistory {
        private int totalPurchases;
        private double totalAmount;
        private long lastPurchaseDate;
        private double outstandingAmount;

        public int getTotalPurchases() { return totalPurchases; }
        public void setTotalPurchases(int totalPurchases) { this.totalPurchases = totalPurchases; }

        public double getTotalAmount() { return totalAmount; }
        public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }

        public long getLastPurchaseDate() { return lastPurchaseDate; }
        public void setLastPurchaseDate(long lastPurchaseDate) { this.lastPurchaseDate = lastPurchaseDate; }

        public double getOutstandingAmount() { return outstandingAmount; }
        public void setOutstandingAmount(double outstandingAmount) { this.outstandingAmount = outstandingAmount; }
    }

    public static class Metadata {
        private String relationship;
        private float rating;
        private String notes;
        private long createdAt;

        public String getRelationship() { return relationship; }
        public void setRelationship(String relationship) { this.relationship = relationship; }

        public float getRating() { return rating; }
        public void setRating(float rating) { this.rating = rating; }

        public String getNotes() { return notes; }
        public void setNotes(String notes) { this.notes = notes; }

        public long getCreatedAt() { return createdAt; }
        public void setCreatedAt(long createdAt) { this.createdAt = createdAt; }
    }

    // Getters and Setters for main class
    public String getPartnerId() { return partnerId; }
    public void setPartnerId(String partnerId) { this.partnerId = partnerId; }

    public String getFarmerId() { return farmerId; }
    public void setFarmerId(String farmerId) { this.farmerId = farmerId; }

    @PropertyName("partnerInfo")
    public PartnerInfo getPartnerInfo() { return partnerInfo; }
    @PropertyName("partnerInfo")
    public void setPartnerInfo(PartnerInfo partnerInfo) { this.partnerInfo = partnerInfo; }

    @PropertyName("preferences")
    public Preferences getPreferences() { return preferences; }
    @PropertyName("preferences")
    public void setPreferences(Preferences preferences) { this.preferences = preferences; }

    @PropertyName("transactionHistory")
    public TransactionHistory getTransactionHistory() { return transactionHistory; }
    @PropertyName("transactionHistory")
    public void setTransactionHistory(TransactionHistory transactionHistory) { this.transactionHistory = transactionHistory; }

    @PropertyName("metadata")
    public Metadata getMetadata() { return metadata; }
    @PropertyName("metadata")
    public void setMetadata(Metadata metadata) { this.metadata = metadata; }
}
