package com.greenledger.app.reports;

import android.content.Context;

import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.greenledger.app.models.Sale;
import com.greenledger.app.models.Expense;
import com.greenledger.app.models.RawMaterial;
import com.greenledger.app.models.Labour;
import com.greenledger.app.models.Crop;
import com.greenledger.app.utils.FirebaseHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ReportGenerator {
    public interface ReportCallback<T> {
        void onReportGenerated(T data);
        void onError(String error);
    }

    // Predefined colors for pie chart slices
    private static final int[] PIE_COLORS = {
        0xFF4CAF50, // Green
        0xFFFF9800, // Orange
        0xFF2196F3, // Blue
        0xFFF44336, // Red
        0xFF9C27B0, // Purple
        0xFF00BCD4, // Cyan
        0xFFFFEB3B, // Yellow
        0xFFFF5722, // Deep Orange
        0xFF673AB7, // Deep Purple
        0xFF3F51B5, // Indigo
        0xFF009688, // Teal
        0xFFC0CA33  // Lime
    };

    // ...existing code...


    // Revenue Report - User-specific data
    public static void generateRevenueReport(Context context, String userId, long startDate, long endDate,
                                           ReportCallback<BarData> callback) {
        if (userId == null) {
            callback.onError("User ID is required");
            return;
        }

        FirebaseHelper.getInstance().getSalesRef()
                .orderByChild("userId")
                .equalTo(userId)
                .get()
                .addOnSuccessListener(dataSnapshot -> {
                    List<BarEntry> entries = new ArrayList<>();
                    Map<String, Float> monthlyRevenue = new HashMap<>();

                    for (var snapshot : dataSnapshot.getChildren()) {
                        Sale sale = snapshot.getValue(Sale.class);
                        if (sale != null && sale.getSaleDate() != null) {
                            // Filter by date range if applicable
                            long saleTimestamp = sale.getSaleDate().getTime();
                            if (saleTimestamp >= startDate && saleTimestamp <= endDate) {
                                String month = String.valueOf(sale.getSaleDate().getMonth());
                                float revenue = monthlyRevenue.getOrDefault(month, 0f);
                                revenue += sale.getTotalAmount();
                                monthlyRevenue.put(month, revenue);
                            }
                        }
                    }

                    int i = 0;
                    for (Map.Entry<String, Float> entry : monthlyRevenue.entrySet()) {
                        entries.add(new BarEntry(i++, entry.getValue()));
                    }

                    BarDataSet dataSet = new BarDataSet(entries, "Monthly Revenue");
                    BarData barData = new BarData(dataSet);
                    callback.onReportGenerated(barData);
                })
                .addOnFailureListener(e -> callback.onError(e.getMessage()));
    }

    // Expense Distribution Report - Shows total from 3 main sections: Expenses, Raw Materials, Labour
    // Filters data by userId to ensure each user sees only their own data
    public static void generateExpenseDistributionReport(Context context, String userId,
                                                       ReportCallback<PieData> callback) {
        if (userId == null) {
            callback.onError("User ID is required");
            return;
        }

        // Get total expenses for current user
        FirebaseHelper.getInstance().getExpensesRef()
                .orderByChild("userId")
                .equalTo(userId)
                .get()
                .addOnSuccessListener(expenseSnapshot -> {
                    float totalExpensesAmount = 0;
                    for (var snapshot : expenseSnapshot.getChildren()) {
                        Expense expense = snapshot.getValue(Expense.class);
                        if (expense != null) {
                            totalExpensesAmount += expense.getAmount();
                        }
                    }
                    final float totalExpenses = totalExpensesAmount;

                    // Get total raw materials for current user
                    FirebaseHelper.getInstance().getRawMaterialsRef()
                            .orderByChild("userId")
                            .equalTo(userId)
                            .get()
                            .addOnSuccessListener(materialSnapshot -> {
                                float totalMaterialsAmount = 0;
                                for (var snapshot : materialSnapshot.getChildren()) {
                                    RawMaterial material = snapshot.getValue(RawMaterial.class);
                                    if (material != null) {
                                        // Calculate total cost: quantity * costPerUnit
                                        totalMaterialsAmount += (material.getQuantity() * material.getCostPerUnit());
                                    }
                                }
                                final float totalMaterials = totalMaterialsAmount;

                                // Get total labour costs for current user
                                FirebaseHelper.getInstance().getLabourRef()
                                        .orderByChild("userId")
                                        .equalTo(userId)
                                        .get()
                                        .addOnSuccessListener(labourSnapshot -> {
                                            float totalLabour = 0;
                                            for (var snapshot : labourSnapshot.getChildren()) {
                                                Labour labour = snapshot.getValue(Labour.class);
                                                if (labour != null) {
                                                    totalLabour += labour.getTotalPay();
                                                }
                                            }

                                            // Create pie entries for the 3 main sections
                                            List<PieEntry> entries = new ArrayList<>();
                                            final float grandTotal = totalExpenses + totalMaterials + totalLabour;

                                            if (grandTotal > 0) {
                                                // Add Expenses section
                                                float expensePercentage = (totalExpenses / grandTotal) * 100;
                                                String expenseLabel = String.format("Expense Management: ₹%.0f (%.1f%%)",
                                                        totalExpenses, expensePercentage);
                                                entries.add(new PieEntry(totalExpenses, expenseLabel));

                                                // Add Raw Materials section
                                                float materialPercentage = (totalMaterials / grandTotal) * 100;
                                                String materialLabel = String.format("Raw Materials: ₹%.0f (%.1f%%)",
                                                        totalMaterials, materialPercentage);
                                                entries.add(new PieEntry(totalMaterials, materialLabel));

                                                // Add Labour section
                                                float labourPercentage = (totalLabour / grandTotal) * 100;
                                                String labourLabel = String.format("Labour Management: ₹%.0f (%.1f%%)",
                                                        totalLabour, labourPercentage);
                                                entries.add(new PieEntry(totalLabour, labourLabel));
                                            }

                                            PieDataSet dataSet = new PieDataSet(entries, "Cost Distribution");

                                            // Set colors for the 3 sections
                                            List<Integer> colors = new ArrayList<>();
                                            colors.add(PIE_COLORS[0]); // Green for Expenses
                                            colors.add(PIE_COLORS[1]); // Orange for Raw Materials
                                            colors.add(PIE_COLORS[2]); // Blue for Labour
                                            dataSet.setColors(colors);

                                            // Additional styling
                                            dataSet.setSliceSpace(2f);
                                            dataSet.setValueTextSize(11f);
                                            dataSet.setDrawValues(false); // Hide values on pie slices

                                            PieData pieData = new PieData(dataSet);
                                            callback.onReportGenerated(pieData);
                                        })
                                        .addOnFailureListener(e -> callback.onError(e.getMessage()));
                            })
                            .addOnFailureListener(e -> callback.onError(e.getMessage()));
                })
                .addOnFailureListener(e -> callback.onError(e.getMessage()));
    }

    // Crop Yield Report - User-specific data
    public static void generateCropYieldReport(Context context, String userId,
                                             ReportCallback<BarData> callback) {
        if (userId == null) {
            callback.onError("User ID is required");
            return;
        }

        FirebaseHelper.getInstance().getCropsRef()
                .orderByChild("userId")
                .equalTo(userId)
                .get()
                .addOnSuccessListener(dataSnapshot -> {
                    Map<String, Float> cropYields = new HashMap<>();

                    for (var snapshot : dataSnapshot.getChildren()) {
                        Crop crop = snapshot.getValue(Crop.class);
                        if (crop != null && crop.getQuantity() != null) {
                            String cropType = crop.getCropInfo().getType();
                            float yield = cropYields.getOrDefault(cropType, 0f);
                            yield += crop.getQuantity().getActual();
                            cropYields.put(cropType, yield);
                        }
                    }

                    List<BarEntry> entries = new ArrayList<>();
                    int i = 0;
                    for (Map.Entry<String, Float> entry : cropYields.entrySet()) {
                        entries.add(new BarEntry(i++, entry.getValue()));
                    }

                    BarDataSet dataSet = new BarDataSet(entries, "Crop Yields");
                    BarData barData = new BarData(dataSet);
                    callback.onReportGenerated(barData);
                })
                .addOnFailureListener(e -> callback.onError(e.getMessage()));
    }

    // Crop Revenue Report - Shows revenue generated by each crop through sales
    // Each crop has different color for easy identification
    public static void generateCropRevenueReport(Context context, String userId,
                                                ReportCallback<BarData> callback) {
        if (userId == null) {
            callback.onError("User ID is required");
            return;
        }

        try {
            // Get all sales for current user
            FirebaseHelper.getInstance().getSalesRef()
                    .orderByChild("userId")
                    .equalTo(userId)
                    .get()
                    .addOnSuccessListener(dataSnapshot -> {
                        try {
                            // Map to store crop names and their revenue
                            Map<String, Float> cropRevenue = new LinkedHashMap<>();
                            // Map to store crop index for color assignment
                            Map<String, Integer> cropIndexMap = new LinkedHashMap<>();

                            int cropIndex = 0;

                            for (var snapshot : dataSnapshot.getChildren()) {
                                Sale sale = snapshot.getValue(Sale.class);
                                if (sale != null && sale.getCropId() != null && sale.getTotalAmount() > 0) {
                                    String cropName = sale.getCropId();  // Using cropId as crop name

                                    // Add crop to index map if new
                                    if (!cropIndexMap.containsKey(cropName)) {
                                        cropIndexMap.put(cropName, cropIndex++);
                                    }

                                    // Add revenue for this crop
                                    float revenue = cropRevenue.getOrDefault(cropName, 0f);
                                    revenue += (float) sale.getTotalAmount();
                                    cropRevenue.put(cropName, revenue);
                                }
                            }

                            if (cropRevenue.isEmpty()) {
                                // Return empty BarData instead of error
                                BarDataSet dataSet = new BarDataSet(new ArrayList<BarEntry>(), "Crop Revenue");
                                BarData barData = new BarData(dataSet);
                                callback.onReportGenerated(barData);
                                return;
                            }

                            // Create bar entries for each crop
                            List<BarEntry> entries = new ArrayList<>();
                            List<String> cropLabels = new ArrayList<>();
                            List<Integer> colors = new ArrayList<>();

                            int index = 0;
                            for (Map.Entry<String, Float> entry : cropRevenue.entrySet()) {
                                entries.add(new BarEntry(index, entry.getValue()));
                                cropLabels.add(entry.getKey());
                                // Assign color from PIE_COLORS array (cycling through colors)
                                colors.add(PIE_COLORS[index % PIE_COLORS.length]);
                                index++;
                            }

                            // Create dataset with multiple colors
                            BarDataSet dataSet = new BarDataSet(entries, "Crop Revenue");
                            dataSet.setColors(colors);
                            dataSet.setValueTextSize(10f);

                            // Create BarData
                            BarData barData = new BarData(dataSet);
                            callback.onReportGenerated(barData);
                        } catch (Exception e) {
                            android.util.Log.e("ReportGenerator", "Error processing crop revenue data", e);
                            callback.onError("Error processing data: " + e.getMessage());
                        }
                    })
                    .addOnFailureListener(e -> {
                        android.util.Log.e("ReportGenerator", "Firebase error: " + e.getMessage(), e);
                        callback.onError("Error loading sales data");
                    });
        } catch (Exception e) {
            android.util.Log.e("ReportGenerator", "Unexpected error", e);
            callback.onError("Unexpected error: " + e.getMessage());
        }
    }
}
