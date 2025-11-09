package com.greenledger.app.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieEntry;
import com.greenledger.app.R;
import com.greenledger.app.utils.ChartHelper;
import com.greenledger.app.utils.ReportGenerator;

import java.util.ArrayList;
import java.util.List;

public class AnalyticsDashboardActivity extends AppCompatActivity {
    private ChartHelper chartHelper;
    private ReportGenerator reportGenerator;
    private ProgressBar progressBar;
    private TextView tvTotalRevenue;
    private TextView tvTotalExpenses;
    private TextView tvNetProfit;
    private CardView cardExpenseChart;
    private CardView cardRevenueChart;
    private CardView cardLabourChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analytics_dashboard);

        initializeViews();
        setupCharts();
        loadDashboardData();
    }

    private void initializeViews() {
        progressBar = findViewById(R.id.progressBar);
        tvTotalRevenue = findViewById(R.id.tvTotalRevenue);
        tvTotalExpenses = findViewById(R.id.tvTotalExpenses);
        tvNetProfit = findViewById(R.id.tvNetProfit);
        cardExpenseChart = findViewById(R.id.cardExpenseChart);
        cardRevenueChart = findViewById(R.id.cardRevenueChart);
        cardLabourChart = findViewById(R.id.cardLabourChart);

        chartHelper = new ChartHelper(this);
        reportGenerator = new ReportGenerator(this);
    }

    private void setupCharts() {
        // Example data - replace with real data from your database
        setupExpenseChart();
        setupRevenueChart();
        setupLabourChart();
    }

    private void setupExpenseChart() {
        List<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(30f, "Labour"));
        entries.add(new PieEntry(20f, "Seeds"));
        entries.add(new PieEntry(25f, "Fertilizers"));
        entries.add(new PieEntry(15f, "Equipment"));
        entries.add(new PieEntry(10f, "Others"));

        PieChart pieChart = chartHelper.createPieChart(entries, "Expense Distribution");
        cardExpenseChart.addView(pieChart);
    }

    private void setupRevenueChart() {
        List<Entry> entries = new ArrayList<>();
        List<String> labels = new ArrayList<>();

        // Add example data points
        entries.add(new Entry(0, 30000f));
        entries.add(new Entry(1, 45000f));
        entries.add(new Entry(2, 35000f));
        entries.add(new Entry(3, 50000f));
        entries.add(new Entry(4, 60000f));
        entries.add(new Entry(5, 40000f));

        labels.add("Jan");
        labels.add("Feb");
        labels.add("Mar");
        labels.add("Apr");
        labels.add("May");
        labels.add("Jun");

        LineChart lineChart = chartHelper.createLineChart(entries, "Monthly Revenue", labels);
        cardRevenueChart.addView(lineChart);
    }

    private void setupLabourChart() {
        List<BarEntry> entries = new ArrayList<>();
        List<String> labels = new ArrayList<>();

        entries.add(new BarEntry(0, 20f));
        entries.add(new BarEntry(1, 15f));
        entries.add(new BarEntry(2, 30f));
        entries.add(new BarEntry(3, 25f));
        entries.add(new BarEntry(4, 10f));

        labels.add("Plowing");
        labels.add("Sowing");
        labels.add("Harvesting");
        labels.add("Maintenance");
        labels.add("Other");

        BarChart barChart = chartHelper.createBarChart(entries, "Labour Hours by Type", labels);
        cardLabourChart.addView(barChart);
    }

    private void loadDashboardData() {
        progressBar.setVisibility(View.VISIBLE);

        // Example static data - replace with real data from your database
        double totalRevenue = 200000.0;
        double totalExpenses = 150000.0;
        double netProfit = totalRevenue - totalExpenses;

        tvTotalRevenue.setText(String.format("₹%.2f", totalRevenue));
        tvTotalExpenses.setText(String.format("₹%.2f", totalExpenses));
        tvNetProfit.setText(String.format("₹%.2f", netProfit));

        progressBar.setVisibility(View.GONE);
    }

    public void onGenerateReportClick(View view) {
        try {
            // Example: Generate P&L report
            // Replace with actual report generation logic
            Toast.makeText(this, "Generating report...", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "Error generating report: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void onExportDataClick(View view) {
        try {
            // Example: Export data to Excel
            // Replace with actual export logic
            Toast.makeText(this, "Exporting data...", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "Error exporting data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
