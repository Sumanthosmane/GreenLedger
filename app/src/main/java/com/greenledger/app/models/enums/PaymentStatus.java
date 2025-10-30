package com.greenledger.app.models.enums;

public enum PaymentStatus {
    PAID("Paid"),
    PENDING("Pending"),
    PARTIAL("Partial");

    private final String displayName;

    PaymentStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static PaymentStatus fromString(String text) {
        for (PaymentStatus status : PaymentStatus.values()) {
            if (status.displayName.equalsIgnoreCase(text)) {
                return status;
            }
        }
        return PENDING; // Default
    }
}
