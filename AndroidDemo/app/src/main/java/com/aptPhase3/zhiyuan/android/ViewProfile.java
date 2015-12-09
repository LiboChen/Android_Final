package com.aptPhase3.zhiyuan.android;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
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

public class ViewProfile extends ActionBarActivity {
    Context context = this;
    private String TAG  = "View Profile";
    protected MyApplication myApp;
    private View.OnClickListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);
        TextView nickName = (TextView) findViewById(R.id.nick_name);
        TextView description = (TextView) findViewById(R.id.description);
        //ImageView photo = (ImageView) findViewById(R.id.photo);
        myApp = (MyApplication)this.getApplication();

        //test
        nickName.setText(myApp.userName);
        description.setText("I like travelling and making friends!");



        final String request_url = myApp.back_end + "android/view_profile?user_id="+myApp.userName;
        AsyncHttpClient httpClient = new AsyncHttpClient();
        httpClient.get(request_url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
//                final ArrayList<String> photoUrls = new ArrayList<String>();
//                final ArrayList<String> userIds = new ArrayList<String>();

                try {
                    JSONObject jObject = new JSONObject(new String(response));
//                    JSONArray displayPhotos = jObject.getJSONArray("friendPhotos");
//                    JSONArray displayNames = jObject.getJSONArray("friendNames");
                    String imageUrl = "http://www.wikihow.com/images/f/ff/Draw-a-Cute-Cartoon-Person-Step-14.jpg";
                    new GetImage().execute(imageUrl);

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
                    String fake = "http://www.wikihow.com/images/f/ff/Draw-a-Cute-Cartoon-Person-Step-14.jpg";
                    Bitmap bitmap = BitmapFactory.decodeStream((InputStream)new URL(fake).getContent());
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


}
