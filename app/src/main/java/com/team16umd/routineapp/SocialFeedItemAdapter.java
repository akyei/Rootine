package com.team16umd.routineapp;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.facebook.login.widget.ProfilePictureView;

import java.util.List;

/**
 * Created by thekyei on 4/27/16.
 */
public class SocialFeedItemAdapter extends BaseAdapter {
    private List<SocialFeedItem> mSocialFeedItems;
    private Context mContext;

    public SocialFeedItemAdapter(Context context){
        mContext = context;
    }
    public int getCount(){
        return mSocialFeedItems.size();
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        ViewHolder holder = null;
        View row = convertView;
        return row;
    }

    public void add(SocialFeedItem item){
        mSocialFeedItems.add(item);
        notifyDataSetChanged();
    }

    @Override
    public Object getItem(int pos){
        return mSocialFeedItems.get(pos);
    }

    @Override
    public long getItemId(int pos){
        return pos;
    }

    private static class ViewHolder {
        ProfilePictureView pictureView;
        TextView usersName;
        TextView feedText;
    }
}
