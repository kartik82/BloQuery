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
import android.widget.Toast;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.firebase.ui.FirebaseRecyclerAdapter;
import com.squareup.picasso.Picasso;

public class HomeActivity extends AppCompatActivity {

    private String question_user;
    private String display_name;
    private Firebase ref;
    private Firebase questionsRef;
    private Firebase usersRef;
    private Firebase answersRef;
    FirebaseRecyclerAdapter<Question, QuestionsAdapterViewHolder> frAdapter;
    private Cloudinary cloudinary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Firebase.setAndroidContext(this);

        ref = new Firebase(DatabaseConfig.FIREBASE_URL);
        questionsRef = ref.child("questions");

        cloudinary = new Cloudinary(DatabaseConfig.CLOUDINARY_URL);

        final ImageView iv_listquestion_userphoto = (ImageView)findViewById(R.id.iv_listquestion_userphoto);

        question_user = getIntent().getExtras().getString("user_id");

        final RecyclerView rv_home_questionslist = (RecyclerView) findViewById(R.id.rv_home_questionslist);
        rv_home_questionslist.setHasFixedSize(true);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);

        rv_home_questionslist.setLayoutManager(mLayoutManager);


        frAdapter = new FirebaseRecyclerAdapter<Question, QuestionsAdapterViewHolder>(Question.class, R.layout.list_question,QuestionsAdapterViewHolder.class, questionsRef) {
            @Override
            public void populateViewHolder(final QuestionsAdapterViewHolder questionsAdapterViewHolder, final Question question, final int position) {
                questionsAdapterViewHolder.tv_listquestion_text.setText(question.getQuestion_text());

                answersRef = ref.child("answers/" + frAdapter.getRef(position).getKey());

                answersRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        Long numberofanswers = snapshot.getChildrenCount();

                        if (numberofanswers == 1) {
                            questionsAdapterViewHolder.tv_listquestion_numberofanswers.setText(numberofanswers.toString() + " answer");
                        } else {
                            questionsAdapterViewHolder.tv_listquestion_numberofanswers.setText(numberofanswers.toString() + " answers");
                        }
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {
                        Toast.makeText(getApplicationContext(), "The read failed: " + firebaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

                usersRef = ref.child("users/" + question.getQuestion_user());

                usersRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        User user = snapshot.getValue(User.class);

                        Picasso.with(getApplicationContext())
                                .load(cloudinary.url().transformation(new Transformation().width(80).height(80).radius("max").gravity("face").crop("thumb")).generate(user.getPhoto_url()))
                                .into(questionsAdapterViewHolder.iv_listquestion_userphoto);

                        display_name = user.getDisplay_name();
                        questionsAdapterViewHolder.tv_listquestion_user.setText(display_name);
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {
                        Toast.makeText(getApplicationContext(), "The read failed: " + firebaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });



                questionsAdapterViewHolder.tv_listquestion_text.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String question_key = frAdapter.getRef(position).getKey();

                        Intent intent = new Intent(HomeActivity.this, QuestionActivity.class);
                        Bundle extras = new Bundle();
                        extras.putString("question_key",question_key);
                        extras.putString("user_id",question_user);
                        intent.putExtras(extras);
                        startActivity(intent);

                    }
                });

                questionsAdapterViewHolder.tv_listquestion_user.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String question_key = frAdapter.getRef(position).getKey();

                        Intent intent = new Intent(HomeActivity.this, UserActivity.class);
                        Bundle extras = new Bundle();
                        extras.putString("user_id",question.getQuestion_user());
                        intent.putExtras(extras);
                        startActivity(intent);

                    }
                });


                questionsAdapterViewHolder.iv_listquestion_userphoto.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String question_key = frAdapter.getRef(position).getKey();

                        Intent intent = new Intent(HomeActivity.this, UserActivity.class);
                        Bundle extras = new Bundle();
                        extras.putString("user_id",question.getQuestion_user());
                        intent.putExtras(extras);
                        startActivity(intent);

                    }
                });


            }
        };
        rv_home_questionslist.setAdapter(frAdapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_question) {
            FragmentManager fm = getFragmentManager();
            AddQuestionFragment fragment_addquestion = new AddQuestionFragment();

            Bundle extras = new Bundle();
            extras.putString("user_id",question_user);
            fragment_addquestion.setArguments(extras);

            fragment_addquestion.show(fm, "fragment_addquestion");
            return true;
        }

        if (id == R.id.action_user) {
            Intent intent = new Intent(HomeActivity.this, UserActivity.class);
            Bundle extras = new Bundle();
            extras.putString("user_id",question_user);
            intent.putExtras(extras);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        frAdapter.cleanup();
    }
}