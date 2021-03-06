package com.aptPhase3.zhiyuan.android;

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

/**
 * Created by libochen on 12/9/15.
 */
public class PersonAdapter extends ArrayAdapter<Person>{
    Context context;
    int layoutResourceId;
    ArrayList<Person> data;

    public PersonAdapter(Context context, int layoutResourceId, ArrayList<Person> data){
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    public View getView(int position, View convertView, ViewGroup parent){
        View row = convertView;
        PersonHolder holder = null;

        if(row == null){
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new PersonHolder();
            holder.imgIcon = (ImageView)row.findViewById(R.id.imgIcon);
            holder.displayName = (TextView)row.findViewById(R.id.displayName);

            row.setTag(holder);
        }
        else{
            holder = (PersonHolder)row.getTag();
        }

        Person person = data.get(position);
        holder.displayName.setText(person.nickName);
        Picasso.with(context).load(person.photo).resize(140, 140).centerInside().into(holder.imgIcon);
        return row;
    }

    static class PersonHolder{
        ImageView imgIcon;
        TextView displayName;
    }


}
