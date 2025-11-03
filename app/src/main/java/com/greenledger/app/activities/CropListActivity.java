package com.greenledger.app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.greenledger.app.R;
import com.greenledger.app.adapters.CropAdapter;
import com.greenledger.app.models.Crop;
import com.greenledger.app.utils.FirebaseHelper;

import java.util.ArrayList;
import java.util.List;

public class CropListActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private CropAdapter adapter;
    private FirebaseHelper firebaseHelper;
    private FloatingActionButton addCropButton;
    private View emptyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop_list);

        // Initialize Firebase
        firebaseHelper = FirebaseHelper.getInstance();

        // Initialize views
        recyclerView = findViewById(R.id.cropList);
        addCropButton = findViewById(R.id.addCropButton);
        emptyView = findViewById(R.id.emptyView);

        // Setup RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CropAdapter(this);
        recyclerView.setAdapter(adapter);

        // Setup FAB click listener
        addCropButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddCropActivity.class);
            startActivity(intent);
        });

        // Load data
        loadCrops();
    }

    private void loadCrops() {
        String userId = firebaseHelper.getCurrentUserId();
        if (userId == null) return;

        firebaseHelper.getCropsByUserRef(userId)
            .addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    List<Crop> crops = new ArrayList<>();
                    for (DataSnapshot cropSnapshot : snapshot.getChildren()) {
                        Crop crop = cropSnapshot.getValue(Crop.class);
                        if (crop != null) {
                            crops.add(crop);
                        }
                    }

                    adapter.setCrops(crops);
                    updateEmptyView(crops.isEmpty());
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // TODO: Show error message
                }
            });
    }

    private void updateEmptyView(boolean isEmpty) {
        if (isEmpty) {
            recyclerView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
        }
    }
}
