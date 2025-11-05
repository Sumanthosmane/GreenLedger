package com.greenledger.app.models;

import com.greenledger.app.models.embedded.BankDetails;
import com.greenledger.app.models.embedded.Address;

public class Buyer {
    private String id;
    private String name;
    private String businessName;
    private String contactNumber;
    private String email;
    private Address address;
    private BankDetails bankDetails;
    private String gstin; // GST Identification Number
    private String pan; // PAN Card Number
    private String notes;
    private double totalPurchaseAmount;
    private int totalTransactions;

    // Default constructor for Firebase
    public Buyer() {}

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getBusinessName() { return businessName; }
    public void setBusinessName(String businessName) { this.businessName = businessName; }

    public String getContactNumber() { return contactNumber; }
    public void setContactNumber(String contactNumber) { this.contactNumber = contactNumber; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public Address getAddress() { return address; }
    public void setAddress(Address address) { this.address = address; }

    public BankDetails getBankDetails() { return bankDetails; }
    public void setBankDetails(BankDetails bankDetails) { this.bankDetails = bankDetails; }

    public String getGstin() { return gstin; }
    public void setGstin(String gstin) { this.gstin = gstin; }

    public String getPan() { return pan; }
    public void setPan(String pan) { this.pan = pan; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public double getTotalPurchaseAmount() { return totalPurchaseAmount; }
    public void setTotalPurchaseAmount(double totalPurchaseAmount) { this.totalPurchaseAmount = totalPurchaseAmount; }

    public int getTotalTransactions() { return totalTransactions; }
    public void setTotalTransactions(int totalTransactions) { this.totalTransactions = totalTransactions; }
}
