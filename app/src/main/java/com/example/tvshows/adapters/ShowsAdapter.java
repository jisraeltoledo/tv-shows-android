package com.example.tvshows.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.example.tvshows.R;
import com.example.tvshows.model.TVShow;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ShowsAdapter extends ArrayAdapter<TVShow> {
    private static final String TAG = "ShowsAdapter";
    private Context mContext;
    private List<TVShow> tvShows = new ArrayList<>();

    public ShowsAdapter(@NonNull Context context,  ArrayList<TVShow> list) {
        super(context, 0 , list);
        mContext = context;
        tvShows = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if(listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.list_item,parent,false);

        TVShow tvShow = tvShows.get(position);

        ImageView image = (ImageView)listItem.findViewById(R.id.image);
        Glide.with(getContext()).load(tvShow.getPoster()).into(image);

        TextView name = (TextView) listItem.findViewById(R.id.name);
        name.setText(tvShow.getName());

        return listItem;
    }


}
