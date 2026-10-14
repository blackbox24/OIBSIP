package com.example.todoapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    private List<Task> taskList;
    private OnTaskClickListener listener;
    private OnTaskCheckedChangeListener checkListener;

    public interface OnTaskClickListener {
        void onTaskClick(Task task);
    }
    public interface OnTaskCheckedChangeListener {
        void onCheckedChanged(Task task, boolean isChecked);
    }
    public TaskAdapter(List<Task> taskList, OnTaskClickListener listener, OnTaskCheckedChangeListener checkListener) {
        this.taskList = taskList;
        this.listener = listener;
        this.checkListener = checkListener;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_task, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task task = taskList.get(position);
        holder.tvTitle.setText(task.getTitle());
        holder.tvDesc.setText(task.getDescription());

        // Prevent trigger loop when recycling views
        holder.cbComplete.setOnCheckedChangeListener(null);
        holder.cbComplete.setChecked(task.isComplete());

        // Handle checkbox toggling
        holder.cbComplete.setOnCheckedChangeListener((buttonView, isChecked) -> {
            checkListener.onCheckedChanged(task, isChecked);
        });

        // Set click listener on the item view
        holder.itemView.setOnClickListener(v -> listener.onTaskClick(task));
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public static class TaskViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvDesc;
        CheckBox cbComplete;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTaskTitle);
            tvDesc = itemView.findViewById(R.id.tvTaskDesc);
            cbComplete = itemView.findViewById(R.id.cbTaskComplete);
        }
    }
}