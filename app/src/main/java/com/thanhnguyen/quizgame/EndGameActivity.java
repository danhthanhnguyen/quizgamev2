package com.thanhnguyen.quizgame;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class EndGameActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private String userID;
    private String userMail;

    private TextView congratulationsTextView;
    private TextView finalScoreTextView;
    private TextView finalTimeTextView;
    private Button yesSave;
    private Button noSave;
    private int score;
    private int timeLeft;
    private String date;
    private ArrayList<GlobalRecord> globalRecords = new ArrayList<GlobalRecord>();

    MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_endgame);

        mediaPlayer = MediaPlayer.create(this, R.raw.end);
        mediaPlayer.setLooping(true);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        userID = mAuth.getCurrentUser().getUid();
        userMail = mAuth.getCurrentUser().getEmail();
        loadUI();
        score = getIntent().getIntExtra("score", 0);
        timeLeft = getIntent().getIntExtra("time_left", 0);
        date = new SimpleDateFormat("dd/MM/yyyy").format(new Date());// get current timestamp
        finalTimeTextView.setText("Time left: " + String.valueOf(timeLeft));
        finalScoreTextView.setText("Score: " + String.valueOf(score));
    }

    private void loadUI() {
        congratulationsTextView = (TextView) findViewById(R.id.congratsTextView);
        runAnimation();
        yesSave = (Button) findViewById(R.id.yesButton);
        noSave = (Button) findViewById(R.id.noButton);
        finalScoreTextView = (TextView) findViewById(R.id.finalScoreTextView);
        finalTimeTextView = (TextView) findViewById(R.id.finalTimeTextView);

        yesSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Save score and user
                String newKey = database.getReference().child("db").child("personalScores").child(userID).push().getKey();
                database.getReference().child("db").child("personalScores").child(userID).child(newKey).setValue(score);

                DatabaseReference myRef = database.getReference().child("db").child("globalRecords");
                myRef.addValueEventListener(new ValueEventListener() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot scoreSnapshot : snapshot.getChildren()) {
                            GlobalRecord record = scoreSnapshot.getValue(GlobalRecord.class);
                            globalRecords.add(record);
                        }

                        GlobalRecord newRecord = new GlobalRecord(score, userMail, date);
                        globalRecords.add(newRecord);
                        Collections.sort(globalRecords, new Comparator<GlobalRecord>() {
                            @Override
                            public int compare(GlobalRecord o1, GlobalRecord o2) {
                                return o1.getScore() < o2.getScore() ? 1 : -1;
                            }
                        });
                        globalRecords.remove(10);

                        Map<String, Object> childUpdates = new HashMap<>();
                        for (int i = 0; i < 10; i++) {// top 10 high score
                            childUpdates.put(String.valueOf(i + 1), globalRecords.get(i));
                        }
                        database.getReference().child("db").child("globalRecords").updateChildren(childUpdates);
                        Intent i = new Intent(EndGameActivity.this, HomeActivity.class);
                        startActivity(i);
                        finish();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Getting data failed, log a message
                        Log.w("FIREBASE", "Failed to read value.", error.toException());
                    }
                });
            }
        });

        noSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(EndGameActivity.this, HomeActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

    private void runAnimation() {
        @SuppressLint("ResourceType") Animation scaleAnimation = AnimationUtils.loadAnimation(EndGameActivity.this, R.animator.scale);
        congratulationsTextView.startAnimation(scaleAnimation);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mediaPlayer.start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mediaPlayer.pause();
    }
}