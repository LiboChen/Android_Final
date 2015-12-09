package com.aptPhase3.zhiyuan.android;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.content.Intent;
import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import org.apache.http.Header;


public class EditProfile extends ActionBarActivity {
    protected MyApplication myApp;

    Context context = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        myApp = (MyApplication)this.getApplication();
        Button submitButton = (Button) findViewById(R.id.buttonSubmit);
        final EditText nickNameText   = (EditText)findViewById(R.id.nickName);
        final EditText descriptionText = (EditText) findViewById(R.id.description);
        submitButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String nick_name = nickNameText.getText().toString();
                        String description = descriptionText.getText().toString();
                        String request_url = myApp.back_end + "android/edit_profile?nick_name="+nick_name;
                        request_url += "&description=" + description;
                        request_url += "&user_id=" + myApp.userName;
                        AsyncHttpClient httpClient = new AsyncHttpClient();
                        httpClient.get(request_url, new AsyncHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                                Intent i = new Intent(context, ViewProfile.class);
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
