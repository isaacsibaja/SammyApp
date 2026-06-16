package com.example.sammyapp;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ColorsActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {

    private ImageView ivColor;
    private TextView tvEnglish, tvSpanish;
    private List<ColorItem> colorList;
    private int currentIndex = 0;
    private TextToSpeech tts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_colors);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Learn Colors");
        }

        ivColor = findViewById(R.id.ivColor);
        tvEnglish = findViewById(R.id.tvEnglish);
        tvSpanish = findViewById(R.id.tvSpanish);
        
        tts = new TextToSpeech(this, this);
        loadColorData();
        updateUI();

        findViewById(R.id.btnPrevious).setOnClickListener(v -> {
            currentIndex = (currentIndex - 1 + colorList.size()) % colorList.size();
            updateUI();
        });

        findViewById(R.id.btnNext).setOnClickListener(v -> {
            currentIndex = (currentIndex + 1) % colorList.size();
            updateUI();
        });

        findViewById(R.id.btnSpeak).setOnClickListener(v -> speakOut());
    }

    private void loadColorData() {
        colorList = new ArrayList<>();
        colorList.add(new ColorItem("Red", "Rojo", "red.png"));
        colorList.add(new ColorItem("Blue", "Azul", "blue.png"));
        colorList.add(new ColorItem("Yellow", "Amarillo", "yellow.png"));
        colorList.add(new ColorItem("Green", "Verde", "green.png"));
        colorList.add(new ColorItem("Purple", "Morado", "purple.png"));
        colorList.add(new ColorItem("Pink", "Rosa", "pink.png"));
        colorList.add(new ColorItem("Orange", "Naranja", "orange.png"));
    }

    private void updateUI() {
        ColorItem item = colorList.get(currentIndex);
        tvEnglish.setText(item.englishName);
        tvSpanish.setText(item.spanishName);

        try (InputStream is = getAssets().open("images/" + item.imageName)) {
            ivColor.setImageDrawable(Drawable.createFromStream(is, null));
        } catch (IOException e) {
            Log.e("ColorsActivity", "Error loading: " + item.imageName);
            ivColor.setImageResource(android.R.drawable.ic_menu_report_image);
        }
    }

    private void speakOut() {
        tts.speak(colorList.get(currentIndex).englishName, TextToSpeech.QUEUE_FLUSH, null, null);
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

    private static class ColorItem {
        String englishName, spanishName, imageName;
        ColorItem(String en, String es, String img) {
            this.englishName = en; this.spanishName = es; this.imageName = img;
        }
    }
}