package com.kartik82.android.bloquery;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Kartik on 27-Apr-16.
 */
public class AnswersAdapterViewHolder extends RecyclerView.ViewHolder {
    TextView tv_listanswer_text;
    TextView tv_listanswer_user;
    View v_answer;

    public AnswersAdapterViewHolder(View itemView) {
        super(itemView);
        tv_listanswer_text = (TextView)itemView.findViewById(R.id.tv_listanswer_text);
        tv_listanswer_user = (TextView)itemView.findViewById(R.id.tv_listanswer_user);
        v_answer = itemView;
    }
}
