package com.greenledger.app.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.PieData;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.tabs.TabLayout;
import com.greenledger.app.R;
import com.greenledger.app.reports.ReportGenerator;
import com.greenledger.app.utils.CSVExporter;
import com.greenledger.app.utils.FirebaseHelper;

import java.io.File;
import java.util.Calendar;

public class ReportActivity extends AppCompatActivity {
    private MaterialToolbar toolbar;
    private TabLayout tabLayout;
    private BarChart revenueChart;
    private PieChart expenseChart;
    private BarChart cropYieldChart;
    private BarChart cropRevenueChart;
    private FirebaseHelper firebaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        firebaseHelper = FirebaseHelper.getInstance();

        initializeViews();
        setupTabs();
        loadInitialReports();

        // Set up toolbar
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void initializeViews() {
        toolbar = findViewById(R.id.toolbar);
        tabLayout = findViewById(R.id.tabLayout);
        revenueChart = findViewById(R.id.revenueChart);
        expenseChart = findViewById(R.id.expenseChart);
        cropYieldChart = findViewById(R.id.cropYieldChart);
        cropRevenueChart = findViewById(R.id.cropRevenueChart);

        setupCharts();
    }


    private void setupTabs() {
        tabLayout.addTab(tabLayout.newTab().setText("Revenue"));
        tabLayout.addTab(tabLayout.newTab().setText("Expenses"));
        tabLayout.addTab(tabLayout.newTab().setText("Crop Yield"));
        tabLayout.addTab(tabLayout.newTab().setText("Crop Revenue"));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                updateChartVisibility(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
    }

    private void setupCharts() {
        // Revenue Chart
        Description revenueDesc = new Description();
        revenueDesc.setText("Monthly Revenue");
        revenueChart.setDescription(revenueDesc);
        revenueChart.setDrawGridBackground(false);

        // Expense Chart
        Description expenseDesc = new Description();
        expenseDesc.setText("Expense Distribution");
        expenseChart.setDescription(expenseDesc);
        expenseChart.setHoleRadius(35f);
        expenseChart.setTransparentCircleRadius(40f);

        // Configure legend for expense chart
        com.github.mikephil.charting.components.Legend expenseLegend = expenseChart.getLegend();
        expenseLegend.setVerticalAlignment(com.github.mikephil.charting.components.Legend.LegendVerticalAlignment.TOP);
        expenseLegend.setHorizontalAlignment(com.github.mikephil.charting.components.Legend.LegendHorizontalAlignment.LEFT);
        expenseLegend.setOrientation(com.github.mikephil.charting.components.Legend.LegendOrientation.VERTICAL);
        expenseLegend.setTextSize(13f);
        expenseLegend.setDrawInside(false);
        expenseLegend.setWordWrapEnabled(true);
        expenseLegend.setEnabled(true);

        // Crop Yield Chart
        Description yieldDesc = new Description();
        yieldDesc.setText("Crop Yields");
        cropYieldChart.setDescription(yieldDesc);
        cropYieldChart.setDrawGridBackground(false);

        // Crop Revenue Chart
        Description cropRevenueDesc = new Description();
        cropRevenueDesc.setText("Revenue by Crop");
        cropRevenueChart.setDescription(cropRevenueDesc);
        cropRevenueChart.setDrawGridBackground(false);

        // Configure legend for crop revenue chart with colors
        com.github.mikephil.charting.components.Legend cropRevenueLegend = cropRevenueChart.getLegend();
        cropRevenueLegend.setVerticalAlignment(com.github.mikephil.charting.components.Legend.LegendVerticalAlignment.TOP);
        cropRevenueLegend.setHorizontalAlignment(com.github.mikephil.charting.components.Legend.LegendHorizontalAlignment.RIGHT);
        cropRevenueLegend.setOrientation(com.github.mikephil.charting.components.Legend.LegendOrientation.VERTICAL);
        cropRevenueLegend.setTextSize(12f);
        cropRevenueLegend.setDrawInside(false);
        cropRevenueLegend.setWordWrapEnabled(true);
        cropRevenueLegend.setEnabled(true);
    }


    private void loadInitialReports() {
        String userId = firebaseHelper.getCurrentUserId();
        if (userId == null) {
            Toast.makeText(this, "User not authenticated", Toast.LENGTH_SHORT).show();
            return;
        }

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, -6); // Last 6 months
        long startDate = cal.getTimeInMillis();
        long endDate = System.currentTimeMillis();

        // Load all reports with error handling
        loadRevenueReport(userId, startDate, endDate);
        loadExpenseReport(userId);
        loadCropYieldReport(userId);
        loadCropRevenueReport(userId);
    }

    private void loadRevenueReport(String userId, long startDate, long endDate) {
        ReportGenerator.generateRevenueReport(this, userId, startDate, endDate,
                new ReportGenerator.ReportCallback<BarData>() {
                    @Override
                    public void onReportGenerated(BarData data) {
                        if (data != null) {
                            revenueChart.setData(data);
                            revenueChart.invalidate();
                        }
                    }

                    @Override
                    public void onError(String error) {
                        android.util.Log.e("ReportActivity", "Revenue error: " + error);
                        // Don't show toast for no data - just leave chart empty
                    }
                });
    }

