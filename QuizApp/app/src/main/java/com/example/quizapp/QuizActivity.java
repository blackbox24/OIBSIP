package com.example.quizapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.List;

public class QuizActivity extends AppCompatActivity {

    TextView tvQuestionCounter, tvQuestionText;
    RadioGroup radioGroupOptions;
    RadioButton[] radioButtons = new RadioButton[4];
    Button btnSubmitNext;

    List<Question> questionList;
    int currentQuestionIndex = 0;
    int score = 0;
    int correctCount = 0;
    int incorrectCount = 0;
    boolean isAnswerSubmitted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        tvQuestionCounter = findViewById(R.id.tvQuestionCounter);
        tvQuestionText = findViewById(R.id.tvQuestionText);
        radioGroupOptions = findViewById(R.id.radioGroupOptions);

        radioButtons[0] = findViewById(R.id.rbOption0);
        radioButtons[1] = findViewById(R.id.rbOption1);
        radioButtons[2] = findViewById(R.id.rbOption2);
        radioButtons[3] = findViewById(R.id.rbOption3);

        btnSubmitNext = findViewById(R.id.btnSubmitNext);

        // Load shuffled questions
        questionList = QuestionBank.getQuestions();
        loadQuestion();

        btnSubmitNext.setOnClickListener(v -> {
            if (!isAnswerSubmitted) {
                // Step 1: Validate that an option is selected
                int selectedId = radioGroupOptions.getCheckedRadioButtonId();
                if (selectedId == -1) {
                    Toast.makeText(QuizActivity.this, "Please select an option", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Find index of selected radio button
                int selectedIndex = -1;
                for (int i = 0; i < radioButtons.length; i++) {
                    if (radioButtons[i].getId() == selectedId) {
                        selectedIndex = i;
                        break;
                    }
                }

                checkAnswer(selectedIndex);

            } else {
                // Step 2: Advance to next question or results
                currentQuestionIndex++;
                if (currentQuestionIndex < questionList.size()) {
                    loadQuestion();
                } else {
                    // Navigate to ResultActivity
                    Intent intent = new Intent(QuizActivity.this, ResultActivity.class);
                    intent.putExtra("SCORE", score);
                    intent.putExtra("CORRECT", correctCount);
                    intent.putExtra("INCORRECT", incorrectCount);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    private void loadQuestion() {
        isAnswerSubmitted = false;
        radioGroupOptions.clearCheck();
        btnSubmitNext.setText("Submit Answer");
        resetRadioButtonStyles();

        Question currentQuestion = questionList.get(currentQuestionIndex);

        // Update counter and text
        tvQuestionCounter.setText("Question " + (currentQuestionIndex + 1) + " of " + questionList.size());
        tvQuestionText.setText(currentQuestion.getQuestionText());

        // Set radio button option texts
        String[] options = currentQuestion.getOptions();
        for (int i = 0; i < radioButtons.length; i++) {
            radioButtons[i].setText(options[i]);
        }
    }

    private void checkAnswer(int selectedIndex) {
        isAnswerSubmitted = true;
        Question currentQuestion = questionList.get(currentQuestionIndex);
        int correctIndex = currentQuestion.getCorrectOptionIndex();

        if (selectedIndex == correctIndex) {
            score += 10;
            correctCount++;
            radioButtons[selectedIndex].setTextColor(Color.parseColor("#4CAF50")); // Green
        } else {
            incorrectCount++;
            radioButtons[selectedIndex].setTextColor(Color.parseColor("#F44336")); // Red
            radioButtons[correctIndex].setTextColor(Color.parseColor("#4CAF50")); // Show correct answer in green
        }

        // Disable options after submission
        setRadioButtonsEnabled(false);

        // Update button text for next step
        if (currentQuestionIndex == questionList.size() - 1) {
            btnSubmitNext.setText("Finish Quiz");
        } else {
            btnSubmitNext.setText("Next Question");
        }
    }

    private void resetRadioButtonStyles() {
        for (RadioButton rb : radioButtons) {
            rb.setEnabled(true);
            rb.setTextColor(Color.parseColor("#000000")); // Reset text color to default black/theme color
        }
    }

    private void setRadioButtonsEnabled(boolean enabled) {
        for (RadioButton rb : radioButtons) {
            rb.setEnabled(enabled);
        }
    }
}