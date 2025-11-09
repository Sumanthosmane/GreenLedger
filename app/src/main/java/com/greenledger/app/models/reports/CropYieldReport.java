package com.greenledger.app.models.reports;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class CropYieldReport {
    private String cropId;
    private String cropName;
    private Date harvestDate;
    private double expectedYield;
    private double actualYield;
    private double yieldVariance;
    private double totalCost;
    private double revenueGenerated;
    private double profitMargin;
    private Map<String, Double> costBreakdown;
    private List<QualityMetric> qualityMetrics;

    public static class QualityMetric {
        private String metricName;
        private double value;
        private String unit;
        private String grade;

        // Getters and setters
        public String getMetricName() { return metricName; }
        public void setMetricName(String metricName) { this.metricName = metricName; }
        public double getValue() { return value; }
        public void setValue(double value) { this.value = value; }
        public String getUnit() { return unit; }
        public void setUnit(String unit) { this.unit = unit; }
        public String getGrade() { return grade; }
        public void setGrade(String grade) { this.grade = grade; }
    }

    // Getters and setters
    public String getCropId() { return cropId; }
    public void setCropId(String cropId) { this.cropId = cropId; }
    public String getCropName() { return cropName; }
    public void setCropName(String cropName) { this.cropName = cropName; }
    public Date getHarvestDate() { return harvestDate; }
    public void setHarvestDate(Date harvestDate) { this.harvestDate = harvestDate; }
    public double getExpectedYield() { return expectedYield; }
    public void setExpectedYield(double expectedYield) { this.expectedYield = expectedYield; }
    public double getActualYield() { return actualYield; }
    public void setActualYield(double actualYield) { this.actualYield = actualYield; }
    public double getYieldVariance() { return yieldVariance; }
    public void setYieldVariance(double yieldVariance) { this.yieldVariance = yieldVariance; }
    public double getTotalCost() { return totalCost; }
    public void setTotalCost(double totalCost) { this.totalCost = totalCost; }
    public double getRevenueGenerated() { return revenueGenerated; }
    public void setRevenueGenerated(double revenueGenerated) { this.revenueGenerated = revenueGenerated; }
    public double getProfitMargin() { return profitMargin; }
    public void setProfitMargin(double profitMargin) { this.profitMargin = profitMargin; }
    public Map<String, Double> getCostBreakdown() { return costBreakdown; }
    public void setCostBreakdown(Map<String, Double> costBreakdown) { this.costBreakdown = costBreakdown; }
    public List<QualityMetric> getQualityMetrics() { return qualityMetrics; }
    public void setQualityMetrics(List<QualityMetric> qualityMetrics) { this.qualityMetrics = qualityMetrics; }
}
