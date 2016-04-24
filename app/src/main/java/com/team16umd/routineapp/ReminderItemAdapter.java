package com.team16umd.routineapp;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by thekyei on 4/20/16.
 */

//TODO Fully implement the adapter
public class ReminderItemAdapter extends BaseAdapter {

    private final Context mContext;
    private final List<ReminderItem> mReminderItems = new ArrayList<ReminderItem>();

    public ReminderItemAdapter(Context context){
        mContext = context;
    }

    public void add(ReminderItem item){
        mReminderItems.add(item);
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
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent){
        //TODO Implement the getView method for the adapter.


        return null;
    }
}
