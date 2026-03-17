package com.example.meme_orymatch;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class DiffPage extends AppCompatActivity {
    Button buttonEasy, buttonMedium, buttonHard, backbtn; // INITIALIZE BUTTONS

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diff_page);

        String playerName = getIntent().getStringExtra("player_name"); // PASS USERNAME

        //BUTTON ID'S
        buttonEasy = findViewById(R.id.buttonEasy);
        buttonMedium = findViewById(R.id.buttonMedium);
        buttonHard = findViewById(R.id.buttonHard);
        backbtn =findViewById(R.id.backbtn);

        // BUTTONS FOR EASY, MEDIUM, HARD
        buttonEasy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGame("easy");
            }
        });
        buttonMedium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGame("medium");
            }
        });
        buttonHard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGame("hard");
            }
        });
//---------------------------------- BACK BUTTON -----------------------------------------
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DiffPage.this, MainActivity.class);
                intent.putExtra("player_name", playerName);
                startActivity(intent);
                finish();
            }
        });
    }
    // -------------- START THE GAME BASED ON THE SELECTED DIFFICULTY LEVEL ---------------------------------
    private void startGame(String difficulty) {
        String playerName = getIntent().getStringExtra("player_name");
        if (playerName == null || playerName.trim().isEmpty()) {
            Toast.makeText(DiffPage.this, "No user info found. Please log in again.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(DiffPage.this, LoginActivity.class);
            startActivity(intent);
            finish(); // Close the current activity
        } else {
            String player = playerName.trim().toLowerCase();
            Intent intent = new Intent(DiffPage.this, GamePage.class);
            intent.putExtra("player_name", playerName);
            intent.putExtra("difficulty", difficulty);  // Pass the selected difficulty
            startActivity(intent);
        }
    }
}
