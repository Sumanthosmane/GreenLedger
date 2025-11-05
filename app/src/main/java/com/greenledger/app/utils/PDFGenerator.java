package com.greenledger.app.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.greenledger.app.models.Sale;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class PDFGenerator {
    private static final String TAG = "PDFGenerator";
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());

    public interface PDFGenerationCallback {
        void onPDFGenerated(File pdfFile);
    }

    public static void generateSaleInvoice(Context context, Sale sale, PDFGenerationCallback callback) {
        try {
            // Create a new file in the app's files directory
            File pdfFile = new File(context.getFilesDir(), "invoice_" + sale.getId() + ".pdf");

            // Create PDF document
            PdfDocument document = new PdfDocument();

            // Create a page (A4 size: 595 x 842 points)
            PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(595, 842, 1).create();
            PdfDocument.Page page = document.startPage(pageInfo);
            Canvas canvas = page.getCanvas();

            // Draw content
            Paint paint = new Paint();
            paint.setColor(Color.BLACK);
            int y = 50;
            int leftMargin = 50;

            // Draw header
            paint.setTextSize(24);
            canvas.drawText("INVOICE", leftMargin, y, paint);
            y += 40;

            // Draw invoice details
            paint.setTextSize(12);
            canvas.drawText("Invoice No: " + sale.getId(), leftMargin, y, paint);
            y += 20;
            canvas.drawText("Date: " + dateFormat.format(sale.getSaleDate()), leftMargin, y, paint);
            y += 40;

            // Draw buyer info
            paint.setTextSize(14);
            canvas.drawText("Buyer Information", leftMargin, y, paint);
            y += 20;
            paint.setTextSize(12);
            canvas.drawText("Buyer ID: " + sale.getBuyerId(), leftMargin, y, paint);
            y += 40;

            // Draw sale details
            paint.setTextSize(14);
            canvas.drawText("Sale Details", leftMargin, y, paint);
            y += 20;
            paint.setTextSize(12);
            canvas.drawText("Farm: " + sale.getFarmId(), leftMargin, y, paint);
            y += 20;
            canvas.drawText("Crop: " + sale.getCropId(), leftMargin, y, paint);
            y += 20;
            canvas.drawText(String.format(Locale.getDefault(),
                    "Quantity: %.2f %s", sale.getQuantity(), sale.getUnit()),
                    leftMargin, y, paint);
            y += 20;
            canvas.drawText(String.format(Locale.getDefault(),
                    "Price per unit: ₹%.2f", sale.getPricePerUnit()),
                    leftMargin, y, paint);
            y += 40;

            // Draw total
            paint.setTextSize(16);
            paint.setFakeBoldText(true);
            canvas.drawText(String.format(Locale.getDefault(),
                    "Total Amount: ₹%.2f", sale.getTotalAmount()),
                    leftMargin, y, paint);
            y += 40;

            // Draw footer
            paint.setTextSize(10);
            paint.setFakeBoldText(false);
            canvas.drawText("This is a computer generated invoice", leftMargin, y, paint);

            // Finish the page
            document.finishPage(page);

            // Write the PDF file
            document.writeTo(new FileOutputStream(pdfFile));
            document.close();

            // Notify on main thread
            new Handler(Looper.getMainLooper()).post(() -> callback.onPDFGenerated(pdfFile));

        } catch (IOException e) {
            Log.e(TAG, "Error generating PDF", e);
            new Handler(Looper.getMainLooper()).post(() -> callback.onPDFGenerated(null));
        }
    }
}
