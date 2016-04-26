package com.kartik82.android.bloquery;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.firebase.client.Firebase;
import com.firebase.ui.FirebaseRecyclerAdapter;

import java.util.HashMap;
import java.util.Map;

public class HomeActivity extends AppCompatActivity {

    private Firebase ref;
    private Firebase questionsRef;
    FirebaseRecyclerAdapter<Question, QuestionsAdapterViewHolder> frAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Firebase.setAndroidContext(this);

        ref = new Firebase(DatabaseConfig.FIREBASE_URL);
        questionsRef = ref.child("questions");

        final RecyclerView rv_home_questionslist = (RecyclerView) findViewById(R.id.rv_home_questionslist);
        rv_home_questionslist.setHasFixedSize(true);

        rv_home_questionslist.setLayoutManager(new LinearLayoutManager(this));

        frAdapter = new FirebaseRecyclerAdapter<Question, QuestionsAdapterViewHolder>(Question.class, R.layout.list_question,QuestionsAdapterViewHolder.class, questionsRef) {
            @Override
            public void populateViewHolder(QuestionsAdapterViewHolder questionsAdapterViewHolder, final Question question, final int position) {
                questionsAdapterViewHolder.tv_listquestion_text.setText(question.getQuestion_text());

                questionsAdapterViewHolder.tv_listquestion_text.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.w("MyApp", "You clicked on "+ frAdapter.getRef(position).getKey());
                        String question_key = frAdapter.getRef(position).getKey();

                        Intent intent = new Intent(HomeActivity.this, QuestionActivity.class);
                        Bundle extras = new Bundle();
                        extras.putString("question_key",question_key);
                        intent.putExtras(extras);
                        startActivity(intent);

                    }
                });

            }
        };
        rv_home_questionslist.setAdapter(frAdapter);


        //prepareTestQuestions();

    }

    void prepareTestQuestions() {

        Map<String,Object> values = new HashMap<>();
        values.put("question_text", "Who is the smartest human being that's ever lived?");
        questionsRef.push().setValue(values);

        values.put("question_text", "Which fictional character do you hate most, and why?");
        questionsRef.push().setValue(values);

        values.put("question_text", "What color setting on a DSLR camera do professional photographers like to use?");
        questionsRef.push().setValue(values);

        values.put("question_text", "What can I do in 5 minutes in the morning to make my whole day better?");
        questionsRef.push().setValue(values);

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        frAdapter.cleanup();
    }
}