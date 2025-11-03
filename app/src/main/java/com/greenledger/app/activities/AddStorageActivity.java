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
import com.google.firebase.database.DatabaseReference;
import com.greenledger.app.R;
import com.greenledger.app.models.Storage;
import com.greenledger.app.models.enums.StorageType;
import com.greenledger.app.utils.FirebaseHelper;

public class AddStorageActivity extends AppCompatActivity {
    private TextInputLayout storageNameLayout;
    private TextInputLayout storageTypeLayout;
    private TextInputLayout capacityLayout;
    private TextInputLayout locationLayout;

    private TextInputEditText storageName;
    private AutoCompleteTextView storageType;
    private TextInputEditText capacity;
    private TextInputEditText location;

    private MaterialButton saveButton;
    private MaterialButton cancelButton;

    private FirebaseHelper firebaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_storage);

        // Initialize Firebase
        firebaseHelper = FirebaseHelper.getInstance();

        // Set up action bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.add_storage);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Initialize views
        initViews();

        // Set up storage type dropdown
        setupStorageTypeDropdown();

        // Set up button listeners
        setupButtons();
    }

    private void initViews() {
        storageNameLayout = findViewById(R.id.storageNameLayout);
        storageTypeLayout = findViewById(R.id.storageTypeLayout);
        capacityLayout = findViewById(R.id.capacityLayout);
        locationLayout = findViewById(R.id.locationLayout);

        storageName = findViewById(R.id.storageName);
        storageType = findViewById(R.id.storageType);
        capacity = findViewById(R.id.capacity);
        location = findViewById(R.id.location);

        saveButton = findViewById(R.id.saveButton);
        cancelButton = findViewById(R.id.cancelButton);
    }

    private void setupStorageTypeDropdown() {
        String[] types = new String[StorageType.values().length];
        for (int i = 0; i < StorageType.values().length; i++) {
            types[i] = StorageType.values()[i].toString();
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, types);
        storageType.setAdapter(adapter);
    }

    private void setupButtons() {
        saveButton.setOnClickListener(v -> validateAndSave());
        cancelButton.setOnClickListener(v -> finish());
    }

    private void validateAndSave() {
        // Reset errors
        storageNameLayout.setError(null);
        storageTypeLayout.setError(null);
        capacityLayout.setError(null);
        locationLayout.setError(null);

        // Get values
        String name = storageName.getText().toString().trim();
        String type = storageType.getText().toString().trim();
        String capacityStr = capacity.getText().toString().trim();
        String locationStr = location.getText().toString().trim();

        // Validate fields
        boolean isValid = true;

        if (TextUtils.isEmpty(name)) {
            storageNameLayout.setError("Storage name is required");
            isValid = false;
        }

        if (TextUtils.isEmpty(type)) {
            storageTypeLayout.setError("Storage type is required");
            isValid = false;
        }

        if (TextUtils.isEmpty(capacityStr)) {
            capacityLayout.setError("Capacity is required");
            isValid = false;
        }

        double capacityValue = 0;
        try {
            capacityValue = Double.parseDouble(capacityStr);
            if (capacityValue <= 0) {
                capacityLayout.setError("Capacity must be greater than 0");
                isValid = false;
            }
        } catch (NumberFormatException e) {
            capacityLayout.setError("Invalid capacity value");
            isValid = false;
        }

        if (TextUtils.isEmpty(locationStr)) {
            locationLayout.setError("Location is required");
            isValid = false;
        }

        if (!isValid) {
            return;
        }

        // Create storage object
        Storage storage = new Storage();
        Storage.StorageInfo info = new Storage.StorageInfo();
        info.setName(name);
        info.setType(StorageType.valueOf(type));
        info.setCapacity(capacityValue);
        info.setCurrentOccupancy(0); // New storage starts empty
        info.setLocation(locationStr);
        storage.setStorageInfo(info);

        // Initialize metadata
        storage.setMetadata(new Storage.Metadata());

        // Save to Firebase
        firebaseHelper.addStorage(storage, (error, ref) -> {
            if (error != null) {
                Toast.makeText(this, "Error saving storage: " + error.getMessage(),
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Storage added successfully", Toast.LENGTH_SHORT).show();
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
