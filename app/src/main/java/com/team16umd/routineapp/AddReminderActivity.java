package com.team16umd.routineapp;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;

public class AddReminderActivity extends Activity {
    private Firebase mFirebase;
    private AuthData mAuthData;

    //TODO Define XML layout for AddReminder in res/layout/activity_add_reminder
    //TODO See UILab for inspiration
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(getApplicationContext());
        setContentView(R.layout.activity_add_reminder);
        mFirebase = new Firebase(getResources().getString(R.string.firebase_url));
        mAuthData = mFirebase.getAuth();

        if (mAuthData != null){
            Log.d(LoginActivity.TAG, "AuthData: " + mAuthData.toString());
        }
    }
}
