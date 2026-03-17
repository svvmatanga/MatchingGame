package com.example.meme_orymatch;

import static com.example.meme_orymatch.LeaderboardDatabaseHelper.COLUMN_PLAYER;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AlignmentSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.Collections;

public class GamePage extends AppCompatActivity implements View.OnClickListener {

    //----------------  UI ELEMENTS AND GAME VARIABLES ---------------------------------//
    TextView textViewGameStatus, textLev, textTimeLeft;
    Button buttonEasy, buttonMedium, buttonHard, pauseBtn;

    private final int[] imageViewCardIds = {
            R.id.imageView0, R.id.imageView1, R.id.imageView2, R.id.imageView3,
            R.id.imageView4, R.id.imageView5, R.id.imageView6, R.id.imageView7,
            R.id.imageView8, R.id.imageView9, R.id.imageView10, R.id.imageView11,
            R.id.imageView12, R.id.imageView13, R.id.imageView14, R.id.imageView15
    };

    private final int[] drawableCardIds = {
            R.drawable.meme1, R.drawable.meme2, R.drawable.meme3, R.drawable.meme4,
            R.drawable.meme5, R.drawable.meme6, R.drawable.meme7, R.drawable.meme8
    };
//------------------------------GAME LOGIC VARIABLES---------------------------------------------//
    CardInfo cardInfo1 = null;
    int matchCount = 0;
    int moveCount = 0;
    boolean isWaiting = false;
    CountDownTimer gameTimer;
    private String difficultyLevel = "easy";
    private int totalMatchScore = 0;
    private boolean gameWon = false;
    private int maxFlips = 0;
    private long timeLeftInMillis = 61000;
    private String playerName;

    //----------------------- INITIALIZE UI FROM XML --------------------------------------------//
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_page);

        playerName = getIntent().getStringExtra("player_name");
        textViewGameStatus = findViewById(R.id.textViewGameStatus);
        textLev = findViewById(R.id.textlev);
        textTimeLeft = findViewById(R.id.timer);

        pauseBtn = findViewById(R.id.pausebtn);
        pauseBtn.setOnClickListener(v -> showPauseDialog());
        Intent intent = getIntent();
        difficultyLevel = intent.getStringExtra("difficulty");
        setupNewGame(difficultyLevel);
    }
//--------------------------- DIFFICULTY LEVELS -------------------------------------------------//
    private void setupNewGame(String difficulty) {
        ArrayList<Integer> shuffledDrawableIds = new ArrayList<>();
        int cardPairs;
        int maxCards;

        //EASY//
        if (difficulty.equals("easy")) { //EASY
            cardPairs = 8;
            maxCards = 16;
            totalMatchScore = 8000;
            maxFlips = Integer.MAX_VALUE;
            textLev.setText("EASY ROUND");
            textViewGameStatus.setText("SCORE: " + matchCount + "\nFLIPS: " + moveCount);
            textTimeLeft.setVisibility(View.INVISIBLE);

        //MEDIUM//
        } else if (difficulty.equals("medium")) {
            cardPairs = 8;
            maxCards = 16;
            totalMatchScore = 8000;
            maxFlips = 25;
            textLev.setText("MEDIUM ROUND");
            textViewGameStatus.setText("SCORE: " + matchCount + "\nFLIPS: " + moveCount + "/25");
            textTimeLeft.setVisibility(View.INVISIBLE);

        //HARD//
        } else {
            cardPairs = 8;
            maxCards = 16;
            totalMatchScore = 8000;
            maxFlips = Integer.MAX_VALUE;
            textLev.setText("HARD ROUND");
            textViewGameStatus.setText("SCORE: " + matchCount + "\nFLIPS: " + moveCount);
            textTimeLeft.setVisibility(View.VISIBLE);  // Show the time left text view
            timeLeftInMillis = 61000; // RESET TIMER before starting
            startGameTimer();         // START TIMER
        }
        // SHUFFLE THE CARDS
        for (int i = 0; i < cardPairs; i++) {
            shuffledDrawableIds.add(drawableCardIds[i % drawableCardIds.length]);
            shuffledDrawableIds.add(drawableCardIds[i % drawableCardIds.length]);
        }
        Collections.shuffle(shuffledDrawableIds);

        //ASSIGN COVERS, HIDE COVERS, CARD CLICKABLE FOR FLIP
        for (int i = 0; i < imageViewCardIds.length; i++) {
            ImageView imageView = findViewById(imageViewCardIds[i]);
            if (i < maxCards) {
                int drawableId = shuffledDrawableIds.get(i);
                CardInfo cardInfo = new CardInfo(imageViewCardIds[i], drawableId);
                imageView.setImageResource(R.drawable.cover1);
                imageView.setTag(cardInfo);
                imageView.setVisibility(View.VISIBLE);
                imageView.setOnClickListener(this);
            } else {
                imageView.setVisibility(View.INVISIBLE);
            }

        }

        // RESET FOR ANOTHER GAME
        cardInfo1 = null;
        matchCount = 0;
        moveCount = 0;
        isWaiting = false;
        gameWon = false;

        //SET TIMER FOR THE HARD ROUND ONLY
        if (difficulty.equals("hard")) {
            startGameTimer();
            timeLeftInMillis = 61000;

        // NO TIMER FOR THE EASY AND MEDIUM ROUND
        } else if (gameTimer != null) {
            gameTimer.cancel();
        }
        updateGameStatus();
    }
