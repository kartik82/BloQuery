package com.kartik82.android.bloquery;

import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.firebase.ui.FirebaseRecyclerAdapter;

public class QuestionActivity extends AppCompatActivity {

    private String answer_user;
    private String display_name;
    private String question_key;
    private Firebase ref;
    private Firebase questionsRef;
    private Firebase answersRef;
    private Firebase usersRef;
    FirebaseRecyclerAdapter<Answer, AnswersAdapterViewHolder> frAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        question_key = getIntent().getExtras().getString("question_key");
        final TextView tv_question_text = (TextView) findViewById(R.id.tv_question_text);

        ref = new Firebase(DatabaseConfig.FIREBASE_URL);
        questionsRef = ref.child("questions/" + question_key);

        answer_user = getIntent().getExtras().getString("user_id");

        questionsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                    Question question = snapshot.getValue(Question.class);
                    if (tv_question_text != null) {
                        tv_question_text.setText(question.getQuestion_text());
                    }

                usersRef = ref.child("users/" + question.getQuestion_user());

                usersRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        User user = snapshot.getValue(User.class);
                        display_name = user.getDisplay_name();
                        getSupportActionBar().setTitle(display_name + " asks...");
                      }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {
                        Toast.makeText(getApplicationContext(), "The read failed: " + firebaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Toast.makeText(getApplicationContext(), "The read failed: " + firebaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

       answersRef = ref.child("answers/" + question_key);

        final RecyclerView rv_question_answerslist = (RecyclerView) findViewById(R.id.rv_question_answerslist);
        rv_question_answerslist.setHasFixedSize(true);

        rv_question_answerslist.setLayoutManager(new LinearLayoutManager(this));

        frAdapter = new FirebaseRecyclerAdapter<Answer, AnswersAdapterViewHolder>(Answer.class, R.layout.list_answer,AnswersAdapterViewHolder.class, answersRef) {
            @Override
            public void populateViewHolder(final AnswersAdapterViewHolder answersAdapterViewHolder, final Answer answer, final int position) {
                answersAdapterViewHolder.tv_listanswer_text.setText(answer.getAnswer_text());

                usersRef = ref.child("users/" + answer.getAnswer_user());

                usersRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        User user = snapshot.getValue(User.class);
                        display_name = user.getDisplay_name();
                        answersAdapterViewHolder.tv_listanswer_user.setText(display_name);
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {
                        Toast.makeText(getApplicationContext(), "The read failed: " + firebaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            }
        };
        rv_question_answerslist.setAdapter(frAdapter);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_question, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_answer) {
            FragmentManager fm = getFragmentManager();
            AddAnswerFragment fragment_addanswer = new AddAnswerFragment();

            Bundle extras = new Bundle();
            extras.putString("user_id",answer_user);
            extras.putString("question_key",question_key);
            fragment_addanswer.setArguments(extras);

            fragment_addanswer.show(fm, "fragment_addanswer");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
