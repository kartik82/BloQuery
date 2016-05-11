package com.kartik82.android.bloquery;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // Register User
        final EditText et_register_email = (EditText) findViewById(R.id.et_register_email);
        final EditText et_register_password = (EditText) findViewById(R.id.et_register_password);
        final EditText et_register_name = (EditText) findViewById(R.id.et_register_name);
        Button btn_register_register = (Button) findViewById(R.id.btn_register_register);

        if (btn_register_register != null) {
            btn_register_register.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Firebase ref = new Firebase(DatabaseConfig.FIREBASE_URL);
                    if (!(et_register_email.getText().toString().equals("")) && !(et_register_password.getText().toString().equals("")) && !(et_register_name.getText().toString().equals(""))) {
                        ref.createUser(et_register_email.getText().toString(), et_register_password.getText().toString(), new Firebase.ValueResultHandler<Map<String, Object>>() {
                            @Override
                            public void onSuccess(Map<String, Object> result) {
                                Toast.makeText(getApplicationContext(), "Your account has been created! You can now proceed and login.", Toast.LENGTH_SHORT).show();

                                ref.authWithPassword(et_register_email.getText().toString(), et_register_password.getText().toString(),
                                        new Firebase.AuthResultHandler() {

                                            @Override
                                            public void onAuthenticated(AuthData authData) {
                                                Map<String, String> map = new HashMap<String, String>();
                                                map.put("provider", authData.getProvider());
                                                map.put("display_name", et_register_name.getText().toString());
                                                map.put("photo_url", "BloQuery/default-avatar.png");
                                                map.put("full_name","");
                                                map.put("description","");

                                                ref.child("users").child(authData.getUid()).setValue(map);

                                                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                                startActivity(intent);
                                            }

                                            @Override
                                            public void onAuthenticationError(FirebaseError error) {
                                                // Something went wrong :(
                                            }
                                        });

                            }

                            @Override
                            public void onError(FirebaseError firebaseError) {
                                Toast.makeText(getApplicationContext(), "There was an error in creating your account.", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        Toast.makeText(getApplicationContext(), "Please ensure all fields are filled in", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        // Show Login Activity
        TextView tv_register_clicklogin = (TextView) findViewById(R.id.tv_register_clicklogin);

        if (tv_register_clicklogin != null) {
            tv_register_clicklogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            });
        }

    }
}