//------------------------------- TEXTVIEW FLIPS IN MEDIUM ROUND ---------------------------------//
    private void updateGameStatus() {
        // 30 FLIPS FOR MEDIUM ROUND
        if (difficultyLevel.equals("medium")) {
            textViewGameStatus.setText("SCORE: " + matchCount + "\nFLIPS: " + moveCount + "/25");
        // FOR EASY AND HARD NO /30
        } else {
            textViewGameStatus.setText("SCORE: " + matchCount + "\nFLIPS: " + moveCount);
        }
    }
//----------------------------- TIMER FOR HARD ROUND --------------------------------------------//
    private void startGameTimer() {
        //CANCEL EXISTING TIMER TO PREVENT OVERLAP
        if (gameTimer != null) gameTimer.cancel();
        //NEW COUNTDOWN TIMER
        gameTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                //UPDATE REMAINING TIME, SCORE AND FLIPS
                timeLeftInMillis = millisUntilFinished;
                textTimeLeft.setText("Time Left: " + millisUntilFinished / 1000 + "s");
                textViewGameStatus.setText("SCORE: " + matchCount + "\nFLIPS: " + moveCount);
            }
            //---------------------- CALL WHEN TIMER FINISHED ----------------------------------//
            @Override
            public void onFinish() {
                timeLeftInMillis = 0; // RESET TO 0
                textViewGameStatus.setText("TIME'S UP! " + matchCount);
                gameWon = false; // LOSE, TIMER RUN OUT
                showGameOverDialog();
            }
        }.start();
    }

    //-------------------------------- WIN OR LOSE FOR HARD ROUND -------------------------------//
    private void showGameOverDialog() {
        //-------------- DIALOG TITLE AND MESSAGE (WIN OR LOSE) ---------------------------------
        String titleText = gameWon ? "YOU WIN!" : "YOU LOSE";
        String messageText = gameWon
                ? "Final Score: " + matchCount + "\nFlips: " + moveCount
                : "Game Over. Time's up!\nFinal Score: " + matchCount + "\nFlips: " + moveCount;
        //---------------------- UI -------------------------------------------------------------
        int blackColor = Color.parseColor("#000000");
        SpannableString title = new SpannableString(titleText);
        title.setSpan(new ForegroundColorSpan(blackColor), 0, title.length(), 0);
        SpannableString message = new SpannableString(messageText);
        message.setSpan(new ForegroundColorSpan(blackColor), 0, message.length(), 0);

        //------------------------ PASS USERNAME AND SAVE SCORE TO LEADERBOARD --------------------
        String playerName = getIntent().getStringExtra("player_name");
        if (playerName == null || playerName.trim().isEmpty()) {
            Toast.makeText(GamePage.this, "No user info found. Please log in again.", Toast.LENGTH_SHORT).show();
        }else {
            String player = playerName.trim().toLowerCase();
            LeaderboardDatabaseHelper db = new LeaderboardDatabaseHelper(this);
            db.addScore(playerName, matchCount);

        //-----------------------------------------------------------------------------------------------
            AlertDialog dialog = new AlertDialog.Builder(GamePage.this)
                    .setTitle(title)
                    .setMessage(message)
                    .setPositiveButton("PLAY AGAIN", (dialogInterface, which) -> resetGame())
                    .setNegativeButton("HOME", (dialogInterface, which) -> {
                        Intent intent = new Intent(GamePage.this, MainActivity.class);
                        intent.putExtra("player_name", playerName);
                        startActivity(intent);
                        finish();
                    })
                    .setNeutralButton("SCORE BOARD", (dialogInterface, which) -> {
                        Intent intent = new Intent(GamePage.this, LeaderboardActivity.class);
                        intent.putExtra("player_name", playerName);
                        startActivity(intent);
                        finish();
                    })
                    .setCancelable(false)
                    .create();

            if (dialog.getWindow() != null) {
                dialog.getWindow().setBackgroundDrawable(
                        new ColorDrawable(Color.parseColor("#ffc0cb"))
                );
            }
            dialog.show();
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(blackColor);
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(blackColor);
        }
    }
    //PLAY AGAIN BUTTON
    private void resetGame() {
        setupNewGame(difficultyLevel);  // Restart the game with the same difficulty
        if (difficultyLevel.equals("hard")) {
            startGameTimer();  // Restart the countdown timer
        }
    }
