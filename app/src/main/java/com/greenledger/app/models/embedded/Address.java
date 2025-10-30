package com.greenledger.app.models.embedded;

public class Address {
    private String street;
    private String village;
    private String district;
    private String state;
    private String pincode;

    public Address() {
        // Required empty constructor for Firebase
    }

    public Address(String street, String village, String district, String state, String pincode) {
        this.street = street;
        this.village = village;
        this.district = district;
        this.state = state;
        this.pincode = pincode;
    }

    // Getters and setters
    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getVillage() {
        return village;
    }

    public void setVillage(String village) {
        this.village = village;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getFullAddress() {
        StringBuilder sb = new StringBuilder();
        if (street != null && !street.isEmpty()) sb.append(street).append(", ");
        if (village != null && !village.isEmpty()) sb.append(village).append(", ");
        if (district != null && !district.isEmpty()) sb.append(district).append(", ");
        if (state != null && !state.isEmpty()) sb.append(state).append(" - ");
        if (pincode != null && !pincode.isEmpty()) sb.append(pincode);
        return sb.toString();
    }
}
