package com.aptPhase3.zhiyuan.android;

/**
 * Created by libochen on 12/10/15.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.app.Activity;
import android.view.LayoutInflater;

import com.squareup.picasso.Picasso;
import java.util.ArrayList;

public class StreamAdapter extends ArrayAdapter<Stream>{
    Context context;
    int layoutResourceId;
    ArrayList<Stream> data;

    public StreamAdapter(Context context, int layoutResourceId, ArrayList<Stream> data){
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    public View getView(int position, View convertView, ViewGroup parent){
        View row = convertView;
        StreamHolder holder = null;

        if(row == null){
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new StreamHolder();
            holder.imgCover = (ImageView)row.findViewById(R.id.imgIcon);
            holder.displayName = (TextView)row.findViewById(R.id.displayName);
            holder.information = (TextView) row.findViewById(R.id.information);

            row.setTag(holder);
        }
        else{
            holder = (StreamHolder)row.getTag();
        }

        Stream stream = data.get(position);
        holder.displayName.setText(stream.streamName);
        holder.information.setText(stream.information);
        Picasso.with(context).load(stream.coverUrl).resize(360, 360).centerInside().into(holder.imgCover);
        return row;
    }

    static class StreamHolder{
        ImageView imgCover;
        TextView displayName;
        TextView information;
    }


}
