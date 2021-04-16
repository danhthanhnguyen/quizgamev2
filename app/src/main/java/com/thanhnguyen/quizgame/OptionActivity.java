package com.thanhnguyen.quizgame;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;

public class OptionActivity extends AppCompatActivity {
    private CardView cvMath;
    private CardView cvGeography;
    private CardView cvLiterature;
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_option);

        mediaPlayer = MediaPlayer.create(this, R.raw.option);
        mediaPlayer.start();
        mediaPlayer.setLooping(true);

        cvMath = findViewById(R.id.cvMath);
        cvGeography = findViewById(R.id.cvGeography);
        cvLiterature = findViewById(R.id.cvLiterature);

        findViewById(R.id.imageViewQuizOption).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(OptionActivity.this, HomeActivity.class);
                startActivity(i);
            }
        });

        cvMath.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(OptionActivity.this, GameActivity.class);
                i.putExtra("option", "cvMath");
                startActivity(i);
            }
        });

        cvGeography.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(OptionActivity.this, GameActivity.class);
                i.putExtra("option", "cvGeography");
                startActivity(i);
            }
        });

        cvLiterature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(OptionActivity.this, GameActivity.class);
                i.putExtra("option", "cvLiterature");
                startActivity(i);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        mediaPlayer.release();
        mediaPlayer = null;
    }
}