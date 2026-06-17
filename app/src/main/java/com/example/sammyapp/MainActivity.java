package com.example.sammyapp;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SplashScreen.installSplashScreen(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btnColors).setOnClickListener(v -> 
            startActivity(new Intent(this, ColorsActivity.class)));

        findViewById(R.id.btnAnimals).setOnClickListener(v -> 
            startActivity(new Intent(this, AnimalsActivity.class)));

        findViewById(R.id.btnAlphabet).setOnClickListener(v -> 
            startActivity(new Intent(this, AlphabetActivity.class)));

        findViewById(R.id.btnNumbers).setOnClickListener(v -> 
            startActivity(new Intent(this, NumbersActivity.class)));

        findViewById(R.id.btnFruits).setOnClickListener(v -> 
            startActivity(new Intent(this, FruitsActivity.class)));

        findViewById(R.id.btnObjects).setOnClickListener(v -> 
            startActivity(new Intent(this, ObjectsActivity.class)));

        findViewById(R.id.btnDays).setOnClickListener(v -> 
            startActivity(new Intent(this, DaysActivity.class)));
    }
}