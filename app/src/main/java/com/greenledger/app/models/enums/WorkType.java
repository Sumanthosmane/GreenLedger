package com.greenledger.app.models.enums;

public enum WorkType {
    PLOWING("Plowing"),
    SOWING("Sowing"),
    IRRIGATION("Irrigation"),
    FERTILIZING("Fertilizing"),
    PEST_CONTROL("Pest Control"),
    WEEDING("Weeding"),
    HARVESTING("Harvesting"),
    REAPING("Reaping"),
    MAINTENANCE("Maintenance"),
    OTHER("Other");

    private final String displayName;

    WorkType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static WorkType fromString(String text) {
        for (WorkType type : WorkType.values()) {
            if (type.displayName.equalsIgnoreCase(text)) {
                return type;
            }
        }
        return OTHER; // Default
    }
}
