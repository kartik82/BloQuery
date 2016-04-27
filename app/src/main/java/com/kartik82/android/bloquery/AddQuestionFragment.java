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
public class AddQuestionFragment extends DialogFragment {

    private Firebase ref;
    private Firebase questionsRef;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_addquestion, container, false);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        final String question_user = getArguments().getString("user_id");

        ref = new Firebase(DatabaseConfig.FIREBASE_URL);
        questionsRef = ref.child("questions");

        final EditText et_addqn_enterqn = (EditText)rootView.findViewById(R.id.et_addqn_enterqn);

        Button btn_addqn_addqn = (Button)rootView.findViewById(R.id.btn_addqn_addqn);
        btn_addqn_addqn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (!(et_addqn_enterqn.getText().toString().equals("")) && !(question_user.isEmpty())) {

                    Map<String, Object> values = new HashMap<>();
                    values.put("question_user", question_user);
                    values.put("question_text", et_addqn_enterqn.getText().toString());
                    values.put("question_time", ServerValue.TIMESTAMP);
                    questionsRef.push().setValue(values);

                    Toast.makeText(rootView.getContext(), "Your question has been successfully posted!", Toast.LENGTH_SHORT).show();

                    dismiss();

                } else {

                    Toast.makeText(rootView.getContext(), "Please enter a question", Toast.LENGTH_SHORT).show();

                }

            }
        });

        return rootView;
    }

}
