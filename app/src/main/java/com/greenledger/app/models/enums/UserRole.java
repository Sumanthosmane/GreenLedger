package com.greenledger.app.models.enums;

public enum UserRole {
    FARMER(10),
    LABOURER(1),
    BUSINESS_PARTNER(5);

    private final int accessLevel;

    UserRole(int accessLevel) {
        this.accessLevel = accessLevel;
    }

    public int getAccessLevel() {
        return accessLevel;
    }
}
