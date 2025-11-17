package com.greenledger.app.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.greenledger.app.R;
import com.greenledger.app.models.Expense;

import java.util.ArrayList;
import java.util.List;

public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ExpenseViewHolder> {
    private List<Expense> expenses = new ArrayList<>();
    private OnDeleteClickListener deleteListener;

    public interface OnDeleteClickListener {
        void onDeleteClick(String expenseId);
    }

    public ExpenseAdapter() {
    }

    public void setDeleteListener(OnDeleteClickListener listener) {
        this.deleteListener = listener;
    }

    @NonNull
    @Override
    public ExpenseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_expense, parent, false);
        return new ExpenseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExpenseViewHolder holder, int position) {
        Expense expense = expenses.get(position);
        holder.bind(expense, deleteListener);
    }

    @Override
    public int getItemCount() {
        return expenses.size();
    }

    public void setExpenses(List<Expense> expenses) {
        this.expenses = expenses;
        notifyDataSetChanged();
    }

    static class ExpenseViewHolder extends RecyclerView.ViewHolder {
        private final TextView categoryText;
        private final TextView amountText;
        private final TextView cropText;
        private final TextView descriptionText;
        private final TextView dateText;
        private final ImageButton deleteButton;

        public ExpenseViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryText = itemView.findViewById(R.id.categoryText);
            amountText = itemView.findViewById(R.id.amountText);
            cropText = itemView.findViewById(R.id.cropText);
            descriptionText = itemView.findViewById(R.id.descriptionText);
            dateText = itemView.findViewById(R.id.dateText);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }

        public void bind(Expense expense, OnDeleteClickListener deleteListener) {
            categoryText.setText(expense.getCategory());
            amountText.setText(String.format("₹%.2f", expense.getAmount()));
            cropText.setText(String.format("Crop: %s", expense.getCrop() != null ? expense.getCrop() : "N/A"));
            descriptionText.setText(expense.getDescription());
            dateText.setText(expense.getDate());

            deleteButton.setOnClickListener(v -> {
                if (deleteListener != null) {
                    deleteListener.onDeleteClick(expense.getExpenseId());
                }
            });
        }
    }
}

