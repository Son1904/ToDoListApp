package com.example.todolist;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private ArrayList<Task> taskList;
    private TaskAdapter adapter;
    private ListView lvTasks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        dbHelper = new DatabaseHelper(this);
        lvTasks = findViewById(R.id.listView_tasks);
    }

    public void onClickAddTask(View view) {
        Intent intent = new Intent(getApplicationContext(), AddTaskActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Load tasks from the database every time the activity is resumed
        loadTasksFromDB();
    }

    private void loadTasksFromDB() {
        // Initialize a new list
        taskList = new ArrayList<>();

        // Get a readable database instance
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // A Cursor is a pointer to the query result set
        Cursor cursor = db.query(DatabaseHelper.TABLE_TASKS, null, null, null, null, null, null);

        // Loop through all rows in the result set
        if (cursor.moveToFirst()) {
            do {
                // Get data from columns
                long id = cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_NAME));
                String descriptions = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_DESCRIPTIONS));
                int duration = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_DURATION));
                long deadlineMillis = cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_DEADLINE));
                int completedInt = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_COMPLETED));

                // Convert data to appropriate types
                Date deadline = new Date(deadlineMillis);
                boolean isCompleted = (completedInt == 1);

                // Create new Task object and add to list
                Task task = new Task(id, name, deadline, duration, descriptions, isCompleted);
                taskList.add(task);

            } while (cursor.moveToNext());
        }

        // Close the cursor and database to free up resources
        cursor.close();
        db.close();

        // Create the adapter with the new list from the database
        adapter = new TaskAdapter(this, taskList);
        // Set the adapter on the ListView to display the data
        lvTasks.setAdapter(adapter);
    }
}