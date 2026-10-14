package com.example.todoapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    DatabaseHelper databaseHelper;
    TextView tvWelcomeUser;
    Button btnLogout, btnAddTask;
    RecyclerView recyclerViewTasks;
    TaskAdapter taskAdapter;
    List<Task> taskList;

    String userIdentifier;
    int currentUserId = 1;
    TextView tvEmptyState; // Declare at class level



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseHelper = new DatabaseHelper(this);

        tvWelcomeUser = findViewById(R.id.tvWelcomeUser);
        btnLogout = findViewById(R.id.btnLogout);
        btnAddTask = findViewById(R.id.btnAddTask);
        recyclerViewTasks = findViewById(R.id.recyclerViewTasks);
        tvEmptyState = findViewById(R.id.tvEmptyState);

        // 1. Retrieve the username or email passed from LoginActivity via Intent extra
        userIdentifier = getIntent().getStringExtra("USERNAME");

        if (userIdentifier != null && !userIdentifier.isEmpty()) {
            tvWelcomeUser.setText(String.format("Welcome back, %s!", userIdentifier));
        } else {
            tvWelcomeUser.setText("Welcome to your To-Do Dashboard!");
        }

        recyclerViewTasks.setLayoutManager(new LinearLayoutManager(this));
        loadUserTasks();

        // Handle Add Task Click (Opens an input dialog)
        btnAddTask.setOnClickListener(v -> showAddTaskDialog());

        // 2. Handle Logout action
        btnLogout.setOnClickListener(v -> {
            // Return to Log in screen
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish(); // Destroy MainActivity so user can't press back into the dashboard after logging out
        });
    }


    private void loadUserTasks() {
        taskList = databaseHelper.getAllTasksForUser(currentUserId);

        if (taskList.isEmpty()) {
            recyclerViewTasks.setVisibility(android.view.View.GONE);
            tvEmptyState.setVisibility(android.view.View.VISIBLE);
        } else {
            recyclerViewTasks.setVisibility(android.view.View.VISIBLE);
            tvEmptyState.setVisibility(android.view.View.GONE);

            // Pass click listener to adapter
            taskAdapter = new TaskAdapter(taskList, task -> showTaskOptionsDialog(task));
            recyclerViewTasks.setAdapter(taskAdapter);

        }
    }
    private void showTaskOptionsDialog(Task task) {
        CharSequence[] options = {"Edit Task", "Delete Task"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(task.getTitle());
        builder.setItems(options, (dialog, which) -> {
            if (which == 0) {
                // Edit Option Selected
                showEditTaskDialog(task);
            } else if (which == 1) {
                // Delete Option Selected
                boolean success = databaseHelper.deleteTask(task.getId());
                if (success) {
                    Toast.makeText(MainActivity.this, "Task Deleted", Toast.LENGTH_SHORT).show();
                    loadUserTasks();
                } else {
                    Toast.makeText(MainActivity.this, "Failed to delete task", Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.show();
    }

    private void showEditTaskDialog(Task task) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Edit Task");

        android.widget.LinearLayout layout = new android.widget.LinearLayout(this);
        layout.setOrientation(android.widget.LinearLayout.VERTICAL);
        layout.setPadding(40, 20, 40, 20);

        final EditText inputTitle = new EditText(this);
        inputTitle.setText(task.getTitle());
        layout.addView(inputTitle);

        final EditText inputDesc = new EditText(this);
        inputDesc.setText(task.getDescription());
        layout.addView(inputDesc);

        builder.setView(layout);

        builder.setPositiveButton("Update", (dialog, which) -> {
            String updatedTitle = inputTitle.getText().toString().trim();
            String updatedDesc = inputDesc.getText().toString().trim();

            if (!updatedTitle.isEmpty()) {
                boolean success = databaseHelper.updateTask(task.getId(), updatedTitle, updatedDesc);
                if (success) {
                    Toast.makeText(MainActivity.this, "Task Updated!", Toast.LENGTH_SHORT).show();
                    loadUserTasks();
                } else {
                    Toast.makeText(MainActivity.this, "Error updating task", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(MainActivity.this, "Title cannot be empty", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        builder.show();
    }
    private void showAddTaskDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add New Task");

        // Set up the input fields via a vertical layout inside the dialog
        android.widget.LinearLayout layout = new android.widget.LinearLayout(this);
        layout.setOrientation(android.widget.LinearLayout.VERTICAL);
        layout.setPadding(40, 20, 40, 20);

        final EditText inputTitle = new EditText(this);
        inputTitle.setHint("Task Title");
        layout.addView(inputTitle);

        final EditText inputDesc = new EditText(this);
        inputDesc.setHint("Task Description");
        layout.addView(inputDesc);

        builder.setView(layout);

        // Setup dialog buttons
        builder.setPositiveButton("Add", (dialog, which) -> {
            String title = inputTitle.getText().toString().trim();
            String desc = inputDesc.getText().toString().trim();

            if (!title.isEmpty()) {
                boolean success = databaseHelper.addTask(title, desc, currentUserId);
                if (success) {
                    Toast.makeText(MainActivity.this, "Task Added!", Toast.LENGTH_SHORT).show();
                    loadUserTasks(); // Refresh RecyclerView list
                } else {
                    Toast.makeText(MainActivity.this, "Error adding task", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(MainActivity.this, "Title cannot be empty", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        builder.show();
    }
}
