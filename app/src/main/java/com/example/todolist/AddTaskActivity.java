package com.example.todolist;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Calendar;
import java.util.Date;

public class AddTaskActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_task);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize the DatabaseHelper
        dbHelper = new DatabaseHelper(this);
    }

    public void onClickAddTask(View view) {
        // Get the UI components
        EditText etTaskName = findViewById(R.id.etTaskName);
        EditText etDuration = findViewById(R.id.etDuration);
        EditText etmDescriptions = findViewById(R.id.etmDescriptions);
        DatePicker dpDeadline = findViewById(R.id.dpDeadline);

        // Get the data from the components
        String name = etTaskName.getText().toString();
        String descriptions = etmDescriptions.getText().toString();
        String durationStr = etDuration.getText().toString();

        // Validate that required fields are not empty
        if (name.isEmpty() || durationStr.isEmpty()) {
            Toast.makeText(this, R.string.toast_fill_all_fields, Toast.LENGTH_SHORT).show();
            return;
        }

        int duration = Integer.parseInt(durationStr);

        // Get the date from the DatePicker
        int day = dpDeadline.getDayOfMonth();
        int month = dpDeadline.getMonth();
        int year = dpDeadline.getYear();

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        Date deadline = calendar.getTime();

        // ** NEW LOGIC: SAVE TO DATABASE **
        // Get a writable database instance
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Create a ContentValues object to hold the data
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_NAME, name);
        values.put(DatabaseHelper.COLUMN_DESCRIPTIONS, descriptions);
        values.put(DatabaseHelper.COLUMN_DURATION, duration);
        values.put(DatabaseHelper.COLUMN_DEADLINE, deadline.getTime()); // Save date as milliseconds
        values.put(DatabaseHelper.COLUMN_COMPLETED, 0); // Default to not completed (0 = false)

        // Insert the new row into the tasks table
        db.insert(DatabaseHelper.TABLE_TASKS, null, values);

        // Close the database connection
        db.close();

        // Show confirmation and finish the activity
        Toast.makeText(this, R.string.toast_task_created, Toast.LENGTH_SHORT).show();
        finish();
    }
}