package com.greenledger.app.activities;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.greenledger.app.R;
import com.greenledger.app.adapters.RawMaterialAdapter;
import com.greenledger.app.models.RawMaterial;
import com.greenledger.app.utils.FirebaseHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class RawMaterialActivity extends AppCompatActivity {
    private MaterialToolbar toolbar;
    private RecyclerView recyclerView;
    private FloatingActionButton addMaterialFab;
    private RawMaterialAdapter adapter;
    private FirebaseHelper firebaseHelper;
    private Calendar calendar = Calendar.getInstance();
    private SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_raw_material);

        firebaseHelper = FirebaseHelper.getInstance();

        initializeViews();
        setupToolbar();
        setupRecyclerView();
        loadMaterials();

        addMaterialFab.setOnClickListener(v -> showAddMaterialDialog());
    }

    private void initializeViews() {
        toolbar = findViewById(R.id.toolbar);
        recyclerView = findViewById(R.id.materialRecyclerView);
        addMaterialFab = findViewById(R.id.addMaterialFab);
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void setupRecyclerView() {
        adapter = new RawMaterialAdapter();
        adapter.setDeleteListener(this::deleteMaterial);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void loadMaterials() {
        String userId = firebaseHelper.getCurrentUserId();
        if (userId == null) return;

        firebaseHelper.getRawMaterialsRef().orderByChild("userId").equalTo(userId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        List<RawMaterial> materials = new ArrayList<>();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            RawMaterial material = dataSnapshot.getValue(RawMaterial.class);
                            if (material != null) {
                                materials.add(material);
                            }
                        }
                        adapter.setMaterials(materials);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(RawMaterialActivity.this,
                                "Failed to load materials", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void showAddMaterialDialog() {
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_material, null);

        TextInputEditText nameEditText = dialogView.findViewById(R.id.nameEditText);
        AutoCompleteTextView cropAutoComplete = dialogView.findViewById(R.id.cropAutoComplete);
        TextInputEditText quantityEditText = dialogView.findViewById(R.id.quantityEditText);
        AutoCompleteTextView unitAutoComplete = dialogView.findViewById(R.id.unitAutoComplete);
        TextInputEditText costEditText = dialogView.findViewById(R.id.costEditText);
        TextInputEditText dateEditText = dialogView.findViewById(R.id.dateEditText);

        // Setup crop dropdown
        String[] crops = getResources().getStringArray(R.array.crop_types);
        ArrayAdapter<String> cropAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, crops);
        cropAutoComplete.setAdapter(cropAdapter);

        // Setup unit dropdown
        String[] units = getResources().getStringArray(R.array.material_units);
        ArrayAdapter<String> unitAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, units);
        unitAutoComplete.setAdapter(unitAdapter);

        // Set current date
        dateEditText.setText(dateFormatter.format(calendar.getTime()));

        // Date picker
        dateEditText.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    RawMaterialActivity.this,
                    (view, year, month, dayOfMonth) -> {
                        calendar.set(year, month, dayOfMonth);
                        dateEditText.setText(dateFormatter.format(calendar.getTime()));
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
            );
            datePickerDialog.show();
        });

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(dialogView)
                .create();

        MaterialButton saveButton = dialogView.findViewById(R.id.saveButton);
        MaterialButton cancelButton = dialogView.findViewById(R.id.cancelButton);

        saveButton.setOnClickListener(v -> {
            String name = nameEditText.getText().toString().trim();
            String crop = cropAutoComplete.getText().toString().trim();
            String quantityStr = quantityEditText.getText().toString().trim();
            String unit = unitAutoComplete.getText().toString().trim();
            String costStr = costEditText.getText().toString().trim();
            String date = dateEditText.getText().toString().trim();

            if (validateMaterialInput(name, crop, quantityStr, unit, costStr, date)) {
                double quantity = Double.parseDouble(quantityStr);
                double cost = Double.parseDouble(costStr);
                saveMaterial(name, crop, quantity, unit, cost, date);
                dialog.dismiss();
            }
        });

        cancelButton.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    private boolean validateMaterialInput(String name, String crop, String quantity, String unit, String cost, String date) {
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, "Please enter material name", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (TextUtils.isEmpty(crop)) {
            Toast.makeText(this, "Please select a crop", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (TextUtils.isEmpty(quantity)) {
            Toast.makeText(this, "Please enter quantity", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (TextUtils.isEmpty(unit)) {
            Toast.makeText(this, "Please select unit", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (TextUtils.isEmpty(cost)) {
            Toast.makeText(this, "Please enter cost per unit", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (TextUtils.isEmpty(date)) {
            Toast.makeText(this, "Please select a date", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void saveMaterial(String name, String crop, double quantity, String unit, double cost, String date) {
        String userId = firebaseHelper.getCurrentUserId();
        if (userId == null) return;

        String materialId = firebaseHelper.getRawMaterialsRef().push().getKey();
        if (materialId == null) return;

        RawMaterial material = new RawMaterial(materialId, userId, name, crop, quantity, unit, cost, date);

        firebaseHelper.getRawMaterialsRef().child(materialId).setValue(material)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(RawMaterialActivity.this,
                                "Material added successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(RawMaterialActivity.this,
                                "Failed to add material", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void deleteMaterial(String materialId) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Material")
                .setMessage("Are you sure you want to delete this material? This action cannot be undone.")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Delete", (dialog, which) -> {
                    firebaseHelper.getRawMaterialsRef().child(materialId).removeValue()
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    Toast.makeText(RawMaterialActivity.this,
                                            "Material deleted successfully", Toast.LENGTH_SHORT).show();
                                    loadMaterials();
                                } else {
                                    Toast.makeText(RawMaterialActivity.this,
                                            "Failed to delete material", Toast.LENGTH_SHORT).show();
                                }
                            });
                })
                .show();
    }
}
