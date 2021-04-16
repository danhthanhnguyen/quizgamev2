package com.thanhnguyen.quizgame;

public class GlobalRecord {
    public int score;
    public String user;
    public String date;

    public GlobalRecord() {
        //
    }

    public GlobalRecord(int score, String user, String date) {
        this.score = score;
        this.user = user;
        this.date = date;
    }

    public int getScore() {
        return this.score;
    }
}
