package com.example.meme_orymatch;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class StartPage extends AppCompatActivity {
    private Button moveToDiffPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_start_page);

        String playerName = getIntent().getStringExtra("player_name"); //PASS USERNAME

//--------------------------------- START BUTTON ------------------------------------------
        moveToDiffPage = findViewById(R.id.start);
        moveToDiffPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (playerName == null || playerName.trim().isEmpty()) {
                    Toast.makeText(StartPage.this, "No user info found. Please log in again.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(StartPage.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    String player = playerName.trim().toLowerCase();
                    Intent intent = new Intent(StartPage.this, MainActivity.class); //NAVIGATE
                    intent.putExtra("player_name", playerName);
                    startActivity(intent);
                }
            }
        });
        Toast.makeText(this, "Welcome to MEME-ORY MATCH!", Toast.LENGTH_SHORT).show();
    }
}
