package com.greenledger.app.utils;

import android.content.Context;
import android.graphics.Color;
import android.os.Environment;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.greenledger.app.models.reports.ProfitLossReport;
import com.greenledger.app.models.reports.CropYieldReport;
import com.greenledger.app.models.reports.LabourReport;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ReportGenerator {
    private Context context;
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

    public ReportGenerator(Context context) {
        this.context = context;
    }

    public File generatePDF(Object report) throws IOException {
        File pdfFile = createOutputFile("pdf");
        PdfWriter writer = new PdfWriter(pdfFile);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        if (report instanceof ProfitLossReport) {
            generatePLReportPDF((ProfitLossReport) report, document);
        } else if (report instanceof CropYieldReport) {
            generateCropYieldReportPDF((CropYieldReport) report, document);
        } else if (report instanceof LabourReport) {
            generateLabourReportPDF((LabourReport) report, document);
        }

        document.close();
        return pdfFile;
    }

    public File generateExcel(Object report) throws IOException {
        File excelFile = createOutputFile("xlsx");
        Workbook workbook = new XSSFWorkbook();

        if (report instanceof ProfitLossReport) {
            generatePLReportExcel((ProfitLossReport) report, workbook);
        } else if (report instanceof CropYieldReport) {
            generateCropYieldReportExcel((CropYieldReport) report, workbook);
        } else if (report instanceof LabourReport) {
            generateLabourReportExcel((LabourReport) report, workbook);
        }

        FileOutputStream fos = new FileOutputStream(excelFile);
        workbook.write(fos);
        workbook.close();
        fos.close();
        return excelFile;
    }

    private void generatePLReportPDF(ProfitLossReport report, Document document) {
        document.add(new Paragraph("Profit & Loss Report"));
        document.add(new Paragraph("Period: " + DATE_FORMAT.format(report.getStartDate()) +
                                 " to " + DATE_FORMAT.format(report.getEndDate())));
        document.add(new Paragraph("Total Revenue: ₹" + report.getTotalRevenue()));
        document.add(new Paragraph("Total Expenses: ₹" + report.getTotalExpenses()));
        document.add(new Paragraph("Net Profit: ₹" + report.getNetProfit()));

        // Add transaction table
        Table table = new Table(4);
        table.addCell("Date");
        table.addCell("Description");
        table.addCell("Category");
        table.addCell("Amount");

        for (ProfitLossReport.Transaction transaction : report.getTransactions()) {
            table.addCell(DATE_FORMAT.format(transaction.getDate()));
            table.addCell(transaction.getDescription());
            table.addCell(transaction.getCategory());
            table.addCell("₹" + transaction.getAmount());
        }
        document.add(table);
    }

    private void generatePLReportExcel(ProfitLossReport report, Workbook workbook) {
        Sheet sheet = workbook.createSheet("P&L Report");
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Date");
        headerRow.createCell(1).setCellValue("Description");
        headerRow.createCell(2).setCellValue("Category");
        headerRow.createCell(3).setCellValue("Amount");

        int rowNum = 1;
        for (ProfitLossReport.Transaction transaction : report.getTransactions()) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(DATE_FORMAT.format(transaction.getDate()));
            row.createCell(1).setCellValue(transaction.getDescription());
            row.createCell(2).setCellValue(transaction.getCategory());
            row.createCell(3).setCellValue(transaction.getAmount());
        }

        // Auto-size columns
        for (int i = 0; i < 4; i++) {
            sheet.autoSizeColumn(i);
        }
    }

    private void generateCropYieldReportPDF(CropYieldReport report, Document document) {
        // Similar implementation for Crop Yield Report
        document.add(new Paragraph("Crop Yield Report"));
        document.add(new Paragraph("Crop: " + report.getCropName()));
        document.add(new Paragraph("Harvest Date: " + DATE_FORMAT.format(report.getHarvestDate())));
        // Add more crop yield specific details...
    }

    private void generateCropYieldReportExcel(CropYieldReport report, Workbook workbook) {
        // Similar implementation for Crop Yield Report in Excel format
        Sheet sheet = workbook.createSheet("Crop Yield Report");
        // Add report specific data...
    }

    private void generateLabourReportPDF(LabourReport report, Document document) {
        // Similar implementation for Labour Report
        document.add(new Paragraph("Labour Report"));
        document.add(new Paragraph("Period: " + DATE_FORMAT.format(report.getStartDate()) +
                                 " to " + DATE_FORMAT.format(report.getEndDate())));
        // Add more labour report specific details...
    }

    private void generateLabourReportExcel(LabourReport report, Workbook workbook) {
        // Similar implementation for Labour Report in Excel format
        Sheet sheet = workbook.createSheet("Labour Report");
        // Add report specific data...
    }

    private File createOutputFile(String extension) throws IOException {
        File dir = new File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "reports");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        String fileName = "report_" + System.currentTimeMillis() + "." + extension;
        return new File(dir, fileName);
    }
}
