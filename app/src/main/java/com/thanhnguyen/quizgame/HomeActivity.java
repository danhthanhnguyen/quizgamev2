package com.thanhnguyen.quizgame;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomeActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;

    MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mediaPlayer = MediaPlayer.create(this, R.raw.intro);
        mediaPlayer.setLooping(true);

        mAuth = FirebaseAuth.getInstance();

        TextView tvUsername = findViewById(R.id.tvUsernameHome);
        CardView cvStartQuiz = findViewById(R.id.cvStartQuiz);
        CardView cvRule = findViewById(R.id.cvRule);
        CardView cvHistory = findViewById(R.id.cvHistory);
        CardView cvEditPassword = findViewById(R.id.cvEditPassword);
        CardView cvLogout = findViewById(R.id.cvLogout);

        FirebaseUser user = mAuth.getCurrentUser();
        tvUsername.setText("Hello " + String.valueOf(user.getEmail()).substring(0, user.getEmail().lastIndexOf('@')));

        cvStartQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HomeActivity.this, OptionActivity.class);
                startActivity(i);// Redirect to game page
            }
        });

        cvRule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HomeActivity.this, MyScoreActivity.class);
                startActivity(i);// Redirect to my score page
            }
        });

        cvHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HomeActivity.this, HighScoreActivity.class);
                startActivity(i);// Redirect to high score page
            }
        });

        cvEditPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HomeActivity.this, EditPassword.class);
                startActivity(i);// Redirect to high score page
            }
        });

        cvLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();//sign out
                Intent i = new Intent(HomeActivity.this, MainActivity.class);
                startActivity(i);
                mediaPlayer.release();
                mediaPlayer = null;
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mediaPlayer.start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mediaPlayer != null){
            mediaPlayer.pause();

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mediaPlayer != null){
            mediaPlayer.release();

        }
    }
}
