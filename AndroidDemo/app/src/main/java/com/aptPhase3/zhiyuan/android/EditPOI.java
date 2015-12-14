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

public class EditPOI extends ActionBarActivity {
    protected MyApplication myApp;
    Context context = this;
    private String streamId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_poi);
        myApp = (MyApplication)this.getApplication();

        Bundle extras = getIntent().getExtras();
        streamId = extras.getString("stream_id");

        Button submitButton = (Button) findViewById(R.id.buttonSubmit);
        final EditText descriptionText = (EditText) findViewById(R.id.description);
        final EditText photoText = (EditText)findViewById(R.id.photoUrl);
        submitButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String description = descriptionText.getText().toString();
                        String photo = photoText.getText().toString();
                        String request_url = myApp.back_end + "android/edit_POI?description=" + description;
                        request_url += "&stream_id=" + streamId;
                        request_url += "&photo_url=" + photo;
                        System.out.println("photo url is " + photo);
                        request_url = request_url.replace(" ", "%20");
                        AsyncHttpClient httpClient = new AsyncHttpClient();
                        httpClient.get(request_url, new AsyncHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                                Intent i = new Intent(context, ViewAStreamActivity.class);
                                i.putExtra("stream_id", streamId);
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
