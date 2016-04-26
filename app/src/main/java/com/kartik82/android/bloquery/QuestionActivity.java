package com.kartik82.android.bloquery;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

public class QuestionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        String question_key = getIntent().getExtras().getString("question_key");
        final TextView tv_question_text = (TextView) findViewById(R.id.tv_question_text);

        Firebase ref = new Firebase(DatabaseConfig.FIREBASE_URL + "questions/" + question_key);

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                    Question question = snapshot.getValue(Question.class);
                    if (tv_question_text != null) {
                        tv_question_text.setText(question.getQuestion_text());
                    }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Toast.makeText(getApplicationContext(), "The read failed: " + firebaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}
