package com.greenledger.app.managers;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.greenledger.app.firebase.FirebaseHelper;
import com.greenledger.app.models.Schedule;
import com.greenledger.app.models.enums.WorkType;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ScheduleManager {
    private final DatabaseReference schedulesRef;

    public ScheduleManager() {
        this.schedulesRef = FirebaseHelper.getInstance().getSchedulesRef();
    }

    public Task<Void> createSchedule(Schedule schedule) {
        String scheduleId = schedulesRef.push().getKey();
        if (scheduleId != null) {
            schedule.setScheduleId(scheduleId);
            return schedulesRef.child(scheduleId).setValue(schedule);
        }
        return null;
    }

    public Task<Void> updateSchedule(Schedule schedule) {
        if (schedule.getScheduleId() != null) {
            return schedulesRef.child(schedule.getScheduleId()).setValue(schedule);
        }
        return null;
    }

    public Task<Void> deleteSchedule(String scheduleId) {
        return schedulesRef.child(scheduleId).removeValue();
    }

    public void getScheduleById(String scheduleId, ValueEventListener listener) {
        schedulesRef.child(scheduleId).addValueEventListener(listener);
    }

    public void getFarmSchedules(String farmId, ValueEventListener listener) {
        Query query = schedulesRef.orderByChild("farmId").equalTo(farmId);
        query.addValueEventListener(listener);
    }

    public void getLabourerSchedules(String labourerId, ValueEventListener listener) {
        schedulesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Schedule> schedules = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Schedule schedule = snapshot.getValue(Schedule.class);
                    if (schedule != null && schedule.getAssignedLabourers() != null) {
                        for (Schedule.AssignedLabourer labourer : schedule.getAssignedLabourers()) {
                            if (labourer.getLabourerId().equals(labourerId)) {
                                schedules.add(schedule);
                                break;
                            }
                        }
                    }
                }
                if (listener != null) {
                    listener.onDataChange(dataSnapshot);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                if (listener != null) {
                    listener.onCancelled(databaseError);
                }
            }
        });
    }

    public void getDailySchedules(String farmerId, long date, ValueEventListener listener) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        long startTime = cal.getTimeInMillis();

        cal.add(Calendar.DAY_OF_MONTH, 1);
        long endTime = cal.getTimeInMillis();

        Query query = schedulesRef
            .orderByChild("farmerId")
            .equalTo(farmerId);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Schedule> schedules = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Schedule schedule = snapshot.getValue(Schedule.class);
                    if (schedule != null && schedule.getScheduleInfo() != null) {
                        long scheduleDate = schedule.getScheduleInfo().getDate();
                        if (scheduleDate >= startTime && scheduleDate < endTime) {
                            schedules.add(schedule);
                        }
                    }
                }
                if (listener != null) {
                    listener.onDataChange(dataSnapshot);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                if (listener != null) {
                    listener.onCancelled(databaseError);
                }
            }
        });
    }

    public void getSchedulesByWorkType(String farmerId, WorkType workType, ValueEventListener listener) {
        Query query = schedulesRef
            .orderByChild("farmerId")
            .equalTo(farmerId);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Schedule> schedules = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Schedule schedule = snapshot.getValue(Schedule.class);
                    if (schedule != null && schedule.getScheduleInfo() != null &&
                        schedule.getScheduleInfo().getWorkType() == workType) {
                        schedules.add(schedule);
                    }
                }
                if (listener != null) {
                    listener.onDataChange(dataSnapshot);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                if (listener != null) {
                    listener.onCancelled(databaseError);
                }
            }
        });
    }

    public Task<Void> updateLabourerStatus(String scheduleId, String labourerId, String status) {
        return schedulesRef.child(scheduleId)
            .child("assignedLabourers")
            .orderByChild("labourerId")
            .equalTo(labourerId)
            .get()
            .continueWithTask(task -> {
                if (task.isSuccessful() && task.getResult() != null) {
                    for (DataSnapshot snapshot : task.getResult().getChildren()) {
                        return snapshot.getRef().child("status").setValue(status);
                    }
                }
                return null;
            });
    }
}
