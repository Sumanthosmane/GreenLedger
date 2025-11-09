package com.greenledger.app.models.reports;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class LabourReport {
    private Date startDate;
    private Date endDate;
    private int totalWorkers;
    private double totalHours;
    private double totalWages;
    private Map<String, Double> hoursPerWorker;
    private Map<String, Double> wagesPerWorker;
    private List<WorkEntry> workEntries;

    public static class WorkEntry {
        private String workerId;
        private String workerName;
        private Date date;
        private double hours;
        private String workType;
        private double wage;
        private String status; // PAID, PENDING

        // Getters and setters
        public String getWorkerId() { return workerId; }
        public void setWorkerId(String workerId) { this.workerId = workerId; }
        public String getWorkerName() { return workerName; }
        public void setWorkerName(String workerName) { this.workerName = workerName; }
        public Date getDate() { return date; }
        public void setDate(Date date) { this.date = date; }
        public double getHours() { return hours; }
        public void setHours(double hours) { this.hours = hours; }
        public String getWorkType() { return workType; }
        public void setWorkType(String workType) { this.workType = workType; }
        public double getWage() { return wage; }
        public void setWage(double wage) { this.wage = wage; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
    }

    // Getters and setters
    public Date getStartDate() { return startDate; }
    public void setStartDate(Date startDate) { this.startDate = startDate; }
    public Date getEndDate() { return endDate; }
    public void setEndDate(Date endDate) { this.endDate = endDate; }
    public int getTotalWorkers() { return totalWorkers; }
    public void setTotalWorkers(int totalWorkers) { this.totalWorkers = totalWorkers; }
    public double getTotalHours() { return totalHours; }
    public void setTotalHours(double totalHours) { this.totalHours = totalHours; }
    public double getTotalWages() { return totalWages; }
    public void setTotalWages(double totalWages) { this.totalWages = totalWages; }
    public Map<String, Double> getHoursPerWorker() { return hoursPerWorker; }
    public void setHoursPerWorker(Map<String, Double> hoursPerWorker) { this.hoursPerWorker = hoursPerWorker; }
    public Map<String, Double> getWagesPerWorker() { return wagesPerWorker; }
    public void setWagesPerWorker(Map<String, Double> wagesPerWorker) { this.wagesPerWorker = wagesPerWorker; }
    public List<WorkEntry> getWorkEntries() { return workEntries; }
    public void setWorkEntries(List<WorkEntry> workEntries) { this.workEntries = workEntries; }
}
