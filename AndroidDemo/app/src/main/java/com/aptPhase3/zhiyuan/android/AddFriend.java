package com.aptPhase3.zhiyuan.android;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;

public class AddFriend extends ActionBarActivity {
    protected MyApplication myApp;
    Context context = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);
        myApp = (MyApplication)this.getApplication();
        Button addButton = (Button) findViewById(R.id.buttonAdd);
        final EditText friendNameText   = (EditText)findViewById(R.id.friendName);
        addButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String friend_name = friendNameText.getText().toString();
                        String request_url = myApp.back_end + "android/add_friend?nick_name=" + friend_name;
                        request_url += "&user_id=" + myApp.userName;
                        AsyncHttpClient httpClient = new AsyncHttpClient();
                        httpClient.get(request_url, new AsyncHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                                Intent i = new Intent(context, ViewFriends.class);
                                i.putExtra("userId", myApp.userName);
                                startActivity(i);
                            }

                            @Override
                            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable e) {
//                                Log.e(TAG, "There was a problem in retrieving the url : " + e.toString());
                            }
                        });


                    }
                }
        );

    }

}
