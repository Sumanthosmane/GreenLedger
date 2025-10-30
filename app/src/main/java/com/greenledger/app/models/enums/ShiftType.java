package com.greenledger.app.models.enums;

public enum ShiftType {
    FULL_DAY("Full Day"),
    HALF_DAY("Half Day"),
    HOURLY("Hourly");

    private final String displayName;

    ShiftType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static ShiftType fromString(String text) {
        for (ShiftType type : ShiftType.values()) {
            if (type.displayName.equalsIgnoreCase(text)) {
                return type;
            }
        }
        return FULL_DAY; // Default
    }
}
