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

public class MonthsActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {

    private ImageView ivMonth;
    private TextView tvEnglish, tvSpanish;
    private List<MonthItem> monthList;
    private int currentIndex = 0;
    private TextToSpeech tts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_months);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Learn Months");
        }

        ivMonth = findViewById(R.id.ivMonth);
        tvEnglish = findViewById(R.id.tvEnglish);
        tvSpanish = findViewById(R.id.tvSpanish);

        tts = new TextToSpeech(this, this);
        loadMonthData();
        updateUI();

        findViewById(R.id.btnPrevious).setOnClickListener(v -> {
            currentIndex = (currentIndex - 1 + monthList.size()) % monthList.size();
            updateUI();
        });

        findViewById(R.id.btnNext).setOnClickListener(v -> {
            currentIndex = (currentIndex + 1) % monthList.size();
            updateUI();
        });

        findViewById(R.id.btnSpeak).setOnClickListener(v -> speakOut());
    }

    private void loadMonthData() {
        monthList = new ArrayList<>();
        monthList.add(new MonthItem("January", "Enero", "january.png"));
        monthList.add(new MonthItem("February", "Febrero", "february.png"));
        monthList.add(new MonthItem("March", "Marzo", "march.png"));
        monthList.add(new MonthItem("April", "Abril", "april.png"));
        monthList.add(new MonthItem("May", "Mayo", "may.png"));
        monthList.add(new MonthItem("June", "Junio", "june.png"));
        monthList.add(new MonthItem("July", "Julio", "july.png"));
        monthList.add(new MonthItem("August", "Agosto", "august.png"));
        monthList.add(new MonthItem("September", "Septiembre", "september.png"));
        monthList.add(new MonthItem("October", "Octubre", "october.png"));
        monthList.add(new MonthItem("November", "Noviembre", "november.png"));
        monthList.add(new MonthItem("December", "Diciembre", "december.png"));
    }

    private void updateUI() {
        MonthItem item = monthList.get(currentIndex);
        tvEnglish.setText(item.englishName);
        tvSpanish.setText(item.spanishName);

        try (InputStream is = getAssets().open("images/" + item.imageName)) {
            ivMonth.setImageDrawable(Drawable.createFromStream(is, null));
        } catch (IOException e) {
            Log.e("MonthsActivity", "Error loading: " + item.imageName);
            ivMonth.setImageResource(android.R.drawable.ic_menu_report_image);
        }
    }

    private void speakOut() {
        tts.speak(monthList.get(currentIndex).englishName, TextToSpeech.QUEUE_FLUSH, null, null);
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

    private static class MonthItem {
        String englishName, spanishName, imageName;
        MonthItem(String en, String es, String img) {
            this.englishName = en; this.spanishName = es; this.imageName = img;
        }
    }
}