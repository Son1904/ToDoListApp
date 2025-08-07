package com.example.todolist;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Database name and version
    private static final String DATABASE_NAME = "tasks.db";
    private static final int DATABASE_VERSION = 1;

    // Table and column names
    public static final String TABLE_TASKS = "tasks";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_DEADLINE = "deadline";
    public static final String COLUMN_DURATION = "duration";
    public static final String COLUMN_DESCRIPTIONS = "descriptions";
    public static final String COLUMN_COMPLETED = "completed";

    // SQL statement to create the tasks table
    private static final String CREATE_TABLE_TASKS = "CREATE TABLE " + TABLE_TASKS + " (" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_NAME + " TEXT, " +
            COLUMN_DEADLINE + " INTEGER, " + // Will store the date as a Long (milliseconds)
            COLUMN_DURATION + " INTEGER, " +
            COLUMN_DESCRIPTIONS + " TEXT, " +
            COLUMN_COMPLETED + " INTEGER);"; // Will store the boolean as 0 for false, 1 for true

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // This method is called only ONCE when the database is first created
        db.execSQL(CREATE_TABLE_TASKS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This method is called when you increment the DATABASE_VERSION
        // For simplicity, we'll just drop the old table and recreate it
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASKS);
        onCreate(db);
    }
}