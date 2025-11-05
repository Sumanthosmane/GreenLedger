package com.greenledger.app.ui.partner;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.greenledger.app.R;
import com.greenledger.app.managers.BusinessPartnerManager;
import com.greenledger.app.models.BusinessPartner;
import java.util.ArrayList;
import java.util.List;

public class BusinessPartnerActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private FloatingActionButton addPartnerFab;
    private LinearLayout emptyView;
    private BusinessPartnerManager partnerManager;
    private BusinessPartnerAdapter adapter;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_partner);

        initializeViews();
        setupRecyclerView();
        setupFab();
        loadPartners();
    }

    private void initializeViews() {
        recyclerView = findViewById(R.id.recyclerView);
        addPartnerFab = findViewById(R.id.addPartnerFab);
        emptyView = findViewById(R.id.emptyView);

        partnerManager = new BusinessPartnerManager();

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Business Partners");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void setupRecyclerView() {
        adapter = new BusinessPartnerAdapter(new ArrayList<>(), partner -> {
            Intent intent = new Intent(this, BusinessPartnerDetailsActivity.class);
            intent.putExtra("partnerId", partner.getPartnerId());
            startActivity(intent);
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void setupFab() {
        addPartnerFab.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddBusinessPartnerActivity.class);
            startActivity(intent);
        });
    }

    private void loadPartners() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        partnerManager.getFarmerPartners(userId, new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<BusinessPartner> partners = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    BusinessPartner partner = snapshot.getValue(BusinessPartner.class);
                    if (partner != null) {
                        partners.add(partner);
                    }
                }
                updateUI(partners);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error
            }
        });
    }

    private void updateUI(List<BusinessPartner> partners) {
        if (partners.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
            adapter.updatePartners(partners);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_business_partner, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchPartners(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchPartners(newText);
                return true;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void searchPartners(String query) {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        partnerManager.searchPartners(userId, query, new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<BusinessPartner> partners = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    BusinessPartner partner = snapshot.getValue(BusinessPartner.class);
                    if (partner != null) {
                        partners.add(partner);
                    }
                }
                updateUI(partners);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error
            }
        });
    }
}
