package com.greenledger.app.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Button;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.greenledger.app.R;
import com.greenledger.app.activities.StorageDetailsActivity;
import com.greenledger.app.models.Storage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class StorageAdapter extends RecyclerView.Adapter<StorageAdapter.StorageViewHolder> {
    private final Context context;
    private final List<Storage> storageUnits;
    private final SimpleDateFormat dateFormat;

    public StorageAdapter(Context context) {
        this.context = context;
        this.storageUnits = new ArrayList<>();
        this.dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
    }

    @NonNull
    @Override
    public StorageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_storage, parent, false);
        return new StorageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StorageViewHolder holder, int position) {
        Storage storage = storageUnits.get(position);
        holder.bind(storage);
    }

    @Override
    public int getItemCount() {
        return storageUnits.size();
    }

    public void setStorageUnits(List<Storage> newStorageUnits) {
        storageUnits.clear();
        storageUnits.addAll(newStorageUnits);
        notifyDataSetChanged();
    }

    public void addStorage(Storage storage) {
        storageUnits.add(storage);
        notifyItemInserted(storageUnits.size() - 1);
    }

    public void updateStorage(Storage storage) {
        int index = -1;
        for (int i = 0; i < storageUnits.size(); i++) {
            if (storageUnits.get(i).getStorageId().equals(storage.getStorageId())) {
                index = i;
                break;
            }
        }
        if (index != -1) {
            storageUnits.set(index, storage);
            notifyItemChanged(index);
        }
    }

    public void removeStorage(String storageId) {
        int index = -1;
        for (int i = 0; i < storageUnits.size(); i++) {
            if (storageUnits.get(i).getStorageId().equals(storageId)) {
                index = i;
                break;
            }
        }
        if (index != -1) {
            storageUnits.remove(index);
            notifyItemRemoved(index);
        }
    }

    class StorageViewHolder extends RecyclerView.ViewHolder {
        private final TextView storageName;
        private final TextView storageType;
        private final TextView location;
        private final TextView capacity;
        private final TextView currentOccupancy;
        private final TextView inventoryValue;
        private final ProgressBar occupancyBar;
        private final Button viewDetailsButton;

        public StorageViewHolder(@NonNull View itemView) {
            super(itemView);
            storageName = itemView.findViewById(R.id.storageName);
            storageType = itemView.findViewById(R.id.storageType);
            location = itemView.findViewById(R.id.location);
            capacity = itemView.findViewById(R.id.capacity);
            currentOccupancy = itemView.findViewById(R.id.currentOccupancy);
            inventoryValue = itemView.findViewById(R.id.inventoryValue);
            occupancyBar = itemView.findViewById(R.id.occupancyBar);
            viewDetailsButton = itemView.findViewById(R.id.viewDetailsButton);
        }

        public void bind(Storage storage) {
            // Set storage name and type
            storageName.setText(storage.getStorageInfo().getName());
            storageType.setText(storage.getStorageInfo().getType().toString());

            // Set location
            location.setText(storage.getStorageInfo().getLocation());

            // Set capacity and occupancy
            String capacityText = String.format(Locale.getDefault(),
                "Capacity: %.2f quintals",
                storage.getStorageInfo().getCapacity());
            capacity.setText(capacityText);

            String occupancyText = String.format(Locale.getDefault(),
                "Current: %.2f quintals",
                storage.getStorageInfo().getCurrentOccupancy());
            currentOccupancy.setText(occupancyText);

            // Set occupancy progress
            int progress = (int) (storage.getStorageInfo().getOccupancyPercentage());
            occupancyBar.setProgress(progress);

            // Set inventory value
            String valueText = String.format(Locale.getDefault(),
                "Total Value: ₹%.2f",
                storage.getTotalInventoryValue());
            inventoryValue.setText(valueText);

            // Set click listeners
            viewDetailsButton.setOnClickListener(v -> {
                Intent intent = new Intent(context, StorageDetailsActivity.class);
                intent.putExtra("storageId", storage.getStorageId());
                context.startActivity(intent);
            });

            itemView.setOnClickListener(v -> viewDetailsButton.performClick());
        }
    }
}
