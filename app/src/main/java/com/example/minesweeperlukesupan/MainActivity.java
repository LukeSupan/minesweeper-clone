package com.example.minesweeperlukesupan;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

// this activity is requirement 1: title / instructions screen. it does this fully

/*
layout elements will be:
    ImageView for logo
    TextView for title
    TextView for instructions
    Two Buttons:
        start game
        settings

logic will be:
    settings opens settings activity
    start game opens GameActivity with default or last used settings, which will be stored in SharedPreferences
    this one is pretty easy thankfully





1x image view
2x text view
2x buttons
*/

public class MainActivity extends AppCompatActivity {

    // start and settings buttons variables
    Button startButton, settingsButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // full screen effect for games
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        // loads xml
        setContentView(R.layout.activity_main);

        // makes content work good in edge-to-edge mode
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });




        // get respective buttons
        startButton = findViewById(R.id.buttonStart);
        settingsButton = findViewById(R.id.buttonSettings);

        // switch to GameActivity
        startButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, GameActivity.class);
            startActivity(intent);
        });

        // switch to SettingsActivity
        settingsButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
        });
    }
}