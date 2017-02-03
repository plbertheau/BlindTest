package com.blindtest.deezer.deezerblindtest.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blindtest.deezer.deezerblindtest.Holders.HViewHolder;
import com.blindtest.deezer.deezerblindtest.Model.Players;
import com.blindtest.deezer.deezerblindtest.R;

import java.util.List;

/**
 * Created by pierre-louisbertheau on 02/02/2017.
 */

public class HAdapter extends RecyclerView.Adapter<HViewHolder> {

    List<Players> mPlayersList;
    Context mContext;

    public HAdapter(Context context, List<Players> playersList) {
        mPlayersList = playersList;
        mContext = context;

    }

    @Override
    public HViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;

        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listinflate, parent,false);

        return new HViewHolder(view, mContext);
    }

    @Override
    public void onBindViewHolder(HViewHolder holder, int position) {
        Players player = mPlayersList.get(position);
        holder.bind(player);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return mPlayersList.size();
    }



}


