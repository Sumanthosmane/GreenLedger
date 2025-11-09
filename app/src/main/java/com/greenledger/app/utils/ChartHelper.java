package com.greenledger.app.utils;

import android.content.Context;
import android.graphics.Color;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.List;

public class ChartHelper {
    private Context context;

    public ChartHelper(Context context) {
        this.context = context;
    }

    public LineChart createLineChart(List<Entry> entries, String label, List<String> xAxisLabels) {
        LineChart lineChart = new LineChart(context);
        LineDataSet dataSet = new LineDataSet(entries, label);
        styleLineDataSet(dataSet);

        LineData lineData = new LineData(dataSet);
        lineChart.setData(lineData);
        styleLineChart(lineChart, xAxisLabels);

        return lineChart;
    }

    public PieChart createPieChart(List<PieEntry> entries, String label) {
        PieChart pieChart = new PieChart(context);
        PieDataSet dataSet = new PieDataSet(entries, label);
        stylePieDataSet(dataSet);

        PieData pieData = new PieData(dataSet);
        pieChart.setData(pieData);
        stylePieChart(pieChart);

        return pieChart;
    }

    public BarChart createBarChart(List<BarEntry> entries, String label, List<String> xAxisLabels) {
        BarChart barChart = new BarChart(context);
        BarDataSet dataSet = new BarDataSet(entries, label);
        styleBarDataSet(dataSet);

        BarData barData = new BarData(dataSet);
        barChart.setData(barData);
        styleBarChart(barChart, xAxisLabels);

        return barChart;
    }

    private void styleLineDataSet(LineDataSet dataSet) {
        dataSet.setColor(Color.BLUE);
        dataSet.setCircleColor(Color.BLUE);
        dataSet.setDrawCircles(true);
        dataSet.setDrawValues(true);
        dataSet.setLineWidth(2f);
        dataSet.setValueTextSize(12f);
    }

    private void styleLineChart(LineChart chart, List<String> xAxisLabels) {
        chart.getDescription().setEnabled(false);
        chart.setDrawGridBackground(false);
        chart.setDrawBorders(false);

        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(xAxisLabels));
        xAxis.setGranularity(1f);
        xAxis.setLabelRotationAngle(45);

        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setDrawGridLines(true);
        leftAxis.setDrawLabels(true);

        chart.getAxisRight().setEnabled(false);
        chart.getLegend().setEnabled(true);
        chart.animateY(1000);
    }

    private void stylePieDataSet(PieDataSet dataSet) {
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        dataSet.setValueTextSize(12f);
        dataSet.setValueTextColor(Color.WHITE);
        dataSet.setSliceSpace(3f);
    }

    private void stylePieChart(PieChart chart) {
        chart.getDescription().setEnabled(false);
        chart.setExtraOffsets(5, 10, 5, 5);
        chart.setDragDecelerationFrictionCoef(0.95f);
        chart.setDrawHoleEnabled(true);
        chart.setHoleColor(Color.WHITE);
        chart.setTransparentCircleColor(Color.WHITE);
        chart.setTransparentCircleAlpha(110);
        chart.setHoleRadius(58f);
        chart.setTransparentCircleRadius(61f);
        chart.setDrawCenterText(true);
        chart.setRotationAngle(0);
        chart.setRotationEnabled(true);
        chart.animateY(1000);
    }

    private void styleBarDataSet(BarDataSet dataSet) {
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        dataSet.setValueTextSize(12f);
        dataSet.setValueTextColor(Color.BLACK);
    }

    private void styleBarChart(BarChart chart, List<String> xAxisLabels) {
        chart.getDescription().setEnabled(false);
        chart.setDrawGridBackground(false);
        chart.setDrawBorders(false);

        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(xAxisLabels));
        xAxis.setGranularity(1f);
        xAxis.setLabelRotationAngle(45);

        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setDrawGridLines(true);
        leftAxis.setDrawLabels(true);

        chart.getAxisRight().setEnabled(false);
        chart.getLegend().setEnabled(true);
        chart.animateY(1000);
    }
}