    private void loadExpenseReport(String userId) {
        ReportGenerator.generateExpenseDistributionReport(this, userId,
                new ReportGenerator.ReportCallback<PieData>() {
                    @Override
                    public void onReportGenerated(PieData data) {
                        if (data != null) {
                            expenseChart.setData(data);
                            expenseChart.invalidate();
                        }
                    }

                    @Override
                    public void onError(String error) {
                        android.util.Log.e("ReportActivity", "Expense error: " + error);
                        // Don't show toast for no data - just leave chart empty
                    }
                });
    }

    private void loadCropYieldReport(String userId) {
        ReportGenerator.generateCropYieldReport(this, userId,
                new ReportGenerator.ReportCallback<BarData>() {
                    @Override
                    public void onReportGenerated(BarData data) {
                        if (data != null) {
                            cropYieldChart.setData(data);
                            cropYieldChart.invalidate();
                        }
                    }

                    @Override
                    public void onError(String error) {
                        android.util.Log.e("ReportActivity", "Crop yield error: " + error);
                        // Don't show toast for no data - just leave chart empty
                    }
                });
    }

    private void loadCropRevenueReport(String userId) {
        ReportGenerator.generateCropRevenueReport(this, userId,
                new ReportGenerator.ReportCallback<BarData>() {
                    @Override
                    public void onReportGenerated(BarData data) {
                        if (data != null) {
                            cropRevenueChart.setData(data);
                            cropRevenueChart.invalidate();
                        }
                    }

                    @Override
                    public void onError(String error) {
                        android.util.Log.e("ReportActivity", "Crop revenue error: " + error);
                        // Don't show toast for no data - just leave chart empty
                    }
                });
    }

    private void updateChartVisibility(int position) {
        revenueChart.setVisibility(position == 0 ? View.VISIBLE : View.GONE);
        expenseChart.setVisibility(position == 1 ? View.VISIBLE : View.GONE);
        cropYieldChart.setVisibility(position == 2 ? View.VISIBLE : View.GONE);
        cropRevenueChart.setVisibility(position == 3 ? View.VISIBLE : View.GONE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_report, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        if (item.getItemId() == R.id.action_export) {
            exportCurrentReport();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void exportCurrentReport() {
        int currentTab = tabLayout.getSelectedTabPosition();
        switch (currentTab) {
            case 0: // Revenue
                exportSalesData();
                break;
            case 1: // Expenses
                exportExpenseData();
                break;
            case 2: // Crop Yield
                exportCropData();
                break;
            case 3: // Crop Revenue
                exportSalesData(); // Same as revenue - sales data
                break;
        }
    }

    private void exportSalesData() {
        firebaseHelper.getSalesRef().get()
                .addOnSuccessListener(dataSnapshot ->
                    CSVExporter.exportSalesData(this, dataSnapshot, new CSVExporter.ExportCallback() {
                        @Override
                        public void onExportComplete(File file) {
                            shareExcelFile(file);
                        }

                        @Override
                        public void onError(String error) {
                            Toast.makeText(ReportActivity.this,
                                    "Export failed: " + error, Toast.LENGTH_SHORT).show();
                        }
                    }))
                .addOnFailureListener(e ->
                    Toast.makeText(this, "Failed to fetch sales data", Toast.LENGTH_SHORT).show());
    }

    private void exportExpenseData() {
        firebaseHelper.getExpensesRef().get()
                .addOnSuccessListener(dataSnapshot ->
                    CSVExporter.exportExpenseData(this, dataSnapshot, new CSVExporter.ExportCallback() {
                        @Override
                        public void onExportComplete(File file) {
                            shareExcelFile(file);
                        }

                        @Override
                        public void onError(String error) {
                            Toast.makeText(ReportActivity.this,
                                    "Export failed: " + error, Toast.LENGTH_SHORT).show();
                        }
                    }))
                .addOnFailureListener(e ->
                    Toast.makeText(this, "Failed to fetch expense data", Toast.LENGTH_SHORT).show());
    }

    private void exportCropData() {
        firebaseHelper.getCropsRef().get()
                .addOnSuccessListener(dataSnapshot ->
                    CSVExporter.exportCropData(this, dataSnapshot, new CSVExporter.ExportCallback() {
                        @Override
                        public void onExportComplete(File file) {
                            shareExcelFile(file);
                        }

                        @Override
                        public void onError(String error) {
                            Toast.makeText(ReportActivity.this,
                                    "Export failed: " + error, Toast.LENGTH_SHORT).show();
                        }
                    }))
                .addOnFailureListener(e ->
                    Toast.makeText(this, "Failed to fetch crop data", Toast.LENGTH_SHORT).show());
    }

    private void shareExcelFile(File file) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/csv");
        Uri uri = Uri.fromFile(file);
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        startActivity(Intent.createChooser(shareIntent, "Share Report"));
    }
}
