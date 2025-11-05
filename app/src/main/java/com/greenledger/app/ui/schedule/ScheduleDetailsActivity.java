package com.greenledger.app.ui.schedule;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.greenledger.app.R;
import com.greenledger.app.managers.ScheduleManager;
import com.greenledger.app.models.Schedule;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class ScheduleDetailsActivity extends AppCompatActivity {
    private TextView titleText;
    private TextView timeText;
    private TextView workTypeText;
    private TextView priorityText;
    private TextView instructionsText;
    private RecyclerView labourersRecyclerView;
    private ScheduleManager scheduleManager;
    private String scheduleId;
    private Schedule currentSchedule;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_details);

        scheduleId = getIntent().getStringExtra("scheduleId");
        if (scheduleId == null) {
            finish();
            return;
        }

        initializeViews();
        setupActionBar();
        loadScheduleDetails();
    }

    private void initializeViews() {
        titleText = findViewById(R.id.titleText);
        timeText = findViewById(R.id.timeText);
        workTypeText = findViewById(R.id.workTypeText);
        priorityText = findViewById(R.id.priorityText);
        instructionsText = findViewById(R.id.instructionsText);
        labourersRecyclerView = findViewById(R.id.labourersRecyclerView);

        scheduleManager = new ScheduleManager();
        labourersRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setupActionBar() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Schedule Details");
        }
    }

    private void loadScheduleDetails() {
        scheduleManager.getScheduleById(scheduleId, new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                currentSchedule = snapshot.getValue(Schedule.class);
                if (currentSchedule != null) {
                    updateUI(currentSchedule);
                } else {
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                finish();
            }
        });
    }

    private void updateUI(Schedule schedule) {
        Schedule.ScheduleInfo info = schedule.getScheduleInfo();
        if (info != null) {
            titleText.setText(info.getTitle());

            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
            String timeRange = timeFormat.format(info.getStartTime()) + " - " +
                             timeFormat.format(info.getEndTime());
            timeText.setText(timeRange);

            workTypeText.setText(info.getWorkType().toString());
            priorityText.setText(info.getPriority());
        }

        instructionsText.setText(schedule.getInstructions());

        if (schedule.getAssignedLabourers() != null && !schedule.getAssignedLabourers().isEmpty()) {
            labourersRecyclerView.setVisibility(View.VISIBLE);
            // TODO: Create and set labourers adapter
        } else {
            labourersRecyclerView.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_schedule_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
            return true;
        } else if (id == R.id.action_edit) {
            // TODO: Implement edit functionality
            return true;
        } else if (id == R.id.action_delete) {
            confirmDelete();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void confirmDelete() {
        new AlertDialog.Builder(this)
            .setTitle("Delete Schedule")
            .setMessage("Are you sure you want to delete this schedule?")
            .setPositiveButton("Delete", (dialog, which) -> deleteSchedule())
            .setNegativeButton("Cancel", null)
            .show();
    }

    private void deleteSchedule() {
        scheduleManager.deleteSchedule(scheduleId)
            .addOnSuccessListener(aVoid -> finish())
            .addOnFailureListener(e -> {
                // Handle error
            });
    }
}
