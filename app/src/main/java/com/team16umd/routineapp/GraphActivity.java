package com.team16umd.routineapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.share.model.ShareContent;
import com.facebook.share.model.ShareMediaContent;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;
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

import org.w3c.dom.Text;

import java.util.ArrayList;

public class GraphActivity extends Activity {

    private Firebase mFirebase;
    private Firebase mUserRef;
    private TextView mPoints;
    private AuthData mAuth;
    //private static LineChart mLineChart;
    public static final String TAG = "GRAPH_ACTIVITY";

    private static ArrayList<Entry> mEntries = new ArrayList<>();
    private static ArrayList<String> mLabels = new ArrayList<>();
    private static LineData mData;
    private static LineDataSet mDataset;
    private ShareDialog shareDialog;
    private TextView mRoutineTitle;
    private RelativeLayout mProgressView;
    private TextView mCurrentStreak;
    private TextView mBestStreak;

    public SharePhoto shareItem(Bitmap image, String description) {
        SharePhoto sp1 = new SharePhoto.Builder().setBitmap(image).setCaption(description).build();
        /*if (ShareDialog.canShow(SharePhotoContent.class)) {
            SharePhoto photo = new SharePhoto.Builder().setBitmap(image).setCaption(description)
                    .build();
            SharePhotoContent content = new SharePhotoContent.Builder()
                    .addPhoto(photo).build();
            shareDialog.show(content);
            return true;
        }
        return false;*/
        return sp1;
    }

    public static Bitmap getBitmapFromView(View view) {
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(),Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        Drawable bgDrawable =view.getBackground();
        if (bgDrawable!=null)
            bgDrawable.draw(canvas);
        else
            canvas.drawColor(Color.WHITE);
        view.draw(canvas);
        return returnedBitmap;
    }
    // Method to clear the Graph
    public static void clearEntries(){
        mEntries.clear();
    }
    //Sets labels for graph (accessible from other classes)
    public static void setLabels(ArrayList<String> labels){
        mLabels = new ArrayList<>(labels);
    }

    //Adds new points to Graph, then graphs the graph
    public static void addEntries(ArrayList<Entry> entries, int color){
        mEntries = new ArrayList<>(entries);
        mDataset = new LineDataSet(mEntries, "Streak Info");
        mDataset.setDrawFilled(true);
        setColor(color);
        mData = new LineData(mLabels, mDataset);
        //mLineChart.setData(mData);
        //mLineChart.invalidate();
        Log.i(LoginActivity.TAG, "Drawing Graph");
    }

    //Sets fill color for the graph
    public static void setColor(int color){
        mDataset.setFillColor(color);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);
        shareDialog = new ShareDialog(this);
        mRoutineTitle = (TextView) findViewById(R.id.text_view);
        mCurrentStreak = (TextView) findViewById(R.id.current_streak_number);
        mBestStreak = (TextView) findViewById(R.id.best_streak_number);
        ListView listView = (ListView) findViewById(android.R.id.list);
        mProgressView = (RelativeLayout) findViewById(R.id.activity_information);
        listView.setAdapter(new ReminderItemAdapterGraph(getApplicationContext()));
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> adapterGraph, View v, int position, long id) {
                ReminderItem reminderItem = (ReminderItem) adapterGraph.getAdapter().getItem(position);
                Log.v("long clicked", "pos" + " " + position);
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getRootView().getContext());
                builder.setMessage("Title: " + reminderItem.getmTitle())
                        .setTitle("Options")
                        .setNegativeButton(R.string.progress, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent intent = new Intent(GraphActivity.this, GraphActivity.class);
                                startActivity(intent);
                            }
                        })
                                //Share Button
                        .setPositiveButton("Share", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Log.i(TAG, "Dialog Share");
                                Bitmap shareImage = getBitmapFromView(mProgressView);
                                dialog.dismiss();

                                SharePhoto sp = shareItem(shareImage, "Check out my how great I've been doing!");
                                ShareContent shareContent = new ShareMediaContent.Builder().addMedium(sp).build();
                                //shareDialog.show(shareContent, ShareDialog.Mode.AUTOMATIC);
                                ShareDialog.show(GraphActivity.this, shareContent);

                                /*Test*/
                                /*AlertDialog.Builder share = new AlertDialog.Builder(v.getRootView().getContext());
                                share.setTitle("Share With Your Friends on Facebook!");

                                AlertDialog.Builder builder = new AlertDialog.Builder(v.getRootView().getContext());
                                builder.setTitle("Share Your Progress!");
                                builder.setMessage("Upload to Facebook?");
                                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener(){
                                    public void onClick(DialogInterface dialog, int which){
                                        Bitmap image = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.five_day_streak);
                                        SharePhoto photo = new SharePhoto.Builder()
                                                .setBitmap(image)
                                                .build();
                                        SharePhotoContent content = new SharePhotoContent.Builder()
                                                .addPhoto(photo)
                                                .build();
                                        ShareDialog share = new ShareDialog(this);

                                /*End Test*/
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
                return true;
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //DONE: open dialog box with option to delete or edit completion status
                //DONE: Implement Graphing Behavior
                ReminderItem reminderItem = (ReminderItem) parent.getAdapter().getItem(position);
                mRoutineTitle.setText(reminderItem.getmTitle());
                Firebase historyRef = mUserRef.child("completed").child(reminderItem.getmTitle()).child("basic_stats");

                historyRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getValue() == null) {
                            Log.i(GraphActivity.TAG, "No Data for this Routine");
                            mBestStreak.setText("No Data");
                            mCurrentStreak.setText("No Data");


                        } else {
                            Log.i(GraphActivity.TAG, "Updating View with Data for this Routine");
                            mBestStreak.setText(dataSnapshot.child("best_streak").getValue().toString());
                            mCurrentStreak.setText(dataSnapshot.child("current_streak").getValue().toString());

                            // GraphActivity.setLabels(labels);
                            //GraphActivity.addEntries(entries, ContextCompat.getColor(mContext, R.color.app_main));


                        }
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });

            }
        });

        //listView.performItemClick(listView.getChildAt(0), 0, listView.getAdapter().getItemId(0));


        //Graph initilization Code.
        /*mLineChart = (LineChart) findViewById(R.id.line_chart);
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
        mLineChart.getXAxis().setTextSize(15f);*/

        //Set points for User in top bar
        mPoints = (TextView) findViewById(R.id.points);
        mFirebase = new Firebase(getResources().getString(R.string.firebase_url));
        mAuth = mFirebase.getAuth();
        if (mAuth != null){
            mUserRef = mFirebase.child(mAuth.getUid());
            mUserRef.child("points").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getValue() == null ){
                        Log.w(TAG, "Couldn't find points for the logged in User, defaulting to 0");
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


    }
}
