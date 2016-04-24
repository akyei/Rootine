package com.team16umd.routineapp;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.facebook.FacebookSdk;
import com.firebase.client.AuthData;
import com.firebase.client.Firebase;

public class AddReminderActivity extends Activity {
    private Firebase mFirebase;
    private Firebase mUserRef;
    private AuthData mAuthData;
    private String mUid;

    //TODO Define XML layout for AddReminder in res/layout/activity_add_reminder
    //TODO See UILab for inspiration
    //TODO Bundle the intent to pass to the activity that started this one. Remember to set the result
    //code to RESULT_OK

    /*
    Firebase README
    To get a reference to the firebase server, You need a Firebase variable (mFirebase)
    To connect it, you should initialize mFirebase with the line
    mFirebase = new Firebase(getResources().getString(R.string.firebase_url));

    From there, to get an auth token for the user, you need an AuthData Variable
    I use AuthData mAuthData. To initialize the token, you need the line
    mAuthData = mFirebase.getAuth(). If the authData is null, then the user is logged out/or
    their token has expired. You can get a unique id for the user with
    mAuthData.getUid().

    To insert data related to a specific user, you should use the firebase reference I created
    called mUserRef. You can add new fields with
    mUserRef.updateChildren(String key, Map<String, Object> param)
    The map parameter must take strings as a key and the values can either be another
    Map<String, Object> or String, Long, Double, Boolean, and List<Object>. (These types
    correspond to valid JSON types.

    see https://www.firebase.com/docs/android/guide/saving-data.html for a detailed explanation.
    In LoginActivity, I have code that adds a login timestamp and profile_img url

 */

    /*
        This activity should create a data intent that is returned when the activity finishes.
        The Intent should bundle specific information that can be used to create a new reminder Item.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        Firebase.setAndroidContext(getApplicationContext());
        setContentView(R.layout.activity_add_reminder);
        mFirebase = new Firebase(getResources().getString(R.string.firebase_url));
        mAuthData = mFirebase.getAuth();

        if (mAuthData != null){
            Log.d(LoginActivity.TAG, "AuthData: " + mAuthData.toString());
            mUid = mAuthData.getUid();
            mUserRef = new Firebase(getResources().getString(R.string.firebase_url) + mUid);
        }
    }
}
