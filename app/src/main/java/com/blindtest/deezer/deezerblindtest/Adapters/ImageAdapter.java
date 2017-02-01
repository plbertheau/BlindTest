package com.blindtest.deezer.deezerblindtest.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.blindtest.deezer.deezerblindtest.Holders.NewHolder;
import com.blindtest.deezer.deezerblindtest.R;
import com.bumptech.glide.Glide;
import com.deezer.sdk.model.Playlist;

import java.util.List;

/**
 * Created by pierre-louisbertheau on 30/01/2017.
 */

public class ImageAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater inflater;
    private List<Playlist> mList;

    public ImageAdapter(Context c, List<Playlist> list) {

        mContext = c;
        mList = list;
        //
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int i) {
        return mList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {


        NewHolder holder = null;
//        ImageView imageView;

        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);


        if (convertView == null) {
            holder = new NewHolder();

            convertView = inflater.inflate(R.layout.grid_item, viewGroup, false);

            holder.imageView = (ImageView)convertView.findViewById(R.id.imageview_id);

            holder.imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            convertView.setTag(holder);
            holder.imageView.setPadding(8, 8, 8, 8);


        } else {
            holder = (NewHolder) convertView.getTag();

        }

        Glide.with(mContext).load(mList.get(position).getPictureUrl()).asBitmap().into(holder.imageView);

        return convertView;


    }

    public void refreshData(List<Playlist> list) {
        mList = list;
    }

    private Integer[] mThumbIds = {
            //TODO
    };
}
