package com.team16umd.routineapp;

import android.app.Activity;
import android.app.ListActivity;
import android.os.Bundle;
import android.os.SystemClock;
import android.widget.TextView;

import java.util.ArrayList;

public class SocialFeedActivity extends ListActivity {
    private ArrayList<String> recentReminders;
    private long timeUpdated;
    private SocialFeedItemAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social_feed);

        TextView topBarText = (TextView) findViewById(R.id.top_bar_text);
        topBarText.setText("Recently Completed Routines");


        recentReminders = getRecentlyCompletedReminders();

        SocialFeedItemAdapter mAdapter = new SocialFeedItemAdapter(getApplicationContext());
        getListView().setAdapter(mAdapter);

    }

    private void updateList(){
        mAdapter.clear();
        for(int i = 0; i < recentReminders.size(); i++){
            mAdapter.add(new SocialFeedItem(recentReminders.get(i)));
        }
        mAdapter.notifyDataSetChanged();
    }

    private ArrayList<String> getRecentlyCompletedReminders(){
        ArrayList<String> result = new ArrayList<String>();
        //TODO- get the last 10 recently completed reminders from Firebase

        timeUpdated = SystemClock.elapsedRealtime();
        return result;
    }

    @Override
    protected void onResume() {

        if (SystemClock.elapsedRealtime() - timeUpdated > 5000){
            recentReminders = getRecentlyCompletedReminders();

        }

        super.onResume();
    }
}
