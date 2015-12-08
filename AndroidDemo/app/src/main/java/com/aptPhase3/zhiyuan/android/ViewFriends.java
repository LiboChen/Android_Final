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

//import android.widget.ProgressBar;
//import android.widget.SimpleCursorAdapter;
//import android.view.ViewGroup;
//import android.view.Gravity;
//import android.database.Cursor;
//import android.app.LoaderManager;

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
        //final ListView listview2 = (ListView) findViewById(R.id.listView2);
        myApp = (MyApplication)this.getApplication();
        final String request_url = myApp.back_end + "android/view_friends?user_id="+myApp.userName;
        AsyncHttpClient httpClient = new AsyncHttpClient();
        httpClient.get(request_url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                final ArrayList<String> photoUrls = new ArrayList<String>();
                final ArrayList<String> userIds = new ArrayList<String>();

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
                    //listview1.setAdapter(new ImageAdapter(context, photoUrls));
                    final ArrayAdapter adapter = new ArrayAdapter(context, R.layout.row_layout, R.id.label, userIds);
                    listview1.setAdapter(adapter);

                    listview1.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                        @Override
                        public void onItemClick(AdapterView<?> parent, final View view,
                                                int position, long id) {
                            //to be implemented
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

//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_view_friends);
//
//        final ListView listview = (ListView) findViewById(R.id.listView2);
//        String[] values = new String[] { "Android", "iPhone", "WindowsMobile",
//                "Blackberry", "WebOS", "Ubuntu", "Windows7", "Max OS X",
//                "Linux", "OS/2", "Ubuntu", "Windows7", "Max OS X", "Linux",
//                "OS/2", "Ubuntu", "Windows7", "Max OS X", "Linux", "OS/2",
//                "Android", "iPhone", "WindowsMobile" };
//
//        final ArrayList<String> list = new ArrayList<String>();
//        for (int i = 0; i < values.length; ++i) {
//            list.add(values[i]);
//        }
//        final StableArrayAdapter adapter = new StableArrayAdapter(this, android.R.layout.simple_list_item_1, list);
//        listview.setAdapter(adapter);
//
//        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
//            @Override
//            public void onItemClick(AdapterView<?> parent, final View view,
//                                    int position, long id) {
//                final String item = (String) parent.getItemAtPosition(position);
//                view.animate().setDuration(2000).alpha(0).withEndAction(new Runnable() {
//                            @Override
//                            public void run() {
//                                list.remove(item);
//                                adapter.notifyDataSetChanged();
//                                view.setAlpha(1);
//                            }
//                        });
//            }
//
//        });
//    }

    private class StableArrayAdapter extends ArrayAdapter<String> {

        HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();

        public StableArrayAdapter(Context context, int textViewResourceId,
                                  List<String> objects) {
            super(context, textViewResourceId, objects);
            for (int i = 0; i < objects.size(); ++i) {
                mIdMap.put(objects.get(i), i);
            }
        }

        @Override
        public long getItemId(int position) {
            String item = getItem(position);
            return mIdMap.get(item);
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

    }

}
