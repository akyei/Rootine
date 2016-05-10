package com.team16umd.routineapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.facebook.login.widget.ProfilePictureView;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by thekyei on 4/27/16.
 */
public class SocialFeedItemAdapter extends BaseAdapter {
    private List<SocialFeedItem> mSocialFeedItems;
    private Context mContext;

    public SocialFeedItemAdapter(Context context){
        mContext = context;
        mSocialFeedItems = new LinkedList<>();
    }
    public int getCount(){
        return mSocialFeedItems.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        if (convertView != null){
            return convertView;
        }
        SocialFeedItem item = mSocialFeedItems.get(position);
        LayoutInflater mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = mInflater.from(mContext).inflate(R.layout.anon_feed_item, parent, false);
        ((TextView) v.findViewById(R.id.anon_feed_text)).setText(item.getMessage());

        return v;
    }


    public void add(SocialFeedItem item){
        mSocialFeedItems.add(0,item);
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

    public void clear(){
        mSocialFeedItems.clear();
        notifyDataSetChanged();
    }

}
