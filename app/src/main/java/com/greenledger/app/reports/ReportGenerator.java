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
import com.greenledger.app.models.Crop;
import com.greenledger.app.utils.FirebaseHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReportGenerator {
    public interface ReportCallback<T> {
        void onReportGenerated(T data);
        void onError(String error);
    }

    // Revenue Report
    public static void generateRevenueReport(Context context, long startDate, long endDate,
                                           ReportCallback<BarData> callback) {
        FirebaseHelper.getInstance().getSalesRef()
                .orderByChild("saleDate")
                .startAt(startDate)
                .endAt(endDate)
                .get()
                .addOnSuccessListener(dataSnapshot -> {
                    List<BarEntry> entries = new ArrayList<>();
                    Map<String, Float> monthlyRevenue = new HashMap<>();

                    for (var snapshot : dataSnapshot.getChildren()) {
                        Sale sale = snapshot.getValue(Sale.class);
                        if (sale != null) {
                            String month = String.valueOf(sale.getSaleDate().getMonth());
                            float revenue = monthlyRevenue.getOrDefault(month, 0f);
                            revenue += sale.getTotalAmount();
                            monthlyRevenue.put(month, revenue);
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

    // Expense Distribution Report
    public static void generateExpenseDistributionReport(Context context,
                                                       ReportCallback<PieData> callback) {
        FirebaseHelper.getInstance().getExpensesRef()
                .get()
                .addOnSuccessListener(dataSnapshot -> {
                    Map<String, Float> categoryExpenses = new HashMap<>();

                    for (var snapshot : dataSnapshot.getChildren()) {
                        Expense expense = snapshot.getValue(Expense.class);
                        if (expense != null) {
                            String category = expense.getCategory();
                            float amount = categoryExpenses.getOrDefault(category, 0f);
                            amount += expense.getAmount();
                            categoryExpenses.put(category, amount);
                        }
                    }

                    List<PieEntry> entries = new ArrayList<>();
                    for (Map.Entry<String, Float> entry : categoryExpenses.entrySet()) {
                        entries.add(new PieEntry(entry.getValue(), entry.getKey()));
                    }

                    PieDataSet dataSet = new PieDataSet(entries, "Expense Distribution");
                    PieData pieData = new PieData(dataSet);
                    callback.onReportGenerated(pieData);
                })
                .addOnFailureListener(e -> callback.onError(e.getMessage()));
    }

    // Crop Yield Report
    public static void generateCropYieldReport(Context context,
                                             ReportCallback<BarData> callback) {
        FirebaseHelper.getInstance().getCropsRef()
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
}
