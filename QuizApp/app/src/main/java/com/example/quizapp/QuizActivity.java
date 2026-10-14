package com.example.quizapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.List;

public class QuizActivity extends AppCompatActivity {

    TextView tvQuestionCounter, tvQuestionText;
    Button[] optionButtons = new Button[4];
    Button btnNext;

    List<Question> questionList;
    int currentQuestionIndex = 0;
    int score = 0;
    int correctCount = 0;
    int incorrectCount = 0;
    boolean answered = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        tvQuestionCounter = findViewById(R.id.tvQuestionCounter);
        tvQuestionText = findViewById(R.id.tvQuestionText);

        optionButtons[0] = findViewById(R.id.btnOption0);
        optionButtons[1] = findViewById(R.id.btnOption1);
        optionButtons[2] = findViewById(R.id.btnOption2);
        optionButtons[3] = findViewById(R.id.btnOption3);

        btnNext = findViewById(R.id.btnNext);

        // Load shuffled questions
        questionList = QuestionBank.getQuestions();
        loadQuestion();

        // Set click listeners for the 4 option buttons
        for (int i = 0; i < optionButtons.length; i++) {
            final int optionIndex = i;
            optionButtons[i].setOnClickListener(v -> {
                if (!answered) {
                    checkAnswer(optionIndex);
                }
            });
        }

        // Handle Next button click
        btnNext.setOnClickListener(v -> {
            currentQuestionIndex++;
            if (currentQuestionIndex < questionList.size()) {
                loadQuestion();
            } else {
                // Quiz completed, transition to ResultActivity
                Intent intent = new Intent(QuizActivity.this, ResultActivity.class);
                intent.putExtra("SCORE", score);
                intent.putExtra("CORRECT", correctCount);
                intent.putExtra("INCORRECT", incorrectCount);
                startActivity(intent);
                finish();
            }
        });
    }

    private void loadQuestion() {
        // Reset states for the new question
        answered = false;
        btnNext.setEnabled(false);
        resetButtonColors();

        Question currentQuestion = questionList.get(currentQuestionIndex);

        // Update counter and question text
        tvQuestionCounter.setText("Question " + (currentQuestionIndex + 1) + " of " + questionList.size());
        tvQuestionText.setText(currentQuestion.getQuestionText());

        // Populate option texts
        String[] options = currentQuestion.getOptions();
        for (int i = 0; i < optionButtons.length; i++) {
            optionButtons[i].setText(options[i]);
        }
    }

    private void checkAnswer(int selectedOptionIndex) {
        answered = true;
        Question currentQuestion = questionList.get(currentQuestionIndex);
        int correctIndex = currentQuestion.getCorrectOptionIndex();

        if (selectedOptionIndex == correctIndex) {
            // Correct answer
            score += 10; // 10 points per question
            correctCount++;
            optionButtons[selectedOptionIndex].setBackgroundColor(Color.parseColor("#4CAF50")); // Green
        } else {
            // Wrong answer
            incorrectCount++;
            optionButtons[selectedOptionIndex].setBackgroundColor(Color.parseColor("#F44336")); // Red
            // Highlight the correct option in green so the user learns
            optionButtons[correctIndex].setBackgroundColor(Color.parseColor("#4CAF50"));
        }

        // Disable options so user can't change answer
        for (Button btn : optionButtons) {
            btn.setEnabled(false);
        }

        // Enable Next or finish button
        btnNext.setEnabled(true);
        if (currentQuestionIndex == questionList.size() - 1) {
            btnNext.setText("Finish Quiz");
        }
    }

    private void resetButtonColors() {
        for (Button btn : optionButtons) {
            btn.setBackgroundColor(Color.parseColor("#6200EE")); // Default primary purple tint (or standard button color)
            btn.setEnabled(true);
        }
    }
}