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
    private FirebaseHelper firebaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        firebaseHelper = FirebaseHelper.getInstance();

        initializeViews();
        setupToolbar();
        setupTabs();
        loadInitialReports();
    }

    private void initializeViews() {
        toolbar = findViewById(R.id.toolbar);
        tabLayout = findViewById(R.id.tabLayout);
        revenueChart = findViewById(R.id.revenueChart);
        expenseChart = findViewById(R.id.expenseChart);
        cropYieldChart = findViewById(R.id.cropYieldChart);

        setupCharts();
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    private void setupTabs() {
        tabLayout.addTab(tabLayout.newTab().setText("Revenue"));
        tabLayout.addTab(tabLayout.newTab().setText("Expenses"));
        tabLayout.addTab(tabLayout.newTab().setText("Crop Yield"));

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

        // Crop Yield Chart
        Description yieldDesc = new Description();
        yieldDesc.setText("Crop Yields");
        cropYieldChart.setDescription(yieldDesc);
        cropYieldChart.setDrawGridBackground(false);
    }

    private void loadInitialReports() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, -6); // Last 6 months
        long startDate = cal.getTimeInMillis();
        long endDate = System.currentTimeMillis();

        loadRevenueReport(startDate, endDate);
        loadExpenseReport();
        loadCropYieldReport();
    }

    private void loadRevenueReport(long startDate, long endDate) {
        ReportGenerator.generateRevenueReport(this, startDate, endDate,
                new ReportGenerator.ReportCallback<BarData>() {
                    @Override
                    public void onReportGenerated(BarData data) {
                        revenueChart.setData(data);
                        revenueChart.invalidate();
                    }

                    @Override
                    public void onError(String error) {
                        Toast.makeText(ReportActivity.this,
                                "Failed to load revenue data", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void loadExpenseReport() {
        ReportGenerator.generateExpenseDistributionReport(this,
                new ReportGenerator.ReportCallback<PieData>() {
                    @Override
                    public void onReportGenerated(PieData data) {
                        expenseChart.setData(data);
                        expenseChart.invalidate();
                    }

                    @Override
                    public void onError(String error) {
                        Toast.makeText(ReportActivity.this,
                                "Failed to load expense data", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void loadCropYieldReport() {
        ReportGenerator.generateCropYieldReport(this,
                new ReportGenerator.ReportCallback<BarData>() {
                    @Override
                    public void onReportGenerated(BarData data) {
                        cropYieldChart.setData(data);
                        cropYieldChart.invalidate();
                    }

                    @Override
                    public void onError(String error) {
                        Toast.makeText(ReportActivity.this,
                                "Failed to load crop yield data", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void updateChartVisibility(int position) {
        revenueChart.setVisibility(position == 0 ? View.VISIBLE : View.GONE);
        expenseChart.setVisibility(position == 1 ? View.VISIBLE : View.GONE);
        cropYieldChart.setVisibility(position == 2 ? View.VISIBLE : View.GONE);
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
