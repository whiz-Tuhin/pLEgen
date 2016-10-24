package com.example.tuhin_khare.persona;

        import android.content.Intent;
        import android.os.NetworkOnMainThreadException;
        import android.support.v4.app.Fragment;
        import android.os.Bundle;
        import android.support.v4.app.NotificationCompatExtras;
        import android.support.v4.view.accessibility.AccessibilityManagerCompat;
        import android.util.Log;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.TextView;
        import android.widget.Toast;

        import com.facebook.AccessToken;
        import com.facebook.AccessTokenTracker;
        import com.facebook.CallbackManager;
        import com.facebook.FacebookCallback;
        import com.facebook.FacebookException;
        import com.facebook.FacebookSdk;
        import com.facebook.GraphRequest;
        import com.facebook.GraphResponse;

        import com.facebook.HttpMethod;

        import com.facebook.Profile;
        import com.facebook.ProfileTracker;
        import com.facebook.login.LoginResult;
        import com.facebook.login.widget.LoginButton;

        import org.json.JSONArray;

        import java.io.BufferedInputStream;
        import java.io.BufferedReader;
        import java.io.DataOutputStream;
        import java.io.IOException;
        import java.io.InputStream;
        import java.io.InputStreamReader;
        import java.net.HttpURLConnection;
        import java.net.MalformedURLException;
        import java.net.URL;
        import java.util.ArrayList;
        import java.util.Arrays;
        import java.util.List;
        import java.util.Map;

        import org.json.JSONException;
        import org.json.JSONObject;

        import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * A placeholder fragment containing a simple view.
 */



public class Main3ActivityFragment extends Fragment {

    private CallbackManager callbackManager;
    private TextView textView;

    private AccessTokenTracker accessTokenTracker;
    private ProfileTracker profileTracker;

    URL url =null;
    HttpURLConnection urlConnection = null;
    String result = "",token;

    String mStringArray2[] = {"5845317146","6460713406","22404294985","35312278675","105930651606","171605907303","199592894970","274598553922","340368556015","100270610030980"};

    double politics = 0.0,religion=0.0,conc=0.0,art=0.0,bio=0.0,business=0.0,it=0.0,eng=0.0,journalism=0.0,finance=0.0,history=0.0,law=0.0,psych=0.0;

    //declaring a string array for the generated like ids
    String myLikeIds[ ] = new String[1000];
    String category[][] = new String[100][2];

    int l = -1;

    //FACEBOOK CALLBACK FOR LOGIN

