package com.kartik82.android.bloquery;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Kartik on 26-Apr-16.
 */
public class QuestionsAdapterViewHolder extends RecyclerView.ViewHolder {
    TextView tv_listquestion_text;
    TextView tv_listquestion_user;
    ImageView iv_listquestion_userphoto;
    View v_question;

    public QuestionsAdapterViewHolder(View itemView) {
        super(itemView);
        tv_listquestion_text = (TextView)itemView.findViewById(R.id.tv_listquestion_text);
        tv_listquestion_user = (TextView)itemView.findViewById(R.id.tv_listquestion_user);
        iv_listquestion_userphoto = (ImageView) itemView.findViewById(R.id.iv_listquestion_userphoto);
        v_question = itemView;
    }
}
