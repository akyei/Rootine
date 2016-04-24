package com.team16umd.routineapp;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.login.widget.ProfilePictureView;
import com.firebase.client.AuthData;
import com.firebase.client.Firebase;


//TODO Implement the list activity


public class ReminderListActivity extends ListActivity {
    private AuthData mAuthData;
    private ProfilePictureView mProfilePic;
    private Firebase mFirebase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        Firebase.setAndroidContext(this);
        getListView().setFooterDividersEnabled(true);
        //TODO add footer to list view (Define xml file for the footer)
        setContentView(R.layout.activity_reminder_list);



        mProfilePic = (ProfilePictureView) findViewById(R.id.profile_pic);
        /*
            Creates reference to firebase (mFirebase) then gets an authentication
            token (mAuthData)
         */
        mFirebase = new Firebase("https://routinereminder.firebaseio.com/users");
        mAuthData = mFirebase.getAuth();

        if (mAuthData != null){

            Log.d(LoginActivity.TAG, "Login Token Valid - AuthData: " + mAuthData.toString());
            mProfilePic.setProfileId((String) mAuthData.getProviderData().get("id"));
        } else {
            //TODO Handle the case when token is invalid (i.e return to LoginActivity)
        }



    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

}
