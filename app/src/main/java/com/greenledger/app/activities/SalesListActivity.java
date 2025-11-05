package com.greenledger.app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.greenledger.app.R;
import com.greenledger.app.adapters.SalesAdapter;
import com.greenledger.app.models.Sale;
import com.greenledger.app.utils.FirebaseHelper;

import java.util.ArrayList;
import java.util.List;

public class SalesListActivity extends AppCompatActivity {
    private MaterialToolbar toolbar;
    private RecyclerView salesRecyclerView;
    private SwipeRefreshLayout swipeRefresh;
    private FloatingActionButton fabAddSale;
    private FirebaseHelper firebaseHelper;
    private SalesAdapter salesAdapter;
    private List<Sale> salesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_list);

        firebaseHelper = FirebaseHelper.getInstance();

        initializeViews();
        setupToolbar();
        setupRecyclerView();
        setupListeners();
        loadSales();
    }

    private void initializeViews() {
        toolbar = findViewById(R.id.toolbar);
        salesRecyclerView = findViewById(R.id.salesRecyclerView);
        swipeRefresh = findViewById(R.id.swipeRefresh);
        fabAddSale = findViewById(R.id.fabAddSale);
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    private void setupRecyclerView() {
        salesList = new ArrayList<>();
        salesAdapter = new SalesAdapter(salesList, sale -> {
            // Handle sale item click
            Intent intent = new Intent(this, SaleDetailsActivity.class);
            intent.putExtra("saleId", sale.getId());
            startActivity(intent);
        });

        salesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        salesRecyclerView.setAdapter(salesAdapter);
    }

    private void setupListeners() {
        fabAddSale.setOnClickListener(v ->
            startActivity(new Intent(this, AddSaleActivity.class))
        );

        swipeRefresh.setOnRefreshListener(this::loadSales);
    }

    private void loadSales() {
        swipeRefresh.setRefreshing(true);

        firebaseHelper.getSalesRef().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                salesList.clear();
                for (DataSnapshot saleSnapshot : snapshot.getChildren()) {
                    Sale sale = saleSnapshot.getValue(Sale.class);
                    if (sale != null) {
                        salesList.add(sale);
                    }
                }
                salesAdapter.notifyDataSetChanged();
                swipeRefresh.setRefreshing(false);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                swipeRefresh.setRefreshing(false);
                // Handle error
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
