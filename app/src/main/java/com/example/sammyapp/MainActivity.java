package com.example.sammyapp;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btnColors).setOnClickListener(v -> 
            startActivity(new Intent(this, ColorsActivity.class)));

        findViewById(R.id.btnAnimals).setOnClickListener(v -> 
            startActivity(new Intent(this, AnimalsActivity.class)));

        findViewById(R.id.btnAlphabet).setOnClickListener(v -> 
            startActivity(new Intent(this, AlphabetActivity.class)));
    }
}