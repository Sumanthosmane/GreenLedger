package com.greenledger.app.models.enums;

public enum CropStatus {
    PLANNING("Planning"),
    SOWING("Sowing"),
    GROWING("Growing"),
    HARVESTING("Harvesting"),
    COMPLETED("Completed");

    private final String displayName;

    CropStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static CropStatus fromString(String text) {
        for (CropStatus status : CropStatus.values()) {
            if (status.displayName.equalsIgnoreCase(text)) {
                return status;
            }
        }
        return PLANNING; // Default
    }
}
