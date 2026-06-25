package com.example.sammyapp;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class MathActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {

    private TextView tvTableContent, tvTableContent1, tvTableContent2, tvSpanish;
    private List<MathTable> mathTables;
    private int currentIndex = 0;
    private TextToSpeech tts;
    private Random random = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_math);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Math Tables");
        }

        // Detectar si estamos en Portrait (tvTableContent) o Landscape (tvTableContent1/2)
        tvTableContent = findViewById(R.id.tvTableContent);
        tvTableContent1 = findViewById(R.id.tvTableContent1);
        tvTableContent2 = findViewById(R.id.tvTableContent2);
        tvSpanish = findViewById(R.id.tvSpanish);

        tts = new TextToSpeech(this, this);
        loadMathData();
        updateUI();

        findViewById(R.id.btnPrevious).setOnClickListener(v -> {
            currentIndex = (currentIndex - 1 + mathTables.size()) % mathTables.size();
            updateUI();
            speakTableName();
        });

        findViewById(R.id.btnNext).setOnClickListener(v -> {
            currentIndex = (currentIndex + 1) % mathTables.size();
            updateUI();
            speakTableName();
        });

        findViewById(R.id.btnPractice).setOnClickListener(v -> showPracticeDialog());
    }

    private void loadMathData() {
        mathTables = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            StringBuilder col1 = new StringBuilder();
            StringBuilder col2 = new StringBuilder();
            StringBuilder full = new StringBuilder();

            for (int j = 1; j <= 10; j++) {
                String line = i + " x " + j + " = " + (i * j);
                full.append(line).append("\n");
                
                if (j <= 5) {
                    col1.append(line).append("\n");
                } else {
                    col2.append(line).append("\n");
                }
            }

            mathTables.add(new MathTable(
                    i,
                    "Table of " + i,
                    "Tabla del " + i,
                    full.toString().trim(),
                    col1.toString().trim(),
                    col2.toString().trim()
            ));
        }
    }

    private void updateUI() {
        MathTable table = mathTables.get(currentIndex);
        tvSpanish.setText(table.spanishTitle);

        // Si estamos en Portrait
        if (tvTableContent != null) {
            tvTableContent.setText(table.content);
        }
        
        // Si estamos en Landscape (dos columnas)
        if (tvTableContent1 != null && tvTableContent2 != null) {
            tvTableContent1.setText(table.contentCol1);
            tvTableContent2.setText(table.contentCol2);
        }
    }

    private void speakTableName() {
        if (tts != null) {
            tts.speak(mathTables.get(currentIndex).englishTitle, TextToSpeech.QUEUE_FLUSH, null, null);
        }
    }

    private void showPracticeDialog() {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_practice, null);
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(dialogView)
                .setCancelable(false)
                .create();

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }

        TextView tvQuestion = dialogView.findViewById(R.id.tvQuestion);
        TextInputEditText etAnswer = dialogView.findViewById(R.id.etAnswer);
        TextView tvFeedback = dialogView.findViewById(R.id.tvFeedback);
        MaterialButton btnVerify = dialogView.findViewById(R.id.btnVerify);
        MaterialButton btnNextQuestion = dialogView.findViewById(R.id.btnNextQuestion);
        View btnClose = dialogView.findViewById(R.id.btnClosePractice);

        final int tableNumber = mathTables.get(currentIndex).number;
        final int[] randomMultiplier = {random.nextInt(10) + 1};

        tvQuestion.setText(tableNumber + " x " + randomMultiplier[0] + " = ?");

        btnVerify.setOnClickListener(v -> {
            String input = etAnswer.getText().toString().trim();
            if (input.isEmpty()) return;

            int userAnswer = Integer.parseInt(input);
            int correctAnswer = tableNumber * randomMultiplier[0];

            tvFeedback.setVisibility(View.VISIBLE);
            if (userAnswer == correctAnswer) {
                tvFeedback.setText("Correct! 🌟");
                tvFeedback.setTextColor(getResources().getColor(R.color.pastel_green));
                if (tts != null) tts.speak("Correct", TextToSpeech.QUEUE_ADD, null, null);
            } else {
                tvFeedback.setText("Try again ❌");
                tvFeedback.setTextColor(android.graphics.Color.RED);
                if (tts != null) tts.speak("Try again", TextToSpeech.QUEUE_ADD, null, null);
            }
        });

        btnNextQuestion.setOnClickListener(v -> {
            randomMultiplier[0] = random.nextInt(10) + 1;
            tvQuestion.setText(tableNumber + " x " + randomMultiplier[0] + " = ?");
            etAnswer.setText("");
            tvFeedback.setVisibility(View.INVISIBLE);
        });

        btnClose.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            tts.setLanguage(Locale.US);
            tts.speak("Mathematics", TextToSpeech.QUEUE_FLUSH, null, null);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }

    private static class MathTable {
        int number;
        String englishTitle, spanishTitle, content, contentCol1, contentCol2;
        MathTable(int number, String en, String es, String content, String c1, String c2) {
            this.number = number;
            this.englishTitle = en;
            this.spanishTitle = es;
            this.content = content;
            this.contentCol1 = c1;
            this.contentCol2 = c2;
        }
    }
}