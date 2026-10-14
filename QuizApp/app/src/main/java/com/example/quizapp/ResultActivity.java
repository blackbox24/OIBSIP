package com.example.quizapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class ResultActivity extends AppCompatActivity {

    TextView tvFinalScore, tvCorrectCount, tvIncorrectCount;
    Button btnRestart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        tvFinalScore = findViewById(R.id.tvFinalScore);
        tvCorrectCount = findViewById(R.id.tvCorrectCount);
        tvIncorrectCount = findViewById(R.id.tvIncorrectCount);
        btnRestart = findViewById(R.id.btnRestart);

        // Retrieve data passed from QuizActivity via Intent extras
        int score = getIntent().getIntExtra("SCORE", 0);
        int correct = getIntent().getIntExtra("CORRECT", 0);
        int incorrect = getIntent().getIntExtra("INCORRECT", 0);

        // Display metrics
        tvFinalScore.setText("Final Score: " + score);
        tvCorrectCount.setText("Correct Answers: " + correct);
        tvIncorrectCount.setText("Incorrect Answers: " + incorrect);

        // Handle Restart Action
        btnRestart.setOnClickListener(v -> {
            Intent intent = new Intent(ResultActivity.this, MainActivity.class);
            // Clear activity stack so pressing back doesn't loop back to results
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });
    }
}