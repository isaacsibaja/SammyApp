package com.example.sammyapp;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class NumbersActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {

    private ImageView ivNumber;
    private TextView tvEnglish, tvSpanish;
    private List<NumberItem> numberList;
    private int currentIndex = 0;
    private TextToSpeech tts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_numbers);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Learn Numbers");
        }

        ivNumber = findViewById(R.id.ivNumber);
        tvEnglish = findViewById(R.id.tvEnglish);
        tvSpanish = findViewById(R.id.tvSpanish);

        tts = new TextToSpeech(this, this);
        loadNumberData();
        updateUI();

        findViewById(R.id.btnPrevious).setOnClickListener(v -> {
            currentIndex = (currentIndex - 1 + numberList.size()) % numberList.size();
            updateUI();
        });

        findViewById(R.id.btnNext).setOnClickListener(v -> {
            currentIndex = (currentIndex + 1) % numberList.size();
            updateUI();
        });

        findViewById(R.id.btnSpeak).setOnClickListener(v -> speakOut());
    }

    private void loadNumberData() {
        numberList = new ArrayList<>();
        numberList.add(new NumberItem("One", "Uno", "one.png"));
        numberList.add(new NumberItem("Two", "Dos", "two.png"));
        numberList.add(new NumberItem("Three", "Tres", "three.png"));
        numberList.add(new NumberItem("Four", "Cuatro", "four.png"));
        numberList.add(new NumberItem("Five", "Cinco", "five.png"));
        numberList.add(new NumberItem("Six", "Seis", "six.png"));
        numberList.add(new NumberItem("Seven", "Siete", "seven.png"));
        numberList.add(new NumberItem("Eight", "Ocho", "eight.png"));
        numberList.add(new NumberItem("Nine", "Nueve", "nine.png"));
        numberList.add(new NumberItem("Ten", "Diez", "ten.png"));
    }

    private void updateUI() {
        NumberItem item = numberList.get(currentIndex);
        tvEnglish.setText(item.englishName);
        tvSpanish.setText(item.spanishName);

        try (InputStream is = getAssets().open("images/" + item.imageName)) {
            ivNumber.setImageDrawable(Drawable.createFromStream(is, null));
        } catch (IOException e) {
            Log.e("NumbersActivity", "Error loading: " + item.imageName);
            ivNumber.setImageResource(android.R.drawable.ic_menu_report_image);
        }
    }

    private void speakOut() {
        tts.speak(numberList.get(currentIndex).englishName, TextToSpeech.QUEUE_FLUSH, null, null);
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) tts.setLanguage(Locale.US);
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

    private static class NumberItem {
        String englishName, spanishName, imageName;
        NumberItem(String en, String es, String img) {
            this.englishName = en; this.spanishName = es; this.imageName = img;
        }
    }
}