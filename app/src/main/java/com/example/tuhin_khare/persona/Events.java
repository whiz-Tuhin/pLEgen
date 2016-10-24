package com.example.tuhin_khare.persona;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;

public class Events extends AppCompatActivity {

    String result = "", keyword = "", date = "FUTURE", location = "Bangalore", category = "", within = "15";
    String link = "http://api.eventful.com/json/events/search?app_key=bPrTdmsXJVhLQRsM&keywords=" + keyword + "&date=" + date + "&category=" + category + "&location=" + location + "&within=" + within;
    String latalongs[][] = new String[500][2];
    int p=-1;
    private ListView list;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> arrayList;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);

        //GETTING ALL THE VALUES FROM PREVIOUS ACTIVITY
        Bundle extras = getIntent().getExtras();
        Double politics = Double.parseDouble(extras.getString("Politics"));
        Double religion = Double.parseDouble(extras.getString("Religion"));
        Double conc = Double.parseDouble(extras.getString("Concentration"));
        Double art = Double.parseDouble(extras.getString("Art"));
        Double bio = Double.parseDouble(extras.getString("Biology"));
        Double business = Double.parseDouble(extras.getString("Business"));
        Double it = Double.parseDouble(extras.getString("Information Tech"));
        Double eng = Double.parseDouble(extras.getString("Engineering"));
        Double journalism = Double.parseDouble(extras.getString("Journalism"));
        Double finance = Double.parseDouble(extras.getString("Finance"));
        Double history = Double.parseDouble(extras.getString("History"));
        Double law = Double.parseDouble(extras.getString("Law"));
        Double psych = Double.parseDouble(extras.getString("Psychology"));
        Integer lvalue = Integer.parseInt(extras.getString("lvalue"));

        String[][] passedString_categ = (String[][]) extras.getSerializable("list");


        Log.i("JAISHREE", lvalue.toString());

        //ALGO TO DECIDE ON KEYWORDS AND CATEGORY

        String[][] keyword_values = {{"Art", String.valueOf(art * 100)},
                {"Biology", String.valueOf(bio * 100)},
                {"Business", String.valueOf(business * 100)},
                {"Information Tech", String.valueOf(it * 100)},
                {"Engineering", String.valueOf(eng * 100)},
                {"Journalism", String.valueOf(journalism * 100)},
                {"Finance", String.valueOf(finance * 100)},
                {"History", String.valueOf(history * 100)},
                {"Law", String.valueOf(law * 100)},
                {"Psychology", String.valueOf(psych * 100)}
        };

        //DESCENDING BUBBLE SORT


        for (int i = 0; i < 10 - 1; i++) {
            for (int j = 0; j < 10 - i - 1; j++) {
                if (keyword_values[j + 1][1].compareTo(keyword_values[j][1]) > 0) {
                    String temp = keyword_values[j][1];
                    keyword_values[j][1] = keyword_values[j + 1][1];
                    keyword_values[j + 1][1] = temp;
                    temp = keyword_values[j][0];
                    keyword_values[j][0] = keyword_values[j + 1][0];
                    keyword_values[j + 1][0] = temp;
                }
            }
            Log.i("Array index", keyword_values[i].toString());

        }


        for (int i = 0; i < lvalue; i++) {
            for (int j = 0; j < lvalue - i; j++) {
                Log.i("&&&&&", passedString_categ[j][0] + "   " + passedString_categ[j][1] + "    " + passedString_categ[j + 1][0] + "   " + passedString_categ[j + 1][1]);

                try {
                    if (passedString_categ[j][1].compareTo(passedString_categ[j + 1][1]) > 0) {
                        String temp = passedString_categ[j][1];
                        passedString_categ[j][1] = passedString_categ[j + 1][1];
                        passedString_categ[j + 1][1] = temp;
                        temp = passedString_categ[j][0];
                        passedString_categ[j][0] = passedString_categ[j + 1][0];
                        passedString_categ[j + 1][0] = temp;
                    }
                } catch (Exception e) {
                    continue;
                }
                Log.i("check    777777", "" + i);
            }

        }

        //merge sort
        String finals[] = new String[100];
        int l = -1;
        int i = 0, j = 0;
        while ((i < 10 && j <= lvalue)) {
            try {
                if (keyword_values[i][1].compareTo(passedString_categ[j][1]) > 0) {
                    finals[++l] = keyword_values[i][0];
                    i++;
                } else {
                    finals[++l] = passedString_categ[j][0];
                    j++;
                }
            } catch (Exception e) {
                i++;
                continue;
            }
        }
        Log.i("check", "hiii");
        while (i < 10) {
            finals[++l] = keyword_values[i][0];
            i++;
        }
        while (j <= lvalue) {
            try {
                finals[++l] = passedString_categ[j][0];
                j++;
            } catch (Exception e) {
                continue;
            }
        }

        for (i = 0; i <= l; i++)
            Log.i("********", finals[i]);


        //CALLING EVENTFUL
        for (i = 0; i <= l; i++) {
            keyword = finals[i];
            category = finals[i];
            eventful();
        }

        list = (ListView) findViewById(R.id.listView);
        arrayList = new ArrayList<String>();

        // Adapter: You need three parameters 'the context, id of the layout (it will be where the data is shown),
        // and the array that contains the data
        adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, arrayList);

        // Here, you set the data in your ListView
        list.setAdapter(adapter);

//        Intent intent = new Intent(getApplicationContext(),maps.class);
//        startActivity(intent);
        list.setOnItemClickListener(
                new

                        AdapterView.OnItemClickListener() {

                            @Override
                            public void onItemClick (AdapterView < ? > arg0, View view,
                                                     int position, long id){
                                // TODO Auto-generated method stub
                                Object o = list.getItemAtPosition(position);
//                                String pen = o.toString();
//                                Toast.makeText(getApplicationContext(), "You have chosen the pen: " + " " + pen+"   "+position, Toast.LENGTH_LONG).show();
                                Intent inte =new Intent(getApplicationContext(),maps.class);
                                Bundle extras = new Bundle();
                                extras.putString("lats", latalongs[position][0]);
                                extras.putString("longs",latalongs[position][1]);
                                inte.putExtras(extras);
                                startActivity(inte);

                            }
                        }

        );


    }

    public void eventful() {


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


                    InputStream in = null;
                    in = new BufferedInputStream(urlConnection.getInputStream());
                    result = readStream(in);

                    Log.i("CHECK RESULT", result);

                    JSONObject data = new JSONObject(result);
                    JSONArray event = data.getJSONObject("events").optJSONArray("event");

                    for (int i = 0; i < event.length(); i++) {
                        JSONObject each = event.optJSONObject(i);
                        final String title = each.optString("title");
                        String lats = each.optString("latitude");
                        String longs = each.optString("longitude");
                        latalongs[++p][0]=lats;
                        latalongs[p][1]=longs;
                        runOnUiThread(new Thread(new Runnable() {
                            @Override
                            public void run() {
                                arrayList.add(title);
                                // next thing you have to do is check if your adapter has changed
                                adapter.notifyDataSetChanged();
                            }
                        }));

                        Log.i("check", "" + i);

                    }


                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
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
