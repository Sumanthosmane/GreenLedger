package com.greenledger.app.models.embedded;

public class Inventory {
    private double total;      // Total quantity
    private double sold;       // Quantity sold
    private double unsold;     // Quantity unsold
    private double spoiled;    // Quantity spoiled/damaged
    private double reserved;   // Quantity reserved for orders

    public Inventory() {
        // Required empty constructor for Firebase
    }

    public Inventory(double total) {
        this.total = total;
        this.unsold = total;   // Initially all quantity is unsold
        this.sold = 0;
        this.spoiled = 0;
        this.reserved = 0;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public double getSold() {
        return sold;
    }

    public void setSold(double sold) {
        if (sold >= 0 && sold <= total) {
            this.sold = sold;
            updateUnsold();
        } else {
            throw new IllegalArgumentException("Sold quantity cannot be negative or exceed total");
        }
    }

    public double getUnsold() {
        return unsold;
    }

    public void setUnsold(double unsold) {
        this.unsold = unsold;
    }

    public double getSpoiled() {
        return spoiled;
    }

    public void setSpoiled(double spoiled) {
        if (spoiled >= 0 && spoiled <= total) {
            this.spoiled = spoiled;
            updateUnsold();
        } else {
            throw new IllegalArgumentException("Spoiled quantity cannot be negative or exceed total");
        }
    }

    public double getReserved() {
        return reserved;
    }

    public void setReserved(double reserved) {
        if (reserved >= 0 && reserved <= unsold) {
            this.reserved = reserved;
        } else {
            throw new IllegalArgumentException("Reserved quantity cannot be negative or exceed unsold quantity");
        }
    }

    // Helper method to update unsold quantity
    private void updateUnsold() {
        this.unsold = total - sold - spoiled;
        if (this.reserved > this.unsold) {
            this.reserved = this.unsold; // Adjust reserved if it exceeds new unsold quantity
        }
    }

    // Helper method to get available quantity (unsold - reserved)
    public double getAvailableQuantity() {
        return unsold - reserved;
    }
}
