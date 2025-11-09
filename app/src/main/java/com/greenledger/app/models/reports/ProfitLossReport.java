package com.greenledger.app.models.reports;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class ProfitLossReport {
    private Date startDate;
    private Date endDate;
    private double totalRevenue;
    private double totalExpenses;
    private double netProfit;
    private Map<String, Double> revenueByCategory;
    private Map<String, Double> expensesByCategory;
    private List<Transaction> transactions;

    public static class Transaction {
        private Date date;
        private String description;
        private String category;
        private double amount;
        private String type; // INCOME or EXPENSE

        // Getters and setters
        public Date getDate() { return date; }
        public void setDate(Date date) { this.date = date; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public String getCategory() { return category; }
        public void setCategory(String category) { this.category = category; }
        public double getAmount() { return amount; }
        public void setAmount(double amount) { this.amount = amount; }
        public String getType() { return type; }
        public void setType(String type) { this.type = type; }
    }

    // Getters and setters
    public Date getStartDate() { return startDate; }
    public void setStartDate(Date startDate) { this.startDate = startDate; }
    public Date getEndDate() { return endDate; }
    public void setEndDate(Date endDate) { this.endDate = endDate; }
    public double getTotalRevenue() { return totalRevenue; }
    public void setTotalRevenue(double totalRevenue) { this.totalRevenue = totalRevenue; }
    public double getTotalExpenses() { return totalExpenses; }
    public void setTotalExpenses(double totalExpenses) { this.totalExpenses = totalExpenses; }
    public double getNetProfit() { return netProfit; }
    public void setNetProfit(double netProfit) { this.netProfit = netProfit; }
    public Map<String, Double> getRevenueByCategory() { return revenueByCategory; }
    public void setRevenueByCategory(Map<String, Double> revenueByCategory) { this.revenueByCategory = revenueByCategory; }
    public Map<String, Double> getExpensesByCategory() { return expensesByCategory; }
    public void setExpensesByCategory(Map<String, Double> expensesByCategory) { this.expensesByCategory = expensesByCategory; }
    public List<Transaction> getTransactions() { return transactions; }
    public void setTransactions(List<Transaction> transactions) { this.transactions = transactions; }
}
