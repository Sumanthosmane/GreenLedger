package com.greenledger.app.models.enums;

public enum StorageType {
    BARN("Barn"),
    WAREHOUSE("Warehouse"),
    COLD_STORAGE("Cold Storage"),
    OPEN_STORAGE("Open Storage");

    private final String displayName;

    StorageType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static StorageType fromString(String text) {
        for (StorageType type : StorageType.values()) {
            if (type.displayName.equalsIgnoreCase(text)) {
                return type;
            }
        }
        return WAREHOUSE; // Default
    }
}
