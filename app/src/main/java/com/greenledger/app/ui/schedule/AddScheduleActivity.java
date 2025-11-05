package com.greenledger.app.ui.schedule;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.greenledger.app.R;
import com.greenledger.app.managers.ScheduleManager;
import com.greenledger.app.models.Schedule;
import com.greenledger.app.models.enums.WorkType;
import java.util.ArrayList;
import java.util.Calendar;

public class AddScheduleActivity extends AppCompatActivity {
    private EditText titleEdit;
    private EditText descriptionEdit;
    private Spinner workTypeSpinner;
    private Spinner prioritySpinner;
    private EditText startTimeEdit;
    private EditText endTimeEdit;
    private Button saveButton;
    private ScheduleManager scheduleManager;
    private long selectedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_schedule);

        selectedDate = getIntent().getLongExtra("date", Calendar.getInstance().getTimeInMillis());
        initializeViews();
        setupSaveButton();
    }

    private void initializeViews() {
        titleEdit = findViewById(R.id.titleEdit);
        descriptionEdit = findViewById(R.id.descriptionEdit);
        workTypeSpinner = findViewById(R.id.workTypeSpinner);
        prioritySpinner = findViewById(R.id.prioritySpinner);
        startTimeEdit = findViewById(R.id.startTimeEdit);
        endTimeEdit = findViewById(R.id.endTimeEdit);
        saveButton = findViewById(R.id.saveButton);

        scheduleManager = new ScheduleManager();
    }

    private void setupSaveButton() {
        saveButton.setOnClickListener(v -> validateAndSave());
    }

    private void validateAndSave() {
        String title = titleEdit.getText().toString().trim();
        String description = descriptionEdit.getText().toString().trim();
        WorkType workType = WorkType.valueOf(workTypeSpinner.getSelectedItem().toString());
        String priority = prioritySpinner.getSelectedItem().toString();

        if (title.isEmpty()) {
            titleEdit.setError("Title is required");
            return;
        }

        Schedule schedule = new Schedule();
        schedule.setFarmerId(FirebaseAuth.getInstance().getCurrentUser().getUid());

        Schedule.ScheduleInfo scheduleInfo = new Schedule.ScheduleInfo();
        scheduleInfo.setTitle(title);
        scheduleInfo.setDate(selectedDate);
        scheduleInfo.setWorkType(workType);
        scheduleInfo.setPriority(priority);
        // TODO: Parse and set start/end times

        schedule.setScheduleInfo(scheduleInfo);
        schedule.setInstructions(description);
        schedule.setAssignedLabourers(new ArrayList<>());

        Schedule.Metadata metadata = new Schedule.Metadata();
        metadata.setCreatedAt(System.currentTimeMillis());
        metadata.setCreatedBy(FirebaseAuth.getInstance().getCurrentUser().getUid());
        schedule.setMetadata(metadata);

        saveButton.setEnabled(false);
        scheduleManager.createSchedule(schedule)
            .addOnSuccessListener(aVoid -> finish())
            .addOnFailureListener(e -> {
                saveButton.setEnabled(true);
                // Handle error
            });
    }
}
