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
import com.greenledger.app.adapters.FarmAdapter;
import com.greenledger.app.models.Farm;
import com.greenledger.app.utils.FirebaseHelper;

import java.util.ArrayList;
import java.util.List;

public class FarmListActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private FarmAdapter adapter;
    private FirebaseHelper firebaseHelper;
    private FloatingActionButton addFarmButton;
    private View emptyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farm_list);

        // Initialize Firebase
        firebaseHelper = FirebaseHelper.getInstance();

        // Initialize views
        recyclerView = findViewById(R.id.farmList);
        addFarmButton = findViewById(R.id.addFarmButton);
        emptyView = findViewById(R.id.emptyView);

        // Setup RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new FarmAdapter(this);
        recyclerView.setAdapter(adapter);

        // Setup FAB click listener
        addFarmButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddFarmActivity.class);
            startActivity(intent);
        });

        // Load data
        loadFarms();
    }

    private void loadFarms() {
        String userId = firebaseHelper.getCurrentUserId();
        if (userId == null) return;

        firebaseHelper.getFarmsByUserRef(userId)
            .addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    List<Farm> farms = new ArrayList<>();
                    for (DataSnapshot farmSnapshot : snapshot.getChildren()) {
                        Farm farm = farmSnapshot.getValue(Farm.class);
                        if (farm != null) {
                            farms.add(farm);
                        }
                    }

                    adapter.setFarms(farms);
                    updateEmptyView(farms.isEmpty());
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
