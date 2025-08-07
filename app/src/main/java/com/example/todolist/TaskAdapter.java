package com.example.todolist;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class TaskAdapter extends ArrayAdapter<Task> {

    public TaskAdapter(Context context, ArrayList<Task> tasks) {
        super(context, 0, tasks);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // Get the task object for the current position
        Task task = getItem(position);

        // Check if an existing view is being reused, otherwise inflate a new view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.task_item, parent, false);
        }

        // Find the views in the layout
        TextView tvTaskName = convertView.findViewById(R.id.tvTaskName);
        TextView tvDeadline = convertView.findViewById(R.id.tvDeadline);
        TextView tvDuration = convertView.findViewById(R.id.tvDuration);
        TextView tvDescriptions = convertView.findViewById(R.id.tvDescriptions);
        CheckBox cbCompleted = convertView.findViewById(R.id.cbCompleted);

        // Populate the views with data from the task object
        if (task != null) {
            Context context = getContext();
            tvTaskName.setText(task.name);

            SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
            tvDeadline.setText(context.getString(R.string.label_due_on, sdf.format(task.deadline)));

            tvDuration.setText(context.getString(R.string.label_days_needed, task.duration));
            tvDescriptions.setText(context.getString(R.string.label_details, task.descriptions));

            // ** NEW LOGIC FOR CHECKBOX **
            // Set the checkbox state based on the task's isCompleted property
            cbCompleted.setChecked(task.isCompleted);

            // Set a listener to update the database when the checkbox is clicked
            cbCompleted.setOnClickListener(v -> {
                boolean isChecked = cbCompleted.isChecked();
                task.isCompleted = isChecked;
                updateTaskStatusInDB(task.id, isChecked);
            });
        }

        return convertView;
    }

    private void updateTaskStatusInDB(long taskId, boolean isCompleted) {
        DatabaseHelper dbHelper = new DatabaseHelper(getContext());
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_COMPLETED, isCompleted ? 1 : 0); // Convert boolean to 1 or 0

        // Update the row in the database where the ID matches
        db.update(DatabaseHelper.TABLE_TASKS, values, DatabaseHelper.COLUMN_ID + " = ?", new String[]{String.valueOf(taskId)});

        db.close();
    }
}