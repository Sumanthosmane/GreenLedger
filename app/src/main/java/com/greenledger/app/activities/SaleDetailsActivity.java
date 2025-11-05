package com.greenledger.app.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.greenledger.app.R;
import com.greenledger.app.models.Buyer;
import com.greenledger.app.models.Crop;
import com.greenledger.app.models.Farm;
import com.greenledger.app.models.Sale;
import com.greenledger.app.utils.FirebaseHelper;
import com.greenledger.app.utils.PDFGenerator;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class SaleDetailsActivity extends AppCompatActivity {
    private MaterialToolbar toolbar;
    private TextView saleIdText;
    private TextView saleDateText;
    private TextView buyerNameText;
    private TextView businessNameText;
    private TextView contactText;
    private TextView cropNameText;
    private TextView farmNameText;
    private TextView quantityText;
    private TextView pricePerUnitText;
    private TextView totalAmountText;
    private MaterialButton generateInvoiceButton;
    private MaterialButton shareInvoiceButton;
    private FirebaseHelper firebaseHelper;
    private Sale sale;
    private SimpleDateFormat dateFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sale_details);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            getOnBackInvokedDispatcher().registerOnBackInvokedCallback(
                android.window.OnBackInvokedDispatcher.PRIORITY_DEFAULT,
                this::finish
            );
        }

        String saleId = getIntent().getStringExtra("saleId");
        if (saleId == null) {
            finish();
            return;
        }

        firebaseHelper = FirebaseHelper.getInstance();
        dateFormat = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());

        initializeViews();
        setupToolbar();
        loadSaleData(saleId);
        setupButtonListeners();
    }

    private void initializeViews() {
        toolbar = findViewById(R.id.toolbar);
        saleIdText = findViewById(R.id.saleIdText);
        saleDateText = findViewById(R.id.saleDateText);
        buyerNameText = findViewById(R.id.buyerNameText);
        businessNameText = findViewById(R.id.businessNameText);
        contactText = findViewById(R.id.contactText);
        cropNameText = findViewById(R.id.cropNameText);
        farmNameText = findViewById(R.id.farmNameText);
        quantityText = findViewById(R.id.quantityText);
        pricePerUnitText = findViewById(R.id.pricePerUnitText);
        totalAmountText = findViewById(R.id.totalAmountText);
        generateInvoiceButton = findViewById(R.id.generateInvoiceButton);
        shareInvoiceButton = findViewById(R.id.shareInvoiceButton);
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    private void loadSaleData(String saleId) {
        firebaseHelper.getSalesRef().child(saleId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                sale = snapshot.getValue(Sale.class);
                if (sale != null) {
                    displaySaleData();
                    loadBuyerData(sale.getBuyerId());
                    loadCropData(sale.getCropId());
                    loadFarmData(sale.getFarmId());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(SaleDetailsActivity.this,
                        "Failed to load sale data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displaySaleData() {
        saleIdText.setText(getString(R.string.sale_id_format,
                getString(R.string.sales_management), sale.getId()));
        saleDateText.setText(dateFormat.format(sale.getSaleDate()));
        quantityText.setText(String.format(Locale.getDefault(),
                "%.2f %s", sale.getQuantity(), sale.getUnit()));
        pricePerUnitText.setText(String.format(Locale.getDefault(),
                "₹%.2f per %s", sale.getPricePerUnit(), sale.getUnit()));
        totalAmountText.setText(String.format(Locale.getDefault(),
                "₹%.2f", sale.getTotalAmount()));
    }

    private void loadBuyerData(String buyerId) {
        firebaseHelper.getBuyersRef().child(buyerId).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Buyer buyer = snapshot.getValue(Buyer.class);
                        if (buyer != null) {
                            buyerNameText.setText(buyer.getName());
                            businessNameText.setText(buyer.getBusinessName());
                            contactText.setText(buyer.getContactNumber());
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(SaleDetailsActivity.this,
                                "Failed to load buyer data", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void loadCropData(String cropId) {
        firebaseHelper.getCropsRef().child(cropId).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Crop crop = snapshot.getValue(Crop.class);
                        if (crop != null) {
                            cropNameText.setText(crop.getName());
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(SaleDetailsActivity.this,
                                "Failed to load crop data", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void loadFarmData(String farmId) {
        firebaseHelper.getFarmsRef().child(farmId).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Farm farm = snapshot.getValue(Farm.class);
                        if (farm != null) {
                            farmNameText.setText(farm.getName());
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(SaleDetailsActivity.this,
                                "Failed to load farm data", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void setupButtonListeners() {
        generateInvoiceButton.setOnClickListener(v -> generateInvoice());
        shareInvoiceButton.setOnClickListener(v -> shareInvoice());
    }

    private void generateInvoice() {
        if (sale != null) {
            PDFGenerator.generateSaleInvoice(this, sale, pdfFile -> {
                if (pdfFile != null) {
                    sale.setInvoiceGenerated(true);
                    sale.setInvoicePdfUrl(pdfFile.getAbsolutePath());
                    firebaseHelper.getSalesRef().child(sale.getId()).setValue(sale);
                    Toast.makeText(this, R.string.invoice_generated, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void shareInvoice() {
        if (sale != null && sale.isInvoiceGenerated() && sale.getInvoicePdfUrl() != null) {
            File pdfFile = new File(sale.getInvoicePdfUrl());
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("application/pdf");
            Uri pdfUri = Uri.fromFile(pdfFile);
            shareIntent.putExtra(Intent.EXTRA_STREAM, pdfUri);
            startActivity(Intent.createChooser(shareIntent, getString(R.string.share_invoice)));
        } else {
            Toast.makeText(this, "Please generate the invoice first", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

}
