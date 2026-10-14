package com.example.todoapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView tvResultMessage = findViewById(R.id.tvResultMessage);
        Button btnBack = findViewById(R.id.btnBack);

        // Retrieve the data passed through the Intent
        String incomingMessage = getIntent().getStringExtra("EXTRA_MESSAGE");
        if (incomingMessage != null) {
            tvResultMessage.setText(incomingMessage);
        }

        // Back button simply finishes this activity and returns to the previous screen
        btnBack.setOnClickListener(v -> finish());
    }
}
