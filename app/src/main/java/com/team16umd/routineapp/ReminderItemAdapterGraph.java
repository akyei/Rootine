package com.team16umd.routineapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.MutableData;
import com.firebase.client.Transaction;
import com.firebase.client.ValueEventListener;
import com.github.mikephil.charting.data.Entry;

import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by thekyei on 4/20/16.
 */

public class ReminderItemAdapterGraph extends BaseAdapter {

    private final Context mContext;
    private Firebase mFirebase;
    private static Firebase mUserRef = null;
    private static Firebase mCompletedRef = null;
    private Firebase mRemindersRef = null;
    private AuthData mAuthData;
    private String mUid;
    private long mCurrentStreak;

    private Bitmap check_bp;
    private Bitmap circle_bp;

    private final List<ReminderItem> mReminderItems = new ArrayList<ReminderItem>();

    public ReminderItemAdapterGraph(Context context){

        Firebase.setAndroidContext(context);
        mContext = context;
        mFirebase = new Firebase(mContext.getResources().getString(R.string.firebase_url));
        mAuthData = mFirebase.getAuth();
        circle_bp = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.empty_circle);
        check_bp = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.checkmark);
        if(mAuthData != null){
            mUid = mAuthData.getUid();
            mUserRef = new Firebase(mContext.getResources().getString(R.string.firebase_url) + mUid);
            mCompletedRef = new Firebase(mContext.getResources().getString(R.string.firebase_url) + mUid + "/" + "completed/");
            mRemindersRef = new Firebase(mContext.getResources().getString(R.string.firebase_url) + mUid + "/" + "reminders/");

            mRemindersRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getValue() != null) {
                        Log.i(LoginActivity.TAG, dataSnapshot.getValue().toString());
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            Log.i(LoginActivity.TAG, postSnapshot.getValue().toString());
                            ReminderItem item = postSnapshot.getValue(ReminderItem.class);
                            ReminderItemAdapterGraph.this.add(item, false);
                        }
                    }
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {
                    Log.i(LoginActivity.TAG, "The read failed: " + firebaseError.getMessage());
                }
            });
        }
    }

    public void add(ReminderItem item, Boolean toFirebase){

        /* If toFirebase is set to true, then add the item to Firebase as well */
        if (toFirebase){
            //DONE: - add some kind of firebase functionality?
            Map<String, Object> jsonObj = item.toMap();
            Firebase newRef = mRemindersRef.push();
            newRef.setValue(jsonObj);
            item.addReferenceId(newRef.getKey());
            //TODO - add some kind of firebase functionality?
            mReminderItems.add(item);
            notifyDataSetChanged();
        } else {
            mReminderItems.add(item);
            //notifyDataSetChanged();
        }
    }

    public void clear(){
        mReminderItems.clear();
    }

    @Override
    public Object getItem(int pos){
        return mReminderItems.get(pos);
    }

    @Override
    public long getItemId(int pos){
        return pos;
    }

    @Override
    public int getCount() {
        return mReminderItems.size() ;
    }

    public View getView(int position, View convertView, ViewGroup parent){
        ViewHolder holder = null;
        View row = convertView;

        if (row == null){
            row = (RelativeLayout) LayoutInflater.from(mContext).inflate(R.layout.reminder_item, parent, false);
            holder = new ViewHolder();
            holder.title = (TextView) row.findViewById(R.id.reminder_item_text);
            holder.description = (TextView) row.findViewById(R.id.reminder_item_desc);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        final ReminderItem reminderItem = (ReminderItem) getItem(position);
        holder.description.setText(reminderItem.getmDesc());
        holder.title.setText(reminderItem.getmTitle());
        row.setOnClickListener(new ListView.OnClickListener() {
            @Override
            public void onClick(View v) {
                //DONE: open dialog box with option to delete or edit completion status
                //DONE: Implement Graphing Behavior
                GraphActivity.clearEntries();
                final ArrayList<Entry> entries = new ArrayList<Entry>();
                final ArrayList<String> labels = new ArrayList<String>();
                Firebase historyRef = mUserRef.child("completed").child(reminderItem.getmTitle()).child("history");

                historyRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getValue() == null){
                            Log.i(LoginActivity.TAG, "YOU'RE SOL");
                            //SOL
                        } else {
                            Log.i(LoginActivity.TAG, "YOU'RE IN LUCK PARTIALLY");
                            float max = 0;
                            Long curr_val;
                            int i = 0;
                            for (DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                                curr_val = (Long) postSnapshot.getValue();
                                if (max < curr_val.floatValue()){
                                    max = curr_val.floatValue();
                                }
                                entries.add(new Entry(((Long) postSnapshot.getValue()).floatValue(), i));
                                labels.add(postSnapshot.getKey());
                                i++;
                            }

                            GraphActivity.setLabels(labels);
                            GraphActivity.addEntries(entries, ContextCompat.getColor(mContext, R.color.app_main));


                        }
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });

            }
        });

        return row;

    }




    private static class ViewHolder {
        TextView title;
        TextView description;
    }
}
