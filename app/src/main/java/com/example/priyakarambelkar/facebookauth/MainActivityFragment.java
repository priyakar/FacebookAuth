package com.example.priyakarambelkar.facebookauth;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.LoggingBehavior;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {
    private static final String TAG = MainActivity.class.getSimpleName();
    CallbackManager callbackManager;
    TextView display;
    Button getBtn;
    TimeTracker date = new TimeTracker();
    private FacebookCallback<LoginResult> callback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            AccessToken token = loginResult.getAccessToken();
            displayProfileName();
        }

        @Override
        public void onCancel() {

        }

        @Override
        public void onError(FacebookException e) {

        }
    };

    private void displayProfileName() {
        Profile profile = Profile.getCurrentProfile();
        if (profile != null) {
            display.setText(profile.getName());
        } else {
            display.setText("please login");
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        displayProfileName();

    }

    public static MainActivityFragment newInstance() {
        return new MainActivityFragment();
    }

    public MainActivityFragment() {

    }

    LoginButton loginBtn;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());
        callbackManager = CallbackManager.Factory.create();

        Log.e("date","seconds "+date.currentDate.getTimeInMillis()+" \ndate "+date.currentDate.get(Calendar.YEAR));

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loginBtn = (LoginButton) view.findViewById(R.id.login_button);
        display = (TextView) view.findViewById(R.id.textView);
        getBtn = (Button) view.findViewById(R.id.get_content);
        loginBtn.setFragment(this);
        loginBtn.setReadPermissions("public_profile", "read_stream", "user_posts","user_likes","user_photos","user_friends", "user_tagged_places");
        loginBtn.registerCallback(callbackManager, callback);
        getBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //getUserProfile();
                getUserPosts();
            }
        });


    }

    private void getUserPosts() {
        FacebookSdk.addLoggingBehavior(LoggingBehavior.REQUESTS);
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, 2013);
        long time = cal.getTimeInMillis()/1000;
        Log.e("fields", "" + time);
        String until = String.valueOf(time);
        Bundle parameters = new Bundle();
       parameters.putString("since"," 1340582400");
       parameters.putString("until"," 1340668799");
      // parameters.putString("fields","feed.since(1340582400)");
        new GraphRequest(AccessToken.getCurrentAccessToken(),
                "/me/feed/", parameters, HttpMethod.GET,
                new GraphRequest.Callback() {
                    @Override
                    public void onCompleted(GraphResponse graphResponse) {
                       /* JSONObject jsonObject;
                        jsonObject = graphResponse.getJSONObject();
                        try {
                            JSONObject json = (JSONObject) jsonObject.get("feed");

                            Log.e("data", json.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
*/                      Log.e("data", graphResponse.toString());
                    }
        }).executeAsync();


        //request.setParameters(parameters);
    }

    private void getUserProfile() {
        GraphRequest request = GraphRequest.newMeRequest(
                AccessToken.getCurrentAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(
                            JSONObject object,
                            GraphResponse response) {
                        try {
                            String location = object.getJSONObject("location").optString("name");
                            Log.e("check it", location);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
        request.executeAsync();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

    }
}
