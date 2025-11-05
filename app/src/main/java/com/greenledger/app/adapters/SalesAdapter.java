package com.greenledger.app.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.greenledger.app.R;
import com.greenledger.app.models.Sale;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class SalesAdapter extends RecyclerView.Adapter<SalesAdapter.SaleViewHolder> {
    private final List<Sale> sales;
    private final OnSaleClickListener listener;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());

    public interface OnSaleClickListener {
        void onSaleClick(Sale sale);
    }

    public SalesAdapter(List<Sale> sales, OnSaleClickListener listener) {
        this.sales = sales;
        this.listener = listener;
    }

    @NonNull
    @Override
    public SaleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_sale, parent, false);
        return new SaleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SaleViewHolder holder, int position) {
        Sale sale = sales.get(position);
        holder.bind(sale);
    }

    @Override
    public int getItemCount() {
        return sales.size();
    }

    class SaleViewHolder extends RecyclerView.ViewHolder {
        private final TextView dateText;
        private final TextView buyerText;
        private final TextView cropText;
        private final TextView amountText;
        private final TextView statusText;

        public SaleViewHolder(@NonNull View itemView) {
            super(itemView);
            dateText = itemView.findViewById(R.id.dateText);
            buyerText = itemView.findViewById(R.id.buyerText);
            cropText = itemView.findViewById(R.id.cropText);
            amountText = itemView.findViewById(R.id.amountText);
            statusText = itemView.findViewById(R.id.statusText);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    listener.onSaleClick(sales.get(position));
                }
            });
        }

        public void bind(Sale sale) {
            dateText.setText(dateFormat.format(sale.getSaleDate()));
            buyerText.setText(sale.getBuyerId()); // TODO: Load buyer name
            cropText.setText(sale.getCropId()); // TODO: Load crop name
            amountText.setText(String.format(Locale.getDefault(), "₹%.2f", sale.getTotalAmount()));
            statusText.setText(sale.isInvoiceGenerated() ? "Invoiced" : "Pending");
        }
    }
}
