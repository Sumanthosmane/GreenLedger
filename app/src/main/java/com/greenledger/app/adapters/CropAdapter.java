package com.greenledger.app.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.greenledger.app.R;
import com.greenledger.app.activities.CropDetailsActivity;
import com.greenledger.app.models.Crop;
import com.greenledger.app.models.CropStage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CropAdapter extends RecyclerView.Adapter<CropAdapter.CropViewHolder> {
    private final Context context;
    private final List<Crop> crops;
    private final SimpleDateFormat dateFormat;

    public CropAdapter(Context context) {
        this.context = context;
        this.crops = new ArrayList<>();
        this.dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
    }

    @NonNull
    @Override
    public CropViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_crop, parent, false);
        return new CropViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CropViewHolder holder, int position) {
        Crop crop = crops.get(position);
        holder.bind(crop);
    }

    @Override
    public int getItemCount() {
        return crops.size();
    }

    public void setCrops(List<Crop> newCrops) {
        crops.clear();
        crops.addAll(newCrops);
        notifyDataSetChanged();
    }

    public void addCrop(Crop crop) {
        crops.add(crop);
        notifyItemInserted(crops.size() - 1);
    }

    public void updateCrop(Crop crop) {
        int index = -1;
        for (int i = 0; i < crops.size(); i++) {
            if (crops.get(i).getCropId().equals(crop.getCropId())) {
                index = i;
                break;
            }
        }
        if (index != -1) {
            crops.set(index, crop);
            notifyItemChanged(index);
        }
    }

    public void removeCrop(String cropId) {
        int index = -1;
        for (int i = 0; i < crops.size(); i++) {
            if (crops.get(i).getCropId().equals(cropId)) {
                index = i;
                break;
            }
        }
        if (index != -1) {
            crops.remove(index);
            notifyItemRemoved(index);
        }
    }

    class CropViewHolder extends RecyclerView.ViewHolder {
        private final TextView cropName;
        private final TextView cropType;
        private final TextView cropStatus;
        private final TextView currentStage;
        private final TextView expectedHarvest;
        private final TextView cropYield;
        private final ProgressBar progressBar;
        private final ImageView cropImage;
        private final Button viewDetailsButton;

        public CropViewHolder(@NonNull View itemView) {
            super(itemView);
            cropName = itemView.findViewById(R.id.cropName);
            cropType = itemView.findViewById(R.id.cropType);
            cropStatus = itemView.findViewById(R.id.cropStatus);
            currentStage = itemView.findViewById(R.id.currentStage);
            expectedHarvest = itemView.findViewById(R.id.expectedHarvest);
            cropYield = itemView.findViewById(R.id.cropYield);
            progressBar = itemView.findViewById(R.id.progressBar);
            cropImage = itemView.findViewById(R.id.cropImage);
            viewDetailsButton = itemView.findViewById(R.id.viewDetailsButton);
        }

        public void bind(Crop crop) {
            // Set crop name and type
            String name = String.format("%s - %s",
                crop.getCropInfo().getType(),
                crop.getCropInfo().getVariety());
            cropName.setText(name);
            cropType.setText(crop.getCropInfo().getCategory());

            // Set status and progress
            cropStatus.setText(crop.getLifecycle().getStatus().toString());
            CropStage currentCropStage = crop.getLifecycle().getCurrentStage();
            if (currentCropStage != null) {
                currentStage.setText(currentCropStage.getStage());

                // Calculate progress
                long totalTime = crop.getLifecycle().getExpectedHarvestDate() - crop.getLifecycle().getSowingDate();
                long elapsedTime = System.currentTimeMillis() - crop.getLifecycle().getSowingDate();
                int progress = (int) ((elapsedTime * 100) / totalTime);
                progressBar.setProgress(Math.min(100, Math.max(0, progress)));
            }

            // Set expected harvest date
            String harvestDate = dateFormat.format(new Date(crop.getLifecycle().getExpectedHarvestDate()));
            expectedHarvest.setText(String.format("Expected Harvest: %s", harvestDate));

            // Set yield information
            String yieldText = String.format(Locale.getDefault(),
                "Expected: %.2f %s",
                crop.getQuantity().getExpected(),
                crop.getQuantity().getUnit());
            cropYield.setText(yieldText);

            // Load crop image if available
            if (currentCropStage != null && currentCropStage.getPhotos() != null && !currentCropStage.getPhotos().isEmpty()) {
                Glide.with(context)
                    .load(currentCropStage.getPhotos().get(0))
                    .placeholder(R.drawable.placeholder_crop)
                    .error(R.drawable.placeholder_crop)
                    .into(cropImage);
            }

            // Set click listeners
            viewDetailsButton.setOnClickListener(v -> {
                Intent intent = new Intent(context, CropDetailsActivity.class);
                intent.putExtra("cropId", crop.getCropId());
                context.startActivity(intent);
            });

            itemView.setOnClickListener(v -> viewDetailsButton.performClick());
        }
    }
}
