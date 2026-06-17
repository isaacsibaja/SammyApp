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

public class DaysActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {

    private ImageView ivDay;
    private TextView tvEnglish, tvSpanish;
    private List<DayItem> dayList;
    private int currentIndex = 0;
    private TextToSpeech tts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_days);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Learn Days of the Week");
        }

        ivDay = findViewById(R.id.ivDay);
        tvEnglish = findViewById(R.id.tvEnglish);
        tvSpanish = findViewById(R.id.tvSpanish);

        tts = new TextToSpeech(this, this);
        loadDayData();
        updateUI();

        findViewById(R.id.btnPrevious).setOnClickListener(v -> {
            currentIndex = (currentIndex - 1 + dayList.size()) % dayList.size();
            updateUI();
        });

        findViewById(R.id.btnNext).setOnClickListener(v -> {
            currentIndex = (currentIndex + 1) % dayList.size();
            updateUI();
        });

        findViewById(R.id.btnSpeak).setOnClickListener(v -> speakOut());
    }

    private void loadDayData() {
        dayList = new ArrayList<>();
        dayList.add(new DayItem("Monday", "Lunes", "monday.png"));
        dayList.add(new DayItem("Tuesday", "Martes", "tuesday.png"));
        dayList.add(new DayItem("Wednesday", "Miércoles", "wednesday.png"));
        dayList.add(new DayItem("Thursday", "Jueves", "thursday.png"));
        dayList.add(new DayItem("Friday", "Viernes", "friday.png"));
        dayList.add(new DayItem("Saturday", "Sábado", "saturday.png"));
        dayList.add(new DayItem("Sunday", "Domingo", "sunday.png"));
    }

    private void updateUI() {
        DayItem item = dayList.get(currentIndex);
        tvEnglish.setText(item.englishName);
        tvSpanish.setText(item.spanishName);

        try (InputStream is = getAssets().open("images/" + item.imageName)) {
            ivDay.setImageDrawable(Drawable.createFromStream(is, null));
        } catch (IOException e) {
            Log.e("DaysActivity", "Error loading: " + item.imageName);
            ivDay.setImageResource(android.R.drawable.ic_menu_report_image);
        }
    }

    private void speakOut() {
        tts.speak(dayList.get(currentIndex).englishName, TextToSpeech.QUEUE_FLUSH, null, null);
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

    private static class DayItem {
        String englishName, spanishName, imageName;
        DayItem(String en, String es, String img) {
            this.englishName = en; this.spanishName = es; this.imageName = img;
        }
    }
}