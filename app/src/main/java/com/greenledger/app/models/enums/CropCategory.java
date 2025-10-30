package com.greenledger.app.models.enums;

public enum CropCategory {
    KHARIF("Kharif (Monsoon)"),
    RABI("Rabi (Winter)"),
    ZAID("Zaid (Summer)");

    private final String displayName;

    CropCategory(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static CropCategory fromString(String text) {
        for (CropCategory category : CropCategory.values()) {
            if (category.displayName.equalsIgnoreCase(text) ||
                category.name().equalsIgnoreCase(text)) {
                return category;
            }
        }
        return KHARIF; // Default
    }
}
