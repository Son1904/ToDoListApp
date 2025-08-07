package com.example.todolist;

import java.util.Date;

public class Task {

    // Add id to manage in the database
    public long id;
    public String name;
    public Date deadline;
    public int duration;
    public String descriptions;
    public boolean isCompleted; // NEW PROPERTY

    // Constructor for creating a new task object (not yet in DB)
    public Task(String name, Date deadline, int duration, String descriptions) {
        this.name = name;
        this.deadline = deadline;
        this.duration = duration;
        this.descriptions = descriptions;
        this.isCompleted = false; // Default to not completed
    }

    // Constructor for creating an object from DB data (already has an id and status)
    public Task(long id, String name, Date deadline, int duration, String descriptions, boolean isCompleted) {
        this.id = id;
        this.name = name;
        this.deadline = deadline;
        this.duration = duration;
        this.descriptions = descriptions;
        this.isCompleted = isCompleted;
    }
}