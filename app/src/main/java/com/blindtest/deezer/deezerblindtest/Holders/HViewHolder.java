package com.blindtest.deezer.deezerblindtest.Holders;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.blindtest.deezer.deezerblindtest.Model.Players;
import com.blindtest.deezer.deezerblindtest.R;
import com.blindtest.deezer.deezerblindtest.Transformation.RoundFilledBackgroundTransformation;
import com.bumptech.glide.Glide;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by pierre-louisbertheau on 02/02/2017.
 */

public class HViewHolder extends RecyclerView.ViewHolder{

//    @Bind(R.id.tv_points_players)
    TextView point;
//    @Bind(R.id.tv_name_players)
    TextView name;
//    @Bind(R.id.iv_avatar_players)
    ImageView img;

    Context mContext;

    public HViewHolder(View itemView, Context context) {
        super(itemView);
        mContext = context;

//        ButterKnife.bind(this, itemView);
        point = (TextView) itemView.findViewById(R.id.tv_points_players);
        name = (TextView) itemView.findViewById(R.id.tv_name_players);
        img = (ImageView) itemView.findViewById(R.id.iv_avatar_players);
    }

    public void bind(Players player) {
        name.setText(player.getName());
//        point.setText(player.getScore());
        Glide.with(mContext)
                .load(player.getAvatarUrl())
                .transform(new RoundFilledBackgroundTransformation(mContext, 4, mContext.getResources().getColor(R.color.bg_transparency)))
                .placeholder(R.drawable.cover_icon_profile)
                .into(img);
    }
    }

