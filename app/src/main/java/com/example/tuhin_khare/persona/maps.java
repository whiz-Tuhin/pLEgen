package com.example.tuhin_khare.persona;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.IBinder;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.request.target.ImageViewTargetFactory;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class maps extends AppCompatActivity {

    String value1="12.9833",value2="77.5833 ";
    String link = "http://dev.virtualearth.net/REST/v1/Imagery/Map/Road/Routes?waypoint.1="+value1+","+value2+"&waypoint.2=13.360,74.786&key=AgkBeksYxSn0hhjtCP0pES7ABQ_stZOrMjO7yR_58rFfLiQyHUa48vNBdZ4mcVXn";
    String result = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        Bundle extras = getIntent().getExtras();
        value1 = extras.getString("lats");
        value2 =extras.getString("longs");

        Log.i("NEW LATS AND LONGS",value1+"  "+value2);

        makeMaps();

        Log.i("THE NEW URL IS",link);

    }

    public void makeMaps(){

        new Thread(new Runnable() {
            @Override
            public void run() {
                //URL CALL
                URL url = null;
                try {
                    url = new URL(link);
                    //CONNECTING URL
                    HttpURLConnection urlConnection = null;
                    urlConnection = (HttpURLConnection) url.openConnection();

                    int statusCode = urlConnection.getResponseCode();
                    Log.i("CHECK STATUSCODE", "STATUSCODE    " + statusCode);


                    final InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                    final Bitmap bitmap = BitmapFactory.decodeStream(in);

                    runOnUiThread(new Thread(new Runnable() {
                        @Override
                        public void run() {


                            ImageView imageview = (ImageView) findViewById(R.id.imageView);
                            imageview.setImageBitmap(bitmap);
                        }
                    }));




                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    //CONVERTING RESPONSE RECEIVED TO STRING

    private String readStream(InputStream in) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader r = new BufferedReader(new InputStreamReader(in), 1000);
        for (String line = r.readLine(); line != null; line = r.readLine()) {
            sb.append(line);
        }
        in.close();
        return sb.toString();
    }

}

