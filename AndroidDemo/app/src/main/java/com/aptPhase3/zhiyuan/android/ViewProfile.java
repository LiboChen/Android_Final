package com.aptPhase3.zhiyuan.android;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.util.ArrayList;

import java.io.InputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import android.graphics.BitmapFactory;
import java.net.URL;
import java.io.IOException;
import android.os.AsyncTask;
import android.widget.Button;
import android.widget.Toast;


public class ViewProfile extends ActionBarActivity implements View.OnClickListener{
    Context context = this;
    private String TAG  = "View Profile";
    protected MyApplication myApp;
    private View.OnClickListener listener;
    private Button mEditProfile;
    private Button mChat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);
        listener = this;
        mEditProfile = (Button) findViewById(R.id.edit_profile);
        mChat = (Button) findViewById(R.id.chat);
        mEditProfile.setVisibility(View.INVISIBLE);
        mChat.setVisibility(View.INVISIBLE);
        //mEditProfile.setOnClickListener(this);

        final TextView nickName = (TextView) findViewById(R.id.nick_name);
        final TextView description = (TextView) findViewById(R.id.description);
        final ListView listview = (ListView) findViewById(R.id.listViewStream);
        //ImageView photo = (ImageView) findViewById(R.id.photo);
        myApp = (MyApplication)this.getApplication();
        Bundle extras = getIntent().getExtras();
        String queryId = extras.getString("userId");

        final String request_url = myApp.back_end + "android/view_profile?query_id="+queryId + "&user_id=" + myApp.userName;
        AsyncHttpClient httpClient = new AsyncHttpClient();
        httpClient.get(request_url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                try {
                    JSONObject jObject = new JSONObject(new String(response));
                    JSONArray jstreamNames = jObject.getJSONArray("streamNames");
                    JSONArray jstreamInfos = jObject.getJSONArray("streamInfos");
                    JSONArray jstreamCovers = jObject.getJSONArray("streamCovers");
                    final ArrayList<String> streamNames = new ArrayList<String>();
                    final ArrayList<String> streamInfos = new ArrayList<String>();
                    final ArrayList<String> streamCovers = new ArrayList<String>();

                    for(int i = 0; i < jstreamNames.length(); i++){
                        streamNames.add(jstreamNames.getString(i));
                        streamInfos.add(jstreamInfos.getString(i));
                        streamCovers.add(jstreamCovers.getString(i));
                    }


                    if (jObject.getBoolean("isSelf")){
                        System.out.println("it is my self");
                        mEditProfile.setOnClickListener(listener);
                        mEditProfile.setVisibility(View.VISIBLE);
                        mEditProfile.setEnabled(true);
                        mEditProfile.setClickable(true);
                    }

                    else{
                        mChat.setOnClickListener(listener);
                        mChat.setVisibility(View.VISIBLE);
                        mChat.setEnabled(true);
                        mChat.setClickable(true);
                    }

                    //show personal information
                    nickName.setText(jObject.getString("name"));
                    description.setText(jObject.getString("description"));
                    String imageUrl = jObject.getString("photo");
                    new GetImage().execute(imageUrl);

                    //show personal place of interest
                    //String fake = "http://www.wikihow.com/images/f/ff/Draw-a-Cute-Cartoon-Person-Step-14.jpg";


                    ArrayList<Stream> stream_data = new ArrayList<Stream>();
                    for(int i = 0; i < streamNames.size(); i++){
                        Stream stream = new Stream(streamNames.get(i), streamInfos.get(i), streamCovers.get(i));
                        stream_data.add(stream);
                    }
                    final StreamAdapter adapter = new StreamAdapter(context, R.layout.listview_stream_row, stream_data);
                    listview.setAdapter(adapter);
                    listview.setOnItemClickListener(new AdapterView.OnItemClickListener(){

                        @Override
                        public void onItemClick(AdapterView<?> parent, final View view, int position, long id){
                            String streamName = streamNames.get(position);
                            Intent i = new Intent(getApplicationContext(), ViewAStreamActivity.class);
                            i.putExtra("stream_id", streamName);
                            startActivity(i);
                        }
                    });

                }
                catch(JSONException j){
                    System.out.println("JSON Error");
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable e) {
                Log.e(TAG, "There was a problem in retrieving the url : " + e.toString());
            }
        });
    }


    class GetImage extends AsyncTask<String, Void, Bitmap>{
        private Exception exception;

        protected Bitmap doInBackground(String... imageUrl) {
            try {
                try{
                    System.out.println("to Bitmapfactory, url is " + imageUrl[0]);
                    //String fake = "http://www.wikihow.com/images/f/ff/Draw-a-Cute-Cartoon-Person-Step-14.jpg";
                    Bitmap bitmap = BitmapFactory.decodeStream((InputStream)new URL(imageUrl[0]).getContent());
                    return bitmap;
                }catch (MalformedURLException e){
                    e.printStackTrace();
                }catch (IOException e){
                    e.printStackTrace();
                }

            } catch (Exception e) {
                this.exception = e;
                e.printStackTrace();
                return null;
            }
            return null;
        }

        protected void onPostExecute(Bitmap result) {
            ImageView photo = (ImageView) findViewById(R.id.photo);
            photo.setImageBitmap(result);
        }

    }

    @Override
    public void onClick(View v){
        if (!myApp.mGoogleApiClient.isConnecting()) {
            // We only process button clicks when GoogleApiClient is not transitioning
            // between connected and not connected.
//            if (!isOnline()) {
//                Toast.makeText(getApplicationContext(),
//                        "You need internet access to perform this action.", Toast.LENGTH_SHORT).show();
//                return;
//            }
            switch (v.getId()) {
                case R.id.edit_profile:
                    Intent editProfileIntent = new Intent(context, EditProfile.class);
                    startActivity(editProfileIntent);
                    break;
                case R.id.chat:
                    Intent chatIntent = new Intent(context, ChatActivity.class);
                    startActivity(chatIntent);
                    break;
            }
        }

    }


}
