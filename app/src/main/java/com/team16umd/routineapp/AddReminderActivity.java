package com.team16umd.routineapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.facebook.FacebookSdk;
import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

public class AddReminderActivity extends Activity {
    public final String TAG = "ADD_REMINDER_ACTIVITY";
    private Firebase mFirebase;
    private Firebase mUserRef;
    private AuthData mAuthData;
    private String mUid;
    private TextView mPoints;
    private RadioGroup mNotificationGroup;
    private RadioGroup mFeedGroup;
    private boolean mNotification;
    private boolean mFeed;
    private EditText mTitle;
    private EditText mDescription;


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
        //Initialize Sdk's
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        Firebase.setAndroidContext(getApplicationContext());
        //Get the time of day and set the corresponding UI
        if (ReminderItemAdapter.beforeNoon()) {
            setContentView(R.layout.activity_add_reminder);
        } else {
            setContentView(R.layout.activity_add_reminder_night);
        }

        //Create Firebase connection
        mFirebase = new Firebase(getResources().getString(R.string.firebase_url));
        mAuthData = mFirebase.getAuth();

        //Linking variables to UI
        mNotificationGroup = (RadioGroup) findViewById(R.id.notication_group);
        mFeedGroup = (RadioGroup) findViewById(R.id.feed_group);
        mTitle = (EditText) findViewById(R.id.title_box);
        mPoints = (TextView) findViewById(R.id.points);
        mDescription = (EditText) findViewById(R.id.description_box);

        //Checking if User is logged in
        if (mAuthData != null){

            Log.i(TAG, "User is logged in");
            Log.d(TAG, "AuthData: " + mAuthData.toString());
            //Create Reference to user's profile within firebase
            mUid = mAuthData.getUid();
            mUserRef = new Firebase(getResources().getString(R.string.firebase_url) + mUid);

            //Get how many points the user has
            mUserRef.child("points").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getValue() == null){
                        Log.e(TAG, "User did not have any points set in their profile, defaulting to 0");
                        mPoints.setText("0");
                    } else {
                        mPoints.setText(dataSnapshot.getValue().toString());
                    }
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });

        } else {
            //TODO: Build and show a one button alert dialog that redirects user to login Activity
        }

        Button submit = (Button) findViewById(R.id.submit_button);
        Button cancel = (Button) findViewById(R.id.cancel);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTitle == null || mTitle.getText().toString().equals("")){
                    //TODO Build and show a one button alert dialog saying Title is neccesary.
                } else {
                    Intent data = new Intent();
                    mNotification = getNotificationSetting();
                    mFeed = getFeedSetting();
                    ReminderItem.packageIntent(data, mTitle.getText().toString(),
                            mDescription.getText().toString(), mNotification, mFeed);
                    setResult(RESULT_OK, data);
                    finish();
                }
            }
        });
        cancel.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent data = new Intent();
                setResult(RESULT_CANCELED, data);
                finish();
            }
        }));
    }

    /*
        Returns Radio Button values for Notifications
     */

    private boolean getNotificationSetting(){
        switch(mNotificationGroup.getCheckedRadioButtonId()){
            case R.id.notification_yes: {
                return true;
            }case R.id.notification_no: {
                return false;
            }default: {
                return true;
            }
        }
    }
    /*
        Returns values from Radio Buttons
     */
    private boolean getFeedSetting(){
        switch(mFeedGroup.getCheckedRadioButtonId()){
            case R.id.feed_yes: {
                return true;
            }case R.id.feed_no: {
                return false;
            }default: {
                return true;
            }
        }
    }

}
