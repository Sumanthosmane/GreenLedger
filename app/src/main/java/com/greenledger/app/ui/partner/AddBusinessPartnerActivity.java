package com.greenledger.app.ui.partner;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.greenledger.app.R;
import com.greenledger.app.managers.BusinessPartnerManager;
import com.greenledger.app.models.BusinessPartner;

public class AddBusinessPartnerActivity extends AppCompatActivity {
    private TextInputEditText businessNameEdit;
    private TextInputEditText gstNumberEdit;
    private TextInputEditText panNumberEdit;
    private BusinessPartnerManager partnerManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_business_partner);

        initializeViews();
        setupActionBar();
    }

    private void initializeViews() {
        businessNameEdit = findViewById(R.id.businessNameEdit);
        gstNumberEdit = findViewById(R.id.gstNumberEdit);
        panNumberEdit = findViewById(R.id.panNumberEdit);

        partnerManager = new BusinessPartnerManager();
    }

    private void setupActionBar() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Add Business Partner");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void savePartner() {
        String businessName = businessNameEdit.getText().toString().trim();
        String gstNumber = gstNumberEdit.getText().toString().trim();
        String panNumber = panNumberEdit.getText().toString().trim();

        if (businessName.isEmpty()) {
            businessNameEdit.setError("Business name is required");
            return;
        }

        BusinessPartner partner = new BusinessPartner();
        partner.setFarmerId(FirebaseAuth.getInstance().getCurrentUser().getUid());

        BusinessPartner.PartnerInfo partnerInfo = new BusinessPartner.PartnerInfo();
        partnerInfo.setBusinessName(businessName);
        partnerInfo.setGstNumber(gstNumber);
        partnerInfo.setPanNumber(panNumber);
        partner.setPartnerInfo(partnerInfo);

        BusinessPartner.Metadata metadata = new BusinessPartner.Metadata();
        metadata.setCreatedAt(System.currentTimeMillis());
        partner.setMetadata(metadata);

        partnerManager.createBusinessPartner(partner)
            .addOnSuccessListener(aVoid -> {
                Toast.makeText(this, "Partner added successfully", Toast.LENGTH_SHORT).show();
                finish();
            })
            .addOnFailureListener(e -> {
                Toast.makeText(this, "Failed to add partner: " + e.getMessage(),
                    Toast.LENGTH_SHORT).show();
            });
    }
}
