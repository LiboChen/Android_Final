package com.aptPhase3.zhiyuan.android;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;


public class CreatePOIActivity extends ActionBarActivity implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    Context context = this;
    private String stream_id;
    protected MyApplication myApp;
    private Activity activity = this;
    private GoogleApiClient geoClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myApp = (MyApplication) this.getApplication();
        setContentView(R.layout.activity_create_poi);
        buildGoogleApiClient();

        Button chooseFromLibraryButton = (Button) findViewById(R.id.buttonPOI);
        final EditText mEdit   = (EditText)findViewById(R.id.poiName);
        chooseFromLibraryButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        stream_id = mEdit.getText().toString();
                        geoClient.connect();
                    }
                }
        );
    }

    protected synchronized void buildGoogleApiClient() {
        geoClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    //
    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {

    }

    @Override
    public void onConnected(Bundle connectionHint) {
        System.out.println("upload onConnected method called!!!!!!!!!!");
        String request_url = myApp.back_end + "android/create_a_stream?stream_id=" + stream_id;
        request_url += "&user_id=" + myApp.userName;
        request_url = request_url.replace(" ","%20");
        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                geoClient);
        if (mLastLocation != null) {
            String lat = ((Double) mLastLocation.getLatitude()).toString();
            String lon = ((Double) mLastLocation.getLongitude()).toString();
            request_url += "&latitude=" + lat;
            request_url += "&longitude=" + lon;
        } else {
            Toast.makeText(context, "Failed to retrieve location", Toast.LENGTH_SHORT).show();
        }
        System.out.println(request_url);

        AsyncHttpClient client = new AsyncHttpClient();
        client.post(request_url, null, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                Log.w("async", "success!!!!");
                Toast.makeText(context, "Create POI Successful", Toast.LENGTH_SHORT).show();
                activity.finish();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                Log.e("Creating POI", "There was a problem in retrieving the url : " + e.toString());
                Toast.makeText(context, "Create POI Failed", Toast.LENGTH_SHORT).show();
                activity.finish();
            }
        });
    }



}