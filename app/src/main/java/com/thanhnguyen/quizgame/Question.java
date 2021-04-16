package com.thanhnguyen.quizgame;

public class Question {
    public String question;
    public String correct_answer;
    public String incorrect_answer1;
    public String incorrect_answer2;
    public String incorrect_answer3;

    public Question() {
        //
    }

    public Question(String question, String correct_answer, String incorrect_answer1, String incorrect_answer2, String incorrect_answer3) {
        this.question = question;
        this.correct_answer = correct_answer;
        this.incorrect_answer1 = incorrect_answer1;
        this.incorrect_answer2 = incorrect_answer2;
        this.incorrect_answer3 = incorrect_answer3;
    }
}
