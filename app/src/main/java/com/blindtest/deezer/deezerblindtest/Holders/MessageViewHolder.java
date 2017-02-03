package com.blindtest.deezer.deezerblindtest.Holders;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.blindtest.deezer.deezerblindtest.Model.Message;
import com.blindtest.deezer.deezerblindtest.R;
import com.blindtest.deezer.deezerblindtest.Transformation.RoundFilledBackgroundTransformation;
import com.bumptech.glide.Glide;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by pierre-louisbertheau on 30/01/2017.
 */

public class MessageViewHolder extends RecyclerView.ViewHolder {

    @Bind(R.id.image_avatar)
    ImageView imageAvatar;
    @Bind(R.id.message)
    TextView txtMessage;

    Context mContext;

    public MessageViewHolder(View itemView, Context context) {
        super(itemView);
        mContext = context;
        ButterKnife.bind(this, itemView);
    }

    public void bind(Message message) {
        txtMessage.setText(message.getMessage());
        Glide.with(mContext)
                .load(message.getAvatarUrl())
                .transform(new RoundFilledBackgroundTransformation(mContext, 4, mContext.getResources().getColor(R.color.bg_transparency)))
                .placeholder(R.drawable.cover_icon_profile)
                .into(imageAvatar);
    }


}
