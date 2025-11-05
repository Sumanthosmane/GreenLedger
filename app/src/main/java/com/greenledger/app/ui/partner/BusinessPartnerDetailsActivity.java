package com.greenledger.app.ui.partner;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.greenledger.app.R;
import com.greenledger.app.managers.BusinessPartnerManager;
import com.greenledger.app.models.BusinessPartner;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class BusinessPartnerDetailsActivity extends AppCompatActivity {
    private TextView businessNameText;
    private TextView typeText;
    private TextView gstNumberText;
    private TextView panNumberText;
    private TextView transactionInfoText;
    private TextView ratingText;
    private BusinessPartnerManager partnerManager;
    private String partnerId;
    private BusinessPartner currentPartner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_partner_details);

        partnerId = getIntent().getStringExtra("partnerId");
        if (partnerId == null) {
            finish();
            return;
        }

        initializeViews();
        setupActionBar();
        loadPartnerDetails();
    }

    private void initializeViews() {
        businessNameText = findViewById(R.id.businessNameText);
        typeText = findViewById(R.id.typeText);
        gstNumberText = findViewById(R.id.gstNumberText);
        panNumberText = findViewById(R.id.panNumberText);
        transactionInfoText = findViewById(R.id.transactionInfoText);
        ratingText = findViewById(R.id.ratingText);

        partnerManager = new BusinessPartnerManager();
    }

    private void setupActionBar() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Partner Details");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void loadPartnerDetails() {
        partnerManager.getPartnerById(partnerId, new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                currentPartner = dataSnapshot.getValue(BusinessPartner.class);
                if (currentPartner != null) {
                    updateUI(currentPartner);
                } else {
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                finish();
            }
        });
    }

    private void updateUI(BusinessPartner partner) {
        BusinessPartner.PartnerInfo info = partner.getPartnerInfo();
        if (info != null) {
            businessNameText.setText(info.getBusinessName());
            typeText.setText(info.getType());
            gstNumberText.setText(info.getGstNumber());
            panNumberText.setText(info.getPanNumber());
        }

        BusinessPartner.TransactionHistory history = partner.getTransactionHistory();
        if (history != null) {
            String transactionInfo = String.format(Locale.getDefault(),
                "Total Transactions: %d\nTotal Amount: ₹%.2f\nOutstanding: ₹%.2f",
                history.getTotalPurchases(),
                history.getTotalAmount(),
                history.getOutstandingAmount());
            transactionInfoText.setText(transactionInfo);

            SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
            String lastPurchase = "Last Purchase: " +
                (history.getLastPurchaseDate() > 0 ?
                sdf.format(history.getLastPurchaseDate()) : "Never");
            // Add last purchase date to transaction info
        }

        BusinessPartner.Metadata metadata = partner.getMetadata();
        if (metadata != null) {
            String rating = String.format(Locale.getDefault(), "%.1f ★", metadata.getRating());
            ratingText.setText(rating);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_business_partner_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
            return true;
        } else if (id == R.id.action_delete) {
            confirmDelete();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void confirmDelete() {
        new AlertDialog.Builder(this)
            .setTitle("Delete Partner")
            .setMessage("Are you sure you want to delete this business partner?")
            .setPositiveButton("Delete", (dialog, which) -> deletePartner())
            .setNegativeButton("Cancel", null)
            .show();
    }

    private void deletePartner() {
        if (partnerId != null) {
            partnerManager.deleteBusinessPartner(partnerId)
                .addOnSuccessListener(aVoid -> finish())
                .addOnFailureListener(e -> {
                    // Handle error
                });
        }
    }
}
