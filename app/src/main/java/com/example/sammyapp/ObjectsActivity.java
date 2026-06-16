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

public class ObjectsActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {

    private ImageView ivObject;
    private TextView tvEnglish, tvSpanish;
    private List<ObjectItem> objectList;
    private int currentIndex = 0;
    private TextToSpeech tts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_objects);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Learn Objects");
        }

        ivObject = findViewById(R.id.ivObject);
        tvEnglish = findViewById(R.id.tvEnglish);
        tvSpanish = findViewById(R.id.tvSpanish);

        tts = new TextToSpeech(this, this);
        loadObjectData();
        updateUI();

        findViewById(R.id.btnPrevious).setOnClickListener(v -> {
            currentIndex = (currentIndex - 1 + objectList.size()) % objectList.size();
            updateUI();
        });

        findViewById(R.id.btnNext).setOnClickListener(v -> {
            currentIndex = (currentIndex + 1) % objectList.size();
            updateUI();
        });

        findViewById(R.id.btnSpeak).setOnClickListener(v -> speakOut());
    }

    private void loadObjectData() {
        objectList = new ArrayList<>();
        objectList.add(new ObjectItem("Chair", "Silla", "chair.png"));
        objectList.add(new ObjectItem("Table", "Mesa", "table.png"));
        objectList.add(new ObjectItem("Bed", "Cama", "bed.png"));
        objectList.add(new ObjectItem("Book", "Libro", "book.png"));
        objectList.add(new ObjectItem("Pencil", "Lápiz", "pencil.png"));
        objectList.add(new ObjectItem("Bag", "Mochila", "bag.png"));
        objectList.add(new ObjectItem("Shoe", "Zapato", "shoe.png"));
        objectList.add(new ObjectItem("Clock", "Reloj", "clock.png"));
        objectList.add(new ObjectItem("Door", "Puerta", "door.png"));
        objectList.add(new ObjectItem("Window", "Ventana", "window.png"));
    }

    private void updateUI() {
        ObjectItem item = objectList.get(currentIndex);
        tvEnglish.setText(item.englishName);
        tvSpanish.setText(item.spanishName);

        try (InputStream is = getAssets().open("images/" + item.imageName)) {
            ivObject.setImageDrawable(Drawable.createFromStream(is, null));
        } catch (IOException e) {
            Log.e("ObjectsActivity", "Error loading: " + item.imageName);
            ivObject.setImageResource(android.R.drawable.ic_menu_report_image);
        }
    }

    private void speakOut() {
        tts.speak(objectList.get(currentIndex).englishName, TextToSpeech.QUEUE_FLUSH, null, null);
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

    private static class ObjectItem {
        String englishName, spanishName, imageName;
        ObjectItem(String en, String es, String img) {
            this.englishName = en; this.spanishName = es; this.imageName = img;
        }
    }
}