package com.example.meme_orymatch;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class LeaderboardDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "leaderboard.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_NAME = "scores";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_PLAYER = "player";
    public static final String COLUMN_SCORE = "score";

    public LeaderboardDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " ("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_PLAYER + " TEXT, "
                + COLUMN_SCORE + " INTEGER)";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void addScore(String playerRaw, int score) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = null;

        // Normalize player name (avoid case or space issues)
        String player = playerRaw.trim().toLowerCase();

        Log.d("DB", "Attempting to add score for: " + player + " → " + score);

        try {
            cursor = db.rawQuery("SELECT " + COLUMN_SCORE + " FROM " + TABLE_NAME + " WHERE " + COLUMN_PLAYER + " = ?", new String[]{player});

            if (cursor != null && cursor.moveToFirst()) {
                int existingScore = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_SCORE));
                int updatedScore = existingScore + score;

                Log.d("DB", "Updating score for existing player: " + player + " → " + updatedScore);

                ContentValues values = new ContentValues();
                values.put(COLUMN_SCORE, updatedScore);
                db.update(TABLE_NAME, values, COLUMN_PLAYER + " = ?", new String[]{player});
            } else {
                Log.d("DB", "Inserting new player: " + player + " → " + score);
                ContentValues values = new ContentValues();
                values.put(COLUMN_PLAYER, player);
                values.put(COLUMN_SCORE, score);
                db.insert(TABLE_NAME, null, values);
            }

        } catch (Exception e) {
            Log.e("DB", "Error in addScore: " + e.getMessage());
        } finally {
            if (cursor != null) cursor.close();
            db.close();
        }
    }

    public Cursor getAllScores() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME + " ORDER BY " + COLUMN_SCORE + " DESC", null);
    }
}


