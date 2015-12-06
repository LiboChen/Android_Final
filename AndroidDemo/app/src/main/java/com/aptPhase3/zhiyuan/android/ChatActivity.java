package com.aptPhase3.zhiyuan.android;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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


    private GCMClientManager pushClientManager;
    String PROJECT_NUMBER = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        testButton = (Button) findViewById(R.id.registerButton);
        testButton.setOnClickListener(this);

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

    private void register() {
        try {
            // [START register_for_gcm]
            // Initially this call goes out to the network to retrieve the token, subsequent calls
            // are local.
            // R.string.gcm_defaultSenderId (the Sender ID) is typically derived from google-services.json.
            // See https://developers.google.com/cloud-messaging/android/start for details on this file.
            // [START get_token]
            InstanceID instanceID = InstanceID.getInstance(this);
            String token = instanceID.getToken(getString(R.string.gcm_defaultSenderId),
                    GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
            System.out.println("the token is"+token);
            register_id = token;

            // [END get_token]

            // [END register_for_gcm]
        } catch (Exception e) {
            e.printStackTrace();
            // If an exception happens while fetching the new token or updating our registration data
            // on a third-party server, this ensures that we'll attempt the update at a later time.
        }
    }

    private void sendRegisterId(){
        if(regid!=null) {
            String request_url = myApp.back_end + "android/send_message?";
            request_url += "user_id="+myApp.userName;
            request_url += "&reg_id="+regid;
            request_url += "&message=Bobby";

            AsyncHttpClient client = new AsyncHttpClient();
            client.post(request_url, null, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                    Log.w("async", "success!!!!");
                    Toast.makeText(context, "send register id successful", Toast.LENGTH_SHORT).show();
                    activity.finish();
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                    Log.e("send register id", "There was a problem in retrieving the url : " + e.toString());
                    Toast.makeText(context, "Create POI Failed", Toast.LENGTH_SHORT).show();
                    activity.finish();
                }
            });
        }else{
            Toast.makeText(context, "Device not registered, try again later", Toast.LENGTH_SHORT).show();
        }
    }

}
