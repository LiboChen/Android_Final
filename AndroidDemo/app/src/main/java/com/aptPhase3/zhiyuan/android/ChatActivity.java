package com.aptPhase3.zhiyuan.android;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.iid.InstanceID;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.SyncHttpClient;

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

    //static private ArrayList<String> messageHistory = new ArrayList<>();

    private GCMClientManager pushClientManager;
    String PROJECT_NUMBER = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        testButton = (Button) findViewById(R.id.registerButton);
        testButton.setOnClickListener(this);

        //show message received
        final TextView messageText = (TextView) findViewById(R.id.show_message);
        Bundle extras = getIntent().getExtras();
        String message = extras.getString("receivedMessage");
        //messageHistory.add(message);
//        String totalMessage = "";
//        for(int i = 0; i < messageHistory.size(); i++)
//            totalMessage += messageHistory.get(i) + " + ";
        messageText.setText(message);

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
                case R.id.registerButton:
                    System.out.println("register button clicked");
                    sendRegisterId();
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
            final EditText mEdit = (EditText) findViewById(R.id.sendMessage);
            String message = mEdit.getText().toString();
            //messageHistory.add(message);
            String request_url = myApp.back_end + "android/send_message?";
            request_url += "user_id="+myApp.userName;
            request_url += "&reg_id="+regid;
            request_url += "&message=" + message;

            request_url = request_url.replace(" ", "%20");
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
        }else{
            Toast.makeText(context, "Device not registered, try again later", Toast.LENGTH_SHORT).show();
        }
    }

}
