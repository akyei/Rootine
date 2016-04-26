package com.team16umd.routineapp;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.login.widget.ProfilePictureView;
import com.firebase.client.AuthData;
import com.firebase.client.Firebase;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.text.ParseException;
import java.util.Date;


 /*
    TODO Implement the list activity. The list activity should show all Reminder items for the
    current user. You will need to define the XML for each ReminderItem in the list. We also talked
    about having a different UI depending on the time of day. I wouldn't worry about that for the
    initial demo though.
  */


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

public class ReminderListActivity extends ListActivity {
    private AuthData mAuthData;
    private ProfilePictureView mProfilePic;
    private Firebase mFirebase;
    private Firebase mUserRef;
    private String mUid;
    private static final String FILE_NAME = "ReminderAppData.txt";
    // List Adapter for this class
    private ReminderItemAdapter mAdapter;

    // requestCode for adding a reminder;
    static final int ADD_REMINDER_REQUEST = 42;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        Firebase.setAndroidContext(this);
        getListView().setFooterDividersEnabled(true);
        getListView().addFooterView(findViewById(R.id.footer_view));
        setContentView(R.layout.activity_reminder_list);



        mProfilePic = (ProfilePictureView) findViewById(R.id.profile_pic);
        /*
            Creates reference to firebase (mFirebase) then gets an authentication
            token (mAuthData)
         */
        mFirebase = new Firebase(getResources().getString(R.string.firebase_url));
        mAuthData = mFirebase.getAuth();

        if (mAuthData != null){

            Log.d(LoginActivity.TAG, "Login Token Valid - AuthData: " + mAuthData.toString());
            /*
                If the login is valid, set the profile Id for the imageView.
             */
            mProfilePic.setProfileId((String) mAuthData.getProviderData().get("id"));
            /*
                If the login is valid, create the firebase reference to the specific user.
             */
            mUid = (String) mAuthData.getUid();
            mUserRef = new Firebase(getResources().getString(R.string.firebase_url) + mUid);
            // Retrieve available items from firebase and add them to list adapter.
        } else {
            //TODO Handle the case when token is invalid (i.e return to LoginActivity)
        }
        mAdapter = new ReminderItemAdapter(getApplicationContext());
        getListView().setAdapter(mAdapter);

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == ADD_REMINDER_REQUEST && resultCode == RESULT_OK){
            ReminderItem nRi = new ReminderItem(data);

            //TODO- change behavior when FireBase connection exists
            mAdapter.add(nRi, false);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveItems();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadItems();
    }

    private void loadItems() {
        //TODO- load items from FireBase (or locally as a fallback?)
    }

    // Save ToDoItems to file
    private void saveItems() {
        //TODO- store items in Firebase (or locally as a fallback)
    }
    @Override
    public void onDestroy() {
        super.onDestroy();

    }

}
