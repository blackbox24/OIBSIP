package com.example.todoapp; // Match your package name

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Text;

public class LoginActivity extends  AppCompatActivity {
    DatabaseHelper databaseHelper;
    EditText etLoginInput, etPassword;
    Button btnLogin;
    TextView btnGoToRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 1. Inflate (load) the XML layout onto the device screen
        setContentView(R.layout.activity_login);

        databaseHelper = new DatabaseHelper(this);

        // 2. Bridge the Java button variable to the XML button ID
        etLoginInput = findViewById(R.id.etLoginUsername);
        etPassword = findViewById(R.id.etLoginPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnGoToRegister = findViewById(R.id.btnGoToRegister);

        // Navigate to Register screen when "New here" is clicked
        btnGoToRegister.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

        btnLogin.setOnClickListener(v -> {
            String login = etLoginInput.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            System.out.println("Login: " + login + " password: " + password);

            if (login.isEmpty() || password.isEmpty()) {
                Toast.makeText(LoginActivity.this, "Please enter login credentials", Toast.LENGTH_SHORT).show();
                return;
            }

            // Check credentials against SQLite database
            boolean isValidUser = databaseHelper.checkUser(login, password);

            if (isValidUser) {
                Toast.makeText(LoginActivity.this, "Login Successful!", Toast.LENGTH_SHORT).show();

                // Transition to Main Task Activity and pass the username along via Intent extra
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.putExtra("USERNAME", login);
                startActivity(intent);
                finish(); // Close login so pressing back exits the app instead of looping back
            } else {
                Toast.makeText(LoginActivity.this, "Invalid username/email or password", Toast.LENGTH_SHORT).show();
            }
        });
    }
}