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

public class FruitsActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {

    private ImageView ivFruit;
    private TextView tvEnglish, tvSpanish;
    private List<FruitItem> fruitList;
    private int currentIndex = 0;
    private TextToSpeech tts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fruits);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Learn Fruits");
        }

        ivFruit = findViewById(R.id.ivFruit);
        tvEnglish = findViewById(R.id.tvEnglish);
        tvSpanish = findViewById(R.id.tvSpanish);

        tts = new TextToSpeech(this, this);
        loadFruitData();
        updateUI();

        findViewById(R.id.btnPrevious).setOnClickListener(v -> {
            currentIndex = (currentIndex - 1 + fruitList.size()) % fruitList.size();
            updateUI();
        });

        findViewById(R.id.btnNext).setOnClickListener(v -> {
            currentIndex = (currentIndex + 1) % fruitList.size();
            updateUI();
        });

        findViewById(R.id.btnSpeak).setOnClickListener(v -> speakOut());
    }

    private void loadFruitData() {
        fruitList = new ArrayList<>();
        fruitList.add(new FruitItem("Apple", "Manzana", "apple.png"));
        fruitList.add(new FruitItem("Banana", "Plátano", "banana.png"));
        fruitList.add(new FruitItem("Orange", "Naranja", "orange_fruit.png"));
        fruitList.add(new FruitItem("Strawberry", "Fresa", "strawberry.png"));
        fruitList.add(new FruitItem("Grape", "Uva", "grape.png"));
        fruitList.add(new FruitItem("Watermelon", "Sandía", "watermelon.png"));
        fruitList.add(new FruitItem("Pineapple", "Piña", "pineapple.png"));
        fruitList.add(new FruitItem("Pear", "Pera", "pear.png"));
        fruitList.add(new FruitItem("Cherry", "Cereza", "cherry.png"));
        fruitList.add(new FruitItem("Lemon", "Limón", "lemon.png"));
    }

    private void updateUI() {
        FruitItem item = fruitList.get(currentIndex);
        tvEnglish.setText(item.englishName);
        tvSpanish.setText(item.spanishName);

        try (InputStream is = getAssets().open("images/" + item.imageName)) {
            ivFruit.setImageDrawable(Drawable.createFromStream(is, null));
        } catch (IOException e) {
            Log.e("FruitsActivity", "Error loading: " + item.imageName);
            ivFruit.setImageResource(android.R.drawable.ic_menu_report_image);
        }
    }

    private void speakOut() {
        tts.speak(fruitList.get(currentIndex).englishName, TextToSpeech.QUEUE_FLUSH, null, null);
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

    private static class FruitItem {
        String englishName, spanishName, imageName;
        FruitItem(String en, String es, String img) {
            this.englishName = en; this.spanishName = es; this.imageName = img;
        }
    }
}