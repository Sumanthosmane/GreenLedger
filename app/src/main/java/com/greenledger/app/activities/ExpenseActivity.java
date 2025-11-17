package com.greenledger.app.activities;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.greenledger.app.R;
import com.greenledger.app.adapters.ExpenseAdapter;
import com.greenledger.app.models.Expense;
import com.greenledger.app.utils.FirebaseHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class ExpenseActivity extends AppCompatActivity {
    private MaterialToolbar toolbar;
    private RecyclerView recyclerView;
    private FloatingActionButton addExpenseFab;
    private ExpenseAdapter adapter;
    private FirebaseHelper firebaseHelper;
    private Calendar calendar = Calendar.getInstance();
    private SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense);

        firebaseHelper = FirebaseHelper.getInstance();

        initializeViews();
        setupToolbar();
        setupRecyclerView();
        loadExpenses();

        addExpenseFab.setOnClickListener(v -> showAddExpenseDialog());
    }

    private void initializeViews() {
        toolbar = findViewById(R.id.toolbar);
        recyclerView = findViewById(R.id.expenseRecyclerView);
        addExpenseFab = findViewById(R.id.addExpenseFab);
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void setupRecyclerView() {
        adapter = new ExpenseAdapter();
        adapter.setDeleteListener(this::deleteExpense);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void loadExpenses() {
        String userId = firebaseHelper.getCurrentUserId();
        if (userId == null) return;

        firebaseHelper.getExpensesRef().orderByChild("userId").equalTo(userId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        List<Expense> expenses = new ArrayList<>();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            Expense expense = dataSnapshot.getValue(Expense.class);
                            if (expense != null) {
                                expenses.add(expense);
                            }
                        }
                        adapter.setExpenses(expenses);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(ExpenseActivity.this,
                                "Failed to load expenses", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void showAddExpenseDialog() {
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_expense, null);

        AutoCompleteTextView categoryAutoComplete = dialogView.findViewById(R.id.categoryAutoComplete);
        AutoCompleteTextView cropAutoComplete = dialogView.findViewById(R.id.cropAutoComplete);
        TextInputEditText amountEditText = dialogView.findViewById(R.id.amountEditText);
        TextInputEditText descriptionEditText = dialogView.findViewById(R.id.descriptionEditText);
        TextInputEditText dateEditText = dialogView.findViewById(R.id.dateEditText);

        // Setup category dropdown
        String[] categories = getResources().getStringArray(R.array.expense_categories);
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, categories);
        categoryAutoComplete.setAdapter(categoryAdapter);

        // Setup crop dropdown
        String[] crops = getResources().getStringArray(R.array.crop_types);
        ArrayAdapter<String> cropAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, crops);
        cropAutoComplete.setAdapter(cropAdapter);

        // Set current date
        dateEditText.setText(dateFormatter.format(calendar.getTime()));

        // Date picker
        dateEditText.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    ExpenseActivity.this,
                    (view, year, month, dayOfMonth) -> {
                        calendar.set(year, month, dayOfMonth);
                        dateEditText.setText(dateFormatter.format(calendar.getTime()));
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
            );
            datePickerDialog.show();
        });

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(dialogView)
                .create();

        MaterialButton saveButton = dialogView.findViewById(R.id.saveButton);
        MaterialButton cancelButton = dialogView.findViewById(R.id.cancelButton);

        saveButton.setOnClickListener(v -> {
            String category = categoryAutoComplete.getText().toString().trim();
            String crop = cropAutoComplete.getText().toString().trim();
            String amountStr = amountEditText.getText().toString().trim();
            String description = descriptionEditText.getText().toString().trim();
            String date = dateEditText.getText().toString().trim();

            if (validateExpenseInput(category, crop, amountStr, description)) {
                double amount = Double.parseDouble(amountStr);
                saveExpense(category, crop, amount, description, date);
                dialog.dismiss();
            }
        });

        cancelButton.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    private boolean validateExpenseInput(String category, String crop, String amount, String description) {
        if (TextUtils.isEmpty(category)) {
            Toast.makeText(this, "Please select a category", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (TextUtils.isEmpty(crop)) {
            Toast.makeText(this, "Please select a crop", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (TextUtils.isEmpty(amount)) {
            Toast.makeText(this, "Please enter amount", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (TextUtils.isEmpty(description)) {
            Toast.makeText(this, "Please enter description", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void saveExpense(String category, String crop, double amount, String description, String date) {
        String userId = firebaseHelper.getCurrentUserId();
        if (userId == null) return;

        String expenseId = firebaseHelper.getExpensesRef().push().getKey();
        if (expenseId == null) return;

        Expense expense = new Expense(expenseId, userId, category, crop, amount, description, date);

        firebaseHelper.getExpensesRef().child(expenseId).setValue(expense)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(ExpenseActivity.this,
                                "Expense added successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ExpenseActivity.this,
                                "Failed to add expense", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void deleteExpense(String expenseId) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Expense")
                .setMessage("Are you sure you want to delete this expense? This action cannot be undone.")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Delete", (dialog, which) -> {
                    firebaseHelper.getExpensesRef().child(expenseId).removeValue()
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    Toast.makeText(ExpenseActivity.this,
                                            "Expense deleted successfully", Toast.LENGTH_SHORT).show();
                                    loadExpenses();
                                } else {
                                    Toast.makeText(ExpenseActivity.this,
                                            "Failed to delete expense", Toast.LENGTH_SHORT).show();
                                }
                            });
                })
                .show();
    }
}
