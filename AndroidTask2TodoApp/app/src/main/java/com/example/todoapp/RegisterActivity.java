
package com.example.todoapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity  extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        TextView tvResultMessage = findViewById(R.id.tvResultMessage);
        Button btnToGoLogin = findViewById(R.id.btnRegister);



        // Back button simply finishes this activity and returns to the previous screen
        btnToGoLogin.setOnClickListener(v -> {
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);

            startActivity(intent);
        });
    }
}
