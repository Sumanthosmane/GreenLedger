package com.greenledger.app.models.embedded;

public class Quality {
    private String grade;           // A|B|C
    private double moistureLevel;   // Percentage
    private double purityLevel;     // Percentage

    public Quality() {
        // Required empty constructor for Firebase
    }

    public Quality(String grade, double moistureLevel, double purityLevel) {
        this.grade = grade;
        this.moistureLevel = moistureLevel;
        this.purityLevel = purityLevel;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        if (grade != null && (grade.equals("A") || grade.equals("B") || grade.equals("C"))) {
            this.grade = grade;
        } else {
            throw new IllegalArgumentException("Grade must be A, B, or C");
        }
    }

    public double getMoistureLevel() {
        return moistureLevel;
    }

    public void setMoistureLevel(double moistureLevel) {
        if (moistureLevel >= 0 && moistureLevel <= 100) {
            this.moistureLevel = moistureLevel;
        } else {
            throw new IllegalArgumentException("Moisture level must be between 0 and 100");
        }
    }

    public double getPurityLevel() {
        return purityLevel;
    }

    public void setPurityLevel(double purityLevel) {
        if (purityLevel >= 0 && purityLevel <= 100) {
            this.purityLevel = purityLevel;
        } else {
            throw new IllegalArgumentException("Purity level must be between 0 and 100");
        }
    }
}
