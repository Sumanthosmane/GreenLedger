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
import com.greenledger.app.adapters.StorageAdapter;
import com.greenledger.app.models.Storage;
import com.greenledger.app.utils.FirebaseHelper;

import java.util.ArrayList;
import java.util.List;

public class StorageListActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private StorageAdapter adapter;
    private FirebaseHelper firebaseHelper;
    private FloatingActionButton addStorageButton;
    private View emptyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storage_list);

        // Initialize Firebase
        firebaseHelper = FirebaseHelper.getInstance();

        // Initialize views
        recyclerView = findViewById(R.id.storageList);
        addStorageButton = findViewById(R.id.addStorageButton);
        emptyView = findViewById(R.id.emptyView);

        // Setup RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new StorageAdapter(this);
        recyclerView.setAdapter(adapter);

        // Setup FAB click listener
        addStorageButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddStorageActivity.class);
            startActivity(intent);
        });

        // Load data
        loadStorage();
    }

    private void loadStorage() {
        String userId = firebaseHelper.getCurrentUserId();
        if (userId == null) return;

        firebaseHelper.getStorageByUserRef(userId)
            .addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    List<Storage> storageUnits = new ArrayList<>();
                    for (DataSnapshot storageSnapshot : snapshot.getChildren()) {
                        Storage storage = storageSnapshot.getValue(Storage.class);
                        if (storage != null) {
                            storageUnits.add(storage);
                        }
                    }

                    adapter.setStorageUnits(storageUnits);
                    updateEmptyView(storageUnits.isEmpty());
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
