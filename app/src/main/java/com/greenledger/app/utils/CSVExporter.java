package com.greenledger.app.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.greenledger.app.models.Sale;
import com.greenledger.app.models.Expense;
import com.greenledger.app.models.Crop;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class CSVExporter {
    private static final String TAG = "CSVExporter";
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());

    public interface ExportCallback {
        void onExportComplete(File file);
        void onError(String error);
    }

    public static void exportSalesData(Context context, DataSnapshot salesData, ExportCallback callback) {
        try {
            // Create file
            File file = new File(context.getFilesDir(), "sales_data.csv");
            FileWriter fileWriter = new FileWriter(file);

            // Create CSV printer
            CSVFormat csvFormat = CSVFormat.DEFAULT.builder()
                    .setHeader("Sale ID", "Date", "Buyer", "Farm", "Crop", "Quantity", "Unit", "Price/Unit", "Total")
                    .build();

            try (CSVPrinter printer = new CSVPrinter(fileWriter, csvFormat)) {
                // Add data rows
                for (DataSnapshot snapshot : salesData.getChildren()) {
                    Sale sale = snapshot.getValue(Sale.class);
                    if (sale != null) {
                        printer.printRecord(
                            sale.getId(),
                            dateFormat.format(sale.getSaleDate()),
                            sale.getBuyerId(),
                            sale.getFarmId(),
                            sale.getCropId(),
                            sale.getQuantity(),
                            sale.getUnit(),
                            sale.getPricePerUnit(),
                            sale.getTotalAmount()
                        );
                    }
                }
            }

            new Handler(Looper.getMainLooper()).post(() -> callback.onExportComplete(file));

        } catch (IOException e) {
            Log.e(TAG, "Error exporting sales data", e);
            new Handler(Looper.getMainLooper()).post(() ->
                callback.onError("Failed to export data: " + e.getMessage()));
        }
    }

    public static void exportExpenseData(Context context, DataSnapshot expenseData, ExportCallback callback) {
        try {
            // Create file
            File file = new File(context.getFilesDir(), "expense_data.csv");
            FileWriter fileWriter = new FileWriter(file);

            // Create CSV printer
            CSVFormat csvFormat = CSVFormat.DEFAULT.builder()
                    .setHeader("ID", "Date", "Category", "Amount", "Description")
                    .build();

            try (CSVPrinter printer = new CSVPrinter(fileWriter, csvFormat)) {
                // Add data rows
                for (DataSnapshot snapshot : expenseData.getChildren()) {
                    Expense expense = snapshot.getValue(Expense.class);
                    if (expense != null) {
                        printer.printRecord(
                            expense.getId(),
                            dateFormat.format(expense.getDate()),
                            expense.getCategory(),
                            expense.getAmount(),
                            expense.getDescription()
                        );
                    }
                }
            }

            new Handler(Looper.getMainLooper()).post(() -> callback.onExportComplete(file));

        } catch (IOException e) {
            Log.e(TAG, "Error exporting expense data", e);
            new Handler(Looper.getMainLooper()).post(() ->
                callback.onError("Failed to export data: " + e.getMessage()));
        }
    }

    public static void exportCropData(Context context, DataSnapshot cropData, ExportCallback callback) {
        try {
            // Create file
            File file = new File(context.getFilesDir(), "crop_data.csv");
            FileWriter fileWriter = new FileWriter(file);

            // Create CSV printer
            CSVFormat csvFormat = CSVFormat.DEFAULT.builder()
                    .setHeader("ID", "Type", "Variety", "Farm", "Expected Yield", "Actual Yield", "Status")
                    .build();

            try (CSVPrinter printer = new CSVPrinter(fileWriter, csvFormat)) {
                // Add data rows
                for (DataSnapshot snapshot : cropData.getChildren()) {
                    Crop crop = snapshot.getValue(Crop.class);
                    if (crop != null && crop.getCropInfo() != null) {
                        printer.printRecord(
                            crop.getCropId(),
                            crop.getCropInfo().getType(),
                            crop.getCropInfo().getVariety(),
                            crop.getFarmId(),
                            crop.getQuantity() != null ? crop.getQuantity().getExpected() : "",
                            crop.getQuantity() != null ? crop.getQuantity().getActual() : "",
                            crop.getLifecycle() != null ? crop.getLifecycle().getStatus().toString() : ""
                        );
                    }
                }
            }

            new Handler(Looper.getMainLooper()).post(() -> callback.onExportComplete(file));

        } catch (IOException e) {
            Log.e(TAG, "Error exporting crop data", e);
            new Handler(Looper.getMainLooper()).post(() ->
                callback.onError("Failed to export data: " + e.getMessage()));
        }
    }
}
