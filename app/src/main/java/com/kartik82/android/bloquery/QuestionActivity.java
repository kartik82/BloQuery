package com.kartik82.android.bloquery;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ServerValue;
import com.firebase.client.ValueEventListener;
import com.firebase.ui.FirebaseRecyclerAdapter;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class QuestionActivity extends AppCompatActivity {

    private String answer_user;
    private String display_name;
    private String question_key;
    private Firebase ref;
    private Firebase questionsRef;
    private Firebase answersRef;
    private Firebase votesRef;
    private Firebase answervotesRef;
    private Firebase usersRef;
    FirebaseRecyclerAdapter<Answer, AnswersAdapterViewHolder> frAdapter;
    private Cloudinary cloudinary;

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

        cloudinary = new Cloudinary(DatabaseConfig.CLOUDINARY_URL);

        final ImageView iv_listanswer_userphoto = (ImageView)findViewById(R.id.iv_listanswer_userphoto);

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

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);

        rv_question_answerslist.setLayoutManager(mLayoutManager);

        frAdapter = new FirebaseRecyclerAdapter<Answer, AnswersAdapterViewHolder>(Answer.class, R.layout.list_answer,AnswersAdapterViewHolder.class, answersRef) {
            @Override
            public void populateViewHolder(final AnswersAdapterViewHolder answersAdapterViewHolder, final Answer answer, final int position) {
                answersAdapterViewHolder.tv_listanswer_text.setText(answer.getAnswer_text());
                if (answer.getAnswer_votes() != null) {
                    if (answer.getAnswer_votes() == 1) {
                        answersAdapterViewHolder.tv_listanswer_numberofvotes.setText(answer.getAnswer_votes().toString() + " vote");
                    } else {
                        answersAdapterViewHolder.tv_listanswer_numberofvotes.setText(answer.getAnswer_votes().toString() + " votes");
                    }
                }


                answersAdapterViewHolder.tv_listanswer_vote.setText("VOTE");

                votesRef = ref.child("votes/" + frAdapter.getRef(position).getKey());

                votesRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {

                        Iterator<DataSnapshot> itr = snapshot.getChildren().iterator();
                        while(itr.hasNext()) {
                            DataSnapshot dataSnapshot = itr.next();

                            if (dataSnapshot.getKey().equals(answer_user)) {
                                answersAdapterViewHolder.tv_listanswer_vote.setText("VOTED");
                            }

                        }

                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {
                        Toast.makeText(getApplicationContext(), "The read failed: " + firebaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

                usersRef = ref.child("users/" + answer.getAnswer_user());

                usersRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        User user = snapshot.getValue(User.class);

                        Picasso.with(getApplicationContext())
                                .load(cloudinary.url().transformation(new Transformation().width(80).height(80).radius("max").gravity("face").crop("thumb")).generate(user.getPhoto_url()))
                                .into(answersAdapterViewHolder.iv_listanswer_userphoto);

                        display_name = user.getDisplay_name();
                        answersAdapterViewHolder.tv_listanswer_user.setText(display_name);
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {
                        Toast.makeText(getApplicationContext(), "The read failed: " + firebaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

                answersAdapterViewHolder.tv_listanswer_user.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String question_key = frAdapter.getRef(position).getKey();

                        Intent intent = new Intent(QuestionActivity.this, UserActivity.class);
                        Bundle extras = new Bundle();
                        extras.putString("user_id",answer.getAnswer_user());
                        intent.putExtras(extras);
                        startActivity(intent);

                    }
                });


                answersAdapterViewHolder.iv_listanswer_userphoto.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String question_key = frAdapter.getRef(position).getKey();

                        Intent intent = new Intent(QuestionActivity.this, UserActivity.class);
                        Bundle extras = new Bundle();
                        extras.putString("user_id",answer.getAnswer_user());
                        intent.putExtras(extras);
                        startActivity(intent);

                    }
                });

                answersAdapterViewHolder.tv_listanswer_vote.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String answer_key = frAdapter.getRef(position).getKey();

                        votesRef = ref.child("votes/" + answer_key + "/" + answer_user);

                        answervotesRef = ref.child("answers/" + question_key + "/" + answer_key);

                        if (answersAdapterViewHolder.tv_listanswer_vote.getText().equals("VOTE")) {

                            Map<String, Object> values = new HashMap<>();
                            values.put("vote_time", ServerValue.TIMESTAMP);
                            votesRef.push().setValue(values);
                            answersAdapterViewHolder.tv_listanswer_vote.setText("VOTED");

                            answervotesRef.child("answer_votes").setValue(answer.getAnswer_votes() + 1);

                            Toast.makeText(getApplicationContext(), "You have successfully voted.", Toast.LENGTH_SHORT).show();


                        } else {

                            votesRef.removeValue();
                            answersAdapterViewHolder.tv_listanswer_vote.setText("VOTE");

                            answervotesRef.child("answer_votes").setValue(answer.getAnswer_votes() - 1);

                            Toast.makeText(getApplicationContext(), "Your vote has been removed.", Toast.LENGTH_SHORT).show();

                        }


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
