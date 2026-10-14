package com.example.todoapp;

public class Task {
    private int id;
    private String title;
    private String description;
    private int userId;
    private boolean is_complete;

    public Task(int id, String title, String description, int userId, boolean is_complete) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.userId = userId;
        this.is_complete = is_complete;
    }

    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public int getUserId() { return userId; }

    public boolean isComplete(){ return is_complete; }
}