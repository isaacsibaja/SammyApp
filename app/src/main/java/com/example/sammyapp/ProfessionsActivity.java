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

public class ProfessionsActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {

    private ImageView ivProfession;
    private TextView tvEnglish, tvSpanish;
    private List<ProfessionItem> professionList;
    private int currentIndex = 0;
    private TextToSpeech tts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professions);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Learn Professions");
        }

        ivProfession = findViewById(R.id.ivProfession);
        tvEnglish = findViewById(R.id.tvEnglish);
        tvSpanish = findViewById(R.id.tvSpanish);

        tts = new TextToSpeech(this, this);
        loadProfessionData();
        updateUI();

        findViewById(R.id.btnPrevious).setOnClickListener(v -> {
            currentIndex = (currentIndex - 1 + professionList.size()) % professionList.size();
            updateUI();
        });

        findViewById(R.id.btnNext).setOnClickListener(v -> {
            currentIndex = (currentIndex + 1) % professionList.size();
            updateUI();
        });

        findViewById(R.id.btnSpeak).setOnClickListener(v -> speakOut());
    }

    private void loadProfessionData() {
        professionList = new ArrayList<>();
        professionList.add(new ProfessionItem("Teacher", "Profesor", "teacher.png"));
        professionList.add(new ProfessionItem("Doctor", "Doctor", "doctor.png"));
        professionList.add(new ProfessionItem("Nurse", "Enfermera", "nurse.png"));
        professionList.add(new ProfessionItem("Firefighter", "Bombero", "firefighter.png"));
        professionList.add(new ProfessionItem("Police Officer", "Policía", "police.png"));
        professionList.add(new ProfessionItem("Chef", "Cocinero", "chef.png"));
        professionList.add(new ProfessionItem("Pilot", "Piloto", "pilot.png"));
        professionList.add(new ProfessionItem("Astronaut", "Astronauta", "astronaut.png"));
        professionList.add(new ProfessionItem("Scientist", "Científico", "scientist.png"));
        professionList.add(new ProfessionItem("Artist", "Artista", "artist.png"));
        professionList.add(new ProfessionItem("Musician", "Músico", "musician.png"));
        professionList.add(new ProfessionItem("Farmer", "Granjero", "farmer.png"));
        professionList.add(new ProfessionItem("Veterinarian", "Veterinario", "veterinarian.png"));
        professionList.add(new ProfessionItem("Dentist", "Dentista", "dentist.png"));
        professionList.add(new ProfessionItem("Engineer", "Ingeniero", "engineer.png"));
        professionList.add(new ProfessionItem("Carpenter", "Carpintero", "carpenter.png"));
        professionList.add(new ProfessionItem("Baker", "Panadero", "baker.png"));
        professionList.add(new ProfessionItem("Photographer", "Fotógrafo", "photographer.png"));
        professionList.add(new ProfessionItem("Athlete", "Atleta", "athlete.png"));
        professionList.add(new ProfessionItem("Lawyer", "Abogado", "lawyer.png"));
    }

    private void updateUI() {
        ProfessionItem item = professionList.get(currentIndex);
        tvEnglish.setText(item.englishName);
        tvSpanish.setText(item.spanishName);

        try (InputStream is = getAssets().open("images/" + item.imageName)) {
            ivProfession.setImageDrawable(Drawable.createFromStream(is, null));
        } catch (IOException e) {
            Log.e("ProfessionsActivity", "Error loading: " + item.imageName);
            ivProfession.setImageResource(android.R.drawable.ic_menu_report_image);
        }
    }

    private void speakOut() {
        tts.speak(professionList.get(currentIndex).englishName, TextToSpeech.QUEUE_FLUSH, null, null);
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

    private static class ProfessionItem {
        String englishName, spanishName, imageName;
        ProfessionItem(String en, String es, String img) {
            this.englishName = en; this.spanishName = es; this.imageName = img;
        }
    }
}