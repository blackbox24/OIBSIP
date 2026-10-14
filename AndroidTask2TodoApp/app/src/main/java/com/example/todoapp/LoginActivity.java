package com.example.todoapp; // Match your package name

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends  AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 1. Inflate (load) the XML layout onto the device screen
        setContentView(R.layout.activity_login);

        // 2. Bridge the Java button variable to the XML button ID
        Button btnGoToMain = findViewById(R.id.btnLogin);
        Button btnGotToRegister = findViewById(R.id.btnRegister);

        // 3. Set a click listener to listen for the user tapping the button
        btnGoToMain.setOnClickListener(v -> {
            // 4. Create an Intent (the train ticket) to travel from FirstActivity to SecondActivity
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);

            // Optional: Pass data across screens
            intent.putExtra("EXTRA_MESSAGE", "Hello from Screen One!");

            // 5. Trigger the transition
            startActivity(intent);
        });

        btnGotToRegister.setOnClickListener(v -> {
            // 4. Create an Intent (the train ticket) to travel from FirstActivity to SecondActivity
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);

            // Optional: Pass data across screens
            intent.putExtra("EXTRA_MESSAGE", "Hello new comer!");

            // 5. Trigger the transition
            startActivity(intent);
        });
    }
}