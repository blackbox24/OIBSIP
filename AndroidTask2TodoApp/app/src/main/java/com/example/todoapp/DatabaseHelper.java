package com.example.todoapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class DatabaseHelper extends SQLiteOpenHelper {
    // 1. Database Configuration Constants
    private static final String DATABASE_NAME = "TodoApp.db";
    private static final int DATABASE_VERSION = 2;

    // 2. Users Table Constants
    public static final String TABLE_USERS = "users";
    public static final String COLUMN_USER_ID = "id";
    public static final String COLUMN_USERNAME = "username";

    public static final String COLUMN_FIRST_NAME = "first_name";
    public static final String COLUMN_LAST_NAME = "last_name";

    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_PASSWORD = "password";

    // Task Table Constants
    public static final String TABLE_TASKS = "tasks";
    public static final String COLUMN_TASK_ID = "task_id";
    public static final String COLUMN_TASK_TITLE = "task_title";
    public static final String COLUMN_TASK_DESC = "task_description";
    public static final String COLUMN_TASK_USER_ID = "user_id"; // Foreign key mapping to users table
    public static final String COLUMN_TASK_ISCOMPLETE = "is_complete";

    // Constructor: Tells Android OpenHelper to initialize our database file
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USERS_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_USERS + " ("
                + COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_USERNAME + " TEXT UNIQUE, "
                + COLUMN_EMAIL + " TEXT UNIQUE, "
                + COLUMN_FIRST_NAME + " TEXT, "
                + COLUMN_LAST_NAME + " TEXT, "
                + COLUMN_PASSWORD + " TEXT" + ")";

        String CREATE_TASKS_TABLE = "CREATE TABLE " + TABLE_TASKS + "("
                + COLUMN_TASK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_TASK_TITLE + " TEXT, "
                + COLUMN_TASK_DESC + " TEXT, "
                + COLUMN_TASK_ISCOMPLETE + "BOOLEAN DEFAULT false, "
                + COLUMN_TASK_USER_ID + " INTEGER" + ")";
        db.execSQL(CREATE_TASKS_TABLE);

        // Execute the SQL statement to create the table
        db.execSQL(CREATE_USERS_TABLE);
    }

    // 4. onUpgrade runs if you change DATABASE_VERSION integer above
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }

    // 5. Security: Password Hashing
    // Never store plain-text passwords! This method turns "mypassword" into an MD5 hash string.
    public String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            byte[] hash = digest.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return password;
    }

    // 6. Register User Method: Inserts a new row into the users table
    public boolean registerUser(String firstName, String lastName, String email, String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase(); // Get permission to write data
        ContentValues values = new ContentValues();

        values.put(COLUMN_USERNAME, username);
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_FIRST_NAME, firstName);
        values.put(COLUMN_LAST_NAME, lastName);
        values.put(COLUMN_PASSWORD, hashPassword(password)); // Store hash, not plain text!

        long result = db.insert(TABLE_USERS, null, values);
        db.close();

        // db.insert returns -1 if insertion failed (e.g. username already exists)
        return result != -1;
    }

    // 7. Check User Login: Queries the database to see if matching credentials exist
    public boolean checkUser(String login, String password) {
        SQLiteDatabase db = this.getReadableDatabase(); // Get permission to read data
        String hashedPassword = hashPassword(password);

        // A Cursor is a pointer that scans through query results
        Cursor cursor;

        if(login.contains("@")) {
            cursor = db.query(TABLE_USERS,
                    new String[]{COLUMN_USER_ID},
                    COLUMN_EMAIL + " = ? AND " + COLUMN_PASSWORD + " = ?",
                    new String[]{login, hashedPassword},
                    null, null, null);
        }else{
            cursor = db.query(TABLE_USERS,
                    new String[]{COLUMN_USER_ID},
                    COLUMN_USERNAME + " = ? AND " + COLUMN_PASSWORD + " = ?",
                    new String[]{login, hashedPassword},
                    null, null, null);
        }

        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();

        // If count > 0, a matching user was found!
        return cursorCount > 0;
    }

    public boolean addTask(String title, String description, int userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TASK_TITLE, title);
        values.put(COLUMN_TASK_DESC, description);
        values.put(COLUMN_TASK_USER_ID, userId);

        long result = db.insert(TABLE_TASKS, null, values);
        db.close();
        return result != -1;
    }

    public java.util.List<Task> getAllTasksForUser(int userId) {
        java.util.List<Task> taskList = new java.util.ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_TASKS,
                null,
                COLUMN_TASK_USER_ID + " = ?",
                new String[]{String.valueOf(userId)},
                null, null, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_TASK_ID));
                String title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TASK_TITLE));
                String desc = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TASK_DESC));

                taskList.add(new Task(id, title, desc, userId));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return taskList;
    }

    // Update an existing task
    public boolean updateTask(int taskId, String title, String description) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TASK_TITLE, title);
        values.put(COLUMN_TASK_DESC, description);

        int rowsAffected = db.update(TABLE_TASKS, values, COLUMN_TASK_ID + " = ?", new String[]{String.valueOf(taskId)});
        db.close();
        return rowsAffected > 0;
    }

    public boolean toggleTaskComplete(int taskId, boolean is_complete) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
//        values(COLUMN_TASK_ISCOMPLETE, is_complete);

        int rowsAffected = db.update(TABLE_TASKS, values, COLUMN_TASK_ID + " = ?", new String[]{String.valueOf(taskId)});
        db.close();
        return rowsAffected > 0;
    }

    // Delete a task by its ID
    public boolean deleteTask(int taskId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsAffected = db.delete(TABLE_TASKS, COLUMN_TASK_ID + " = ?", new String[]{String.valueOf(taskId)});
        db.close();
        return rowsAffected > 0;
    }
}
