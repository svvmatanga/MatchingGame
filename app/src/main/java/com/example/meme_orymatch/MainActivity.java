package com.example.meme_orymatch;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private Button moveToDiffPage, moveToCardCatalog, exitButton;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String playerName = getIntent().getStringExtra("player_na.\\gradlew cleanme"); //PASS USERNAME
        // ------------------------ Initialize buttons ----------------------------------------------
        moveToDiffPage = findViewById(R.id.options);
        moveToCardCatalog = findViewById(R.id.crdcata);
        exitButton = findViewById(R.id.exit);

        //-------------------- OPTION BUTTON -------------------------------------------------------
        moveToDiffPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String playerName = getIntent().getStringExtra("player_name");
                if (playerName == null || playerName.trim().isEmpty()) {
                    Toast.makeText(MainActivity.this, "No user info found. Please log in again.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    String player = playerName.trim().toLowerCase();
                    Intent intent = new Intent(MainActivity.this, DiffPage.class); //NAVIGATE
                    intent.putExtra("player_name", playerName);
                    startActivity(intent);
                }
            }
        });

        // -----------------------------  CARD CATALOG BUTTON -------------------------------------------------------
        moveToCardCatalog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CardCatalog.class);
                intent.putExtra("player_name", playerName);
                startActivity(intent);
            }
        });
        // ------------------------------ EXIT BUTTON -------------------------------------------------
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishAffinity(); // This closes the app
            }
        });
        //------------------------------ SCOREBOARD BUTTON --------------------------------------------------
        Button viewLeaderboard = findViewById(R.id.scrbrd);
        viewLeaderboard.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, LeaderboardActivity.class);
            intent.putExtra("player_name", playerName);
            startActivity(intent);
        });
    }
}
