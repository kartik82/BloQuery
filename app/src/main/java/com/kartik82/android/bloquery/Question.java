package com.kartik82.android.bloquery;

/**
 * Created by Kartik on 25-Apr-16.
 */
public class Question {

    private String question_text;

    public Question() {
    }

    public Question(String question_text) {
        this.question_text = question_text;
    }

    public String getQuestion_text() {
        return question_text;
    }

    public void setQuestion_text(String question_text) {
        this.question_text = question_text;
    }
}
