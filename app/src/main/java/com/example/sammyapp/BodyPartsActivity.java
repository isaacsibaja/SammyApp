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

public class BodyPartsActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {

    private ImageView ivBodyPart;
    private TextView tvEnglish, tvSpanish;
    private List<BodyPartItem> bodyPartList;
    private int currentIndex = 0;
    private TextToSpeech tts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_body_parts);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Learn Body Parts");
        }

        ivBodyPart = findViewById(R.id.ivBodyPart);
        tvEnglish = findViewById(R.id.tvEnglish);
        tvSpanish = findViewById(R.id.tvSpanish);

        tts = new TextToSpeech(this, this);
        loadBodyPartData();
        updateUI();

        findViewById(R.id.btnPrevious).setOnClickListener(v -> {
            currentIndex = (currentIndex - 1 + bodyPartList.size()) % bodyPartList.size();
            updateUI();
        });

        findViewById(R.id.btnNext).setOnClickListener(v -> {
            currentIndex = (currentIndex + 1) % bodyPartList.size();
            updateUI();
        });

        findViewById(R.id.btnSpeak).setOnClickListener(v -> speakOut());
    }

    private void loadBodyPartData() {
        bodyPartList = new ArrayList<>();
        bodyPartList.add(new BodyPartItem("Head", "Cabeza", "head.png"));
        bodyPartList.add(new BodyPartItem("Eye", "Ojo", "eye.png"));
        bodyPartList.add(new BodyPartItem("Ear", "Oreja", "ear.png"));
        bodyPartList.add(new BodyPartItem("Nose", "Nariz", "nose.png"));
        bodyPartList.add(new BodyPartItem("Mouth", "Boca", "mouth.png"));
        bodyPartList.add(new BodyPartItem("Shoulder", "Hombro", "shoulder.png"));
        bodyPartList.add(new BodyPartItem("Arm", "Brazo", "arm.png"));
        bodyPartList.add(new BodyPartItem("Hand", "Mano", "hand.png"));
        bodyPartList.add(new BodyPartItem("Finger", "Dedo", "finger.png"));
        bodyPartList.add(new BodyPartItem("Leg", "Pierna", "leg.png"));
        bodyPartList.add(new BodyPartItem("Foot", "Pie", "foot.png"));
        bodyPartList.add(new BodyPartItem("Knee", "Rodilla", "knee.png"));
    }

    private void updateUI() {
        BodyPartItem item = bodyPartList.get(currentIndex);
        tvEnglish.setText(item.englishName);
        tvSpanish.setText(item.spanishName);

        try (InputStream is = getAssets().open("images/" + item.imageName)) {
            ivBodyPart.setImageDrawable(Drawable.createFromStream(is, null));
        } catch (IOException e) {
            Log.e("BodyPartsActivity", "Error loading: " + item.imageName);
            ivBodyPart.setImageResource(android.R.drawable.ic_menu_report_image);
        }
    }

    private void speakOut() {
        tts.speak(bodyPartList.get(currentIndex).englishName, TextToSpeech.QUEUE_FLUSH, null, null);
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

    private static class BodyPartItem {
        String englishName, spanishName, imageName;
        BodyPartItem(String en, String es, String img) {
            this.englishName = en; this.spanishName = es; this.imageName = img;
        }
    }
}