    private FacebookCallback<LoginResult> callback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            Log.i("@##%$%%^#$@@%","fhgjh");
            AccessToken accessToken = loginResult.getAccessToken();
            Profile profile = Profile.getCurrentProfile();
            displayMessage(profile);

        }

        @Override
        public void onCancel() {
            Log.i("CHECK","onCancel");
        }

        @Override
        public void onError(FacebookException e) {

        }

    };


    //CALLS TO APPLY MAGIC SAUCE

    public  void authCall() {
        new Thread(new Runnable(){
            @Override
            public void run() {
                // Do network action in this function

                try {

                    url = new URL("http://api-v2.applymagicsauce.com/auth");
                    urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setDoOutput(true);
                    urlConnection.setRequestMethod("POST");
                    urlConnection.setRequestProperty("Content-Type", "application/json");
                    urlConnection.setRequestProperty("Accept", "application/json");

                    DataOutputStream dStream = new DataOutputStream(urlConnection.getOutputStream());

                    JSONObject obj = new JSONObject();
                    obj.put("customer_id", "2516");
                    obj.put("api_key", "pg5cpjq4hr2kl8598vd5u6d4e5");

                    dStream.writeBytes(obj.toString());
                    Log.e("JSON Input", obj.toString());

                    dStream.flush();
                    dStream.close();

                    int statusCode = urlConnection.getResponseCode();
                    Log.i("CHECK STATUS FOR /auth","StatusCode  "+statusCode);

                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                    result=readStream(in);

                    //extract X-Auth Token
                    JSONObject authres = new JSONObject(result);
                    token = authres.optString("token");

                    predictionCall();

                    Log.i("CHECK TOKEN RECIEVED", result);
                } catch (MalformedURLException e) {
                    Log.i("CHECK MALFORMED_URL","MALFORMED_URL");
                    e.printStackTrace();
                } catch (IOException e) {
                    Log.i("CHECK IO_EXCEPTION","IO_EXCEPTION");
                    e.printStackTrace();
                } catch(NetworkOnMainThreadException e){
                    Log.i("CHECK NETWORK_ON_MAIN","NETWORK_ON_MAIN");
                    e.printStackTrace();
                } catch (JSONException e) {
                    Log.i("CHECK JSONEXCEPTION","JSONEXCEPTION");
                    e.printStackTrace();
                } finally {
                    urlConnection.disconnect();
                }

                }
            }).start();
        }


        //CONVERTING RESPONSE RECEIVED TO STRING

    private String readStream(InputStream in) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader r = new BufferedReader(new InputStreamReader(in),1000);
        for (String line = r.readLine(); line != null; line =r.readLine()){
            sb.append(line);
        }
        in.close();
        return sb.toString();
    }



    private void predictionCall(){


        new Thread(new Runnable(){
            @Override
            public void run() {
                // Do network action in this function

                try {
                    url = new URL("http://api-v2.applymagicsauce.com/like_ids");
                    urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setDoOutput(true);
                    urlConnection.setRequestMethod("POST");
                    urlConnection.setRequestProperty("Content-Type", "application/json");
                    urlConnection.setRequestProperty("Accept", "application/json");
                    urlConnection.setRequestProperty("X-Auth-Token",token);


                    DataOutputStream dStream = new DataOutputStream(urlConnection.getOutputStream());


                    JSONArray mJSONArray = new JSONArray(Arrays.asList((myLikeIds)));

                    dStream.writeBytes(mJSONArray.toString());
                    Log.e("JSON Input",mJSONArray.toString());

                    //for closing stream of data
                    dStream.flush();
                    dStream.close();

                    int statusCode = urlConnection.getResponseCode();
                    Log.i("CHECK STATUSCODE","STATUSCODE    "+statusCode);

                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                    result=readStream(in);

                    Log.i("CHECK PREDICTION RESULT", result);


                    //GETTING INDIVIDUAL VALUES OF TRAITS FOR PREDICTION

                    JSONObject extract = new JSONObject(result);
                    JSONArray pred = extract.getJSONArray("predictions");

                    Log.i("******************",pred.toString());



                    for(int i=0;i<pred.length();i++){
                        JSONObject each = pred.optJSONObject(i);
                        String trait = each.optString("trait");
                        Log.i("#######",trait);
                        if(trait.compareTo("Politics")==0 || trait.compareTo("Politics_Conservative")==0 || trait.compareTo("Politics_Liberal")==0 || trait.compareTo("Politics_Uninvolved")==0 || trait.compareTo("Politics_Libertanian")==0) {
                            politics += each.optDouble("value");
                            Log.i("%%%%%%%%%%%%%%", String.valueOf(politics));
                        }
                        else if(trait.compareTo("Religion")==0 || trait.compareTo("Religion_None")==0 || trait.compareTo("Religion_Christian_Other")==0 || trait.compareTo("Religion_Catholic")==0 || trait.compareTo("Religion_Jewish")==0 || trait.compareTo("Religion_Lutheran")==0 || trait.compareTo("Religion_Mormon")==0)
                            religion+=each.optDouble("value");
                        else if(trait.compareTo("Concentration")==0)
                            conc = each.optDouble("value");
                        else if(trait.compareTo("Concentration_Art")==0)
                            art = each.optDouble("value");
                        else if(trait.compareTo("Concentration_Biology")==0)
                            bio = each.optDouble("value");
                        else if(trait.compareTo("Concentration_IT")==0)
                            business = each.optDouble("value");
                        else if(trait.compareTo("Concentration_Education")==0)
                            it = each.optDouble("value");
                        else if(trait.compareTo("Concentration_Engineering")==0)
                            eng = each.optDouble("value");
                        else if(trait.compareTo("Concentration_Journalism")==0)
                            journalism = each.optDouble("value");
                        else if(trait.compareTo("Concentration_Finance")==0)
                            finance = each.optDouble("value");
                        else if(trait.compareTo("Concentration_History")==0)
                            history = each.optDouble("value");
                        else if(trait.compareTo("Concentration_Law")==0)
                            law = each.optDouble("value");
                        else if(trait.compareTo("Concentration_Psychology")==0)
                            psych = each.optDouble("value");

                    }

                    Intent intent = new Intent(getApplicationContext(),Events.class);
                    Bundle extras = new Bundle();
                    extras.putString("Politics", String.valueOf(politics));
                    extras.putString("Religion", String.valueOf(religion));
                    extras.putString("Concentration", String.valueOf(conc));
                    extras.putString("Art", String.valueOf(art));
                    extras.putString("Biology", String.valueOf(bio));
                    extras.putString("Business", String.valueOf(business));
                    extras.putString("Information Tech", String.valueOf(it));
                    extras.putString("Engineering", String.valueOf(eng));
                    extras.putString("Journalism", String.valueOf(journalism));
                    extras.putString("Finance", String.valueOf(finance));
                    extras.putString("History", String.valueOf(history));
                    extras.putString("Law", String.valueOf(law));
                    extras.putString("Psychology", String.valueOf(psych));
                    extras.putSerializable("list", category);
                    extras.putString("lvalue", String.valueOf(l));
                    intent.putExtras(extras);
                    startActivity(intent);


                } catch (MalformedURLException e) {
                    Log.i("CHECK MALFORMED_URL","MALFORMED_URL");
                    e.printStackTrace();
                } catch (IOException e) {
                    Log.i("CHECK IOEXCEPTION","IOEXCEPTION");
                    e.printStackTrace();
                } catch(NetworkOnMainThreadException e){
                    Log.i("CHECK NETWORKONMAIN","NETWORKONMAIN");
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    urlConnection.disconnect();


                }

            }
        }).start();

    }



    public Main3ActivityFragment() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);


        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());

        callbackManager = CallbackManager.Factory.create();

        accessTokenTracker= new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldToken, AccessToken newToken) {



            }
        };

        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile newProfile) {
                displayMessage(newProfile);




            }
        };

        accessTokenTracker.startTracking();
        profileTracker.startTracking();
    }





    @Override
    // onCreate function is the main UI view  (if app doesn't use layouts return null
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main3, container, false);
    }



    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        LoginButton loginButton = (LoginButton) view.findViewById(R.id.login_button);
        textView = (TextView) view.findViewById(R.id.textView);

        loginButton.setReadPermissions(Arrays.asList(" user_likes","email","public_profile"));
        loginButton.setFragment(this);

        Log.i("FLAG CHECK","PASSED");

        AccessToken token = AccessToken.getCurrentAccessToken();

        if(token == null){
            Log.i("CHECK TOKEN","NULL TOKEN");
        }
        else {
            Log.i("CHECK TOKEN", "TOKEN NOT NULL");


            GraphRequest data_request = GraphRequest.newMeRequest(
                    token, new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(
                                JSONObject json_object,
                                GraphResponse response) {

                            try {
                                // convert Json object into Json array
                                Log.i("FLAG CHECK", "PASSED");
                                JSONArray posts = json_object.getJSONObject("likes").optJSONArray("data");

                                Log.i("COMPLETE LIKE DATA", posts.toString());

                                int k = -1;    //STRING ARRAY INDEX OF MYLIKEIDS ARRAY
                                //FOR CATEGORY ARRAY

                                for (int i = 0; i < posts.length(); i++) {

                                    Log.i("INDEX i LOOP", "PROCESSING");
                                    JSONObject post = posts.optJSONObject(i);
                                    String id = post.optString("id");
                                    String categ = post.optString("category");
                                    category[++l][0] = categ;
                                    String name = post.optString("name");
                                    int count = post.optInt("likes");
                                    myLikeIds[++k] = id;


                                    try {
                                        JSONArray like_ids = post.getJSONObject("likes").optJSONArray("data");
                                        Log.i("LIKE_IDs LEN +  IDs", "" + like_ids.length() + "   " + like_ids);
                                        category[l][1] = "" + like_ids.length();
                                        for (int j = 0; j < like_ids.length(); j++) {
                                            try {
                                                JSONObject each = like_ids.optJSONObject(j);

                                                String eachName = each.optString("name");
                                                String eachid = each.optString("id");
                                                Log.i("EACH LIKE(NAME+ID) -->", eachName + "    " + eachid);
                                                myLikeIds[++k] = eachid;
                                            } catch (Exception e) {
                                                continue;
                                            }
                                        }
                                    } catch (JSONException e) {
                                        continue;
                                    }

                                    // print id, page name and number of like of facebook page
                                    Log.i("check something", id + " name -" + name + " category-" + category + " likes count" + count + "    " + i + "    " + posts.length());
                                }

                                Log.i("*****K VALUE *****", "K VALUE " + k);
                                for (int i = 0; i <= k; i++) {
                                    try {
                                        Log.i("CHECK ALL LIKE IDS", myLikeIds[i] + "     " + i);
                                    } catch (Exception e) {
                                        continue;
                                    }
                                }
                                authCall();

                            } catch (Exception e) {
                                Log.i("EXCEPTION IN LIKEIDS", "EXCEPTION");
                            }

                        }
                    });

            Bundle permission_param = new Bundle();

            permission_param.putString("fields", "likes{id,category,name,location,likes}");

            data_request.setParameters(permission_param);
            data_request.executeAsync();

            loginButton.registerCallback(callbackManager, callback);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("*********",""+resultCode);
        callbackManager.onActivityResult(requestCode, resultCode, data);

    }

    private void displayMessage(Profile profile){
        Log.i("DISPLAY MESSAGE","I AM HERE");
        if(profile != null){
            textView.setText(profile.getName());
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        accessTokenTracker.stopTracking();
        profileTracker.stopTracking();
    }

    @Override
    public void onResume() {
        super.onResume();
        Profile profile = Profile.getCurrentProfile();
        displayMessage(profile);
    }
}