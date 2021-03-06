package com.blindtest.deezer.deezerblindtest.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blindtest.deezer.deezerblindtest.Model.Message;
import com.blindtest.deezer.deezerblindtest.Holders.MessageViewHolder;
import com.blindtest.deezer.deezerblindtest.R;

import java.util.ArrayList;

/**
 * Created by pierre-louisbertheau on 30/01/2017.
 */

public class MessagesAdapter extends RecyclerView.Adapter<MessageViewHolder> {

    private ArrayList<Message> messages;

    private static final int RIGHT = 0, LEFT = 1;

    private Context mContext;

    public MessagesAdapter(ArrayList<Message> messages, Context context) {
        this.messages = messages;
        mContext = context;
    }


    /**
     *  On utilise cette méthode pour pouvoir afficher après la "bonne" vue
     *  Si c'est notre message alors cela sera "RIGHT"
     *  et si ce sont les messages des autres alors "LEFT"
     */
    @Override
    public int getItemViewType(int position) {

        if(messages.get(position).isMe())
            return RIGHT;
        else
            return LEFT;

    }

    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = null;

        switch (viewType) {

            case RIGHT:

                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_right, parent,false);

                break;

            case LEFT:

                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_left, parent,false);

                break;
        }

        return new MessageViewHolder(view, mContext);
    }

    @Override
    public void onBindViewHolder(MessageViewHolder holder, int position) {
        Message message = messages.get(position);
        holder.bind(message);
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }
}
