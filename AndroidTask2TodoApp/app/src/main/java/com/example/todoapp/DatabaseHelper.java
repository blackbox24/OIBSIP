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
    private static final int DATABASE_VERSION = 1;

    // 2. Users Table Constants
    public static final String TABLE_USERS = "users";
    public static final String COLUMN_USER_ID = "id";
    public static final String COLUMN_USERNAME = "username";

    public static final String COLUMN_FIRST_NAME = "first_name";
    public static final String COLUMN_LAST_NAME = "last_name";

    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_PASSWORD = "password";

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
}
