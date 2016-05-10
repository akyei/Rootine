package com.team16umd.routineapp;

import android.app.Activity;
import android.app.ListActivity;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.ViewManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SocialFeedActivity extends ListActivity {
    private ArrayList<String> recentReminders;
    private long timeUpdated;
    private SocialFeedItemAdapter mAdapter;
    private Firebase mFeedRef;
    final private ArrayList<String> result = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social_feed);
        ListView listView = (ListView) findViewById(android.R.id.list);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                updateList();
                Log.i("TAG", "UPDATING LIST");
            }
        });

        mFeedRef = new Firebase("https://routinereminder.firebaseio.com/feed");
        TextView topBarText = (TextView) findViewById(R.id.top_bar_text);
        topBarText.setText("Community Feed");



        View settingsButton = findViewById(R.id.settings_button);
        ((ViewManager) settingsButton.getParent()).removeView(settingsButton);

        View points = findViewById(R.id.points);
        ((ViewManager) points.getParent()).removeView(points);


        mAdapter = new SocialFeedItemAdapter(getApplicationContext());
        recentReminders = getRecentlyCompletedReminders();


        getListView().setAdapter(mAdapter);
        updateList();

    }

    private void updateList(){
        mAdapter.clear();
        for(int i = recentReminders.size()-1; i >= 0; i--){
            mAdapter.add(new SocialFeedItem(result.get(i)));
            Log.i("HOLYMOLYIAMATAG", "ADDING ITEM TO ADAPTER");
        }
        mAdapter.notifyDataSetChanged();
    }

    private ArrayList<String> getRecentlyCompletedReminders(){
        //final ArrayList<String> result = new ArrayList<String>();
        mAdapter.clear();
        result.clear();
        // get the last 20 recently completed reminders from Firebase
        Query query = mFeedRef.limitToLast(20);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.i("HOLYMOLYIAMATAG", dataSnapshot.toString());
                if (dataSnapshot == null){
                    Log.i("HOLYMOLYIAMATAG", "THE DATASNAPSHOT WAS NULL");
                } else {
                    for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                        mAdapter.add(new SocialFeedItem(snapshot.getValue().toString()));
                        result.add(snapshot.getValue().toString());
                        Log.i("HOLYMOLYIAMATAG", "THE DATASNAPSHOT WAS  NOT NULL: " + snapshot);
                    }
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        timeUpdated = SystemClock.elapsedRealtime();
        Log.i("HOLYMOLYIAMATAG", "End Result: " + result.toString());
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
