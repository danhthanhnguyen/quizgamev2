package com.thanhnguyen.quizgame;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class GameActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private String userID;
    private String option;
    private ArrayList<Question> dataset = new ArrayList<Question>();

    private TextView currentScoreTextView;
    private TextView timerTextView;
    private TextView questionTextView;
    private TextView questionNumTextView;
    private RadioGroup questionsRadioGroup;
    private RadioButton answer1RadioButton;
    private RadioButton answer2RadioButton;
    private RadioButton answer3RadioButton;
    private RadioButton answer4RadioButton;
    private Button answerButton;

    MediaPlayer mediaPlayer;

    private int questionCounter = 0;
    private int score = 0;
    private CountDownTimer cTimer = null;
    private int timeLeft = 60;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        mediaPlayer = MediaPlayer.create(this, R.raw.soundtrack);
        mediaPlayer.start();

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        userID = mAuth.getCurrentUser().getUid();// get user uid
        option = getIntent().getExtras().getString("option");
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

        PhoneStateListener callStateListener = new PhoneStateListener() {
            public void onCallStateChanged(int state, String incomingNumber) {
                if (state == TelephonyManager.CALL_STATE_RINGING) {
                    Log.d("RINGING", "RINGING");
                    Log.d("TIMER", "Time left: " + String.valueOf(timeLeft));
                    cancelTimer();
                }
                if (state == TelephonyManager.CALL_STATE_OFFHOOK) {
                    Log.d("OFFHOOK", "OFFHOOK");
                    Log.d("TIMER", "Time left: " + String.valueOf(timeLeft));
                    cancelTimer();
                }
                if (state == TelephonyManager.CALL_STATE_IDLE) {
                    if (timeLeft != 60) {
                        Log.d("IDLE", "IDLE");
                        Log.d("TIMER", "Time left: " + String.valueOf(timeLeft));
                        startTimer();
                    }
                }
            }
        };
        telephonyManager.listen(callStateListener, PhoneStateListener.LISTEN_CALL_STATE);

        loadUI();
        loadDataSet();
    }

    private void loadUI() {
        currentScoreTextView = (TextView) findViewById(R.id.currentScoreTextView);
        timerTextView = (TextView) findViewById(R.id.timerTextView);
        questionTextView = (TextView) findViewById(R.id.questionTextView);
        questionNumTextView = (TextView) findViewById(R.id.questionNumTextView);
        questionsRadioGroup = (RadioGroup) findViewById(R.id.questionRadioGroup);
        answer1RadioButton = (RadioButton) findViewById(R.id.radio1);
        answer2RadioButton = (RadioButton) findViewById(R.id.radio2);
        answer3RadioButton = (RadioButton) findViewById(R.id.radio3);
        answer4RadioButton = (RadioButton) findViewById(R.id.radio4);
        answerButton = (Button) findViewById(R.id.answerButton);

        answerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    int selectedId = questionsRadioGroup.getCheckedRadioButtonId();
                    RadioButton radioFinalAnswerButton = (RadioButton) findViewById(selectedId);

                    if (radioFinalAnswerButton.getText().equals(dataset.get(questionCounter).correct_answer)) {
                        score += 10;
                        radioFinalAnswerButton.setBackgroundColor(Color.GREEN);
                    } else {
                        if (score >= 10) {
                            score -= 10;
                        }
                        radioFinalAnswerButton.setBackgroundColor(Color.RED);
                    }

                    CountDownTimer wTimer = null;
                    wTimer = new CountDownTimer(1000, 500) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                            // do something after 0.5s
                        }

                        @Override
                        public void onFinish() {
                            // finish wTimer after 1s
                            questionCounter++;
                            radioFinalAnswerButton.setBackgroundColor(Color.parseColor("#2d4059"));
                            questionsRadioGroup.clearCheck();
                            showQuestion();
                        }
                    };
                    wTimer.start();
                } catch (Exception e) {
                    Toast.makeText(GameActivity.this, "Please select an answer", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void loadDataSet() {
        DatabaseReference myRef = database.getReference().child("db").child("questions").child(option);
        // Read data
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (dataset.size() != 0) {
                    dataset.clear();
                }
                for (DataSnapshot questionSnapshot : snapshot.getChildren()) {
                    Question question = questionSnapshot.getValue(Question.class);
                    dataset.add(question);
                }
                randomizeQuestions();
                showQuestion();
                startTimer();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Getting Post failed, log a message
                Log.w("FIREBASE", "Failed to read value.", error.toException());
            }
        });
    }

    private void randomizeQuestions() {
        Collections.shuffle(dataset);
        for (int i = 19; i > 9; i--) {
            dataset.remove(i);
        }
    }

    private void showQuestion() {
        if (dataset.size() >= (questionCounter + 1)) {
            currentScoreTextView.setText("Score: " + String.valueOf(score));
            questionTextView.setText(dataset.get(questionCounter).question);
            questionNumTextView.setText(String.valueOf(questionCounter + 1) + " / 10");

            ArrayList<String> qa = new ArrayList<String>();
            qa.add(dataset.get(questionCounter).correct_answer);
            qa.add(dataset.get(questionCounter).incorrect_answer1);
            qa.add(dataset.get(questionCounter).incorrect_answer2);
            qa.add(dataset.get(questionCounter).incorrect_answer3);
            Collections.shuffle(qa);
            answer1RadioButton.setText(qa.get(0));
            answer2RadioButton.setText(qa.get(1));
            answer3RadioButton.setText(qa.get(2));
            answer4RadioButton.setText(qa.get(3));
        } else {
            endRound();
        }
    }

    private void startTimer() {
        cTimer = new CountDownTimer(timeLeft * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeft = (int)(millisUntilFinished / 1000);
                timerTextView.setText("Time left: " + String.valueOf(timeLeft));
            }

            @Override
            public void onFinish() {
                endRound();
            }
        };
        cTimer.start();
    }

    private void cancelTimer() {
        if (cTimer != null) {
            cTimer.cancel();
        }
    }

    private void endRound() {
        cancelTimer();

        Intent i = new Intent(GameActivity.this, EndGameActivity.class);
        i.putExtra("time_left", timeLeft);
        int finalScore = score + (timeLeft * 10);
        i.putExtra("score", finalScore);
        startActivity(i);
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mediaPlayer.release();
        mediaPlayer = null;
    }
}
