package com.greenledger.app.ui.partner;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.greenledger.app.R;
import com.greenledger.app.models.BusinessPartner;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class BusinessPartnerAdapter extends RecyclerView.Adapter<BusinessPartnerAdapter.PartnerViewHolder> {
    private List<BusinessPartner> partners;
    private final OnPartnerClickListener listener;

    public interface OnPartnerClickListener {
        void onPartnerClick(BusinessPartner partner);
    }

    public BusinessPartnerAdapter(List<BusinessPartner> partners, OnPartnerClickListener listener) {
        this.partners = partners;
        this.listener = listener;
    }

    @NonNull
    @Override
    public PartnerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_business_partner, parent, false);
        return new PartnerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PartnerViewHolder holder, int position) {
        BusinessPartner partner = partners.get(position);
        holder.bind(partner, listener);
    }

    @Override
    public int getItemCount() {
        return partners.size();
    }

    public void updatePartners(List<BusinessPartner> newPartners) {
        this.partners = newPartners;
        notifyDataSetChanged();
    }

    static class PartnerViewHolder extends RecyclerView.ViewHolder {
        private final TextView businessNameText;
        private final TextView typeText;
        private final TextView transactionInfoText;
        private final TextView ratingText;

        PartnerViewHolder(@NonNull View itemView) {
            super(itemView);
            businessNameText = itemView.findViewById(R.id.businessNameText);
            typeText = itemView.findViewById(R.id.typeText);
            transactionInfoText = itemView.findViewById(R.id.transactionInfoText);
            ratingText = itemView.findViewById(R.id.ratingText);
        }

        void bind(BusinessPartner partner, OnPartnerClickListener listener) {
            if (partner.getPartnerInfo() != null) {
                businessNameText.setText(partner.getPartnerInfo().getBusinessName());
                typeText.setText(partner.getPartnerInfo().getType());
            }

            if (partner.getTransactionHistory() != null) {
                String transactionInfo = String.format(Locale.getDefault(),
                    "Transactions: %d | Outstanding: ₹%.2f",
                    partner.getTransactionHistory().getTotalPurchases(),
                    partner.getTransactionHistory().getOutstandingAmount());
                transactionInfoText.setText(transactionInfo);
            }

            if (partner.getMetadata() != null) {
                String rating = String.format(Locale.getDefault(), "%.1f ★",
                    partner.getMetadata().getRating());
                ratingText.setText(rating);
            }

            itemView.setOnClickListener(v -> listener.onPartnerClick(partner));
        }
    }
}
