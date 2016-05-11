package com.kartik82.android.bloquery;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.firebase.client.ServerValue;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Kartik on 27-Apr-16.
 */
public class AddAnswerFragment extends DialogFragment {

    private Firebase ref;
    private Firebase answersRef;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_addanswer, container, false);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        final String answer_user = getArguments().getString("user_id");
        final String question_key = getArguments().getString("question_key");

        ref = new Firebase(DatabaseConfig.FIREBASE_URL);
        answersRef = ref.child("answers/" + question_key);

        final EditText et_addans_enterans = (EditText)rootView.findViewById(R.id.et_addans_enterans);

        Button btn_addans_addans = (Button)rootView.findViewById(R.id.btn_addans_addans);
        btn_addans_addans.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (!(et_addans_enterans.getText().toString().equals("")) && !(answer_user.isEmpty())) {

                    Map<String, Object> values = new HashMap<>();
                    values.put("answer_user", answer_user);
                    values.put("answer_text", et_addans_enterans.getText().toString());
                    values.put("answer_time", ServerValue.TIMESTAMP);
                    values.put("answer_votes", 0);
                    answersRef.push().setValue(values);

                    Toast.makeText(rootView.getContext(), "Your answer has been successfully posted!", Toast.LENGTH_SHORT).show();

                    dismiss();

                } else {

                    Toast.makeText(rootView.getContext(), "Please enter an answer", Toast.LENGTH_SHORT).show();

                }

            }
        });

        return rootView;
    }


}