package com.greenledger.app.ui.schedule;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.greenledger.app.R;
import com.greenledger.app.models.Schedule;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ScheduleViewHolder> {
    private List<Schedule> schedules;
    private final OnScheduleClickListener listener;
    private final SimpleDateFormat timeFormat;

    public interface OnScheduleClickListener {
        void onScheduleClick(Schedule schedule);
    }

    public ScheduleAdapter(List<Schedule> schedules, OnScheduleClickListener listener) {
        this.schedules = schedules;
        this.listener = listener;
        this.timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
    }

    @NonNull
    @Override
    public ScheduleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_schedule, parent, false);
        return new ScheduleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ScheduleViewHolder holder, int position) {
        Schedule schedule = schedules.get(position);
        holder.bind(schedule, listener);
    }

    @Override
    public int getItemCount() {
        return schedules.size();
    }

    public void updateSchedules(List<Schedule> newSchedules) {
        this.schedules = newSchedules;
        notifyDataSetChanged();
    }

    static class ScheduleViewHolder extends RecyclerView.ViewHolder {
        private final TextView titleText;
        private final TextView timeText;
        private final TextView workTypeText;
        private final TextView priorityText;
        private final TextView labourerCountText;

        ScheduleViewHolder(@NonNull View itemView) {
            super(itemView);
            titleText = itemView.findViewById(R.id.titleText);
            timeText = itemView.findViewById(R.id.timeText);
            workTypeText = itemView.findViewById(R.id.workTypeText);
            priorityText = itemView.findViewById(R.id.priorityText);
            labourerCountText = itemView.findViewById(R.id.labourerCountText);
        }

        void bind(Schedule schedule, OnScheduleClickListener listener) {
            if (schedule.getScheduleInfo() != null) {
                titleText.setText(schedule.getScheduleInfo().getTitle());

                String timeRange = String.format("%s - %s",
                    new SimpleDateFormat("HH:mm", Locale.getDefault()).format(schedule.getScheduleInfo().getStartTime()),
                    new SimpleDateFormat("HH:mm", Locale.getDefault()).format(schedule.getScheduleInfo().getEndTime()));
                timeText.setText(timeRange);

                workTypeText.setText(schedule.getScheduleInfo().getWorkType().toString());
                priorityText.setText(schedule.getScheduleInfo().getPriority());
            }

            int labourerCount = schedule.getAssignedLabourers() != null ?
                schedule.getAssignedLabourers().size() : 0;
            labourerCountText.setText(String.format(Locale.getDefault(),
                "%d Labourer%s", labourerCount, labourerCount == 1 ? "" : "s"));

            itemView.setOnClickListener(v -> listener.onScheduleClick(schedule));
        }
    }
}
