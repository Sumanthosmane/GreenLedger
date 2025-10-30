package com.greenledger.app.models.enums;

public enum UserRole {
    FARMER("Farmer", 100),
    LABOURER("Labourer", 50),
    BUSINESS_PARTNER("Business Partner", 30);

    private final String displayName;
    private final int accessLevel;

    UserRole(String displayName, int accessLevel) {
        this.displayName = displayName;
        this.accessLevel = accessLevel;
    }

    public String getDisplayName() {
        return displayName;
    }

    public int getAccessLevel() {
        return accessLevel;
    }

    public static UserRole fromString(String text) {
        for (UserRole role : UserRole.values()) {
            if (role.displayName.equalsIgnoreCase(text)) {
                return role;
            }
        }
        return FARMER; // Default
    }
}
