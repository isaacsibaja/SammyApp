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

public class AlphabetActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {

    private TextView tvLetter, tvEnglish, tvSpanish;
    private ImageView ivAlphabetImage;
    private List<AlphabetItem> alphabetList;
    private int currentIndex = 0;
    private TextToSpeech tts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alphabet);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Learn Alphabet");
        }

        tvLetter = findViewById(R.id.tvLetter);
        ivAlphabetImage = findViewById(R.id.ivAlphabetImage);
        tvEnglish = findViewById(R.id.tvEnglish);
        tvSpanish = findViewById(R.id.tvSpanish);

        tts = new TextToSpeech(this, this);
        loadAlphabetData();
        updateUI();

        findViewById(R.id.btnPrevious).setOnClickListener(v -> {
            currentIndex = (currentIndex - 1 + alphabetList.size()) % alphabetList.size();
            updateUI();
        });

        findViewById(R.id.btnNext).setOnClickListener(v -> {
            currentIndex = (currentIndex + 1) % alphabetList.size();
            updateUI();
        });

        findViewById(R.id.btnSpeak).setOnClickListener(v -> speakOut());
    }

    private void loadAlphabetData() {
        alphabetList = new ArrayList<>();
        alphabetList.add(new AlphabetItem("A", "Apple", "Manzana", "apple.png"));
        alphabetList.add(new AlphabetItem("B", "Ball", "Pelota", "ball.png"));
        alphabetList.add(new AlphabetItem("C", "Cat", "Gato", "cat.png"));
        alphabetList.add(new AlphabetItem("D", "Dog", "Perro", "dog.png"));
        alphabetList.add(new AlphabetItem("E", "Egg", "Huevo", "egg.png"));
        alphabetList.add(new AlphabetItem("F", "Fish", "Pez", "fish.png"));
        alphabetList.add(new AlphabetItem("G", "Grape", "Uva", "grape.png"));
        alphabetList.add(new AlphabetItem("H", "Hat", "Sombrero", "hat.png"));
        alphabetList.add(new AlphabetItem("I", "Ice Cream", "Helado", "ice_cream.png"));
        alphabetList.add(new AlphabetItem("J", "Juice", "Jugo", "juice.png"));
        alphabetList.add(new AlphabetItem("K", "Kite", "Cometa", "kite.png"));
        alphabetList.add(new AlphabetItem("L", "Lion", "León", "lion.png"));
        alphabetList.add(new AlphabetItem("M", "Monkey", "Mono", "monkey.png"));
        alphabetList.add(new AlphabetItem("N", "Nose", "Nariz", "nose.png"));
        alphabetList.add(new AlphabetItem("O", "Orange", "Naranja", "orange_fruit.png"));
        alphabetList.add(new AlphabetItem("P", "Pig", "Cerdo", "pig.png"));
        alphabetList.add(new AlphabetItem("Q", "Queen", "Reina", "queen.png"));
        alphabetList.add(new AlphabetItem("R", "Rabbit", "Conejo", "rabbit.png"));
        alphabetList.add(new AlphabetItem("S", "Sun", "Sol", "sun.png"));
        alphabetList.add(new AlphabetItem("T", "Tiger", "Tigre", "tiger.png"));
        alphabetList.add(new AlphabetItem("U", "Umbrella", "Paraguas", "umbrella.png"));
        alphabetList.add(new AlphabetItem("V", "Violin", "Violín", "violin.png"));
        alphabetList.add(new AlphabetItem("W", "Whale", "Ballena", "whale.png"));
        alphabetList.add(new AlphabetItem("X", "Xylophone", "Xilófono", "xylophone.png"));
        alphabetList.add(new AlphabetItem("Y", "Yo-yo", "Yo-yo", "yoyo.png"));
        alphabetList.add(new AlphabetItem("Z", "Zebra", "Cebra", "zebra.png"));
    }

    private void updateUI() {
        AlphabetItem item = alphabetList.get(currentIndex);
        tvLetter.setText(item.letter);
        tvEnglish.setText(item.englishWord);
        tvSpanish.setText(item.spanishWord);

        try (InputStream is = getAssets().open("images/" + item.imageName)) {
            ivAlphabetImage.setImageDrawable(Drawable.createFromStream(is, null));
        } catch (IOException e) {
            Log.e("AlphabetActivity", "Error loading: " + item.imageName);
            ivAlphabetImage.setImageResource(android.R.drawable.ic_menu_report_image);
        }
    }

    private void speakOut() {
        tts.speak(alphabetList.get(currentIndex).englishWord, TextToSpeech.QUEUE_FLUSH, null, null);
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

    private static class AlphabetItem {
        String letter, englishWord, spanishWord, imageName;
        AlphabetItem(String letter, String english, String spanish, String image) {
            this.letter = letter; this.englishWord = english;
            this.spanishWord = spanish; this.imageName = image;
        }
    }
}