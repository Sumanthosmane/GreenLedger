package com.greenledger.app.activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.greenledger.app.R;
import com.greenledger.app.models.Farm;
import com.greenledger.app.models.embedded.Location;
import com.greenledger.app.utils.FirebaseHelper;

import java.util.ArrayList;

public class AddFarmActivity extends AppCompatActivity {
    private TextInputLayout farmNameLayout;
    private TextInputLayout totalAreaLayout;
    private TextInputLayout soilTypeLayout;
    private TextInputLayout irrigationTypeLayout;
    private TextInputLayout addressLayout;

    private TextInputEditText farmName;
    private TextInputEditText totalArea;
    private AutoCompleteTextView soilType;
    private AutoCompleteTextView irrigationType;
    private TextInputEditText address;

    private MaterialButton pickLocationButton;
    private MaterialButton saveButton;
    private MaterialButton cancelButton;

    private FirebaseHelper firebaseHelper;

    // Temporary hard-coded types for demo
    private final String[] soilTypes = {
        "Alluvial", "Black", "Red", "Laterite", "Mountainous", "Desert"
    };

    private final String[] irrigationTypes = {
        "Drip", "Sprinkler", "Flood", "Furrow", "Basin", "Border Strip"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_farm);

        // Initialize Firebase
        firebaseHelper = FirebaseHelper.getInstance();

        // Set up action bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.add_farm);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Initialize views
        initViews();

        // Set up dropdowns
        setupDropdowns();

        // Set up button listeners
        setupButtons();
    }

    private void initViews() {
        farmNameLayout = findViewById(R.id.farmNameLayout);
        totalAreaLayout = findViewById(R.id.totalAreaLayout);
        soilTypeLayout = findViewById(R.id.soilTypeLayout);
        irrigationTypeLayout = findViewById(R.id.irrigationTypeLayout);
        addressLayout = findViewById(R.id.addressLayout);

        farmName = findViewById(R.id.farmName);
        totalArea = findViewById(R.id.totalArea);
        soilType = findViewById(R.id.soilType);
        irrigationType = findViewById(R.id.irrigationType);
        address = findViewById(R.id.address);

        pickLocationButton = findViewById(R.id.pickLocationButton);
        saveButton = findViewById(R.id.saveButton);
        cancelButton = findViewById(R.id.cancelButton);
    }

    private void setupDropdowns() {
        ArrayAdapter<String> soilAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, soilTypes);
        soilType.setAdapter(soilAdapter);

        ArrayAdapter<String> irrigationAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, irrigationTypes);
        irrigationType.setAdapter(irrigationAdapter);
    }

    private void setupButtons() {
        saveButton.setOnClickListener(v -> validateAndSave());
        cancelButton.setOnClickListener(v -> finish());
        pickLocationButton.setOnClickListener(v -> {
            // TODO: Implement location picker
            Toast.makeText(this, "Location picker coming soon!", Toast.LENGTH_SHORT).show();
        });
    }

    private void validateAndSave() {
        // Reset errors
        farmNameLayout.setError(null);
        totalAreaLayout.setError(null);
        soilTypeLayout.setError(null);
        irrigationTypeLayout.setError(null);
        addressLayout.setError(null);

        // Get values
        String name = farmName.getText().toString().trim();
        String areaStr = totalArea.getText().toString().trim();
        String soil = soilType.getText().toString().trim();
        String irrigation = irrigationType.getText().toString().trim();
        String addressStr = address.getText().toString().trim();

        // Validate fields
        boolean isValid = true;

        if (TextUtils.isEmpty(name)) {
            farmNameLayout.setError("Farm name is required");
            isValid = false;
        }

        double areaValue = 0;
        if (TextUtils.isEmpty(areaStr)) {
            totalAreaLayout.setError("Total area is required");
            isValid = false;
        } else {
            try {
                areaValue = Double.parseDouble(areaStr);
                if (areaValue <= 0) {
                    totalAreaLayout.setError("Area must be greater than 0");
                    isValid = false;
                }
            } catch (NumberFormatException e) {
                totalAreaLayout.setError("Invalid area value");
                isValid = false;
            }
        }

        if (TextUtils.isEmpty(soil)) {
            soilTypeLayout.setError("Soil type is required");
            isValid = false;
        }

        if (TextUtils.isEmpty(irrigation)) {
            irrigationTypeLayout.setError("Irrigation type is required");
            isValid = false;
        }

        if (TextUtils.isEmpty(addressStr)) {
            addressLayout.setError("Address is required");
            isValid = false;
        }

        if (!isValid) {
            return;
        }

        // Create farm object
        Farm farm = new Farm();
        Farm.FarmDetails details = new Farm.FarmDetails();
        details.setName(name);
        details.setTotalArea(areaValue);
        details.setSoilType(soil);
        details.setIrrigationType(irrigation);

        // Set location (for now just using address, later will add GPS)
        Location location = new Location();
        location.setAddress(addressStr);
        details.setLocation(location);

        farm.setFarmDetails(details);
        farm.setLands(new ArrayList<>()); // Initialize empty lands list
        farm.setMetadata(new Farm.Metadata()); // Initialize metadata

        // Save to Firebase
        firebaseHelper.addFarm(farm, (error, ref) -> {
            if (error != null) {
                Toast.makeText(this, "Error saving farm: " + error.getMessage(),
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Farm added successfully", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
