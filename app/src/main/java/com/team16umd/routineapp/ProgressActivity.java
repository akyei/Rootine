package com.team16umd.routineapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;

import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;

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
                        ArrayList<String> labels = new ArrayList<String>();
                        for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                            entries.add(new BarEntry(((Double) snapshot.getValue()).floatValue(), i));
                            labels.add(String.valueOf(i));
                            i++;
                        }
                        BarDataSet dataSet = new BarDataSet(entries, "Data");

                        BarData data = new BarData(labels, dataSet);
                        mBarChart.setData(data);
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
    }
}
