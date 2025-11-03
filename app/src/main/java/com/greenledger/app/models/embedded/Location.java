package com.greenledger.app.models.embedded;

public class Location {
    private String address;
    private String district;
    private String state;
    private String pincode;

    public Location() {
        // Required empty constructor for Firebase
    }

    public Location(String address, String district, String state, String pincode) {
        this.address = address;
        this.district = district;
        this.state = state;
        this.pincode = pincode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    @Override
    public String toString() {
        return address + "\n" + district + ", " + state + " - " + pincode;
    }
}
