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
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import javax.security.auth.callback.Callback;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {
    private static final String TAG = MainActivity.class.getSimpleName();
    CallbackManager callbackManager;
    TextView display;
    Button getBtn;
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
        loginBtn.setReadPermissions("public_profile","user_posts", "read_stream", "user_posts");
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
        Log.e("in post", AccessToken.getCurrentAccessToken().getToken());
        GraphRequest request = new GraphRequest(AccessToken.getCurrentAccessToken(),
                "me", null, HttpMethod.GET,
                new GraphRequest.Callback() {
            @Override
            public void onCompleted(GraphResponse graphResponse) {
                Log.e("in post", graphResponse.toString());
            }
        });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "posts.since=(1400889600)");
        parameters.putString("fields", "posts.until=(1400975999)");
        request.setParameters(parameters);
        request.executeAsync();
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
