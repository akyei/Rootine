package com.team16umd.routineapp;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class GraphActivity extends Activity {

    private Firebase mFirebase;
    private Firebase mUserRef;
    private TextView mPoints;
    private AuthData mAuth;
    private static LineChart mLineChart;

    private static ArrayList<Entry> mEntries = new ArrayList<>();
    private static ArrayList<String> mLabels = new ArrayList<>();
    private static LineData mData;
    private static LineDataSet mDataset;

    public static void clearEntries(){
        mEntries.clear();
    }

    public static void setLabels(ArrayList<String> labels){
        mLabels = new ArrayList<>(labels);
    }

    public static void addEntries(ArrayList<Entry> entries, int color){
        mEntries = new ArrayList<>(entries);
        mDataset = new LineDataSet(mEntries, "Streak Info");
        mDataset.setDrawFilled(true);
        setColor(color);
        mData = new LineData(mLabels, mDataset);
        mLineChart.setData(mData);
        mLineChart.invalidate();
        Log.i(LoginActivity.TAG, "Drawing Graph");
    }

    public static void setColor(int color){
        mDataset.setFillColor(color);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);
        ListView listView = (ListView) findViewById(android.R.id.list);
        listView.setAdapter(new ReminderItemAdapterGraph(getApplicationContext()));
        /*listView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });*/
        mLineChart = (LineChart) findViewById(R.id.line_chart);
        mLineChart.getAxisLeft().setDrawGridLines(false);
        mLineChart.getXAxis().setDrawGridLines(false);
        mLineChart.setGridBackgroundColor(128);
        mLineChart.setBorderColor(255);
        mLineChart.getAxisRight().setEnabled(false);
        mLineChart.getAxisLeft().setEnabled(false);
        mLineChart.getAxisRight().setDrawLabels(false);
        mLineChart.getAxisLeft().setDrawLabels(false);
        mLineChart.setAutoScaleMinMaxEnabled(true);
        mLineChart.getAxisRight().setDrawGridLines(false);
        mLineChart.setDrawBorders(false);
        mLineChart.getXAxis().setTextSize(15f);
        mPoints = (TextView) findViewById(R.id.points);
        mFirebase = new Firebase(getResources().getString(R.string.firebase_url));
        if (mAuth != null){
            mUserRef = mFirebase.child(mAuth.getUid());
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
        }

        //Firebase historyRef = mUserRef.child("history");



    }
}
