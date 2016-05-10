package com.team16umd.routineapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.MutableData;
import com.firebase.client.ServerValue;
import com.firebase.client.Transaction;
import com.firebase.client.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

public class LoginActivity extends Activity {
    public static final String TAG = "LOGIN ACTIVITY";
    private Button mStartButton;
    private LoginButton mloginButton;
    private CallbackManager callbackManager;
    private AccessTokenTracker facebookAccessTokenTracker;
    private ProgressDialog mAuthProgressDialog;
    private Firebase mFirebaseRef;
    private Firebase mUserRef;
    private AuthData mAuthData;
    private Firebase.AuthStateListener mAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        Firebase.setAndroidContext(this);
        callbackManager = CallbackManager.Factory.create();
        AppEventsLogger.activateApp(this);
        setContentView(R.layout.activity_login);
        mloginButton = (LoginButton) findViewById(R.id.login_button);
        mStartButton = (Button) findViewById(R.id.start_button);
        mloginButton.setReadPermissions("user_friends");
        facebookAccessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                LoginActivity.this.onFacebookAccessTokenChange(currentAccessToken);
            }
        };

        mFirebaseRef = new Firebase(getResources().getString(R.string.firebase_url));
        mAuthProgressDialog = new ProgressDialog(this);
        mAuthProgressDialog.setTitle("Loading");
        mAuthProgressDialog.setMessage("Authenticating with US");
        mAuthProgressDialog.setCancelable(false);
        mAuthProgressDialog.show();

        mAuthStateListener = new Firebase.AuthStateListener() {
            @Override
            public void onAuthStateChanged(AuthData authData) {
                mAuthProgressDialog.hide();
                setAuthenticatedUser(authData);
            }
        };
        mFirebaseRef.addAuthStateListener(mAuthStateListener);

        mStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, ReminderListActivity.class );
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        if(facebookAccessTokenTracker != null){
            facebookAccessTokenTracker.stopTracking();
        }
        mFirebaseRef.removeAuthStateListener(mAuthStateListener);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    private void onFacebookAccessTokenChange(AccessToken token){
        if (token != null) {
            mAuthProgressDialog.show();
            mFirebaseRef.authWithOAuthToken("facebook", token.getToken(), new AuthResultHandler("facebook"));
        } else {
            if (this.mAuthData != null && this.mAuthData.getProvider().equals("facebook")){
                mFirebaseRef.unauth();
                setAuthenticatedUser(null);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
    private void setAuthenticatedUser(AuthData authData){
        if (authData != null) {
           // mloginButton.setVisibility(View.GONE);
            mStartButton.setVisibility(View.VISIBLE);
            String name = null;
            if (authData.getProvider().equals("facebook")){
                Map<String, Object> userInfo = new HashMap<String, Object>();
                mUserRef = new Firebase(getResources().getString(R.string.firebase_url)+ authData.getUid() +"/");
                Firebase loginRef = mUserRef.child("login_info");
                userInfo.put("login_time", ServerValue.TIMESTAMP);
                userInfo.put("profile_url", authData.getProviderData().get("profileImageURL"));
                Firebase pointsRef = mUserRef.child("points");
                pointsRef.runTransaction(new Transaction.Handler() {
                    @Override
                    public Transaction.Result doTransaction(MutableData mutableData) {
                        if (mutableData.getValue() == null){
                            mutableData.setValue(0);
                        } else {
                            //do nothing, points already set
                        }
                        return Transaction.success(mutableData);
                    }

                    @Override
                    public void onComplete(FirebaseError firebaseError, boolean b, DataSnapshot dataSnapshot) {

                    }
                });
                loginRef.updateChildren(userInfo);
                name = (String) authData.getProviderData().get("displayName");

            } else {
                Log.e(TAG, "Invalid Provider: "+ authData.getProvider());
            }

            if (name != null) {

            } else {
                mloginButton.setVisibility(View.VISIBLE);
            }
            this.mAuthData = authData;
        }
    }

    private void showErrorDialog(String message){
        new AlertDialog.Builder(this)
                .setTitle("Error")
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
    private class AuthResultHandler implements Firebase.AuthResultHandler {
        private final String provider;

        public AuthResultHandler(String provider) {
            this.provider = provider;
        }
        @Override
        public void onAuthenticated(AuthData authData) {
            mAuthProgressDialog.hide();
            Log.i(TAG, provider + " auth successful");
            setAuthenticatedUser(authData);
        }
        @Override
        public void onAuthenticationError(FirebaseError firebaseError){
            mAuthProgressDialog.hide();
            showErrorDialog(firebaseError.toString());
        }
    }
}

