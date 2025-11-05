package com.greenledger.app.activities;

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

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class AddSaleActivity extends AppCompatActivity {
    private MaterialToolbar toolbar;
    private AutoCompleteTextView buyerDropdown;
    private AutoCompleteTextView farmDropdown;
    private AutoCompleteTextView cropDropdown;
    private AutoCompleteTextView unitDropdown;
    private TextInputEditText quantityInput;
    private TextInputEditText pricePerUnitInput;
    private TextInputEditText notesInput;
    private MaterialButton saveButton;

    private FirebaseHelper firebaseHelper;
    private Map<String, Buyer> buyersMap = new HashMap<>();
    private Map<String, Farm> farmsMap = new HashMap<>();
    private Map<String, Crop> cropsMap = new HashMap<>();

    private String selectedBuyerId;
    private String selectedFarmId;
    private String selectedCropId;

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
        buyerDropdown = findViewById(R.id.buyerDropdown);
        farmDropdown = findViewById(R.id.farmDropdown);
        cropDropdown = findViewById(R.id.cropDropdown);
        unitDropdown = findViewById(R.id.unitDropdown);
        quantityInput = findViewById(R.id.quantityInput);
        pricePerUnitInput = findViewById(R.id.pricePerUnitInput);
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

        // Load buyers
        loadBuyers();
        // Load farms
        loadFarms();
        // Load crops (will be filtered based on selected farm)
        setupCropDropdown();
    }

    private void loadBuyers() {
        firebaseHelper.getBuyersRef().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                buyersMap.clear();
                List<String> buyerNames = new ArrayList<>();

                for (DataSnapshot buyerSnapshot : snapshot.getChildren()) {
                    Buyer buyer = buyerSnapshot.getValue(Buyer.class);
                    if (buyer != null) {
                        buyersMap.put(buyer.getId(), buyer);
                        buyerNames.add(buyer.getName());
                    }
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<>(AddSaleActivity.this,
                        android.R.layout.simple_dropdown_item_1line, buyerNames);
                buyerDropdown.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AddSaleActivity.this,
                        "Failed to load buyers", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadFarms() {
        firebaseHelper.getFarmsRef().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                farmsMap.clear();
                List<String> farmNames = new ArrayList<>();

                for (DataSnapshot farmSnapshot : snapshot.getChildren()) {
                    Farm farm = farmSnapshot.getValue(Farm.class);
                    if (farm != null) {
                        farmsMap.put(farm.getFarmId(), farm);
                        farmNames.add(farm.getName());
                    }
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<>(AddSaleActivity.this,
                        android.R.layout.simple_dropdown_item_1line, farmNames);
                farmDropdown.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AddSaleActivity.this,
                        "Failed to load farms", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupCropDropdown() {
        farmDropdown.setOnItemClickListener((parent, view, position, id) -> {
            String selectedFarmName = parent.getItemAtPosition(position).toString();
            for (Farm farm : farmsMap.values()) {
                if (farm.getName().equals(selectedFarmName)) {
                    selectedFarmId = farm.getFarmId();
                    loadCropsForFarm(selectedFarmId);
                    break;
                }
            }
        });
    }

    private void loadCropsForFarm(String farmId) {
        firebaseHelper.getFarmCropsRef(farmId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                cropsMap.clear();
                List<String> cropNames = new ArrayList<>();

                for (DataSnapshot cropSnapshot : snapshot.getChildren()) {
                    Crop crop = cropSnapshot.getValue(Crop.class);
                    if (crop != null) {
                        cropsMap.put(crop.getCropId(), crop);
                        cropNames.add(crop.getName());
                    }
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<>(AddSaleActivity.this,
                        android.R.layout.simple_dropdown_item_1line, cropNames);
                cropDropdown.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AddSaleActivity.this,
                        "Failed to load crops", Toast.LENGTH_SHORT).show();
            }
        });
    }

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

        Sale sale = new Sale();
        sale.setBuyerId(selectedBuyerId);
        sale.setFarmId(selectedFarmId);
        sale.setCropId(selectedCropId);
        sale.setQuantity(Double.parseDouble(quantityInput.getText().toString()));
        sale.setPricePerUnit(Double.parseDouble(pricePerUnitInput.getText().toString()));
        sale.setUnit(unitDropdown.getText().toString());
        sale.setTotalAmount(sale.getQuantity() * sale.getPricePerUnit());
        sale.setSaleDate(new Date());
        sale.setNotes(notesInput.getText().toString());

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
        // Add validation logic
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
