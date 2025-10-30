package com.greenledger.app.models.enums;

public enum PaymentMode {
    CASH("Cash"),
    ONLINE("Online"),
    UPI("UPI"),
    CHEQUE("Cheque"),
    CREDIT("Credit");

    private final String displayName;

    PaymentMode(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static PaymentMode fromString(String text) {
        for (PaymentMode mode : PaymentMode.values()) {
            if (mode.displayName.equalsIgnoreCase(text)) {
                return mode;
            }
        }
        return CASH; // Default
    }
}
