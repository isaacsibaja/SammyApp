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

public class AnimalsActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {

    private ImageView ivAnimal;
    private TextView tvEnglish, tvSpanish;
    private List<AnimalItem> animalList;
    private int currentIndex = 0;
    private TextToSpeech tts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animals);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Learn Animals");
        }

        ivAnimal = findViewById(R.id.ivAnimal);
        tvEnglish = findViewById(R.id.tvEnglish);
        tvSpanish = findViewById(R.id.tvSpanish);

        tts = new TextToSpeech(this, this);
        loadAnimalData();
        updateUI();

        findViewById(R.id.btnPrevious).setOnClickListener(v -> {
            currentIndex = (currentIndex - 1 + animalList.size()) % animalList.size();
            updateUI();
        });

        findViewById(R.id.btnNext).setOnClickListener(v -> {
            currentIndex = (currentIndex + 1) % animalList.size();
            updateUI();
        });

        findViewById(R.id.btnSpeak).setOnClickListener(v -> speakOut());
    }

    private void loadAnimalData() {
        animalList = new ArrayList<>();
        animalList.add(new AnimalItem("Dog", "Perro", "dog.png"));
        animalList.add(new AnimalItem("Cat", "Gato", "cat.png"));
        animalList.add(new AnimalItem("Cow", "Vaca", "cow.png"));
        animalList.add(new AnimalItem("Duck", "Pato", "duck.png"));
        animalList.add(new AnimalItem("Bird", "Pájaro", "bird.png"));
        animalList.add(new AnimalItem("Fish", "Pez", "fish.png"));
        animalList.add(new AnimalItem("Frog", "Rana", "frog.png"));
        animalList.add(new AnimalItem("Lion", "León", "lion.png"));
        animalList.add(new AnimalItem("Tiger", "Tigre", "tiger.png"));
        animalList.add(new AnimalItem("Elephant", "Elefante", "elephant.png"));
        animalList.add(new AnimalItem("Monkey", "Mono", "monkey.png"));
        animalList.add(new AnimalItem("Rabbit", "Conejo", "rabbit.png"));
        animalList.add(new AnimalItem("Horse", "Caballo", "horse.png"));
        animalList.add(new AnimalItem("Pig", "Cerdo", "pig.png"));
        animalList.add(new AnimalItem("Sheep", "Oveja", "sheep.png"));
    }

    private void updateUI() {
        AnimalItem item = animalList.get(currentIndex);
        tvEnglish.setText(item.englishName);
        tvSpanish.setText(item.spanishName);

        try (InputStream is = getAssets().open("images/" + item.imageName)) {
            ivAnimal.setImageDrawable(Drawable.createFromStream(is, null));
        } catch (IOException e) {
            Log.e("AnimalsActivity", "Error loading: " + item.imageName);
            ivAnimal.setImageResource(android.R.drawable.ic_menu_report_image);
        }
    }

    private void speakOut() {
        tts.speak(animalList.get(currentIndex).englishName, TextToSpeech.QUEUE_FLUSH, null, null);
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

    private static class AnimalItem {
        String englishName, spanishName, imageName;
        AnimalItem(String en, String es, String img) {
            this.englishName = en; this.spanishName = es; this.imageName = img;
        }
    }
}