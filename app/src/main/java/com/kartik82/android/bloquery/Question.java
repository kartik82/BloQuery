package com.kartik82.android.bloquery;

/**
 * Created by Kartik on 25-Apr-16.
 */
public class Question {

    private String question_text;
    private String question_user;
    private Long question_time;

    public Question() {
    }

    public Question(String question_text, String question_user, Long question_time) {
        this.question_text = question_text;
        this.question_user = question_user;
        this.question_time = question_time;
    }

    public String getQuestion_text() {
        return question_text;
    }

    public void setQuestion_text(String question_text) {
        this.question_text = question_text;
    }

    public String getQuestion_user() {
        return question_user;
    }

    public void setQuestion_user(String question_user) {
        this.question_user = question_user;
    }

    public Long getQuestion_time() {
        return question_time;
    }

    public void setQuestion_time(Long question_time) {
        this.question_time = question_time;
    }
}
