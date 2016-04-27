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

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Firebase.setAndroidContext(this);

        // Login User
        final EditText et_login_email = (EditText) findViewById(R.id.et_login_email);
        final EditText et_login_password = (EditText) findViewById(R.id.et_login_password);
        Button btn_login_login = (Button) findViewById(R.id.btn_login_login);

        if (btn_login_login != null) {
            btn_login_login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Firebase ref = new Firebase(DatabaseConfig.FIREBASE_URL);
                    ref.authWithPassword(et_login_email.getText().toString(), et_login_password.getText().toString(), new Firebase.AuthResultHandler() {
                        @Override
                        public void onAuthenticated(AuthData authData) {
                            Toast.makeText(getApplicationContext(), "Successfully Logged In!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                            Bundle extras = new Bundle();
                            extras.putString("user_id",authData.getUid());
                            intent.putExtras(extras);
                            startActivity(intent);
                        }

                        @Override
                        public void onAuthenticationError(FirebaseError firebaseError) {
                            Toast.makeText(getApplicationContext(), "Invalid Credentials!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }


        // Show Register Activity
        TextView tv_login_clickregister = (TextView) findViewById(R.id.tv_login_clickregister);

        if (tv_login_clickregister != null) {
            tv_login_clickregister.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                    startActivity(intent);
                }
            });
        }

    }

}


