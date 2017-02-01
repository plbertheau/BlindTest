package com.blindtest.deezer.deezerblindtest.Holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.blindtest.deezer.deezerblindtest.Model.Message;
import com.blindtest.deezer.deezerblindtest.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by pierre-louisbertheau on 30/01/2017.
 */

public class MessageViewHolder extends RecyclerView.ViewHolder {

    @Bind(R.id.name)
    TextView txtName;
    @Bind(R.id.message)
    TextView txtMessage;

    public MessageViewHolder(View itemView) {
        super(itemView);

        ButterKnife.bind(this, itemView);
    }

    public void bind(Message message) {
//        txtName.setText(message.getName());
        txtMessage.setText(message.getMessage());
    }


}
