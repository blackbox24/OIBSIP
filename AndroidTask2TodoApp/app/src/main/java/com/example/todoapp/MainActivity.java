package com.example.todoapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    TextView tvWelcomeUser;
    Button btnLogout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvWelcomeUser = findViewById(R.id.tvWelcomeUser);
        btnLogout = findViewById(R.id.btnLogout);

        // 1. Retrieve the username or email passed from LoginActivity via Intent extra
        String userIdentifier = getIntent().getStringExtra("USERNAME");

        if (userIdentifier != null && !userIdentifier.isEmpty()) {
            tvWelcomeUser.setText(String.format("Welcome back, %s!", userIdentifier));
        } else {
            tvWelcomeUser.setText("Welcome to your To-Do Dashboard!");
        }

        // Back button simply finishes this activity and returns to the previous screen
        // 2. Handle Logout action
        btnLogout.setOnClickListener(v -> {
            // Return to Log in screen
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish(); // Destroy MainActivity so user can't press back into the dashboard after logging out
        });
    }
}
