package com.greenledger.app.ui.schedule;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CalendarView;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.greenledger.app.R;
import com.greenledger.app.managers.ScheduleManager;
import com.greenledger.app.models.Schedule;
import com.greenledger.app.models.enums.UserRole;
import com.greenledger.app.utils.PermissionManager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ScheduleActivity extends AppCompatActivity {
    private CalendarView calendarView;
    private RecyclerView scheduleRecyclerView;
    private FloatingActionButton addScheduleFab;
    private LinearLayout emptyView;
    private ScheduleManager scheduleManager;
    private ScheduleAdapter adapter;
    private UserRole userRole;
    private String userId;
    private long selectedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        initializeViews();
        setupCalendar();
        setupRecyclerView();
        setupFab();
        loadSchedules(Calendar.getInstance().getTimeInMillis());
    }

    private void initializeViews() {
        calendarView = findViewById(R.id.calendarView);
        scheduleRecyclerView = findViewById(R.id.scheduleRecyclerView);
        addScheduleFab = findViewById(R.id.addScheduleFab);
        emptyView = findViewById(R.id.emptyView);

        scheduleManager = new ScheduleManager();
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        // TODO: Get user role from shared preferences or user manager
    }

    private void setupCalendar() {
        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            Calendar calendar = Calendar.getInstance();
            calendar.set(year, month, dayOfMonth);
            selectedDate = calendar.getTimeInMillis();
            loadSchedules(selectedDate);
        });
    }

    private void setupRecyclerView() {
        adapter = new ScheduleAdapter(new ArrayList<>(), schedule -> {
            Intent intent = new Intent(this, ScheduleDetailsActivity.class);
            intent.putExtra("scheduleId", schedule.getScheduleId());
            startActivity(intent);
        });

        scheduleRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        scheduleRecyclerView.setAdapter(adapter);
    }

    private void setupFab() {
        if (PermissionManager.canManageLabour(userRole)) {
            addScheduleFab.setVisibility(View.VISIBLE);
            addScheduleFab.setOnClickListener(v -> {
                Intent intent = new Intent(this, AddScheduleActivity.class);
                intent.putExtra("date", selectedDate);
                startActivity(intent);
            });
        } else {
            addScheduleFab.setVisibility(View.GONE);
        }
    }

    private void loadSchedules(long date) {
        scheduleManager.getDailySchedules(userId, date, new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Schedule> schedules = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Schedule schedule = snapshot.getValue(Schedule.class);
                    if (schedule != null) {
                        schedules.add(schedule);
                    }
                }
                updateUI(schedules);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error
            }
        });
    }

    private void updateUI(List<Schedule> schedules) {
        if (schedules.isEmpty()) {
            emptyView.setVisibility(View.VISIBLE);
            scheduleRecyclerView.setVisibility(View.GONE);
        } else {
            emptyView.setVisibility(View.GONE);
            scheduleRecyclerView.setVisibility(View.VISIBLE);
            adapter.updateSchedules(schedules);
        }
    }
}
