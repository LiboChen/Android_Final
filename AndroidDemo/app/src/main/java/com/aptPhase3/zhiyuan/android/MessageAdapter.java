package com.aptPhase3.zhiyuan.android;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.app.Activity;
import android.view.LayoutInflater;

import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.lang.String;

/**
 * Created by libochen on 12/9/15.
 */
//public class MessageAdapter extends BaseAdapter{
//    Context context;
//    int layoutResourceId;
//    private LayoutInflater mInflater;
//    protected MyApplication myApp;
//    private ArrayList<Message> mData = new ArrayList<>();
//
//    private static final int TYEP_LEFT = 0;
//    private static final int TYPE_RIGHT = 1;
//
////    public MessageAdapter(Context context, int layoutResourceId, ArrayList<Message> data){
////        super(context, layoutResourceId, data);
////        this.layoutResourceId = layoutResourceId;
////        this.context = context;
////        this.data = data;
////    }
//
//    public MessageAdapter(){
//        mInflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//    }
//
//    @Override
//     public Message getItem(int position){
//        return mData.get(position);
//    }
//
//    @Override
//    public long getItemId(int position){
//        return position;
//    }
//
//    @Override
//    public int getCount(){
//        return mData.size();
//    }
//
//    public void addItem(final Message item){
//        mData.add(item);
//        notifyDataSetChanged();
//    }
//
//    public int getType(String speaker){
//        if(speaker == myApp.userName)
//            return TYPE_RIGHT;
//        else
//            return TYEP_LEFT;
//    }
//
//
//    public View getView(int position, View convertView, ViewGroup parent){
//        View row = convertView;
//        MessageHolder holder = null;
//
//        if(row == null){
//            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
//            row = inflater.inflate(layoutResourceId, parent, false);
//
//            holder = new MessageHolder();
//            int type = getType(mData.get(position).name);
//            switch (type){
//                case TYEP_LEFT:
//                    convertView = mInflater.inflate(R.layout.listview_chat_row, null);
//                    break;
//                case TYPE_RIGHT:
//                    convertView = mInflater.inflate(R.layout.listview_chat_row1, null);
//                    break;
//            }
//            holder.imgIcon = (ImageView)row.findViewById(R.id.imgIcon);
//            holder.displayName = (TextView)row.findViewById(R.id.displayName);
//            holder.displayMessage =  (TextView) row.findViewById(R.id.displayMessage);
//
//            row.setTag(holder);
//        }
//        else{
//            holder = (MessageHolder)row.getTag();
//        }
//
//        Message message = mData.get(position);
//        holder.displayName.setText(message.name);
//        holder.displayMessage.setText(message.content);
//        Picasso.with(context).load(message.photo).resize(140, 140).centerInside().into(holder.imgIcon);
//        return row;
//    }
//
//    static class MessageHolder{
//        ImageView imgIcon;
//        TextView displayName;
//        TextView displayMessage;
//    }
//
//
//}
