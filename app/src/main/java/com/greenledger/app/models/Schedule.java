package com.greenledger.app.models;

import com.google.firebase.database.PropertyName;
import com.greenledger.app.models.enums.WorkType;

import java.util.ArrayList;
import java.util.List;

public class Schedule {
    private String scheduleId;
    private String farmerId;
    private String farmId;
    private ScheduleInfo scheduleInfo;
    private List<AssignedLabourer> assignedLabourers;
    private String cropId;
    private String landId;
    private String instructions;
    private Metadata metadata;

    public Schedule() {
        // Required empty constructor for Firebase
        assignedLabourers = new ArrayList<>();
    }

    public static class ScheduleInfo {
        private String title;
        private long date;
        private long startTime;
        private long endTime;
        private WorkType workType;
        private String priority;

        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }

        public long getDate() { return date; }
        public void setDate(long date) { this.date = date; }

        public long getStartTime() { return startTime; }
        public void setStartTime(long startTime) { this.startTime = startTime; }

        public long getEndTime() { return endTime; }
        public void setEndTime(long endTime) { this.endTime = endTime; }

        @PropertyName("workType")
        public WorkType getWorkType() { return workType; }
        @PropertyName("workType")
        public void setWorkType(WorkType workType) { this.workType = workType; }

        public String getPriority() { return priority; }
        public void setPriority(String priority) { this.priority = priority; }
    }

    public static class AssignedLabourer {
        private String labourerId;
        private String name;
        private String status;

        public String getLabourerId() { return labourerId; }
        public void setLabourerId(String labourerId) { this.labourerId = labourerId; }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
    }

    public static class Metadata {
        private long createdAt;
        private String createdBy;

        public long getCreatedAt() { return createdAt; }
        public void setCreatedAt(long createdAt) { this.createdAt = createdAt; }

        public String getCreatedBy() { return createdBy; }
        public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }
    }

    // Getters and Setters
    public String getScheduleId() { return scheduleId; }
    public void setScheduleId(String scheduleId) { this.scheduleId = scheduleId; }

    public String getFarmerId() { return farmerId; }
    public void setFarmerId(String farmerId) { this.farmerId = farmerId; }

    public String getFarmId() { return farmId; }
    public void setFarmId(String farmId) { this.farmId = farmId; }

    @PropertyName("scheduleInfo")
    public ScheduleInfo getScheduleInfo() { return scheduleInfo; }
    @PropertyName("scheduleInfo")
    public void setScheduleInfo(ScheduleInfo scheduleInfo) { this.scheduleInfo = scheduleInfo; }

    @PropertyName("assignedLabourers")
    public List<AssignedLabourer> getAssignedLabourers() { return assignedLabourers; }
    @PropertyName("assignedLabourers")
    public void setAssignedLabourers(List<AssignedLabourer> assignedLabourers) { this.assignedLabourers = assignedLabourers; }

    public String getCropId() { return cropId; }
    public void setCropId(String cropId) { this.cropId = cropId; }

    public String getLandId() { return landId; }
    public void setLandId(String landId) { this.landId = landId; }

    public String getInstructions() { return instructions; }
    public void setInstructions(String instructions) { this.instructions = instructions; }

    @PropertyName("metadata")
    public Metadata getMetadata() { return metadata; }
    @PropertyName("metadata")
    public void setMetadata(Metadata metadata) { this.metadata = metadata; }
}
