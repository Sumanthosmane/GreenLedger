package com.greenledger.app.models.embedded;

import com.greenledger.app.models.enums.PaymentMode;
import com.greenledger.app.models.enums.PaymentStatus;

public class PaymentDetails {
    private String mode; // Store as string for Firebase
    private String status; // Store as string for Firebase
    private double paidAmount;
    private double pendingAmount;
    private String transactionId;
    private long paymentDate;

    public PaymentDetails() {
        // Required empty constructor for Firebase
    }

    public PaymentDetails(PaymentMode mode, PaymentStatus status, double paidAmount,
                         double pendingAmount, String transactionId, long paymentDate) {
        this.mode = mode.name();
        this.status = status.name();
        this.paidAmount = paidAmount;
        this.pendingAmount = pendingAmount;
        this.transactionId = transactionId;
        this.paymentDate = paymentDate;
    }

    // Getters and setters
    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public PaymentMode getPaymentMode() {
        try {
            return PaymentMode.valueOf(mode);
        } catch (Exception e) {
            return PaymentMode.CASH;
        }
    }

    public void setPaymentMode(PaymentMode paymentMode) {
        this.mode = paymentMode.name();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public PaymentStatus getPaymentStatus() {
        try {
            return PaymentStatus.valueOf(status);
        } catch (Exception e) {
            return PaymentStatus.PENDING;
        }
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.status = paymentStatus.name();
    }

    public double getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(double paidAmount) {
        this.paidAmount = paidAmount;
    }

    public double getPendingAmount() {
        return pendingAmount;
    }

    public void setPendingAmount(double pendingAmount) {
        this.pendingAmount = pendingAmount;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public long getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(long paymentDate) {
        this.paymentDate = paymentDate;
    }
}
