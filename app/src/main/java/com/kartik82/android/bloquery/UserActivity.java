package com.kartik82.android.bloquery;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.squareup.picasso.Picasso;

public class UserActivity extends AppCompatActivity {

    private String profile_user;
    private Firebase ref;
    private Firebase usersRef;
    private Cloudinary cloudinary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ref = new Firebase(DatabaseConfig.FIREBASE_URL);
        profile_user = getIntent().getExtras().getString("user_id");
        usersRef = ref.child("users/" + profile_user);

        if (profile_user.equals(ref.getAuth().getUid())) {
           getSupportActionBar().setTitle("BloQuery - My Profile");
        }

        cloudinary = new Cloudinary(DatabaseConfig.CLOUDINARY_URL);

        final ImageView iv_user_photo = (ImageView)findViewById(R.id.iv_user_photo);
        final TextView tv_user_fullname_label = (TextView)findViewById(R.id.tv_user_fullname_label);
        final TextView tv_user_fullname_text = (TextView)findViewById(R.id.tv_user_fullname_text);
        final TextView tv_user_description_label = (TextView)findViewById(R.id.tv_user_description_label);
        final TextView tv_user_description_text = (TextView)findViewById(R.id.tv_user_description_text);
        final TextView tv_user_displayname_text = (TextView)findViewById(R.id.tv_user_displayname_text);
        final TextView tv_user_email_label = (TextView)findViewById(R.id.tv_user_email_label);
        final TextView tv_user_email_text = (TextView)findViewById(R.id.tv_user_email_text);
        final Button btn_user_editprofile = (Button)findViewById(R.id.btn_user_editprofile);

        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                    User user = snapshot.getValue(User.class);
                    Picasso.with(getApplicationContext())
                        .load(cloudinary.url().transformation(new Transformation().width(300).height(300).radius("max").gravity("face").crop("thumb")).generate(user.getPhoto_url()))
                        .into(iv_user_photo);

                if(!user.getFull_name().equals("")) {
                    tv_user_fullname_text.setText(user.getFull_name());
                } else {
                    tv_user_fullname_label.setVisibility(View.GONE);
                    tv_user_fullname_text.setVisibility(View.GONE);
                }

                if(!user.getDescription().equals("")){
                    tv_user_description_text.setText(user.getDescription());
                } else {
                    tv_user_description_label.setVisibility(View.GONE);
                    tv_user_description_text.setVisibility(View.GONE);
                }

                    tv_user_displayname_text.setText(user.getDisplay_name());

                    if (profile_user.equals(ref.getAuth().getUid())) {
                        tv_user_email_text.setText(usersRef.getAuth().getProviderData().get("email").toString());
                    } else {
                        tv_user_email_label.setVisibility(View.GONE);
                        tv_user_email_text.setVisibility(View.GONE);
                        btn_user_editprofile.setVisibility(View.GONE);
                    }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });

        if (btn_user_editprofile != null) {
            btn_user_editprofile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(UserActivity.this, EditUserActivity.class);
                    Bundle extras = new Bundle();
                    extras.putString("user_id",profile_user);
                    intent.putExtras(extras);
                    startActivity(intent);
                }
            });
        }

    }
}
