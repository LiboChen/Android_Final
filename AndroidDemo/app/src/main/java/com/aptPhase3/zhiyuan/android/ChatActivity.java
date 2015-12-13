package com.aptPhase3.zhiyuan.android;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.iid.InstanceID;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.SyncHttpClient;
import com.squareup.picasso.Picasso;

import org.apache.http.Header;

import java.io.IOException;
import java.util.ArrayList;

public class ChatActivity extends ActionBarActivity implements
         View.OnClickListener {

    private Button testButton;
    Context context = this;
    private String register_id;
    protected MyApplication myApp;
    private Activity activity = this;

    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    public static final String EXTRA_MESSAGE = "message";
    public static final String PROPERTY_REG_ID = "registration_id";
    private static final String PROPERTY_APP_VERSION = "appVersion";
    private final static String TAG = "LaunchActivity";
    protected String SENDER_ID = "646073523562";
    private GoogleCloudMessaging gcm =null;
    private String regid = null;
    private String message;

    //static private ArrayList<String> messageHistory = new ArrayList<>();

    private GCMClientManager pushClientManager;
    String PROJECT_NUMBER = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        testButton = (Button) findViewById(R.id.sendButton);
        testButton.setOnClickListener(this);

        //set my receiver from call activity bundle
        final TextView mReceiver = (TextView) findViewById(R.id.receiver_name);
        Bundle extras = getIntent().getExtras();
        mReceiver.setText(extras.getString("receiver"));

        //show message received
        message = extras.getString("receivedMessage");

        //set my receiver from message encoded message
        String[] infos = message.split("#");
        mReceiver.setText(infos[infos.length - 1]);
        System.out.println("set receiver from my encoded infomation " + infos[infos.length-1]);

        ArrayList<Message> message_data = new ArrayList<>();

        String default_photo = "http://www.wikihow.com/images/f/ff/Draw-a-Cute-Cartoon-Person-Step-14.jpg";
        for(int i = 0; i < infos.length - 1; i++){
            String tmp = infos[i];
            String[] tmp_info = tmp.split(":");
            Message m = new Message(tmp_info[0], default_photo, tmp_info[1]);
            message_data.add(m);
        }

        ListView listview = (ListView)findViewById(R.id.listViewMessage);
        final MessageAdapter adapter = new MessageAdapter();
        for(int i = 0; i < message_data.size(); i++){
            adapter.addItem(message_data.get(i));
        }
        myApp = (MyApplication) this.getApplication();
        adapter.setFlagUser(myApp.userName);

        listview.setAdapter(adapter);

        gcm = GoogleCloudMessaging.getInstance(this);

        new Thread(new Runnable() {
            public void run() {
                newRegister();
            }
        }).start();
    }

    @Override
    public void onClick(View v) {
            switch (v.getId()) {
                case R.id.sendButton:
                    System.out.println("send button clicked");
                    sendRegisterId();
                    final EditText mEdit = (EditText) findViewById(R.id.sendMessage);
                    String cur_message = mEdit.getText().toString();

                    //show newest message
                    break;
            }
    }

    private void newRegister() {
        String msg = "";
        try
        {
            if (gcm == null)
            {
                gcm = GoogleCloudMessaging.getInstance(context);
            }
            regid = gcm.register(SENDER_ID);               Log.d(TAG, "########################################");
            Log.d(TAG, "Current Device's Registration ID is: "+regid);
        }
        catch (IOException ex)
        {
            msg = "Error :" + ex.getMessage();
        }
    }


    private void sendRegisterId(){
        if(regid!=null) {
            final TextView mReceiver = (TextView) findViewById(R.id.receiver_name);
            Bundle extras = getIntent().getExtras();
            String receiver = null;
            if(extras.getString("receiver") != null) {
                receiver = extras.getString("receiver");
                mReceiver.setText(receiver);
                System.out.println("from extras the receiver is, " + receiver);
            }

            else {
                receiver = mReceiver.getText().toString();
                System.out.println("from text the receiver is, " + receiver);
            }

            final EditText mEdit = (EditText) findViewById(R.id.sendMessage);
            String message = mEdit.getText().toString();
            String request_url = myApp.back_end + "android/send_message?";
            request_url += "user_id="+myApp.userName;
            request_url += "&reg_id="+regid;
            request_url += "&message=" + message;
            request_url += "&receiver=" + receiver;

            System.out.println("is sending message to " + receiver);
            request_url = request_url.replace(" ", "%20");

            if(receiver != null) {
                AsyncHttpClient client = new AsyncHttpClient();
                client.post(request_url, null, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                        Log.w("async", "success!!!!");
                        //Toast.makeText(context, "send register id successful", Toast.LENGTH_SHORT).show();
                        Log.w("async", "success2");
                        //activity.finish();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                        Log.e("send register id", "There was a problem in retrieving the url : " + e.toString());
                        Toast.makeText(context, "Send Message Failed", Toast.LENGTH_SHORT).show();
                        activity.finish();
                    }
                });
            }
        }else{
            Toast.makeText(context, "Device not registered, try again later", Toast.LENGTH_SHORT).show();
        }
    }

    private class MessageAdapter extends BaseAdapter {
        int layoutResourceId;
        private LayoutInflater mInflater;
        protected MyApplication myApp;
        private ArrayList<Message> mData = new ArrayList<>();
        private String flagUser;

        private static final int TYEP_LEFT = 0;
        private static final int TYPE_RIGHT = 1;

//    public MessageAdapter(Context context, int layoutResourceId, ArrayList<Message> data){
//        super(context, layoutResourceId, data);
//        this.layoutResourceId = layoutResourceId;
//        this.context = context;
//        this.data = data;
//    }

        public MessageAdapter() {
            mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        public void setFlagUser(String s) {
            flagUser = s;
        }

        @Override
        public Message getItem(int position) {
            return mData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getCount() {
            return mData.size();
        }

        public void addItem(final Message item) {
            mData.add(item);
            notifyDataSetChanged();
        }

        public int getType(String speaker) {
            System.out.println("speaker is " + speaker);
            System.out.println("user is " + flagUser);
            if (speaker.equals(flagUser))
                return TYPE_RIGHT;
            else
                return TYEP_LEFT;
        }


//        public View getView(int position, View convertView, ViewGroup parent){
//            View row = convertView;
//            MessageHolder holder = null;
//
//            if(row == null){
////                LayoutInflater inflater = ((Activity)context).getLayoutInflater();
////                row = inflater.inflate(layoutResourceId, parent, false);
//
//                holder = new MessageHolder();
//                int type = getType(mData.get(position).name);
//                switch (type){
//                    case TYEP_LEFT:
//                        row = mInflater.inflate(R.layout.listview_chat_row, null);
//                        System.out.println("using left!!!!!!!!! " + "message is " + mData.get(position).content);
//                        break;
//                    case TYPE_RIGHT:
//                        row = mInflater.inflate(R.layout.listview_chat_row1, null);
//                        System.out.println("using right!!!!!!!! " + "message is " + mData.get(position).content);
//                        break;
//                }
//                holder.imgIcon = (ImageView)row.findViewById(R.id.imgIcon);
//                holder.displayName = (TextView)row.findViewById(R.id.displayName);
//                holder.displayMessage =  (TextView) row.findViewById(R.id.displayMessage);
//
//                row.setTag(holder);
//            }
//            else{
//                holder = (MessageHolder)row.getTag();
//            }
//
//            Message message = mData.get(position);
//            holder.displayName.setText(message.name);
//            holder.displayMessage.setText(message.content);
//            Picasso.with(context).load(message.photo).resize(140, 140).centerInside().into(holder.imgIcon);
//            return row;
//        }
//
//        public static class MessageHolder{
//            ImageView imgIcon;
//            TextView displayName;
//            TextView displayMessage;
//        }

        public View getView(int position, View convertView, ViewGroup parent) {
            View row = convertView;

            int type = getType(mData.get(position).name);
            switch (type) {
                case TYEP_LEFT:
                    row = mInflater.inflate(R.layout.listview_chat_row, null);
                    System.out.println("using left!!!!!!!!! " + "message is " + mData.get(position).content);
                    break;
                case TYPE_RIGHT:
                    row = mInflater.inflate(R.layout.listview_chat_row1, null);
                    System.out.println("using right!!!!!!!! " + "message is " + mData.get(position).content);
                    break;
            }
            ImageView mimgIcon = (ImageView) row.findViewById(R.id.imgIcon);
            TextView mdisplayName = (TextView) row.findViewById(R.id.displayName);
            TextView mdisplayMessage = (TextView) row.findViewById(R.id.displayMessage);
            Message message = mData.get(position);
            mdisplayName.setText(message.name);
            mdisplayMessage.setText(message.content);
            Picasso.with(context).load(message.photo).resize(140, 140).centerInside().into(mimgIcon);
            return row;

        }
    }
}