//--------------------------------------- PAUSE DIALOG -----------------------------------------//
    private void showPauseDialog() {
        AlertDialog.Builder pauseDialogBuilder = new AlertDialog.Builder(GamePage.this);
// ------------------------------------ UI ------------------------------------------------------
        SpannableString title = new SpannableString("GAME PAUSED!");
        title.setSpan(new ForegroundColorSpan(Color.BLACK), 0, title.length(), 0);
        pauseDialogBuilder.setTitle(title);
        SpannableString message = new SpannableString("Choose an action:");
        message.setSpan(new ForegroundColorSpan(Color.BLACK), 0, message.length(), 0);
        pauseDialogBuilder.setMessage(message);
// --------------------------------- Buttons ----------------------------------------------------
        pauseDialogBuilder.setPositiveButton("Resume", (dialog, which) -> resumeGame());
        pauseDialogBuilder.setNegativeButton("Restart", (dialog, which) -> restartGame());
        pauseDialogBuilder.setNeutralButton("Home", (dialog, which) -> goHome());

        // -------------------------- UI ----------------------------------------------------------
        AlertDialog pauseDialog = pauseDialogBuilder.create();
        if (pauseDialog.getWindow() != null) {
            pauseDialog.getWindow().setBackgroundDrawable(
                    new ColorDrawable(Color.parseColor("#ffc0cb"))
            );
        }
        pauseDialog.show();
        //------------------------- UI -------------------------------------------------
        pauseDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK);
        pauseDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK);
        pauseDialog.getButton(AlertDialog.BUTTON_NEUTRAL).setTextColor(Color.BLACK);

        //----------------------------- PAUSE TIMER ----------------------------------------
        if (gameTimer != null) {
            gameTimer.cancel();
        }
    }
    private void resumeGame() {
        if (difficultyLevel.equals("hard")) {
            startGameTimer();
        }
        Toast.makeText(this, "Game Resumed", Toast.LENGTH_SHORT).show();
    }
    private void restartGame() {
        timeLeftInMillis = 61000;
        setupNewGame(difficultyLevel);
        if (difficultyLevel.equals("hard")) {
            startGameTimer();
        }
        Toast.makeText(this, "Game Restarted", Toast.LENGTH_SHORT).show();
    }
    private void goHome() {
        Intent intent = new Intent(GamePage.this, MainActivity.class);
        intent.putExtra("player_name", playerName);
        startActivity(intent);
        finish();
    }

    // ---------------- HIDE THE CARDS IF THEY DON'T MATCH-----------------------------------------
    private void coverCard(CardInfo cardInfo) {
        ImageView imageView = findViewById(cardInfo.getImageViewId());
        imageView.setImageResource(R.drawable.cover1);
        cardInfo.setFlipped(false);
    }

    //-------------------------------- CARD FLIPS AND MATCHING LOGIC ------------------------------//
    @Override
    public void onClick(View v) {
        if (!isWaiting) {
            ImageView imageView = (ImageView) v;
            CardInfo cardInfo = (CardInfo) imageView.getTag();
            Log.d("demo", "onClick: " + cardInfo);

            // ----------------- CHECK IF THE CARDS IS ALREADY FLIPPED OR MATCHED ------------------
            if (!cardInfo.isFlipped() && !cardInfo.isMatched()) {
                imageView.setImageResource(cardInfo.getDrawableId());
                cardInfo.setFlipped(true); //MARK AS FLIPPED
//-------------------------------------------------------------------------------------------------
                if (cardInfo1 == null) {  // FIRST CARD SELECTED
                    cardInfo1 = cardInfo;
                } else {
                    moveCount++;
//-------------------------------------------------------------------------------------------------
                    if (moveCount > maxFlips) {  //FLIP LIMIT REACHED FOR MEDIUM ROUND
                        textViewGameStatus.setText("GAME OVER.\nSCORE: " + matchCount);
                        gameWon = false;
                        showGameOverDialog();
                        return;
                    }
//------------------------------------------------------------------------------
                    if (difficultyLevel.equals("medium")) { // UPDATE GAME TEXT STATUS THE /30 FOR MEDIUM AND NONE FOR EASY AND HARD
                        textViewGameStatus.setText("SCORE: " + matchCount + "\nFLIPS: " + moveCount + "/25");
                    } else {
                        textViewGameStatus.setText("SCORE: " + matchCount + "\nFLIPS: " + moveCount);
                    }
//---------------------------------------------------------------------------------------------------------------
                    // MATCHED CARDS
                    if (cardInfo.getDrawableId() == cardInfo1.getDrawableId()) {
                        cardInfo.setMatched(true);
                        cardInfo1.setMatched(true);
                        matchCount += 1000; //SCORING FOR MATCHED CARDS
//---------------------------------------------------------------------------------------------------------
                        // MATCH PAIR REACHED (WON)
                        if (matchCount == 8) {
                            gameWon = true;
                            if (gameTimer != null) gameTimer.cancel(); // STOP TIMER IN HARD ROUND
                            showGameOverDialog(); // SHOW YOU WIN
                        }
                        cardInfo1 = null;
                        updateGameStatus();
//----------------------- RE-DISPLAY TEXT STATUS AFTER THE UPDATE --------------------------------------------------------
                        if (difficultyLevel.equals("medium")) {
                            textViewGameStatus.setText("SCORE: " + matchCount + "\nFLIPS: " + moveCount + "/25");
                        } else {
                            textViewGameStatus.setText("SCORE: " + matchCount + "\nFLIPS: " + moveCount);
                        }
                        cardInfo1 = null;

//--------------------------RECHECK TOTAL MATCH SCORE TO CONFIRM WIN -----------------------------------------------------------------------
                        if (matchCount == totalMatchScore) {
                            gameWon = true;
                            if (gameTimer != null) gameTimer.cancel();
                            showGameOverDialog();
                        }
                    } else {
// ----------------------- 1 SEC BEFORE FLIPPING ANOTHER CARD WHEN THEY DON'T MATCH -------------------------------
                        isWaiting = true;
                        new android.os.Handler().postDelayed(() -> {
                            coverCard(cardInfo);
                            coverCard(cardInfo1);
                            cardInfo1 = null;
                            isWaiting = false;
                        }, 1000);
                    }

                }
            }
        }
    }
}
