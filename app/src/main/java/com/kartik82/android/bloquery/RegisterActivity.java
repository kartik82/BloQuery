package com.kartik82.android.bloquery;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Register User
        final EditText et_register_email = (EditText) findViewById(R.id.et_register_email);
        final EditText et_register_password = (EditText) findViewById(R.id.et_register_password);
        Button btn_register_register = (Button) findViewById(R.id.btn_register_register);

        if (btn_register_register != null && et_register_email != null && et_register_password != null) {
            btn_register_register.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Firebase ref = new Firebase(DatabaseConfig.FIREBASE_URL);
                    ref.createUser(et_register_email.getText().toString(), et_register_password.getText().toString(), new Firebase.ValueResultHandler<Map<String, Object>>() {
                        @Override
                        public void onSuccess(Map<String, Object> result) {
                            Toast.makeText(getApplicationContext(), "Your account has been created! You can now proceed and login.", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                            startActivity(intent);
                        }

                        @Override
                        public void onError(FirebaseError firebaseError) {
                            Toast.makeText(getApplicationContext(), "There was an error in creating your account.", Toast.LENGTH_SHORT).show();
                        }
                    });
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
