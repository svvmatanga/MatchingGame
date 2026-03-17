package com.example.meme_orymatch;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

public class CardCatalog extends AppCompatActivity {

    Button backButton;

    int images[] = {R.drawable.m3, R.drawable.m2, R.drawable.m1, R.drawable.m4, R.drawable.m5, R.drawable.m6, R.drawable.m7, R.drawable.m8};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_catalog);

        String playerName = getIntent().getStringExtra("player_name");

        backButton = findViewById(R.id.backButton);
        ViewPager viewPager = findViewById(R.id.viewPager);

        // adapter
        MyAdapter myAdapter = new MyAdapter(this, images);
        viewPager.setAdapter(myAdapter);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CardCatalog.this, MainActivity.class);
                intent.putExtra("player_name", playerName);
                startActivity(intent);
                finish();
            }
        });
    }
}
