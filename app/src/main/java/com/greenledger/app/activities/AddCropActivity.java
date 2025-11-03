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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.greenledger.app.R;
import com.greenledger.app.models.Crop;
import com.greenledger.app.models.CropStage;
import com.greenledger.app.models.Farm;
import com.greenledger.app.models.embedded.CropInfo;
import com.greenledger.app.models.enums.CropStatus;
import com.greenledger.app.utils.FirebaseHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddCropActivity extends AppCompatActivity {
    private TextInputLayout cropTypeLayout;
    private TextInputLayout varietyLayout;
    private TextInputLayout categoryLayout;
    private TextInputLayout farmLayout;
    private TextInputLayout landLayout;
    private TextInputLayout expectedYieldLayout;
    private TextInputLayout unitLayout;
    private TextInputLayout expectedHarvestLayout;

    private AutoCompleteTextView cropType;
    private TextInputEditText variety;
    private AutoCompleteTextView category;
    private AutoCompleteTextView farm;
    private AutoCompleteTextView land;
    private TextInputEditText expectedYield;
    private AutoCompleteTextView unit;
    private TextInputEditText expectedHarvest;

    private MaterialButton saveButton;
    private MaterialButton cancelButton;

    private FirebaseHelper firebaseHelper;
    private Map<String, Farm> farmMap = new HashMap<>();

    // Temporary hard-coded types for demo
    private final String[] cropTypes = {
        "Rice", "Wheat", "Cotton", "Sugarcane", "Corn", "Soybean"
    };

    private final String[] categories = {
        "Kharif", "Rabi", "Zaid"
    };

    private final String[] units = {
        "kg", "quintal", "ton"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_crop);

        // Initialize Firebase
        firebaseHelper = FirebaseHelper.getInstance();

        // Set up action bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.add_crop);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Initialize views
        initViews();

        // Set up dropdowns
        setupDropdowns();

        // Load farms
        loadFarms();

        // Set up button listeners
        setupButtons();
    }

    private void initViews() {
        cropTypeLayout = findViewById(R.id.cropTypeLayout);
        varietyLayout = findViewById(R.id.varietyLayout);
        categoryLayout = findViewById(R.id.categoryLayout);
        farmLayout = findViewById(R.id.farmLayout);
        landLayout = findViewById(R.id.landLayout);
        expectedYieldLayout = findViewById(R.id.expectedYieldLayout);
        unitLayout = findViewById(R.id.unitLayout);
        expectedHarvestLayout = findViewById(R.id.expectedHarvestLayout);

        cropType = findViewById(R.id.cropType);
        variety = findViewById(R.id.variety);
        category = findViewById(R.id.category);
        farm = findViewById(R.id.farm);
        land = findViewById(R.id.land);
        expectedYield = findViewById(R.id.expectedYield);
        unit = findViewById(R.id.unit);
        expectedHarvest = findViewById(R.id.expectedHarvest);

        saveButton = findViewById(R.id.saveButton);
        cancelButton = findViewById(R.id.cancelButton);
    }

    private void setupDropdowns() {
        ArrayAdapter<String> cropAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, cropTypes);
        cropType.setAdapter(cropAdapter);

        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, categories);
        category.setAdapter(categoryAdapter);

        ArrayAdapter<String> unitAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, units);
        unit.setAdapter(unitAdapter);
    }

    private void loadFarms() {
        String userId = firebaseHelper.getCurrentUserId();
        if (userId == null) return;

        firebaseHelper.getFarmsByUserRef(userId)
            .addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    List<String> farmNames = new ArrayList<>();
                    farmMap.clear();

                    for (DataSnapshot farmSnapshot : snapshot.getChildren()) {
                        Farm farm = farmSnapshot.getValue(Farm.class);
                        if (farm != null && farm.getFarmDetails() != null) {
                            String farmName = farm.getFarmDetails().getName();
                            farmNames.add(farmName);
                            farmMap.put(farmName, farm);
                        }
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<>(AddCropActivity.this,
                            android.R.layout.simple_dropdown_item_1line, farmNames);
                    farm.setAdapter(adapter);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(AddCropActivity.this,
                            "Error loading farms: " + error.getMessage(),
                            Toast.LENGTH_SHORT).show();
                }
            });

        // Update land dropdown when farm is selected
        farm.setOnItemClickListener((parent, view, position, id) -> {
            String selectedFarm = farm.getText().toString();
            Farm farmObj = farmMap.get(selectedFarm);
            if (farmObj != null && farmObj.getLands() != null) {
                List<String> landNames = new ArrayList<>();
                for (com.greenledger.app.models.Land land : farmObj.getLands()) {
                    if (land.isAvailableForCrop()) {
                        landNames.add(land.getName());
                    }
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                        android.R.layout.simple_dropdown_item_1line, landNames);
                land.setAdapter(adapter);
            }
        });
    }

    private void setupButtons() {
        saveButton.setOnClickListener(v -> validateAndSave());
        cancelButton.setOnClickListener(v -> finish());
    }

    private void validateAndSave() {
        // Reset errors
        cropTypeLayout.setError(null);
        varietyLayout.setError(null);
        categoryLayout.setError(null);
        farmLayout.setError(null);
        landLayout.setError(null);
        expectedYieldLayout.setError(null);
        unitLayout.setError(null);
        expectedHarvestLayout.setError(null);

        // Get values
        String type = cropType.getText().toString().trim();
        String varietyStr = variety.getText().toString().trim();
        String categoryStr = category.getText().toString().trim();
        String farmName = farm.getText().toString().trim();
        String landName = land.getText().toString().trim();
        String yieldStr = expectedYield.getText().toString().trim();
        String unitStr = unit.getText().toString().trim();
        String harvestStr = expectedHarvest.getText().toString().trim();

        // Validate fields
        boolean isValid = true;

        if (TextUtils.isEmpty(type)) {
            cropTypeLayout.setError("Crop type is required");
            isValid = false;
        }

        if (TextUtils.isEmpty(varietyStr)) {
            varietyLayout.setError("Variety is required");
            isValid = false;
        }

        if (TextUtils.isEmpty(categoryStr)) {
            categoryLayout.setError("Category is required");
            isValid = false;
        }

        if (TextUtils.isEmpty(farmName)) {
            farmLayout.setError("Farm is required");
            isValid = false;
        }

        if (TextUtils.isEmpty(landName)) {
            landLayout.setError("Land plot is required");
            isValid = false;
        }

        double yieldValue = 0;
        if (TextUtils.isEmpty(yieldStr)) {
            expectedYieldLayout.setError("Expected yield is required");
            isValid = false;
        } else {
            try {
                yieldValue = Double.parseDouble(yieldStr);
                if (yieldValue <= 0) {
                    expectedYieldLayout.setError("Expected yield must be greater than 0");
                    isValid = false;
                }
            } catch (NumberFormatException e) {
                expectedYieldLayout.setError("Invalid yield value");
                isValid = false;
            }
        }

        if (TextUtils.isEmpty(unitStr)) {
            unitLayout.setError("Unit is required");
            isValid = false;
        }

        if (TextUtils.isEmpty(harvestStr)) {
            expectedHarvestLayout.setError("Expected harvest date is required");
            isValid = false;
        }

        if (!isValid) {
            return;
        }

        // Get farm and land IDs
        Farm selectedFarm = farmMap.get(farmName);
        if (selectedFarm == null) {
            Toast.makeText(this, "Selected farm not found", Toast.LENGTH_SHORT).show();
            return;
        }

        String landId = null;
        for (com.greenledger.app.models.Land l : selectedFarm.getLands()) {
            if (l.getName().equals(landName)) {
                landId = l.getLandId();
                break;
            }
        }

        if (landId == null) {
            Toast.makeText(this, "Selected land plot not found", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create crop object
        Crop crop = new Crop();

        // Set crop info
        CropInfo info = new CropInfo();
        info.setType(type);
        info.setVariety(varietyStr);
        info.setCategory(categoryStr);
        crop.setCropInfo(info);

        // Set references
        crop.setFarmId(selectedFarm.getFarmId());
        crop.setLandId(landId);

        // Set lifecycle
        Crop.CropLifecycle lifecycle = new Crop.CropLifecycle();
        lifecycle.setStatus(CropStatus.PLANNING);
        lifecycle.setSowingDate(System.currentTimeMillis());

        try {
            // Parse expected harvest date
            // TODO: Add proper date picker
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.MONTH, 3); // Default to 3 months from now
            lifecycle.setExpectedHarvestDate(cal.getTimeInMillis());
        } catch (Exception e) {
            Toast.makeText(this, "Invalid harvest date format", Toast.LENGTH_SHORT).show();
            return;
        }

        // Add initial planning stage
        List<CropStage> stages = new ArrayList<>();
        CropStage planningStage = new CropStage("Planning", System.currentTimeMillis());
        planningStage.setStatus(CropStatus.PLANNING);
        stages.add(planningStage);
        lifecycle.setStages(stages);

        crop.setLifecycle(lifecycle);

        // Set quantity
        Crop.Quantity quantity = new Crop.Quantity();
        quantity.setExpected(yieldValue);
        quantity.setUnit(unitStr);
        crop.setQuantity(quantity);

        // Initialize other properties
        crop.setQuality(null); // Will be set during harvest
        crop.setInventory(null); // Will be set after harvest
        crop.setStorage(null); // Will be set when stored
        crop.setFinancial(new Crop.Financial()); // Initialize empty financial data
        crop.setMetadata(new Crop.Metadata()); // Initialize metadata

        // Save to Firebase
        firebaseHelper.addCrop(crop, (error, ref) -> {
            if (error != null) {
                Toast.makeText(this, "Error saving crop: " + error.getMessage(),
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Crop added successfully", Toast.LENGTH_SHORT).show();
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
