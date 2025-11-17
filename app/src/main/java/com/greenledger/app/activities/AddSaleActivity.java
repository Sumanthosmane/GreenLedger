package com.greenledger.app.activities;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.greenledger.app.R;
import com.greenledger.app.models.Buyer;
import com.greenledger.app.models.Crop;
import com.greenledger.app.models.Farm;
import com.greenledger.app.models.Sale;
import com.greenledger.app.utils.FirebaseHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class AddSaleActivity extends AppCompatActivity {
    private MaterialToolbar toolbar;
    private TextInputEditText buyerInput;
    private AutoCompleteTextView cropDropdown;
    private AutoCompleteTextView unitDropdown;
    private TextInputEditText quantityInput;
    private TextInputEditText pricePerUnitInput;
    private TextInputEditText saleDateInput;
    private TextInputEditText notesInput;
    private MaterialButton saveButton;

    private FirebaseHelper firebaseHelper;
    private Map<String, Crop> cropsMap = new HashMap<>();
    private Calendar calendar = Calendar.getInstance();
    private SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

    private String selectedCropId;
    private String selectedCropName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_sale);

        firebaseHelper = FirebaseHelper.getInstance();

        initializeViews();
        setupToolbar();
        setupDropdowns();
        setupCalculation();
        setupSaveButton();
    }

    private void initializeViews() {
        toolbar = findViewById(R.id.toolbar);
        buyerInput = findViewById(R.id.buyerInput);
        cropDropdown = findViewById(R.id.cropDropdown);
        unitDropdown = findViewById(R.id.unitDropdown);
        quantityInput = findViewById(R.id.quantityInput);
        pricePerUnitInput = findViewById(R.id.pricePerUnitInput);
        saleDateInput = findViewById(R.id.saleDateInput);
        notesInput = findViewById(R.id.notesInput);
        saveButton = findViewById(R.id.saveButton);
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    private void setupDropdowns() {
        // Setup units dropdown
        String[] units = {"Kilogram", "Quintal", "Ton"};
        ArrayAdapter<String> unitAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, units);
        unitDropdown.setAdapter(unitAdapter);

        // Setup crops dropdown with 13 crop types
        String[] crops = getResources().getStringArray(R.array.crop_types);
        ArrayAdapter<String> cropAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, crops);
        cropDropdown.setAdapter(cropAdapter);

        // Setup date picker
        saleDateInput.setText(dateFormatter.format(calendar.getTime()));
        saleDateInput.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    AddSaleActivity.this,
                    (view, year, month, dayOfMonth) -> {
                        calendar.set(year, month, dayOfMonth);
                        saleDateInput.setText(dateFormatter.format(calendar.getTime()));
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
            );
            datePickerDialog.show();
        });

        // Setup crop selection listener
        cropDropdown.setOnItemClickListener((parent, view, position, id) -> {
            selectedCropName = parent.getItemAtPosition(position).toString();
            // Store selected crop
        });
    }

    // ...removed loadBuyers(), loadFarms(), setupCropDropdown(), loadCropsForFarm() - no longer needed...

    private void setupCalculation() {
        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                calculateTotal();
            }
        };

        quantityInput.addTextChangedListener(watcher);
        pricePerUnitInput.addTextChangedListener(watcher);
    }

    private void calculateTotal() {
        try {
            double quantity = Double.parseDouble(quantityInput.getText().toString());
            double pricePerUnit = Double.parseDouble(pricePerUnitInput.getText().toString());
            double total = quantity * pricePerUnit;
            // Update total display
        } catch (NumberFormatException e) {
            // Handle invalid input
        }
    }

    private void setupSaveButton() {
        saveButton.setOnClickListener(v -> saveSale());
    }

    private void saveSale() {
        if (!validateInputs()) {
            return;
        }

        String userId = firebaseHelper.getCurrentUserId();
        if (userId == null) {
            Toast.makeText(this, "User not authenticated", Toast.LENGTH_SHORT).show();
            return;
        }

        Sale sale = new Sale();
        sale.setUserId(userId);
        sale.setBuyerName(buyerInput.getText().toString().trim());
        sale.setQuantity(Double.parseDouble(quantityInput.getText().toString()));
        sale.setPricePerUnit(Double.parseDouble(pricePerUnitInput.getText().toString()));
        sale.setUnit(unitDropdown.getText().toString());
        sale.setTotalAmount(sale.getQuantity() * sale.getPricePerUnit());
        sale.setSaleDate(calendar.getTime());
        sale.setNotes(notesInput.getText().toString());
        sale.setCropId(selectedCropName);

        String saleId = firebaseHelper.getSalesRef().push().getKey();
        if (saleId != null) {
            sale.setId(saleId);
            firebaseHelper.getSalesRef().child(saleId).setValue(sale)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(this, "Sale saved successfully", Toast.LENGTH_SHORT).show();
                        finish();
                    })
                    .addOnFailureListener(e ->
                            Toast.makeText(this, "Failed to save sale", Toast.LENGTH_SHORT).show());
        }
    }

    private boolean validateInputs() {
        String buyerName = buyerInput.getText().toString().trim();
        String quantity = quantityInput.getText().toString().trim();
        String crop = cropDropdown.getText().toString().trim();
        String unit = unitDropdown.getText().toString().trim();
        String price = pricePerUnitInput.getText().toString().trim();
        String date = saleDateInput.getText().toString().trim();

        if (buyerName.isEmpty()) {
            Toast.makeText(this, "Please enter buyer name", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (crop.isEmpty()) {
            Toast.makeText(this, "Please select a crop", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (quantity.isEmpty()) {
            Toast.makeText(this, "Please enter quantity", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (unit.isEmpty()) {
            Toast.makeText(this, "Please select unit", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (price.isEmpty()) {
            Toast.makeText(this, "Please enter price per unit", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (date.isEmpty()) {
            Toast.makeText(this, "Please select date", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
