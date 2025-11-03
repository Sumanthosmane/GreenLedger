package com.greenledger.app.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.greenledger.app.R;
import com.greenledger.app.activities.FarmDetailsActivity;
import com.greenledger.app.models.Farm;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class FarmAdapter extends RecyclerView.Adapter<FarmAdapter.FarmViewHolder> {
    private final Context context;
    private final List<Farm> farms;
    private final SimpleDateFormat dateFormat;

    public FarmAdapter(Context context) {
        this.context = context;
        this.farms = new ArrayList<>();
        this.dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
    }

    @NonNull
    @Override
    public FarmViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_farm, parent, false);
        return new FarmViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FarmViewHolder holder, int position) {
        Farm farm = farms.get(position);
        holder.bind(farm);
    }

    @Override
    public int getItemCount() {
        return farms.size();
    }

    public void setFarms(List<Farm> newFarms) {
        farms.clear();
        farms.addAll(newFarms);
        notifyDataSetChanged();
    }

    public void addFarm(Farm farm) {
        farms.add(farm);
        notifyItemInserted(farms.size() - 1);
    }

    public void updateFarm(Farm farm) {
        int index = -1;
        for (int i = 0; i < farms.size(); i++) {
            if (farms.get(i).getFarmId().equals(farm.getFarmId())) {
                index = i;
                break;
            }
        }
        if (index != -1) {
            farms.set(index, farm);
            notifyItemChanged(index);
        }
    }

    public void removeFarm(String farmId) {
        int index = -1;
        for (int i = 0; i < farms.size(); i++) {
            if (farms.get(i).getFarmId().equals(farmId)) {
                index = i;
                break;
            }
        }
        if (index != -1) {
            farms.remove(index);
            notifyItemRemoved(index);
        }
    }

    class FarmViewHolder extends RecyclerView.ViewHolder {
        private final TextView farmName;
        private final TextView farmLocation;
        private final TextView totalArea;
        private final TextView cropCount;
        private final TextView lastUpdated;
        private final Button viewDetailsButton;

        public FarmViewHolder(@NonNull View itemView) {
            super(itemView);
            farmName = itemView.findViewById(R.id.farmName);
            farmLocation = itemView.findViewById(R.id.farmLocation);
            totalArea = itemView.findViewById(R.id.totalArea);
            cropCount = itemView.findViewById(R.id.cropCount);
            lastUpdated = itemView.findViewById(R.id.lastUpdated);
            viewDetailsButton = itemView.findViewById(R.id.viewDetailsButton);
        }

        public void bind(Farm farm) {
            // Set farm name
            farmName.setText(farm.getFarmDetails().getName());

            // Set location
            String location = farm.getFarmDetails().getLocation().getAddress();
            farmLocation.setText(location);

            // Set total area
            String areaText = String.format(Locale.getDefault(), "%.2f acres", farm.getFarmDetails().getTotalArea());
            totalArea.setText(areaText);

            // Set crop count (occupied lands)
            String cropCountText = String.format(Locale.getDefault(), "%d Active Crops", farm.getOccupiedLandsCount());
            cropCount.setText(cropCountText);

            // Set last updated date
            String dateText = "Last updated: " + dateFormat.format(new Date(farm.getMetadata().getUpdatedAt()));
            lastUpdated.setText(dateText);

            // Set click listener for view details button
            viewDetailsButton.setOnClickListener(v -> {
                Intent intent = new Intent(context, FarmDetailsActivity.class);
                intent.putExtra("farmId", farm.getFarmId());
                context.startActivity(intent);
            });

            // Set click listener for the entire item
            itemView.setOnClickListener(v -> viewDetailsButton.performClick());
        }
    }
}
