package com.team16umd.routineapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.TextClock;
import android.widget.TextView;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by thekyei on 4/20/16.
 */

public class ReminderItemAdapter extends BaseAdapter {

    private final Context mContext;
    private Firebase mFirebase;
    private Firebase mUserRef = null;
    private AuthData mAuthData;
    private String mUid;


    private final List<ReminderItem> mReminderItems = new ArrayList<ReminderItem>();

    public ReminderItemAdapter(Context context){
        mContext = context;
        mFirebase = new Firebase(mContext.getResources().getString(R.string.firebase_url));
        mAuthData = mFirebase.getAuth();
        if(mAuthData != null){
            mUid = mAuthData.getUid();
            mUserRef = new Firebase(mContext.getResources().getString(R.string.firebase_url) + mUid);
        }
    }

    public void add(ReminderItem item, Boolean toFirebase){

        /* If toFirebase is set to true, then add the item to Firebase as well */
        if (toFirebase){
            //TODO - add some kind of firebase functionality?
            mReminderItems.add(item);
        } else {
            mReminderItems.add(item);
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
        LayoutInflater mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        final ReminderItem reminderItem = (ReminderItem) mReminderItems.get(position);

        RelativeLayout reminderLayout = (RelativeLayout) mInflater.inflate(R.layout.reminder_item, parent);
        reminderLayout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                //TODO- either do a color change on the View or check a box in the layout
            }
        });

        //TODO- populate additional fields of the reminder layout (as fields are added to the ReminderItem class)

        return reminderLayout;
    }
}
