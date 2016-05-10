package com.team16umd.routineapp;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Button;
import android.widget.ImageView;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.login.widget.ProfilePictureView;
import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import org.w3c.dom.Text;

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
import java.util.Calendar;
import java.util.Date;

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
    DONE: Implement the list activity. The list activity should show all Reminder items for the
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
    private Firebase mFirebase;
    private Firebase mUserRef;
    private Button mBadgeButton, mSocialButton, mHistoryButton;
    private String mUid;
    private TextView mPoints;
    private ImageView mSettingsView;

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
        TextView footerView;
        if (true) {
            setContentView(R.layout.activity_reminder_list_night);
            getListView().setFooterDividersEnabled(true);
            footerView = (TextView) this.getLayoutInflater().inflate(R.layout.footer_view_night, null);
            getListView().addFooterView(footerView);
        } else {
            setContentView(R.layout.activity_reminder_list);
            getListView().setFooterDividersEnabled(true);
            footerView = (TextView) this.getLayoutInflater().inflate(R.layout.footer_view, null);
            getListView().addFooterView(footerView);
        }


        mPoints = (TextView) findViewById(R.id.points);
        mBadgeButton = (Button) findViewById(R.id.feed_button);
        //mSocialButton = (Button) findViewById(R.id.social_button);
        mHistoryButton = (Button) findViewById(R.id.graph_button);
        mSettingsView = (ImageView) findViewById(R.id.settings_button);

        mBadgeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReminderListActivity.this, SocialFeedActivity.class);
                startActivity(intent);
            }
        });
        //TODO: Implement a social feature here (TBD)
       /* mSocialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReminderListActivity.this, SocialFeedActivity.class);
                startActivity(intent);
            }
        }); */

        mHistoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReminderListActivity.this, GraphActivity.class);
                startActivity(intent);
            }
        });

        footerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addRoutineIntent = new Intent(ReminderListActivity.this, AddReminderActivity.class);
                startActivityForResult(addRoutineIntent, ADD_REMINDER_REQUEST);
            }
        });
        mSettingsView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                /*Make setting fragment*/
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getRootView().getContext());
                builder.setTitle("Options");
                builder.setMessage("Select One");
                builder.setPositiveButton("Check All", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        for(int i = 0; i < mAdapter.getCount(); i++){
                            ReminderItem temp = (ReminderItem) mAdapter.getItem(i);
                            temp.setmNightStatus(true);
                            temp.setmDayStatus(true);
                            mAdapter.completeItem(temp);
                        }
                        mAdapter.notifyDataSetChanged();
                    }
                });
                builder.setNegativeButton("Uncheck All", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        for(int i = 0; i < mAdapter.getCount(); i++){
                            ReminderItem temp = (ReminderItem) mAdapter.getItem(i);
                            mUserRef.child("reminders").child(temp.getReferenceId()).child("mDayStatus").setValue(false);
                            mUserRef.child("reminders").child(temp.getReferenceId()).child("mNightStatus").setValue(false);
                            temp.setmNightStatus(false);
                            temp.setmDayStatus(false);
                            mAdapter.removePoint();
                        }
                        mAdapter.notifyDataSetChanged();
                    }
                });
                builder.setNeutralButton("Delete All", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int size = mAdapter.getSize();
                        for(int i = mAdapter.getSize()-1; i >= 0; i--){
                            Log.i("TEST***", mAdapter.toString());
                            ReminderItem temp = (ReminderItem) mAdapter.getItem(i);
                            //Delete reminder from firebase
                            mUserRef.child("reminders").child(temp.getReferenceId()).removeValue();
                            //remove item from adapter
                            mAdapter.delete(temp);
                        }
                        //redraw ListView
                        mAdapter.notifyDataSetChanged();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
        /*
            Creates reference to firebase (mFirebase) then gets an authentication
            token (mAuthData)
         */
        mFirebase = new Firebase(getResources().getString(R.string.firebase_url));
        mAuthData = mFirebase.getAuth();

        if (mAuthData != null) {

            Log.d(LoginActivity.TAG, "Login Token Valid - AuthData: " + mAuthData.toString());
            /*
                If the login is valid, set the profile Id for the imageView.
             */
            /*
                If the login is valid, create the firebase reference to the specific user.
             */
            mUid = (String) mAuthData.getUid();
            mUserRef = new Firebase(getResources().getString(R.string.firebase_url) + mUid);
            mUserRef.child("points").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getValue() == null ){
                        mPoints.setText("0");
                    } else {
                        mPoints.setText(dataSnapshot.getValue().toString());
                    }
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });
            // Retrieve available items from firebase and add them to list adapter.
        } else {
            //DONE: Handle the case when token is invalid (i.e return to LoginActivity)
            finish();
        }

        mAdapter = new ReminderItemAdapter(getApplicationContext());
        getListView().setAdapter(mAdapter);
    }
    @Override
    protected void onPause() {
        super.onPause();
        saveItems();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == ADD_REMINDER_REQUEST && resultCode == RESULT_OK){
            ReminderItem nRi = new ReminderItem(data);

            //DONE:- change behavior when FireBase connection exists
            mAdapter.add(nRi, true);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        loadItems();
    }
    /*
        functionality handled in ListAdapter
     */
    private void loadItems() {
        //DONE- load items from FireBase (or locally as a fallback?)
    }

    // Save ToDoItems to file


    private void saveItems() {
        //DONE:- store items in Firebase (or locally as a fallback)
        /*
            Firebase functionality handled in ListAdapter
         */

        //TODO: Store locally when no internet connection, sync later
    }
    @Override
    public void onDestroy() {
        super.onDestroy();

    }

}
