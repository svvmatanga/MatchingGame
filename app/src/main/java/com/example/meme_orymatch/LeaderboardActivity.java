package com.example.meme_orymatch;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class LeaderboardActivity extends AppCompatActivity {

    TextView leaderboardText;
    LeaderboardDatabaseHelper dbHelper;
    Button backButton;
    String playerName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);

        leaderboardText = findViewById(R.id.leaderboardText);
        dbHelper = new LeaderboardDatabaseHelper(this);
        backButton = findViewById(R.id.backButton);
        playerName = getIntent().getStringExtra("player_name");

        displayScores();

        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(LeaderboardActivity.this, MainActivity.class);
            intent.putExtra("player_name", playerName); // Pass the player name
            startActivity(intent);
            finish();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        displayScores();
    }

    private void displayScores() {
        Cursor cursor = dbHelper.getAllScores();
        StringBuilder builder = new StringBuilder();

        if (cursor != null && cursor.moveToFirst()) {
            do {
                String name = cursor.getString(cursor.getColumnIndexOrThrow("player"));
                int score = cursor.getInt(cursor.getColumnIndexOrThrow("score"));
                builder.append(name).append(" - ").append(score).append("\n");
            } while (cursor.moveToNext());
            cursor.close();
        } else {
            builder.append("No scores yet.");
        }

        leaderboardText.setText(builder.toString());
    }
}
