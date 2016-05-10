package com.team16umd.routineapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewManager;
import android.widget.TextView;

import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.renderer.XAxisRenderer;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class ProgressActivity extends Activity {
    private final String TAG = "PROGRESSACTIVITY";
    private BarChart mBarChart;
    private Firebase mGraphRef;
    private Firebase mFirebase;
    private AuthData mAuth;
    private String mUID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress);
        mBarChart = (BarChart)findViewById(R.id.bar_chart);
        mFirebase = new Firebase(getResources().getString(R.string.firebase_url));
        mAuth = mFirebase.getAuth();
        if (mAuth != null){
            mUID = mAuth.getUid();
            Log.i(TAG, "AUTHENTICATED");
            mGraphRef = mFirebase.child(mUID).child("graph");
            Query query = mGraphRef.limitToLast(7);
            mGraphRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    ArrayList<BarEntry> entries = new ArrayList<>();
                    if (dataSnapshot == null){
                        //Do nothing
                    } else {
                        int i = 0;
                        LinkedList<String> labels = new LinkedList<String>();
                        for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                            entries.add(new BarEntry(((Double) snapshot.getValue()).floatValue()*100, i));
                            if (7-i == 1){
                                labels.add(0, String.valueOf(7-i) + " day ago");
                            } else {
                                labels.add(0, String.valueOf(7 - i) + " days ago");
                            }
                            i++;
                            Log.i(TAG, "Adding Real Data: " + i);

                        }
                        for (;i < 7; i++){
                            entries.add(0, new BarEntry(0f, i));
                            if (i+1 == 1){
                                labels.add(0, String.valueOf(7-i) + " day ago");
                            } else {
                                labels.add(0, String.valueOf(7-i) + " days ago");
                            }
                            Log.i(TAG, "Adding Bum Data");
                        }
                        BarDataSet dataSet = new BarDataSet(entries, "Data");

                        BarData data = new BarData(labels, dataSet);
                        mBarChart.setData(data);
                        XAxis axis = mBarChart.getXAxis();
                        axis.setTextColor(Color.BLACK);
                        axis.setTextSize(15f);
                        axis.setPosition(XAxis.XAxisPosition.BOTTOM);
                        mBarChart.setBackgroundColor(Color.WHITE);
                        mBarChart.invalidate();
                    }
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });
        } else {
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getApplicationContext());
            dialogBuilder.setTitle("You need to be logged in!");
            AlertDialog dialog = dialogBuilder.create();
            dialog.show();
            finish();
        }

        View settingsButton = findViewById(R.id.settings_button);
        ((ViewManager) settingsButton.getParent()).removeView(settingsButton);

        View points = findViewById(R.id.points);
        TextView title = (TextView) findViewById(R.id.top_bar_text);
        title.setText("Percent of Routines Completed");
        ((ViewManager) points.getParent()).removeView(points);

        TextView quote = (TextView) findViewById(R.id.random_quote);
        String quotes[] = {"\"The truth is that everyone is bored, and devotes himself to cultivating habits.\"",
                "\"A man who can't bear to share his habits is a man who needs to quit them.\"",
                "\"We become what we repeatedly do.\"",
                "\"Motivation is what gets you started. Habit is what keeps you going.\"" };
        Random r = new Random();
        quote.setText(quotes[r.nextInt(4)]);
    }
}
