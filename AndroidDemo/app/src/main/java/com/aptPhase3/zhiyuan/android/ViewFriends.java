package com.aptPhase3.zhiyuan.android;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.GridView;
import android.util.Log;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import android.widget.ListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

public class ViewFriends extends ActionBarActivity{
    Context context = this;
    private String TAG  = "Display Friends";
    protected MyApplication myApp;
    private View.OnClickListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_friends);
        final ListView listview1 = (ListView) findViewById(R.id.listView);
        myApp = (MyApplication)this.getApplication();
        final String request_url = myApp.back_end + "android/view_friends?user_id="+myApp.userName;
        AsyncHttpClient httpClient = new AsyncHttpClient();
        httpClient.get(request_url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                final ArrayList<String> photoUrls = new ArrayList<String>();
                final ArrayList<String> userIds = new ArrayList<String>();
                final ArrayList<Person> person_data = new ArrayList<Person>();
                try {
                    JSONObject jObject = new JSONObject(new String(response));
                    JSONArray displayPhotos = jObject.getJSONArray("friendPhotos");
                    JSONArray displayNames = jObject.getJSONArray("friendNames");

                    for(int i=0;i<displayPhotos.length();i++) {
                        photoUrls.add(displayPhotos.getString(i));
                        userIds.add(displayNames.getString(i));
                        System.out.println(displayNames.getString(i));
                    }
                    photoUrls.add(displayPhotos.getString(0));
                    photoUrls.add(displayPhotos.getString(0));
                    photoUrls.add(displayPhotos.getString(0));
                    System.out.println("image url is " + displayPhotos.getString(0));
                    userIds.add("angela");
                    userIds.add("henry");
                    userIds.add("david");

                    for(int i = 0; i < userIds.size(); i++){
                        Person p = new Person(userIds.get(i), photoUrls.get(i));
                        person_data.add(p);
                    }

                    final PersonAdapter adapter = new PersonAdapter(context, R.layout.listview_item_row, person_data);
                    listview1.setAdapter(adapter);

                    listview1.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                        @Override
                        public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                            String userId = userIds.get(position);
                            Intent i = new Intent(getApplicationContext(), ViewProfile.class);
                            i.putExtra("userId", userId);
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



}
