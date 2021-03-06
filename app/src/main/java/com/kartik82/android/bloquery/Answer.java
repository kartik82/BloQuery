package com.kartik82.android.bloquery;

/**
 * Created by Kartik on 27-Apr-16.
 */
public class Answer {

    private String answer_text;
    private String answer_user;
    private Long answer_time;
    private Long answer_votes;

    public Answer() {
    }

    public Answer(String answer_text, String answer_user, Long answer_time, Long answer_votes) {
        this.answer_text = answer_text;
        this.answer_user = answer_user;
        this.answer_time = answer_time;
        this.answer_votes = answer_votes;
    }

    public String getAnswer_text() {
        return answer_text;
    }

    public void setAnswer_text(String answer_text) {
        this.answer_text = answer_text;
    }

    public String getAnswer_user() {
        return answer_user;
    }

    public void setAnswer_user(String answer_user) {
        this.answer_user = answer_user;
    }

    public Long getAnswer_time() {
        return answer_time;
    }

    public void setAnswer_time(Long answer_time) {
        this.answer_time = answer_time;
    }

    public Long getAnswer_votes() {
        return answer_votes;
    }

    public void setAnswer_votes(Long answer_votes) {
        this.answer_votes = answer_votes;
    }
}
